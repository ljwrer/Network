package com.example.networktest;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Ray on 2015/6/1.
 */
public class MyHandler extends DefaultHandler {

    private String NodeName;
    private StringBuilder id,name,version;
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equals("app"))
        {
            Log.d("MainActivityXml", "id is " + id.toString().trim());
            Log.d("MainActivityXml","name is "+name.toString().trim());
            Log.d("MainActivityXml","version is "+version.toString().trim());
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        id=new StringBuilder();
        name=new StringBuilder();
        version=new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        NodeName=localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        switch (NodeName)
        {
            case "id":
                id.append(ch,start,length);
                break;
            case "name":
                name.append(ch,start,length);
                break;
            case "version":
                version.append(ch,start,length);
                break;
            default:
                break;
        }
    }
}
