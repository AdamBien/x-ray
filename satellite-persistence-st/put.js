#!/usr/bin/jjs -fv
var satellite = $ENV["SATELLITE_SERVER"];
print("Satellite server is running at ${satellite}");
var target = "${satellite}/satellite/resources/grids/maps/hits/COUNTER";
print("Target uri is ${target}");
var payload = "{\"content\":42}";
var command = "curl -i -XPUT -HContent-type:application/json -d ${payload} ${target}";
print("${command}");
$EXEC(command);
print($OUT);
