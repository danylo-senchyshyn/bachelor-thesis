import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Counter } from 'k6/metrics';

const e2eTotal     = new Trend('e2e_total_ms', true);
const e2eToPayment = new Trend('e2e_to_payment_ms', true);
const e2eToConfirm = new Trend('e2e_to_confirm_ms', true);
const completed    = new Counter('e2e_completed');
const failed       = new Counter('e2e_failed');

const BASE_URL        = __ENV.BASE_URL || 'http://localhost:8080';
const MAX_WAIT_MS     = 30000;  // max 30 s per phase
const POLL_INTERVAL_S = 0.5;   // poll every 500 ms

export const options = {
  vus: 1,
  iterations: 20,
  thresholds: {
    e2e_total_ms: ['p(95)<25000'],
    e2e_failed:   ['count<3'],
  },
};

export function setup() {
  const res = http.post(
    `${BASE_URL}/api/users`,
    JSON.stringify({ email: 'loadtest-e2e@example.com', fullName: 'E2E User' }),
    { headers: { 'Content-Type': 'application/json' } },
  );
  const userId = res.json('id');
  console.log(`Setup complete: userId=${userId}`);
  return { userId };
}

function pollUntil(fn, maxMs) {
  const deadline = Date.now() + maxMs;
  while (Date.now() < deadline) {
    const result = fn();
    if (result !== null && result !== undefined && result !== false) return result;
    sleep(POLL_INTERVAL_S);
  }
  return null;
}

export default function (data) {
  // Step 1: Create reservation
  const e2eStart = Date.now();

  const reservationRes = http.post(
    `${BASE_URL}/api/reservations`,
    JSON.stringify({
      userId: data.userId,
      resourceId: Math.floor(Math.random() * 1000) + 1,
      from: '2026-09-01T10:00',
      to: '2026-09-01T12:00',
    }),
    { headers: { 'Content-Type': 'application/json' } },
  );

  if (!check(reservationRes, { 'reservation created (2xx)': (r) => r.status >= 200 && r.status < 300 })) {
    console.error(`Failed to create reservation: status=${reservationRes.status}`);
    failed.add(1);
    return;
  }

  const reservationId = reservationRes.json('id');

  // Step 2: Wait for a payment to be created for this reservation
  // In monolith this is synchronous, so payment is available immediately
  const paymentPhaseStart = Date.now();

  const paymentId = pollUntil(() => {
    const r = http.get(`${BASE_URL}/api/payments/by-reservation/${reservationId}`);
    if (r.status === 200) {
      const list = r.json();
      if (list && list.length > 0) return list[0].id;
    }
    return null;
  }, MAX_WAIT_MS);

  if (!paymentId) {
    console.error(`Timeout waiting for payment for reservationId=${reservationId}`);
    failed.add(1);
    return;
  }

  e2eToPayment.add(Date.now() - paymentPhaseStart);

  // Step 3: Confirm payment
  const confirmRes = http.post(`${BASE_URL}/api/payments/${paymentId}/confirm`);

  if (!check(confirmRes, { 'payment confirmed (200)': (r) => r.status === 200 })) {
    console.error(`Failed to confirm paymentId=${paymentId}: status=${confirmRes.status}`);
    failed.add(1);
    return;
  }

  const confirmPhaseStart = Date.now();

  // Step 4: Wait for reservation to reach terminal status
  // Monolith ReservationStatus enum:
  //   PAID      — payment confirmed successfully
  //   CANCELLED — payment failed
  const TERMINAL_STATUSES = ['PAID', 'CANCELLED'];

  const finalStatus = pollUntil(() => {
    const r = http.get(`${BASE_URL}/api/reservations/${reservationId}`);
    if (r.status === 200) {
      const status = r.json('status');
      if (TERMINAL_STATUSES.includes(status)) return status;
    }
    return null;
  }, MAX_WAIT_MS);

  if (!finalStatus) {
    console.error(`Timeout waiting for reservation ${reservationId} to reach terminal status`);
    failed.add(1);
    return;
  }

  // Record metrics
  const totalMs = Date.now() - e2eStart;
  e2eTotal.add(totalMs);
  e2eToConfirm.add(Date.now() - confirmPhaseStart);
  completed.add(1);

  console.log(`[OK] reservationId=${reservationId} status=${finalStatus} totalMs=${totalMs}`);
}

export function teardown() {
  http.del(`${BASE_URL}/api/reservations`);
  http.del(`${BASE_URL}/api/payments`);
  http.del(`${BASE_URL}/api/users`);
  console.log('Teardown complete');
}
