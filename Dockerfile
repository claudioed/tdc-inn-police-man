FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
RUN GRPC_HEALTH_PROBE_VERSION=v0.3.6 && \
    wget -qO/bin/grpc_health_probe https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/${GRPC_HEALTH_PROBE_VERSION}/grpc_health_probe-linux-amd64 && \
    chmod +x /bin/grpc_health_probe

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/*-fat.jar /usr/local/lib/app.jar
COPY --from=builder /bin/grpc_health_probe ./grpc_health_probe
EXPOSE 7777
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
