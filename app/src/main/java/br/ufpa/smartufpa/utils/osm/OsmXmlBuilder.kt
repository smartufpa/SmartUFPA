package br.ufpa.smartufpa.utils.osm

import android.util.Xml
import org.xmlpull.v1.XmlSerializer
import java.io.StringWriter


class OsmXmlBuilder {
    companion object {

        private val charsetUtf8 = "UTF-8"
        private val keyComment = "comment"

        private val keyCreatedBy = "created_by"
        private val valueCreatedBy = "Smart-Ufpa 1.0"

        private val keyLocale = "locale"
        private val valueLocale = "pt-BR"



        @JvmStatic
        fun createChangeSetXml(commentText: String): String {
            val serializer : XmlSerializer = Xml.newSerializer()
            val writer = StringWriter()

            serializer.setOutput(writer)
            serializer.startDocument(charsetUtf8,true)

            //<osm>
            serializer.openOsmTag()
                //<changeset version="0.6" generator="Smart-Ufpa" >
                serializer.openChangeSetTag()
                    // <tag k="comment" v="commentText"></tag>
                    serializer.createTagKV(keyComment, commentText)

                    // <tag k="created_by" v="Smart-Ufpa 1.0"></tag>
                    serializer.createTagKV(keyCreatedBy, valueCreatedBy)

                    // <tag k="locale" v="pt-BR"></tag>
                    serializer.createTagKV(keyLocale, valueLocale)

                // </changeset>
                serializer.closeChangesetTag()

            //</osm>
            serializer.closeOsmTag()

            serializer.endDocument()

            return writer.toString()

        }
    }
}

