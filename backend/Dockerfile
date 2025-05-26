FROM gradle:8.5-jdk21-alpine AS build
WORKDIR /app
RUN apk add --no-cache llvm clang
COPY backend .
RUN gradle build -x test

FROM openjdk:21-slim
WORKDIR /app
RUN mkdir -p /app/work && \
    apt-get update && \
    apt-get install -y wget software-properties-common gnupg graphviz && \
    wget https://apt.llvm.org/llvm.sh && chmod +x llvm.sh && \
    ./llvm.sh 19 && \
    apt-get install -y llvm-19 clang-19 && \
    rm -rf /var/lib/apt/lists/* && \
    rm llvm.sh && \
    ln -s /usr/bin/clang-19 /usr/bin/clang && \
    ln -s /usr/bin/opt-19 /usr/bin/opt
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]