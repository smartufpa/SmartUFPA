var net = require('net');

var server = net.createServer(function(socket) {
	socket.write('Echo server\r\n');
	socket.pipe(socket);
	console.log("Listening on port 80")
});

server.listen(80, '10.0.0.4');
