package gis.cityreports.android;

import gis.cityreports.utils.Utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
public class ItemReportHandler extends DefaultHandler 
{
    private static final String ITEM = "ITEM";
    
    private List<Item> _items;
    
    public ItemReportHandler() {
        this._items = new ArrayList<Item>();
    }
    
    public List<Item> getItems() {
        return this._items;
    }
    
    public void clear() {
    	_items.clear();
    	_items = null;
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    }
    
    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException 
    {
    	//Log.d("localName", localName);
    	
        if (localName.compareToIgnoreCase(ItemReportHandler.ITEM) == 0) 
        {	
        	Hashtable<String, String> itemAttributes = new Hashtable<String, String>(); 
        	String xmlAttName, xmlAttValue, itemCategory = null, itemUniqueId = null;
        	boolean itemIdExists = false;
        	
        	for (int i = 0; i < atts.getLength(); i++) {
                xmlAttName = Utils.cleanXml(atts.getLocalName(i));
                xmlAttValue = Utils.cleanXml(atts.getValue(i));

                if (xmlAttName.equalsIgnoreCase(MyReports.ITEM_UNIQUE_IDENTIFIER)) {
                	itemUniqueId = xmlAttValue;
             	   	itemIdExists = true;
                } else if (xmlAttName.equalsIgnoreCase(MyReports.ITEM_IPHONE_INPUT_ALIAS)) {
                	itemCategory = xmlAttValue;
                }
                                
                if (xmlAttValue != null && xmlAttName != null) {
                	itemAttributes.put( xmlAttName, xmlAttValue );
                }
            }
        	
        	if(itemIdExists) {
        		Item item = new Item(itemUniqueId, itemCategory, itemAttributes);
        		//Log.d("ADDING", "Item " + itemUniqueId + ",  Category - " + itemCategory);
        		_items.add(item);
        	}
        	
        }
    }







}
