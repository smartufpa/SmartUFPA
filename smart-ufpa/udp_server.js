// UDP Sample Server

// UDP
var s_port = 80;
var dgram = require("dgram");
var server = dgram.createSocket("udp4");
const net = require('net');
var parseObj = require('./gpsParser.js');
var testMsg = "imei:868683026873335,tracker,170209044352,,F,204347.000,A,0126.2760,S,04829.4726,W,0.00,0;";
var MAX_MSG = 10;
var msgCounter = 0;
var parsedMsg = null;

// Listen
server.on("listening", function() {
  var address = server.address();
  console.log("SERVER-LOG/listening: Server listening on " + address.address + ":" + address.port + "\n");
});

server.on("message", function(msg, rinfo) {
  console.log("SERVER-LOG/message: Server got a message from " + rinfo.address + ":" + rinfo.port);
  var msgToParse = msg.toString('utf-8');
  parsedMsg = parseObj.parseMsg(msgToParse);
  if(parsedMsg != null){
    console.log("latitude: "  + parsedMsg.latitude);
    console.log("longitude: "  + parsed.longitude);
  }else{
    console.log("Empty msg.");
  }

  if(msgCounter === MAX_MSG){
    process.stdout.write('\033c');
    msgCounter = 0;
  }
});

server.on("error", function(err) {
  console.log("server error: \n" + err.stack);
  server.close();
});

// Socket
server.on("close", function() {
  var client = net.connect(8080,function(){
     client.write(parsedMsg);
     client.end("Client Done.");
  });

  console.log("closed.");
});


server.bind(s_port, '10.0.0.4');
