#Start building from here
FROM openjdk:17-alpine AS BuildingStage

#Get APK up to date
RUN apk update && apk upgrade

#Install Maven
RUN apk add maven

COPY . /discord

#Build
RUN mvn -f /discord clean install

#Add unprivileged user and group
#RUN addgroup -S appgroup
#RUN adduser -S appuser -G appgroup

#Add logging directory and allow unprivileged user to create files there
RUN mkdir /discord/logs

#Bring these commands back later
#RUN chown -R appuser /HungryBoii/log
#RUN chgrp -R appgroup /HungryBoii/log
#RUN chmod -R 644 /HungryBoii/log

#Define how to start

#Bring this command back later
#USER appuser
WORKDIR /discord/target/Discord-distributable
ENTRYPOINT ["java", "-jar", "HungryBoii-1.0-SNAPSHOT.jar"]
