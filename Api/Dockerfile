#Start building from here
FROM maven:3-openjdk-18-slim
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
ENTRYPOINT ["java", "-jar", "HungryBoii-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
