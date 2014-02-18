#!/usr/bin/jjs -fv
var satellite = $ENV["SATELLITE_SERVER"];
print("Satellite server is running at ${satellite}");
var target = "${satellite}/satellite/resources/grids/maps/hits/COUNTER";
print("Target uri is ${target}");
var command = "curl ${target}"
print("Executing ${command}");
$EXEC(command);
var rawResult = $OUT;
print("Got ${rawResult}")
var result = JSON.parse(rawResult);
if (result.content > 0) {
    print("+++ ->: ${result.content}");
    exit(0);
} else {
    print("ERROR: entry not set ${result}");
    exit(-1);
}

