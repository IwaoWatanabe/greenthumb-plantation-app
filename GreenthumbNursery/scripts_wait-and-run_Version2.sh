#!/usr/bin/env bash
# wait-and-run.sh
# Waits for MySQL to become available, then imports SQL if not already imported and runs the JAR.
# Usage: placed in ./scripts/ and referenced by docker-compose volume mount.

set -e

DB_HOST="db"
DB_PORT=3306
RETRY=30
SLEEP=2
SQL_FILE="/opt/greenthumb/db/init/greenthumb_nursery.sql"
JAR_GLOB="/opt/greenthumb/target/greenthumb-nursery-*.jar"

echo "Waiting for MySQL at ${DB_HOST}:${DB_PORT}..."

i=0
until mysql --host="${DB_HOST}" --port="${DB_PORT}" -u"${DB_USERNAME}" -p"${DB_PASSWORD}" -e "SELECT 1;" >/dev/null 2>&1; do
  i=$((i+1))
  if [ "$i" -ge "$RETRY" ]; then
    echo "Timed out waiting for MySQL after $((RETRY * SLEEP)) seconds."
    exit 1
  fi
  sleep "${SLEEP}"
done

echo "MySQL is available."

# Import SQL if tables not present
echo "Checking if 'users' table exists..."
if mysql --host="${DB_HOST}" --port="${DB_PORT}" -u"${DB_USERNAME}" -p"${DB_PASSWORD}" -e "USE greenthumb_nursery; SHOW TABLES LIKE 'users';" | grep users >/dev/null 2>&1; then
  echo "Database appears initialized; skipping import."
else
  if [ -f "${SQL_FILE}" ]; then
    echo "Importing schema from ${SQL_FILE}..."
    mysql --host="${DB_HOST}" --port="${DB_PORT}" -u"${DB_USERNAME}" -p"${DB_PASSWORD}" greenthumb_nursery < "${SQL_FILE}"
    echo "Import complete."
  else
    echo "SQL file not found at ${SQL_FILE}; skipping import."
  fi
fi

# Find the JAR and run it
JAR_PATH=$(ls ${JAR_GLOB} 2>/dev/null | head -n1 || true)
if [ -z "${JAR_PATH}" ]; then
  echo "No built JAR found under /opt/greenthumb/target. Exiting. Build the project locally and mount the target/ directory, or attach the JAR to this folder."
  exit 1
fi

echo "Starting application: java -jar ${JAR_PATH}"
exec java -jar "${JAR_PATH}"