package com.example.kaeuc.osmapp.Extras;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

/**
 * Created by kaeuc on 10/21/2016.
 * Class responsável por analisar o objeto XML retornado da busca à API do OSM (XAPI)
 * Referência (Base do código): https://developer.android.com/training/basics/network-ops/xml.html
 * Checar link para o entendimento mais acertado do código.
 * Os métodos dessa classe serão chamados em cascata, para aprofundar a busca em diferentes níveis
 * do objeto XML
 */

public class OsmXmlParser {
    // We don't use namespaces
    private static final String namespace = null;
    private static final String TAG = "OsmXmlParser";

    // Recebe o bjeto XML em forma de String e irá fazer a chamada dos métodos de leituras das tags
    public List<Place> parse(String xmlString) throws XmlPullParserException, IOException {
        try {
            // É necessário ajustar a codificação do input que vai ler a String para UTF-8
            InputStream in = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readOsmTag(parser);
        } finally {
            in.close();
        }
    }
    /* Busca a tag de entrada "osm" para as informações
    *  O formato do xml seguirá em maioria esse padrão:
    *  <osm version="0.6" generator="Overpass API">
    *   ...
    *   <node id="4455189945" lat="-1.4745308" lon="-48.4563598">
    *       <tag k="amenity" v="restaurant"/>
    *       <tag k="name" v="Restaurante da Néia"/>
    *   </node>
    *   ...
    *  </osm>
    */

    private List readOsmTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Place> places = new ArrayList<>();
        // Procura a tag mais externa, ou seja, de entrada ou START_TAG
        parser.require(XmlPullParser.START_TAG, namespace, "osm");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            /* Se o nome da tag é igual a "node", retira as informações relevantes dos atributos
             * dessa tag
             */
            if (name.equals("node")) {
                double lat = Double.valueOf(parser.getAttributeValue(null,"lat"));
                double lon = Double.valueOf(parser.getAttributeValue(null,"lon"));
                // faz a chamada para a leitura do node que retornará os locais encontrados
                places.add(readNode(parser,lat,lon));
            } else {
                // se a tag não é uma das especificadas de interesse, passa para a próxima linha
                skip(parser);
            }
        }
        return places;
    }

    // Busca a tag de entrada "tag" que é interna a tag "node"
    private Place readNode(XmlPullParser parser, double lat, double lon) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,namespace,"node");
        String nomeLocal = null;
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            /* Se o nome da tag é igual a "tag", retira as informações relevantes dos atributos
             * dessa tag
             */
            if(name.equals("tag")){
                nomeLocal = readTag(parser,nomeLocal);
            }
        }
        parser.require(XmlPullParser.END_TAG,namespace,"node");
        return new Place(lat,lon,nomeLocal);
    }
    // Faz a leitura dos atributos  da tag de nome "tag"
    private String readTag(XmlPullParser parser, String nomeLocal) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,namespace,"tag");
        String attr = parser.getAttributeValue(null,"k");
        if (attr.equals("name")) {
            nomeLocal = parser.getAttributeValue(null, "v");
            parser.nextTag();
        }else{
            if(nomeLocal == null)
                nomeLocal = Constants.NAMEPLACE_UNKNOWN;
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, namespace, "tag");
        return nomeLocal;
    }
    /* Método responsável por pular tags indesejadas
       Referência: https://developer.android.com/training/basics/network-ops/xml.html#skip
     */
    private void skip(XmlPullParser parser)  throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
