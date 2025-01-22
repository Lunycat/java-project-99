clean:
	@./gradlew clean

start:
	@./gradlew run

build:
	@./gradlew clean build

installDist:
	@./gradlew clean installDist

run-dist:
	@./build/install/app/bin/app

run:
	@./gradlew run

test:
	@./gradlew test

report:
	@./gradlew jacocoTestReport

lint:
	@./gradlew checkstyleMain checkstyleTest

