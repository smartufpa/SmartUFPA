

// UDP Server Setup
const udp_port = 80;
const dgram = require("dgram");
const udp_socket = dgram.createSocket("udp4");

// HTTP Server Setup

const express = require('express');
const http_socket = express();

// Message Setup
const parseObj = require('./gpsParser.js');
const testMsg = "imei:868683026873335,tracker,170209044352,,F,204347.000,A,0126.2760,S,04829.4726,W,0.00,0;";
const MAX_MSG = 10;
var msgCounter = 0;
var parsedMsg = null;

// GPS Class Setup
var Gps = require('./gps.js');
var currentGPS = new Gps();

/* UDP SERVER CODE START*/
udp_socket.on("listening", function() {
  updateConsole();
  var address = udp_socket.address();
  console.log("UDP-SERVER/listening: UDP server listening on " + address.address + ":" + address.port + "\n");
});

udp_socket.on("message", function(msg, rinfo) {
  updateConsole();
  console.log("UDP-SERVER/message: Udp server got a message from " + rinfo.address + ":" + rinfo.port + "\n");
  var msgToParse = msg.toString('utf-8');
  parsedMsg = parseObj.parseMsg(msgToParse);
  if(parsedMsg != null){
    currentGPS.updateCurrentGpsInfo(parsedMsg.id,parsedMsg.latitude,parsedMsg.longitude);
  }else{
    console.log("UDP-SERVER/message: Empty msg." + "\n");
  }
});

udp_socket.on("error", function(err) {
  updateConsole();
  console.log("UDP-SERVER/error: \n" + err.stack + "\n");
  udp_socket.close();
});

udp_socket.on("close", function() {
  updateConsole();
  console.log("UDP-SERVER/close: Server successfully closed." + "\n");
});

udp_socket.bind(udp_port, '10.0.0.4');

/* UDP SERVER CODE END*/

/* HTTP SERVER CODE START */
http_socket.get('/busLocation',function(req,res){
  updateConsole();
  console.log('HTTP-SERVER/get: Received a get request from ' + req.ip + "\n");
  res.send(currentGPS);
});


http_socket.post('/',function(req,res){
  updateConsole();
  console.log(req.body);
  console.log('post');
});

http_socket.listen(8080,'10.0.0.4',function(){
  updateConsole();
  console.log("HTTP-SERVER/listening: HTTP server listening on port 8080" + "\n");
});

/* HTTP SERVER CODE END */
function updateConsole(){
  msgCounter++;
  if(msgCounter === MAX_MSG){
    process.stdout.write('\033c');
    msgCounter = 0;
  }
}
