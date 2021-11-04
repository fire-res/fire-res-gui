#!/bin/bash

./mvnw clean install -DskipTests
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar app/target/fire-res-app.jar