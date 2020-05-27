FROM maven:3.6.3-jdk-8-slim
COPY . /
RUN mvn package -DskipTest
ENTRYPOINT ["java", "-jar", "target/fisojo-1.3-SNAPSHOT-jar-with-dependencies.jar", "--debug"]