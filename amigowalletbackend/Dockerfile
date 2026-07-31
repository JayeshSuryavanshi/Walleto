# syntax=docker/dockerfile:1

# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Cache dependencies first (re-downloaded only when pom.xml changes)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -q clean package -Dmaven.test.skip=true

# ---- runtime stage ----
FROM eclipse-temurin:21-jre AS run
WORKDIR /app
# curl is used by the container HEALTHCHECK (and Compose health-gating)
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
# Run as an unprivileged user
RUN groupadd --system app && useradd --system --gid app --home /app app
COPY --from=build /app/target/wallet-api-*.jar app.jar
USER app
EXPOSE 3322
HEALTHCHECK --interval=15s --timeout=5s --start-period=60s --retries=5 \
    CMD curl -fsS http://localhost:3322/AmigoWallet/actuator/health || exit 1
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
