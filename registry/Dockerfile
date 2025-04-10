FROM ghcr.io/graalvm/native-image:ol8-java17-22 AS builder

# Install tar and gzip to extract the Maven binaries
RUN microdnf update \
    && microdnf install --nodocs \
    tar \
    gzip \
    && microdnf clean all \
    && rm -rf /var/cache/yum

# Install Maven
# Source:
# 1) https://github.com/carlossg/docker-maven/blob/925e49a1d0986070208e3c06a11c41f8f2cada82/openjdk-17/Dockerfile
# 2) https://maven.apache.org/download.cgi
ARG USER_HOME_DIR="/root"
ARG MAVEN_DOWNLOAD_URL=https://dlcdn.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
    && curl -fsSL -o /tmp/apache-maven.tar.gz ${MAVEN_DOWNLOAD_URL} \
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
    && rm -f /tmp/apache-maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn


ENV MAVEN_HOME /usr/share/maven/bin/
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Set the working directory to /home/app
WORKDIR /build

# Copy the source code into the image for building
COPY . /build

#RUN rm -rvf target

# Setup environment vairables for maven resposiory
ARG PACKAGE_TOKEN
ENV PACKAGE_TOKEN ${PACKAGE_TOKEN}

ARG POM_NAME=pom.xml
ENV POM_NAME ${POM_NAME}

# Build
RUN mvn dependency:go-offline -f ${POM_NAME} -s rc-settings.xml

RUN mvn --no-transfer-progress clean package -DskipTests -f ${POM_NAME}

# The deployment Image
FROM bellsoft/liberica-openjdk-alpine:17.0.6

EXPOSE 8761

#Configurar Zona Horaria
RUN apk upgrade --no-cache
RUN apk add --no-cache tzdata
ENV TZ="America/Guayaquil"

# Copy the native executable into the containers
WORKDIR /app
COPY --from=builder /build/target/registry-1.0.0.jar .
ENTRYPOINT [ "java", "-jar", "/app/registry-1.0.0.jar" ]
