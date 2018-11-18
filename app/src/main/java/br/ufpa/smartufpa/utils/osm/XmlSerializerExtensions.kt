package br.ufpa.smartufpa.utils.osm

import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.Constants.OsmXmlTags.*
import br.ufpa.smartufpa.utils.Constants.OsmXmlAttr.*
import org.xmlpull.v1.XmlSerializer

private val emptyNamespace = ""
private val version = "0.6"
private val generator = "Smart-Ufpa"
fun XmlSerializer.openOsmTag() {
    this.startTag(emptyNamespace, TAG_OSM)
}

fun XmlSerializer.closeOsmTag() {
    this.endTag(emptyNamespace, TAG_OSM)
}

fun XmlSerializer.openCreationChangeSetTag() {
    this.startTag(emptyNamespace, TAG_CHANGESET)
    this.attribute(emptyNamespace, ATTR_VERSION, version)
    this.attribute(emptyNamespace, ATTR_GENERATOR, generator)
}

fun XmlSerializer.closeChangesetTag() {
    this.endTag(emptyNamespace, TAG_CHANGESET)
}

fun XmlSerializer.createTagKV(key: String, value: String) {
    this.startTag(emptyNamespace, TAG_TAG)
    this.attribute(emptyNamespace, ATTR_KEY, key)
    this.attribute(emptyNamespace, ATTR_VALUE, value)
    this.endTag(emptyNamespace, TAG_TAG)

}

fun XmlSerializer.openOsmChangeTag() {
    this.startTag(emptyNamespace, TAG_OSM_CHANGE)
    this.attribute(emptyNamespace, ATTR_VERSION, version)
    this.attribute(emptyNamespace, ATTR_GENERATOR, generator)
}

fun XmlSerializer.openOsmChangeModifyTag() {
    this.startTag(emptyNamespace, TAG_MODIFY)
}

fun XmlSerializer.closeOsmChangeModifyTag() {
    this.endTag(emptyNamespace, TAG_MODIFY)
}

fun XmlSerializer.insertOsmChangeCreateTag() {
    this.startTag(emptyNamespace, TAG_CREATE)
    this.endTag(emptyNamespace, TAG_CREATE)
}

fun XmlSerializer.insertOsmChangeModifyTag() {
    this.startTag(emptyNamespace, TAG_MODIFY)
    this.endTag(emptyNamespace, TAG_MODIFY)
}

fun XmlSerializer.closeEditOsmChangeTag() {
    this.endTag(emptyNamespace, TAG_OSM_CHANGE)
}

fun XmlSerializer.insertOsmChangeDeleteTag() {
    this.startTag(emptyNamespace, TAG_DELETE)
    this.endTag(emptyNamespace, TAG_DELETE)
}

fun XmlSerializer.openOsmChangeCreateTag(){
    this.startTag(emptyNamespace, TAG_CREATE)
}

fun XmlSerializer.closeOsmChangeCreateTag(){
    this.endTag(emptyNamespace, TAG_CREATE)
}



// Node
fun XmlSerializer.insertNodeTag(element: Element, changeSetId: String, nodeVersion: String?) {
    val tagsMap: HashMap<String, String> = element.tags?.getTags()!!
    val tagsKeys = if (tagsMap.size > 0) tagsMap.keys else null

    this.startTag(emptyNamespace, TAG_NODE)
    this.attribute(emptyNamespace, ATTR_ID, element.id.toString())
    if (nodeVersion != null) {
        this.attribute(emptyNamespace, ATTR_VERSION, nodeVersion)
    }
    this.attribute(emptyNamespace, ATTR_CHANGESET_ID, changeSetId)
    this.attribute(emptyNamespace, ATTR_LAT, element.lat.toString())
    this.attribute(emptyNamespace, ATTR_LON, element.lon.toString())


    if (tagsKeys != null) {
        for (key in tagsKeys) {
            this.createTagKV(key, tagsMap[key]!!)
        }
    }
    this.endTag(emptyNamespace, TAG_NODE)
}

// Way
fun XmlSerializer.insertWayTag(element: Element, changeSetId: String, wayVersion : String?) {
    val tagsMap: HashMap<String, String> = element.tags?.getTags()!!
    val tagsKeys = if (tagsMap.size > 0) tagsMap.keys else null
    this.startTag(emptyNamespace, TAG_WAY)

    // Adds id attributes
    this.attribute(emptyNamespace, ATTR_ID, element.id.toString())
    if (wayVersion != null) {
        this.attribute(emptyNamespace, ATTR_VERSION, wayVersion)
    }
    this.attribute(emptyNamespace, ATTR_CHANGESET_ID, changeSetId)

    // Adds nodes related to the way
    for (nodeRef in element.nodes!!){
        this.startTag(emptyNamespace, TAG_WAY_NODE)
        this.attribute(emptyNamespace, ATTR_REF, nodeRef)
        this.endTag(emptyNamespace, TAG_WAY_NODE)
    }

    // Adds the tags related to the way
    if (tagsKeys != null) {
        for (key in tagsKeys) {
            this.createTagKV(key, tagsMap[key]!!)
        }
    }

    this.endTag(emptyNamespace, TAG_WAY)



}



