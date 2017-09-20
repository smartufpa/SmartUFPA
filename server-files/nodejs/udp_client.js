// var port = 80;
// var host = '104.41.62.111';
//
// var dgram = require('dgram');
// var message = new Buffer('My KungFu is Good!');
//
// var client = dgram.createSocket('udp4');
// client.send(message, 0, message.length, port, host, function(err, bytes) {
//     if (err) throw err;
//     console.log('UDP message sent to ' + host +':'+ port);
//     client.close();
// });

var PORT = 80;
var HOST = '104.41.62.111';

var dgram = require('dgram');
var message = new Buffer('"imei:868683026873335,tracker,170209044352,,F,204347.000,A,0126.2760,S,04829.4726,W,0.00,0;"');

var client = dgram.createSocket('udp4');
client.send(message, 0, message.length, PORT, HOST, function(err, bytes) {
    if (err) throw err;
    console.log('UDP message sent to ' + HOST +':'+ PORT);
    client.close();
});
