FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR workspace
COPY . .
RUN ./gradlew bootJar

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

ARG VERSION=v2.0.0
RUN curl -fl "https://github.com/gmr/env-aws-params/releases/download/${VERSION}/env-aws-params_linux-amd64" \
    -o /usr/local/bin/env-aws-params \
    && chmod +x /usr/local/bin/env-aws-params

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl ca-certificates openssl \
    && mkdir -p /app/certs /tmp/rds-certs \
    && curl -sSL -o /app/certs/global-bundle.pem https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem \
    && awk 'BEGIN {c=0} /-----BEGIN CERTIFICATE-----/ {c++; f=sprintf("/tmp/rds-certs/cert-%02d.pem", c)} f {print > f}' /app/certs/global-bundle.pem \
    && for cert in /tmp/rds-certs/cert-*.pem; do \
         alias_name=$(basename "$cert" .pem); \
         keytool -importcert -noprompt -alias "$alias_name" \
           -file "$cert" \
           -keystore /app/certs/rds-truststore.jks \
           -storepass changeit -storetype JKS; \
       done \
    && rm -rf /tmp/rds-certs /app/certs/global-bundle.pem
RUN keytool -list -keystore /app/certs/rds-truststore.jks -storepass changeit | head -n 15
COPY --from=builder /workspace/build/libs/*.jar app.jar

ENTRYPOINT ["env-aws-params"]
CMD ["java","-jar","/app.jar"]
