#!/usr/bin/jjs -fv
var satellite = $ARG[0];
var folder = $ARG[1];
if (!satellite || !folder) {
    print('use: backup.js -- http://192.168.0.42:5680 backup');
    exit();

}
print("Satellite server is running at ${satellite}");
var target = "${satellite}/satellite/resources/backups/";
print("Target uri is ${target}");
var Files = Java.type("java.nio.file.Files");
var Paths = Java.type("java.nio.file.Paths");
var File = Java.type("java.io.File");

startBackup();

function startBackup() {
    var rawResult = fetchContents(target);
    var backupLinks = JSON.parse(rawResult);
    var link;
    for (name in backupLinks) {
        link = backupLinks[name];
        backup(name, link);
    }
}


function backup(name, link) {
    var absolutePath = "${satellite}${link}";
    var rawResult = fetchContents(absolutePath);
    new File("${folder}").mkdirs();
    print("--------------");
    Files.write(Paths.get("${folder}/${name}.backup"), rawResult.bytes);
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

