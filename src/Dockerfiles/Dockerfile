#Where we start
FROM openjdk:12-alpine

#APK
RUN apk update
RUN apk upgrade

#Maven
RUN apk add maven

#Git
RUN apk add git
RUN mkdir /HungryBoiiGit
RUN git clone https://github.com/hannesknutsson/HungryBoii.git /HungryBoiiGit

#Build
RUN mvn -f /HungryBoiiGit clean install

#Save result
RUN mkdir /Executables
RUN cp -r /HungryBoiiGit/target/HungryBoii-distributable/* /Executables

#Remove Git repo
RUN rm -rf /HungryBoiiGit

#Define how to start
WORKDIR /Executables
ENTRYPOINT ["java", "-jar", "HungryBoii-1.0-SNAPSHOT.jar"]