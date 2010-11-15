/**
 * 
 */
package gis.cityreports.android;


import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class ResultHandler extends DefaultHandler {

    private StringBuffer accumulator;
    
    private boolean status = false;
    private String itemId = "";
    
    
	public ResultHandler() {
		accumulator = new StringBuffer();
	}
	
    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }
    
    public String getItemId() {
        return itemId;
    }
    
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        
    	accumulator.setLength(0);
    	
    	if (localName.equalsIgnoreCase(Report.item_id))   
    		status = true;

        
    }

       
    @Override
    public void endElement(String namespaceURI, String localName, String qName) {
    	
    	String temp = accumulator.toString().trim();
    	
    	if (status) {    
    		if (StringUtils.isNotBlank(temp)) 
    			itemId = temp;
    		
    		status = false;
    	}
    }

    @Override
    public void characters(char ch[], int start, int length) {
    	accumulator.append(ch, start, length);
    }

    
}

