#!/bin/bash
JAVA_HOME=$JAVA_8_HOME
mvn clean install
echo '----- Redeploying x-ray-services -----'
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="undeploy x-ray.war"
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="deploy --force ./target/x-ray.war"

echo '----- Running integration tests -----'
cd ../x-ray-services-st/
./it.sh
