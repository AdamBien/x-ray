#!/bin/bash
JAVA_HOME=$JAVA_8_HOME
mvn clean install
HOST=${1:-localhost}
"$GLASSFISH_HOME"/bin/asadmin --host $HOST --port 5348 deploy --force ./target/x-ray.war
start http://localhost:5380/x-ray/resources/maintenance/
