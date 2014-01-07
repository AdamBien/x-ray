#!/bin/bash
JAVA_HOME=$JAVA_8_HOME
mvn clean install
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="undeploy satellite.war"
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="deploy --force ./target/satellite.war"
