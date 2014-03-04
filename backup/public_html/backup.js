#!/usr/bin/jjs -fv

var satellite = $ENV["SATELLITE_SERVER"];
print("Satellite server is running at ${satellite}");
var target = "${satellite}/satellite/resources/backups/";
print("Target uri is ${target}");
var Files = Java.type("java.nio.file.Files");
var Paths = Java.type("java.nio.file.Paths");

startBackup();

function backup(name, link) {
    var absolutePath = "${satellite}${link}";
    var rawResult = fetchContents(absolutePath);
    print("--------------");
    Files.write(Paths.get("./${name}.backup"), rawResult.bytes);
    print("--------------");

}

function fetchContents(link) {
    var command = "curl -v ${link}";
    print("Executing ${command}");
    $EXEC(command);
    var rawResult = $OUT;
    print("Got ${rawResult}")
    return rawResult;
}

function startBackup() {
    var rawResult = fetchContents(target);
    var backupLinks = JSON.parse(rawResult);
    var link;
    for (name in backupLinks) {
        link = backupLinks[name];
        backup(name, link);
    }
}
