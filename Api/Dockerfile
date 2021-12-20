#Start building from here
FROM openjdk:17-alpine AS BuildingStage

#Get APK up to date
RUN apk update && apk upgrade

#Install Maven
RUN apk add maven

#Install Git and retrieve the sources
#RUN apk add git
#RUN git clone https://github.com/hannesknutsson/HungryBoii.git /HungryBoiiGit # copy from local to image?
COPY . /api

#Build
RUN mvn -f /api clean install

#Create second stage in our build to leave the Git and Maven packages behind
#FROM openjdk:12-alpine AS FinalImageStage

#Copy the compiled JAR-file from the previous stage
#COPY --from=BuildingStage /HungryBoiiGit/target/HungryBoii-distributable /HungryBoii

#Add Tesseract package and environment variables
ENV LC_ALL C
RUN apk add tesseract-ocr

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
WORKDIR /api/target/Api-distributable
ENTRYPOINT ["java", "-jar", "HungryBoii-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
