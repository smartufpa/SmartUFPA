function Gps(id,latitude,longitude) {
  this.id = id ;
  this.latitude = latitude;
  this.longitude = longitude;
};

Gps.prototype.getId = function(){
  return this.id;
};

Gps.prototype.getLatitude = function(){
  return this.latitude;
};

Gps.prototype.getLongitude = function(){
  return this.longitude;
};

Gps.prototype.updateCurrentGpsInfo = function(id,latitude,longitude){
  this.id = id;
  this.latitude = latitude;
  this.longitude = longitude;
  console.log("GPS/updateCurrent: Info updated successfully." + "\n");
};


module.exports = Gps;
