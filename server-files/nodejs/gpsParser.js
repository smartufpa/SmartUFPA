// Identificação da msg
// ID [0]: imei:868683026873335,
// MODELO [1]: tracker,
// DATA HORA(Possivelmente) [2]: 170209044352,
// VAZIO [3]:,
// SINAL (Full/Low) [4]: F,
// NÃO-IDENTIFICADO [5]: 204347.000,
// FIX(Active/invalid) [6]: A,
// NÃO-IDENTIFICADO [7]: 0126.2760,
// USADO NA FUNÇÃO fixgeo [8]: S,
// NÃO-IDENTIFICADO [9]: 04829.4726,
// USADO NA FUNÇÃO fixgeo [10]: W,
// NÃO-IDENTIFICADO [11]: 0.00,
// NÃO-IDENTIFICADO [12]: 0;
//  Array Length = 13

var msg_to_parse = "imei:868683026873335,tracker,170209044352,,F,204347.000,A,0126.2760,S,04829.4726,W,0.00,0;";

var gpsParser = {};

gpsParser.parseMsg = function(gpsMsg){
  var str = [];
  var gps = null;

  try {
    gpsMsg = gpsMsg.trim();
    str = gpsMsg.split(',');

    if(str.length === 13 && str[1] === 'tracker'){
        gps = {
          id: extractId(str[0]),
          latitude: fixGeo(str[7],str[8]),
          longitude: fixGeo(str[9],str[10])
        }


    }

  } catch (e) {
    result = null;
    console.log("Erro");
  }

  return gps;

}

var extractId = function(string){
 var array = string.split(':');
 return array[1];
}

// Clean geo positions, with 6 decimals
var fixGeo = function (one, two) {
  var minutes = one.substr (-7, 7);
  var degrees = parseInt (one.replace (minutes, ''), 10);

  one = degrees + (minutes / 60);
  one = parseFloat ((two === 'S' || two === 'W' ? '-' : '') + one);

  return Math.round (one * 1000000) / 1000000;
};


var http = require('http');
http.createServer(function (req, res) {
  gpsParser.parseMsg(msg_to_parse);
  res.end("done");
}).listen(8080);
console.log('Server running at http://localhost:8080/');


module.exports = gpsParser;
