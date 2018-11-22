package br.ufpa.smartufpa.models.overpass

import android.os.Parcel
import android.os.Parcelable
import br.ufpa.smartufpa.R
import com.google.gson.annotations.SerializedName

class Element : Parcelable{

    @SerializedName("type")
    var type: String? = null

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("version")
    var version: Int = 0

    @SerializedName("lat")
    var lat: Double? = null
    get(){
        if(field == null)
            return center?.lat
        return field
    }

    @SerializedName("lon")
    var lon: Double? = null
    get(){
        if(field == null)
            return center?.lon

        return field
    }

    @SerializedName("tags")
    var tags: Tags? = null

    @SerializedName("center")
    var center: Center? = null

    @SerializedName("nodes")
    var nodes : List<String>? = null

    constructor(){
        this.tags = Tags()
        this.version = 1
    }

    fun getName(): String? {
        this.tags.let {
            return it?.name
        }
    }

    fun getMarkerIcon(): Int {
        return this.tags?.markerIconRes!!
    }
    fun setName(name: String){
        this.tags.let {
            it?.name = name
        }
    }

    fun getShortName(): String? {
        this.tags.let {
            return it?.shortName
        }
    }

    fun setShortName(shortName: String){
        this.tags.let {
            it?.shortName = shortName
        }
    }

    fun getLocalName(): String? {
        this.tags.let {
            return it?.locName
        }
    }

    fun setLocalName(localName: String) {
        this.tags.let {
            it?.locName = localName
        }
    }

    fun getWebsite(): String? {
        this.tags.let {
            return it?.website
        }
    }

    fun setWebSite(webSite: String) {
        this.tags.let {
            it?.website = webSite
        }

    }

    fun getDescription(): String? {
        this.tags.let {
            return it?.description
        }
    }
    fun setDescription(description: String) {
        this.tags.let {
            it?.description = description
        }
    }

    fun setIndoor(indoor: Boolean) {
        this.tags.let{
            it?.indoor = if(indoor) "yes" else "no"
        }
    }







    /* Parcelable implementation */
    constructor(parcel: Parcel) : this() {
        type = parcel.readString()
        id = parcel.readLong()
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        lon = parcel.readValue(Double::class.java.classLoader) as? Double
        tags = parcel.readParcelable(Tags::class.java.classLoader)
        center = parcel.readParcelable(Center::class.java.classLoader)
        nodes = parcel.createStringArrayList()
        version = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeLong(id)
        parcel.writeValue(lat)
        parcel.writeValue(lon)
        parcel.writeParcelable(tags, flags)
        parcel.writeParcelable(center, flags)
        parcel.writeStringList(nodes)
        parcel.writeInt(version)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }


}
