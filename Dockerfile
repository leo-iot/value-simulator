FROM maven:3.8.4-openjdk-17-slim as build

RUN curl --location --output graalvm.tgz https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.3.0/graalvm-ce-java17-linux-amd64-21.3.0.tar.gz \
    && tar -xzf graalvm.tgz \
    && mv graalvm-* /opt/graalvm \
    && /opt/graalvm/bin/gu install native-image \
    && apt update \
    && apt install build-essential libz-dev zlib1g-dev -y \
    && rm -r /var/lib/apt/lists


WORKDIR /app

COPY pom.xml .

RUN mvn install

COPY . .

RUN export GRAALVM_HOME=/opt/graalvm && mvn verify -Pnative -DskipTests

FROM ubuntu:impish-20211102 as run

WORKDIR /app

COPY --from=build /app/target/*-runner /app/application

EXPOSE 8080

CMD ["./application"]
