package com.gbosystems.utilities;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Primarily a wrapper class for a SAXParser instance. Also contains various
 * methods for parsing that do not implement the SAXParser and instead use
 * regular expressions or simple string functions.
 *
 * @author Geoff O'Donnell
 */
public class GSParser {

    /* Declare class members */
    private static GSParser instance = null;
    private SAXParser parser;
    
    /**
     * Retrieve the instance of Parser.
     */
    public static GSParser getInstance() {
    	
    	/* Create a new instance if none exists */
        if (instance == null) {
            instance = new GSParser();
        }
        
        return instance;
    }

    /**
     * Private constructor.
     */
    private GSParser() {
    	
    	/* Declare local variables */
        SAXParserFactory factory = SAXParserFactory.newInstance();
        
        /* Instantiate a new SAXParser */
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Parse the content described by the giving Uniform Resource Identifier
     * (URI) as XML using the specified DefaultHandler.
     * @param uri The location of the content to be parsed.
     * @param dh The SAX DefaultHandler to use.
     */    
    public void parse(String uri, DefaultHandler dh) {
    	
    	/* Parse the data located at the supplied URI with supplied handler */
        try {
            parser.parse(uri, dh);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Parse the content of the given InputSource instance as XML
     * using the specified DefaultHandler.
     * @param is an InputSource containing the content to be parsed.
     * @param dh the SAX DefaultHandler to use.
     */   
    public void parse(InputSource is, DefaultHandler dh) {
    	
    	/* Parse the data in the InputSource with supplied handler */
        try {
            parser.parse(is, dh);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Return the value for the supplied header, null if not found.
     * @param content Packet data
     * @param headerName Header name
     * @return a String representing the headers value
     */    
    public static String parseHeaderValue(String content, String headerName) {
        
    	/* Declare local variables */
    	Scanner s = new Scanner(content);
    	String line = null;
    	int index = -1;
    	
    	/* Skip the start line */
        s.nextLine(); 
        
        /* Find the header */
        while (s.hasNextLine()) {
        	
            line = s.nextLine();
            index = line.indexOf(':');
            
            if ( (index = line.indexOf(':')) != -1 ){
                String header = line.substring(0, index);
                if (headerName.equalsIgnoreCase(header.trim())) {
                    return line.substring(index + 1).trim();
                }
            }
        }

        /* Return null if header not found */
        return null;
    }
    
    /**
     * Return the value for the supplied header, null if not found.
     * @param packet Packet data
     * @param headerName Header name
     * @return a String representing the headers value
     */ 
    public static String parseHeaderValue(DatagramPacket packet, String headerName) {
        return parseHeaderValue(new String(packet.getData()), headerName);
    }
    
    /**
     * Return the IP address contained in the the supplied URL String,
     * null if not found.
     * @param url URL String
     * @return IP address String
     */ 
    public static String parseIpAddressFromUrl(String url){
    	
    	/* Declare local variables */
    	Pattern p = Pattern.compile("([0-9]{1,3}+)\\.([0-9]{1,3}+)\\.([0-9]{1,3}+)\\.([0-9]{1,3}+)");
    	Matcher m = p.matcher(url);
    	String ipAddress = null; 
    	
    	if (m.find()) {
            ipAddress = m.group(1) + "." + m.group(2) + "." + m.group(3) + "." + m.group(4);
    	}
    	
    	return ipAddress;
    }
    
    /**
     * Returns the name of the specified file without the extension.
     * 
     * @param in input File
     * @return a String representing the file name without the file extension
     */
    public static String filenameWithoutExtension(File in){
        return in.getName().replaceFirst("[.][^.]+$", "");
    }

}
