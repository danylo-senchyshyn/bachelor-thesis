# Monolith vs. Microservices: A Comparative Study

**Bachelor's Thesis** · Danylo Senchyshyn · Technical University of Košice (TUKE) · 2026

> Practical comparison of monolithic and microservices architectures implemented as a Java/Spring Boot reservation system, evaluated through load testing and code complexity analysis.

---

## Download

[![Download PDF](https://img.shields.io/badge/Download-thesis.pdf-blue?style=for-the-badge&logo=adobeacrobatreader)](https://github.com/danylo-senchyshyn/bachelor-thesis/releases/latest/download/thesis.pdf)

The PDF is automatically built from source on every push to `master` and published as a GitHub Release.

---

## Key Results

| Metric | Monolith | Microservices |
|---|---|---|
| Throughput (60 VUs) | 1 122 req/s | 972 req/s |
| Median latency (60 VUs) | 2.7 ms | 7.1 ms |
| p95 latency (60 VUs) | 7.1 ms | 35.9 ms |
| RAM usage | ~376 MiB | ~3 584 MiB |
| E2E median time | 5 ms | 5 210 ms |
| Lines of code | ~1 400 | ~6 400 |

---

## Prototype Repositories

| Project | Repository |
|---|---|
| Monolith (Spring Boot) | [reservation-monolith](https://github.com/danylo-senchyshyn/reservation-monolith) |
| Microservices (Spring Boot + Kafka) | [reservation-micro](https://github.com/danylo-senchyshyn/reservation-micro) |

Both prototypes run via `docker compose up --build` — no local Java or Maven required.

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.4
- **Database:** PostgreSQL 15
- **Messaging:** Apache Kafka (microservices only)
- **Load testing:** k6
- **Containerization:** Docker Compose

---

## Thesis Structure

| Chapter | Topic |
|---|---|
| 1 | Introduction |
| 2 | Theoretical background — monolithic architecture |
| 3 | Theoretical background — microservices architecture |
| 4 | Related work |
| 5 | Design and implementation |
| 6 | Evaluation (load tests, code complexity) |
| 7 | Conclusion |

---

## Build from Source

Requires Docker Desktop.

```bash
./mkthesis.sh thesis
```

Output: `dist/thesis.pdf`
