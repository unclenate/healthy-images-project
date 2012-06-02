package gis.cityreports.android;

import java.io.Serializable;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class CategoryDetails implements Comparable, Serializable {
	
    private String instance_id;
    private String category_id;
    
    private String iphone_binary_input_id;
    private String iphone_binary_input_required;

    private String iphone_text_input_id;    
    private String iphone_text_input_required;    

    private String iphone_address_input_id;    
    private String iphone_address_input_required;    

    private String iphone_status_input_id;    
    
    private String iphone_input_alias;    

    private String iphone_message;    
    
    private String iphone_contact_required;

    
	/**
	 * 
	 */
	public CategoryDetails() {
		// TODO Auto-generated constructor stub
	}
	
	public String getValue() {
        return category_id;
    }

    public String toString() {
        return iphone_input_alias;
    }


	/**
	 * @return the instance_id
	 */
	public String getInstance_id() {
		return instance_id;
	}


	/**
	 * @param instanceId the instance_id to set
	 */
	public void setInstance_id(String instanceId) {
		instance_id = instanceId;
	}


	/**
	 * @return the category_id
	 */
	public String getCategory_id() {
		return category_id;
	}


	/**
	 * @param categoryId the category_id to set
	 */
	public void setCategory_id(String categoryId) {
		category_id = categoryId;
	}


	/**
	 * @return the iphone_binary_input_id
	 */
	public String getIphone_binary_input_id() {
		return iphone_binary_input_id;
	}


	/**
	 * @param iphoneBinaryInputId the iphone_binary_input_id to set
	 */
	public void setIphone_binary_input_id(String iphoneBinaryInputId) {
		iphone_binary_input_id = iphoneBinaryInputId;
	}


	/**
	 * @return the iphone_binary_input_required
	 */
	public String getIphone_binary_input_required() {
		return iphone_binary_input_required;
	}


	/**
	 * @param iphoneBinaryInputRequired the iphone_binary_input_required to set
	 */
	public void setIphone_binary_input_required(String iphoneBinaryInputRequired) {
		iphone_binary_input_required = iphoneBinaryInputRequired;
	}


	/**
	 * @return the iphone_text_input_id
	 */
	public String getIphone_text_input_id() {
		return iphone_text_input_id;
	}


	/**
	 * @param iphoneTextInputId the iphone_text_input_id to set
	 */
	public void setIphone_text_input_id(String iphoneTextInputId) {
		iphone_text_input_id = iphoneTextInputId;
	}


	/**
	 * @return the iphone_text_input_required
	 */
	public String getIphone_text_input_required() {
		return iphone_text_input_required;
	}


	/**
	 * @param iphoneTextInputRequired the iphone_text_input_required to set
	 */
	public void setIphone_text_input_required(String iphoneTextInputRequired) {
		iphone_text_input_required = iphoneTextInputRequired;
	}


	/**
	 * @return the iphone_address_input_id
	 */
	public String getIphone_address_input_id() {
		return iphone_address_input_id;
	}


	/**
	 * @param iphoneAddressInputId the iphone_address_input_id to set
	 */
	public void setIphone_address_input_id(String iphoneAddressInputId) {
		iphone_address_input_id = iphoneAddressInputId;
	}


	/**
	 * @return the iphone_address_input_required
	 */
	public String getIphone_address_input_required() {
		return iphone_address_input_required;
	}


	/**
	 * @param iphoneAddressInputRequired the iphone_address_input_required to set
	 */
	public void setIphone_address_input_required(String iphoneAddressInputRequired) {
		iphone_address_input_required = iphoneAddressInputRequired;
	}


	/**
	 * @return the iphone_status_input_id
	 */
	public String getIphone_status_input_id() {
		return iphone_status_input_id;
	}


	/**
	 * @param iphoneStatusInputId the iphone_status_input_id to set
	 */
	public void setIphone_status_input_id(String iphoneStatusInputId) {
		iphone_status_input_id = iphoneStatusInputId;
	}


	/**
	 * @return the iphone_input_alias
	 */
	public String getIphone_input_alias() {
		return iphone_input_alias;
	}


	/**
	 * @param iphoneInputAlias the iphone_input_alias to set
	 */
	public void setIphone_input_alias(String iphoneInputAlias) {
		iphone_input_alias = iphoneInputAlias;
	}


	/**
	 * @return the iphone_message
	 */
	public String getIphone_message() {
		return iphone_message;
	}


	/**
	 * @param iphoneMessage the iphone_message to set
	 */
	public void setIphone_message(String iphoneMessage) {
		iphone_message = iphoneMessage;
	}
	
	
	/**
	 * @return the iphone_contact_required
	 */
	public String getIphone_contact_required() {
		return iphone_contact_required;
	}

	/**
	 * @param iphoneContactRequired the iphone_contact_required to set
	 */
	public void setIphone_contact_required(String iphoneContactRequired) {
		iphone_contact_required = iphoneContactRequired;
	}	

	public int compareTo(Object obj) {
		// TODO Auto-generated method stub
		
		CategoryDetails category = (CategoryDetails) obj;
	    int result = iphone_input_alias.compareTo(category.getIphone_input_alias());
	    
		return result;
	}
	
	 public boolean equals(Object obj) {
		 
		 if (!(obj instanceof CategoryDetails)) {
		      return false;
		 }
		 
		 CategoryDetails category = (CategoryDetails) obj;
		 
		 return iphone_input_alias.equals(category.getIphone_input_alias());
	 }
	


}








