package br.ufpa.smartufpa.models.smartufpa

class Building(latitude: Double,
               longitude: Double,
               name: String?,
               shortName: String = "",
               localName: String = "",
               description: String = "",
               var openingTime: String = "",
               var closingTime: String = "",
               var administrativeRole: AdministrativeRole = AdministrativeRole.NONE,
               var website : String = "") : POI(latitude, longitude, name, shortName, localName,description) {

    enum class AdministrativeRole{
        ADMNISTRATIVE, INSTUTE, NONE
    }
}