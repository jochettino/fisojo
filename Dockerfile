FROM gradle:6.9-jdk8
COPY . /home/gradle/fisojo
WORKDIR /home/gradle/fisojo
RUN gradle fatJar --no-daemon
ENTRYPOINT ["java", "-jar", "build/libs/fisojo-1.3-SNAPSHOT-jar-with-dependencies.jar", "--debug"]
