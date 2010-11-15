package gis.cityreports.android;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class ContactHandler extends DefaultHandler {

    private StringBuffer accumulator;
    
    private boolean status = false;
    private boolean updated = false;
    
    private static final String UPDATED = "UPDATED";
    
	public ContactHandler() {
		accumulator = new StringBuffer();
	}
	
    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }
    
    public boolean isUpdated() {
        return updated;
    }
    
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        
    	accumulator.setLength(0);
    	
    		/* 
    		 * Note, this assumes the xml only had 1 element of each, else the last one found
    		 * will overwrite the previous.  
    		 * 
    		 * ToDo: Add a tracker to which node you are currently in.
    		 */
    	
    		if (localName.equalsIgnoreCase(Report.status))   
    			status = true;

        
    }

       
    @Override
    public void endElement(String namespaceURI, String localName, String qName) {
    	
    	String temp = accumulator.toString().trim();
    	
    	if (status) {    
    		if ( StringUtils.isNotBlank(temp) ) {
    			if ( (temp.compareToIgnoreCase(ContactHandler.UPDATED) == 0) )  {
    				//Log.d("UPDATED!", "WAS ABLE TO UPDATE!");
    				updated = true;
    			}
    			
    		}
    		status = false;
    	}
    }

    @Override
    public void characters(char ch[], int start, int length) {
    	accumulator.append(ch, start, length);
    }

    
}

