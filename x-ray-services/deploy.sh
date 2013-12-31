#!/bin/bash
JAVA_HOME=$JAVA_8_HOME
mvn clean install
$WILDFLY_HOME/bin/jboss-cli.sh --connect shutdown
$WILDFLY_HOME/bin/standalone.sh &
sleep 2
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="deploy ./target/x-ray.war"
start http://localhost:8080/x-ray/resources/version/
