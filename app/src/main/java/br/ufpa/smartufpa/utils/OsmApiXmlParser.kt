package br.ufpa.smartufpa.utils

import android.util.Xml
import br.ufpa.smartufpa.models.overpass.OsmUser
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

// Not using namespaces
private val ns: String? = null

class OsmApiXmlParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parseUser(inputStream: InputStream): OsmUser? {
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readUserResponse(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readUserResponse(parser: XmlPullParser): OsmUser? {
        var osmUser: OsmUser? = null

        parser.require(XmlPullParser.START_TAG, ns, "osm")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "user") {
                osmUser = OsmUser(userId = parser.getAttributeValue(null, "id"),
                        displayName = parser.getAttributeValue(null, "display_name"),
                        accountCreated = parser.getAttributeValue(null, "account_created"))
            } else {
                skip(parser)
            }
        }
        return osmUser
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}