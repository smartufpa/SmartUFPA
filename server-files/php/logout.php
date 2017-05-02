<?php
include_once 'session.php';

SessionClient::finishSession();

header("Location: site/login.php");

?>