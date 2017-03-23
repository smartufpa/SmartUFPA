<?php

ignore_user_abort(true);
set_time_limit(0); // disable the time limit for this script

$path = "./public/tiles.zip"; // change the path to fit your websites document structure

$fullPath = $path;

if ($fd = fopen ($fullPath, "r")) {
    $fsize = filesize($fullPath);
    $path_parts = pathinfo($fullPath);
    $ext = strtolower($path_parts["extension"]);
    switch ($ext) {
        case "zip":
        header("Content-type: application/zip");
        header("Content-Disposition: attachment; filename=\"".$path_parts["basename"]."\""); // use 'attachment' to force a file download
        break;
        // add more headers for other content types here
        default;
        header("Content-type: application/octet-stream");
        header("Content-Disposition: filename=\"".$path_parts["basename"]."\"");
        break;
    }
    header("Content-length: $fsize");
    header("Cache-control: private"); //use this to open files directly
    while(!feof($fd)) {
        $buffer = fread($fd, $fsize);
        echo $buffer;
    }
}
fclose ($fd);
exit;
