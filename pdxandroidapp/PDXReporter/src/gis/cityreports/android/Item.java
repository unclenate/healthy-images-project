/**
 * 
 */
package gis.cityreports.android;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;

import android.util.Log;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class Item implements Comparable<Item>, Iterable<Object> {

	private String itemId;
	private String category;
	private Hashtable<String, String> attributes = new Hashtable<String, String>(); 

	private int itemID;
	

	/**
	 * @return the attributes
	 */
	public Hashtable<String, String> getAttributes() {
		return attributes;
	}

	public String getCategory() {
		if (category != null) {
			return category;
		}
		return "";
	}
	
	public String getItemId() {
		if (itemId != null) {
			return itemId;
		}
		return "";
	}
	
	/**
	 * @return the itemID
	 */
	public int getItemID() {
		return itemID;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Hashtable<String, String> attributes) {
		this.attributes = attributes;
	}
	
	private void setCategory(String category) {
		if (category != null) {
			this.category = category.trim();
		}
	}

	private void setItemId(String itemId) {
		if (itemId != null) {
			this.itemId = itemId.trim();
		}
	}

	/**
	 * @param itemID the itemID to set
	 */
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int compareTo(Item another) {
		// TODO Auto-generated method stub
		
		//Log.d("DOING COMPARETo - ", "Comparing: " + this.getItemId() + " to " + another.getItemId());
		int i = this.getCategory().compareTo(another.getCategory());
		
		if (i == 0) {
			//Log.d("LOOK EQUAL NOW DOING item compare - ", "Comparing: " + this.getItemId() + " to " + another.getItemId() + " result " + this.getItemId().compareTo(another.getItemId()) );
			
			if (this.getItemID() > another.getItemID()) {
				//reversing the expected results so we get the sort we want
				return -1;
			} else if (this.getItemID() == another.getItemID())
				return 0;
			
			return 1; 
				
			//return this.getItemId().compareTo(another.getItemId());
		}
		return i;
	}
	
	public Item(String itemId, String category, Hashtable<String, String> attributes) {
		setItemId(itemId);
		setItemID(Integer.parseInt(itemId));
		setCategory(category);
		setAttributes(attributes);
	}

	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}


}

