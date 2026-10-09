FROM eclipse-temurin:26-jdk AS build
WORKDIR /src
COPY . .
RUN ./gradlew fatJar --no-daemon

FROM eclipse-temurin:26-jre
COPY --from=build /src/build/libs/fisojo-1.3-SNAPSHOT-jar-with-dependencies.jar /fisojo.jar
ENTRYPOINT ["java", "-jar", "/fisojo.jar", "--debug"]
