FROM gradle:8.8.0-jdk21

COPY . .

RUN chmod +x gradlew

RUN ./gradlew installDist

CMD ./build/install/app/bin/app