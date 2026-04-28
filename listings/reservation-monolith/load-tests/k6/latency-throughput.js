import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Counter, Rate } from 'k6/metrics';

const createReservationDuration = new Trend('create_reservation_ms', true);
const successRate = new Rate('success_rate');
const totalCreated = new Counter('reservations_created');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    baseline: {
      executor: 'constant-vus',
      vus: 1,
      duration: '30s',
      startTime: '0s',
      tags: { scenario: 'baseline' },
    },
    low_load: {
      executor: 'constant-vus',
      vus: 10,
      duration: '60s',
      startTime: '40s',
      tags: { scenario: 'low_load' },
    },
    medium_load: {
      executor: 'constant-vus',
      vus: 30,
      duration: '60s',
      startTime: '115s',
      tags: { scenario: 'medium_load' },
    },
    high_load: {
      executor: 'constant-vus',
      vus: 60,
      duration: '60s',
      startTime: '190s',
      tags: { scenario: 'high_load' },
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<3000'],
    http_req_failed: ['rate<0.01'],
  },
};

export function setup() {
  const res = http.post(
    `${BASE_URL}/api/users`,
    JSON.stringify({ email: 'loadtest-lt@example.com', fullName: 'LT User' }),
    { headers: { 'Content-Type': 'application/json' } },
  );
  const userId = res.json('id');
  console.log(`Setup complete: userId=${userId}`);
  return { userId };
}

export default function (data) {
  const payload = JSON.stringify({
    userId: data.userId,
    resourceId: Math.floor(Math.random() * 1000) + 1,
    from: '2026-09-01T10:00',
    to: '2026-09-01T12:00',
  });

  const res = http.post(`${BASE_URL}/api/reservations`, payload, {
    headers: { 'Content-Type': 'application/json' },
    tags: { name: 'create_reservation' },
  });

  const ok = check(res, {
    'status is 2xx': (r) => r.status >= 200 && r.status < 300,
    'has id in body': (r) => r.json('id') !== undefined,
  });

  createReservationDuration.add(res.timings.duration);
  successRate.add(ok);
  if (ok) totalCreated.add(1);

  sleep(0.05);
}

export function teardown() {
  http.del(`${BASE_URL}/api/reservations`);
  http.del(`${BASE_URL}/api/payments`);
  http.del(`${BASE_URL}/api/users`);
  console.log('Teardown complete');
}
