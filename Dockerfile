FROM gradle:7.6.0 AS TEMP_BUILD_IMAGE
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar -x test || return 0

FROM adoptopenjdk:11-jre-hotspot
ENV ARTIFACT_NAME=test_solution-0.0.1.jar
RUN mkdir /app

COPY --from=TEMP_BUILD_IMAGE /home/gradle/src/build/libs/test_solution-0.0.1-SNAPSHOT.jar /app/test_solution.jar

ENTRYPOINT exec java -jar /app/test_solution.jar
