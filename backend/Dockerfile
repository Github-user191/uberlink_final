FROM maven:3.8.6-openjdk-11 as build

WORKDIR /app

COPY .mvn .mvn

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src src

RUN mvn package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM maven:3.8.6-openjdk-11

ARG DEPENDENCY=/app/target/dependency


COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.springboot.app.uberlink.UberlinkApplication"]