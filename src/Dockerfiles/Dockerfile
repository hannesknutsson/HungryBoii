#Start building from here
FROM openjdk:12-alpine AS BuildingStage

#Get APK up to date
RUN apk update && apk upgrade

#Install Maven
RUN apk add maven

#Install Git and retrieve the sources
RUN apk add git
RUN git clone https://github.com/hannesknutsson/HungryBoii.git /HungryBoiiGit

#Build
RUN mvn -f /HungryBoiiGit clean install

#Create second stage in our build to leave the Git and Maven packages behind
FROM openjdk:12-alpine AS FinalImageStage

#Copy the compiled JAR-file from the previous stage
COPY --from=BuildingStage /HungryBoiiGit/target/HungryBoii-distributable /HungryBoii

#Add Tesseract package and environment variables
ENV LC_ALL C
RUN apk add tesseract-ocr

#Add unprivileged user and group
#RUN addgroup -S appgroup
#RUN adduser -S appuser -G appgroup

#Add logging directory and allow unprivileged user to create files there
RUN mkdir /HungryBoii/log

#Bring these commands back later
#RUN chown -R appuser /HungryBoii/log
#RUN chgrp -R appgroup /HungryBoii/log
#RUN chmod -R 644 /HungryBoii/log

#Define how to start

#Bring this command back later
#USER appuser
WORKDIR /HungryBoii
ENTRYPOINT ["java", "-jar", "HungryBoii-1.0-SNAPSHOT.jar"]
