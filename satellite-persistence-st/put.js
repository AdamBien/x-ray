#!/usr/bin/jjs -fv
var satellite = $ENV["SATELLITE_SERVER"];
print("Satellite server is running at ${satellite}");
var target = "${satellite}/satellite/resources/grids/maps/hits/COUNTER";
print("Target uri is ${target}");
var payload = "{\"content\": 42}";
var command = "curl -X PUT -H 'Content-type: application/jsone' -d ${payload} ${target}";
print("Executing ${command}");
$EXEC(command);
exit($EXIT);