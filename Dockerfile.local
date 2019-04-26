FROM gradle:5.4.0-jdk11-slim as builder

#TODO find a way how to not copy job-data into the image
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

# second stage -- run built application
FROM openjdk:11-jre-slim as execution

COPY --from=builder /home/gradle/src/simulation/build/distributions/simulation.tar /app/
COPY job-data /app/simulation/job-data

WORKDIR /app
RUN tar -xvf simulation.tar
WORKDIR /app/simulation

CMD bin/simulation
