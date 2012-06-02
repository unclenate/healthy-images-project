package gis.cityreports.android;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */

public class CategoryHandler extends DefaultHandler {

    private static final String POST = "category";

    private List<CategoryDetails> posts;

    
    public CategoryHandler() {
        this.posts = new ArrayList<CategoryDetails>();

        int i = 0;
        String[] categories = {"Diseases & Conditions", "Emergency Preparedness & Response", "Environmental Health", "Healthy Living", "Injury, Violence & Safety", "Life Stages & Populations", "Workplace Safety & Health"};
        for(String cat: categories){
            CategoryDetails post = new CategoryDetails();
            post.setCategory_id(String.valueOf(i));
            post.setInstance_id(String.valueOf(i));
            post.setIphone_input_alias(cat);
            post.setIphone_contact_required("false");
            post.setIphone_address_input_required("false");        	
            this.posts.add(post);
            i++;
        }
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }
    

    public List<CategoryDetails> getPosts() {
        return this.posts;
    }
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    	if (localName.equals(CategoryHandler.POST)) {
        	CategoryDetails post = new CategoryDetails();
            
            post.setInstance_id(getAttributeValue("instance_id", atts));
            post.setCategory_id(getAttributeValue("category_id", atts));
            
            post.setIphone_binary_input_id(getAttributeValue("iphone_binary_input_id", atts));
            post.setIphone_binary_input_required(getAttributeValue("iphone_binary_input_required", atts));
            
            post.setIphone_text_input_id(getAttributeValue("iphone_text_input_id", atts));
            post.setIphone_text_input_required(getAttributeValue("iphone_text_input_required", atts));
            
            post.setIphone_address_input_id(getAttributeValue("iphone_address_input_id", atts));
            post.setIphone_address_input_required(getAttributeValue("iphone_address_input_required", atts));
            
            post.setIphone_status_input_id(getAttributeValue("iphone_status_input_id", atts));
            
            post.setIphone_input_alias(getAttributeValue("iphone_input_alias", atts));
            
            post.setIphone_message(getAttributeValue("iphone_message", atts));
            
            post.setIphone_contact_required(getAttributeValue("iphone_contact_required", atts));
            
            /*
            post.setDesc(getAttributeValue("description", atts));
            post.setHref(getAttributeValue("href", atts));
            post.setTag(getAttributeValue("tag", atts));
            */
            
            this.posts.add(post);
        }
    }

       
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    
    private String getAttributeValue(String attName, Attributes atts) {
        String result = null;
        for (int i = 0; i < atts.getLength(); i++) {
            String thisAtt = atts.getLocalName(i);
            if (attName.equals(thisAtt)) {
                result = atts.getValue(i);
                break;
            }
        }
        return result;
    }
    
    

}
