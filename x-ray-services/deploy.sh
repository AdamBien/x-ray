#!/bin/bash
"$GLASSFISH_HOME"/bin/asadmin --host 192.168.0.50 --port 5348 deploy --force ./target/x-ray.war
