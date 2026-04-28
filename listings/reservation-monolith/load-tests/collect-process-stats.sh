#!/usr/bin/env bash
# Collects CPU% and memory usage of the local JVM process (Spring Boot monolith)
# and PostgreSQL, writing results to a CSV file.
#
# Usage:
#   ./load-tests/collect-process-stats.sh <output.csv> [interval_seconds]
#
# Example:
#   ./load-tests/collect-process-stats.sh load-tests/results/monolith/process-stats-lt.csv 2

set -euo pipefail

OUTPUT="${1:-load-tests/results/monolith/process-stats.csv}"
INTERVAL="${2:-2}"

mkdir -p "$(dirname "$OUTPUT")"

echo "timestamp,process,pid,cpu_pct,mem_rss_mb" > "$OUTPUT"
echo "Collecting process stats every ${INTERVAL}s → $OUTPUT. Press Ctrl+C to stop."

trap 'echo "Stopped."' INT TERM

while true; do
  TS=$(date +%Y-%m-%dT%H:%M:%S)

  # Spring Boot JVM — find by main class or jar name
  JAVA_PID=$(pgrep -f 'MonolithApplication\|reservation-monolith' 2>/dev/null | head -1 || true)
  if [ -n "$JAVA_PID" ]; then
    read -r cpu mem_kb <<< $(ps -p "$JAVA_PID" -o %cpu=,rss= 2>/dev/null || echo "0 0")
    mem_mb=$(echo "scale=1; ${mem_kb:-0} / 1024" | bc)
    echo "${TS},monolith-app,${JAVA_PID},${cpu},${mem_mb}" >> "$OUTPUT"
  fi

  # PostgreSQL — main postmaster process
  PG_PID=$(pgrep -x postgres 2>/dev/null | head -1 || true)
  if [ -n "$PG_PID" ]; then
    read -r cpu mem_kb <<< $(ps -p "$PG_PID" -o %cpu=,rss= 2>/dev/null || echo "0 0")
    mem_mb=$(echo "scale=1; ${mem_kb:-0} / 1024" | bc)
    echo "${TS},postgres,${PG_PID},${cpu},${mem_mb}" >> "$OUTPUT"
  fi

  sleep "$INTERVAL"
done
