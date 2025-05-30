# Build stage
FROM openjdk:23-oraclelinux8 AS builder
ARG BUILD_LIB_DIR
ARG ARTIFACTORY_FILE
COPY ${BUILD_LIB_DIR}/${ARTIFACTORY_FILE} app.jar

# Run stage
FROM openjdk:23-slim
ENV USERNAME k8s
ENV ARTIFACTORY_HOME /home/${USERNAME}
ENV JAVA_OPTS=""

# Add a non-root user
RUN adduser --system --group ${USERNAME} && \
    mkdir -p ${ARTIFACTORY_HOME} && \
    chown ${USERNAME}:${USERNAME} ${ARTIFACTORY_HOME}

WORKDIR ${ARTIFACTORY_HOME}
COPY --from=builder app.jar app.jar
RUN chown ${USERNAME}:${USERNAME} app.jar

USER ${USERNAME}

ENTRYPOINT [ "sh", "-c" ]
CMD ["java ${JAVA_OPTS} -jar app.jar"]