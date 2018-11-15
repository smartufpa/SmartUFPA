package br.ufpa.smartufpa.utils.osm

import br.ufpa.smartufpa.utils.Constants.OsmXmlTags.*
import br.ufpa.smartufpa.utils.Constants.OsmXmlAttr.*
import org.xmlpull.v1.XmlSerializer

private val emptyString = ""
private val version = "0.6"
private val generator = "Smart-Ufpa"

fun XmlSerializer.openOsmTag() {
    this.startTag(emptyString, TAG_OSM)
}

fun XmlSerializer.closeOsmTag() {
    this.endTag(emptyString, TAG_OSM)
}

fun XmlSerializer.openChangeSetTag() {
    this.startTag(emptyString, TAG_CHANGESET)
    this.attribute(emptyString, ATTR_VERSION, version)
    this.attribute(emptyString, ATTR_GENERATOR, generator)
}

fun XmlSerializer.closeChangesetTag() {
    this.endTag(emptyString, TAG_CHANGESET)
}

fun XmlSerializer.createTagKV(key: String, value: String) {
    this.startTag(emptyString, TAG_TAG)
    this.attribute(emptyString, ATTR_KEY, key)
    this.attribute(emptyString, ATTR_VALUE, value)
    this.endTag(emptyString, TAG_TAG)

}