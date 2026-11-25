FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR workspace
COPY . .
RUN ./gradlew bootJar

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

COPY --from=ghcr.io/gmr/env-aws-params:latest /usr/local/bin/env-aws-params /usr/local/bin/env-aws-params
COPY --from=builder /workspace/build/libs/*.jar app.jar

ENTRYPOINT ["env-aws-params"]
CMD ["java","-jar","/app.jar"]
