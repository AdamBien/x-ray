#!/usr/bin/jjs -fv

var satellite = $ENV["SATELLITE_SERVER"];
print("Satellite server is running at ${satellite}");
var target = "${satellite}/satellite/resources/backups/";
print("Target uri is ${target}");
var Files = Java.type("java.nio.file.Files");
var Paths = Java.type("java.nio.file.Paths");

var backup = listBackups();
print(backup);
uploadBackup(target, backup)

function listBackups() {

    var directories = Files.newDirectoryStream(Paths.get("./"), "*.backup").iterator();
    while (directories.hasNext()) {
        var fileName = directories.next().getFileName().toString();
        var backupIndex = fileName.indexOf(".backup");
        print(fileName.substring(0, backupIndex));
    }
    return readLine("Choose a backup:");
}

function uploadBackup(link, file) {
    var command = "curl -i -XPUT --data-binary @${file}.backup ${link}${file}";
    print("Executing ${command}");
    $EXEC(command);
    var rawResult = $OUT;
    print("Got ${rawResult}")
    return rawResult;
}

