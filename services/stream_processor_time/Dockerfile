# This Dockerfile has two required ARGs to determine which base image
# to use for the JDK and which sbt version to install.

ARG OPENJDK_TAG=8u322-jre
ARG OPENJDK_IMAGE=openjdk:${OPENJDK_TAG}

FROM ${OPENJDK_IMAGE} AS builder

ARG SBT_VERSION=1.6.2

# Install sbt as spec-ed on https://www.scala-sbt.org/download.html (April 2022)
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import && \
    chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg && \
    apt-get update && \
    apt-get -y install sbt=${SBT_VERSION}

WORKDIR build/
COPY processor/ ./
RUN sbt test && \
    sbt assembly && \
    find ./ -name *jar
# uncomment to run app in builder
# RUN java -jar /build/target/scala-2.12/processor-assembly-0.1.0-SNAPSHOT.jar

FROM ${OPENJDK_IMAGE}

WORKDIR app/
COPY --from=builder /build/target/scala-2.12/processor-assembly-0.1.0-SNAPSHOT.jar ./
CMD ["java", "-jar", "processor-assembly-0.1.0-SNAPSHOT.jar"]