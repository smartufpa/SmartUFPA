package br.ufpa.smartufpa.utils.osm

import android.util.Xml
import br.ufpa.smartufpa.models.overpass.Element
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
                serializer.openCreationChangeSetTag()
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

        @JvmStatic
        fun uploadChangeSetXml(element: Element, changeSetId: String, elementVersion: String?): String {
            val serializer : XmlSerializer = Xml.newSerializer()
            val writer = StringWriter()
            serializer.setOutput(writer)
            serializer.startDocument(charsetUtf8,true)
                //<osmChange version="0.6" generator="iD">
                serializer.openOsmChangeTag()
                    //<create></create>
                    serializer.insertOsmChangeCreateTag()

                    //<modify>
                    serializer.openOsmChangeModifyTag()

                    // Inserir dados
                    insertTagByElementType(serializer,element,changeSetId,elementVersion)

                    //</modify>
                    serializer.closeOsmChangeModifyTag()

                    //<delete></delete>
                    serializer.insertOsmChangeDeleteTag()
                //</osmChange>
                serializer.closeEditOsmChangeTag()

            serializer.endDocument()

            return writer.toString()
        }

        private fun insertTagByElementType(serializer: XmlSerializer, element: Element, changeSetId:String , elementVersion: String?) {
            val elementType = element.type
            when (elementType) {
                "node" -> {
                    serializer.insertNodeTag(element, changeSetId, elementVersion)
                }
                "way" -> {
                    serializer.insertWayTag(element,changeSetId, elementVersion)
                }
            }
        }
    }
}

