// function Gps(id,latitude,longitude) {
function Gps(id,currentLocation,lastLocation) {
  this.id = id ;
  // this.latitude = latitude;
  // this.longitude = longitude;
  this.currentLocation = {
    latitude: 0.0,
    longitude: 0.0
  };
  this.lastLocation = {
    latitude: 0.0,
    longitude: 0.0
  };
}


Gps.prototype.getCurrentLocation = function(){
  return this.currentLocation;
};

Gps.prototype.getLastLocation = function(){
  return this.lastLocation;
};

Gps.prototype.setCurrentLocation = function(latitude, longitude) {
  this.currentLocation.latitude = latitude;
  this.currentLocation.longitude = longitude;
};

Gps.prototype.setLastLocation = function(latitude,longitude) {
  this.lastLocation.latitude = latitude;
  this.lastLocation.longitude = longitude;

};

Gps.prototype.getId = function(){
  return this.id;
};

// Gps.prototype.getLatitude = function(){
//   return this.latitude;
// };
//
// Gps.prototype.getLongitude = function(){
//   return this.longitude;
// };

Gps.prototype.updateGpsInfo = function(id,latitude,longitude){
  this.id = id;
  this.setLastLocation(this.currentLocation.latitude,this.currentLocation.longitude);
  this.setCurrentLocation(latitude,longitude);
  console.log("GPS/updateGpsInfo: Info updated successfully." + "\n");
};


module.exports = Gps;
