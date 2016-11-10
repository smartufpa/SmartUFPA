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
 */

public class OsmXmlParser {
    // We don't use namespaces
    private static final String namespace = null;
    private static final String TAG = "OsmXmlParser";

    public List<Place> parse(String xmlString) throws XmlPullParserException, IOException {
        try {
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

    private List readOsmTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Place> locais = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, namespace, "osm");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the node tag
            if (name.equals("node")) {
                double lat = Double.valueOf(parser.getAttributeValue(null,"lat"));
                double lon = Double.valueOf(parser.getAttributeValue(null,"lon"));
                locais.add(readNode(parser,lat,lon));
            } else {
                skip(parser);
            }
        }
        return locais;
    }


    private Place readNode(XmlPullParser parser, double lat, double lon) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,namespace,"node");
        String nomeLocal = null;
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("tag")){
                nomeLocal = readTag(parser,nomeLocal);
            }
        }
        parser.require(XmlPullParser.END_TAG,namespace,"node");
        return new Place(lat,lon,nomeLocal);
    }

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
