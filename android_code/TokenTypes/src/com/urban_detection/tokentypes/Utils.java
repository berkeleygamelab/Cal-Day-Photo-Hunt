package com.urban_detection.tokentypes;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;


/**
 * A class filled with useful utility functions. 
 */
public class Utils {
    public static Document makeXMLDoc(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        
        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            doc = db.parse(convertStringToStream(xml));
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch(SAXException se) {
            se.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        return doc;
    }
    
    public static String webKeyValueLookup(String xml, String key) {
        Document doc = Utils.makeXMLDoc(xml);
        return webKeyValueLookup(doc, key);
    }
    
    public static String webKeyValueLookup(Document doc, String key) {
        NodeList nl = doc.getElementsByTagName(key);
        String value = nl.item(0).getFirstChild().getNodeValue();
        return value;
    }
    
    public static String convertInputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return convertStreamToString(reader);
    }

    public static String convertFileReaderToString(FileReader fr) {
        BufferedReader reader = new BufferedReader(fr);
        return convertStreamToString(reader);
    }
    
    public static String convertStreamToString(BufferedReader reader) {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return sb.toString();
    }
    
    public static InputStream convertStringToStream(String text) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e("Utils.convertStringToStream", "UnsupportedEncodingException");
            e.printStackTrace();
        }
        
        return is;
    }
}
