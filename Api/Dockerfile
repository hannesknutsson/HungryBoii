#Start building from here
FROM openjdk:17-alpine AS BuildingStage

#Get APK up to date
RUN apk update && apk upgrade

#Install Maven and Tesseract package and environment variables
ENV LC_ALL C
RUN apk add maven tesseract-ocr

COPY . /api

#Build
RUN mvn -f /api clean install

#Add unprivileged user and group
#RUN addgroup -S appgroup
#RUN adduser -S appuser -G appgroup

#Add logging directory and allow unprivileged user to create files there
RUN mkdir -p /api/logs

#Bring these commands back later
#RUN chown -R appuser /HungryBoii/log
#RUN chgrp -R appgroup /HungryBoii/log
#RUN chmod -R 644 /HungryBoii/log

#Define how to start

#Bring this command back later
#USER appuser
WORKDIR /api/target
ENV TESSDATA_PREFIX=/api/tessdata
ENTRYPOINT ["java", "-jar", "HungryBoii-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
