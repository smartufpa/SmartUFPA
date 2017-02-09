var express = require('express');
var mysql = require('mysql');
var app = express();


var connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: "root",
  database: 'smart_ufpa'
});

app.get('/',function(req,res){
  // connection.connect();
  // console.log("funciona");
  // connection.query('SELECT * FROM places',function(error,results,fields){
  //   if(error) throw error;
  //
  //   for (var i = 0; i < results.length; i++) {
  //     var place = results[i];
  //     console.log("Lugar " + i + ": \n• id: " + place.id
  //                   +"\n• nome: " + place.name
  //                   +"\n• nome curto: " + place.short_name
  //                   +"\n• descrição: " + place.description
  //                   +"\n• latlong:  " + place.latitude + "," + place.longitude);
  //   }
  //
  //   res.send(results);
  // });
  // connection.end();
    



});


app.post('/',function(req,res){
  console.log(req.body);

});

app.listen(8080,'127.0.0.1',function(){
  console.log("Listening on port 8080");
});
