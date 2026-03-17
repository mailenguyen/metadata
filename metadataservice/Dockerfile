# ================================
# Stage 1: Build
# ================================
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /build

# copy maven wrapper
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml .

# download dependency trước để tận dụng docker cache
RUN chmod +x mvnw && ./mvnw -B -q -e -DskipTests dependency:go-offline

# copy source
COPY src ./src

# build jar
RUN ./mvnw -B -DskipTests package


# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# tạo user không phải root (best practice production)
RUN addgroup -S spring && adduser -S spring -G spring

USER spring

# copy jar từ stage build
COPY --from=build /build/target/metadataservice-0.0.1-SNAPSHOT.jar app.jar

# port service của bạn
EXPOSE 8763

# JVM tối ưu cho container
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]