package gis.cityreports.data;

import java.util.Date;

import android.net.Uri;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class ReportInfo {

	private String addressDescription 	= "";
	private String categoryId			= "";
	private String contactEmail			= "";
	private String contactPhone			= "";
	private String contactName			= "";
	private String description 			= "";
	private String deviceId 			= "";
	private String imageAbsolutePath 	= "";
	private String imageFullUriPath		= "";
	private String imageName 			= "";
	private String verificationId 		= "";
	
	private boolean hasContactInfo		= false;
	private boolean validGPSLocation	= false;
	
	private double latitude				= 0.0;
	private double longitude			= 0.0;
	
	private int imageWidth				= 0;
	private int imageHeight				= 0;
	private int instanceId				= 0;
	
	private long creationDateTime;
	private long lastUpdateDateTime;
	
	private long lastContactAlert		= 0;
	
	private Uri imageUri;
	
	
	
	
	
    
	/**
	 * @return the lastContactAlert
	 */
	public long getLastContactAlert() {
		return lastContactAlert;
	}


	/**
	 * @param lastContactAlert the lastContactAlert to set
	 */
	public void setLastContactAlert(long lastContactAlert) {
		this.lastContactAlert = lastContactAlert;
	}


	/**
	 * @return the hasContact
	 */
	public boolean hasContact() {
		return hasContactInfo;
	}


	/**
	 * @param hasContact the hasContact to set
	 */
	public void setHasContact(boolean hasContact) {
		this.hasContactInfo = hasContact;
	}




	/**
	 * @return the creationDateTime
	 */
	public long getCreationDateTime() {
		return creationDateTime;
	}





	/**
	 * @return the lastUpdateDateTime
	 */
	public long getLastUpdateDateTime() {
		return lastUpdateDateTime;
	}





	/**
	 * @param creationDateTime the creationDateTime to set
	 */
	public void setCreationDateTime(long creationDateTime) {
		this.creationDateTime = creationDateTime;
	}





	/**
	 * @param lastUpdateDateTime the lastUpdateDateTime to set
	 */
	public void setLastUpdateDateTime(long lastUpdateDateTime) {
		this.lastUpdateDateTime = lastUpdateDateTime;
	}





	/**
	 * @return the addressDescription
	 */
	public String getAddressDescription() {
		return addressDescription;
	}



	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	

	
	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	

	/**
	 * @return the contactPhone
	 */
	public String getContactPhone() {
		return contactPhone;
	}
	
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		if (description != null)
			return description.trim();
		
		return "";
	}





	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}





	/**
	 * @return the imageAbsolutePath
	 */
	public String getImageAbsolutePath() {
		if (imageAbsolutePath != null)
			return imageAbsolutePath;
		
		return "";
	}


	
	/**
	 * @return the imageFullUriPath
	 */
	public String getImageFullUriPath() {
		return imageFullUriPath;
	}




	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}





	/**
	 * @return the imageUri
	 */
	public Uri getImageUri() {
		return imageUri;
	}





	/**
	 * @return the verificationId
	 */
	public String getVerificationId() {
		return verificationId;
	}





	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}





	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}





	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}





	/**
	 * @return the imageWidth
	 */
	public int getImageWidth() {
		return imageWidth;
	}





	/**
	 * @return the imageHeight
	 */
	public int getImageHeight() {
		return imageHeight;
	}





	/**
	 * @return the instanceId
	 */
	public int getInstanceId() {
		return instanceId;
	}


	public boolean isValidGPSLocation() {
    	return validGPSLocation;
    }



	/**
	 * @param addressDescription the addressDescription to set
	 */
	public void setAddressDescription(String addressDescription) {
		this.addressDescription = addressDescription;
	}



	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	


	
	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	

	
	/**
	 * @param contactPhone the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	
	

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}





	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}





	/**
	 * @param imageAbsolutePath the imageAbsolutePath to set
	 */
	public void setImageAbsolutePath(String imageAbsolutePath) {
		this.imageAbsolutePath = imageAbsolutePath;
	}


	/**
	 * @param imageFullUriPath the imageFullUriPath to set
	 */
	public void setImageFullUriPath(String imageFullUriPath) {
		this.imageFullUriPath = imageFullUriPath;
	}



	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}





	/**
	 * @param imageUri the imageUri to set
	 */
	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}





	/**
	 * @param verificationId the verificationId to set
	 */
	public void setVerificationId(String verificationId) {
		this.verificationId = verificationId;
	}





	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}





	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}





	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}





	/**
	 * @param imageWidth the imageWidth to set
	 */
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}





	/**
	 * @param imageHeight the imageHeight to set
	 */
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}





	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}


    public void setValidGPSLocation(boolean status) {
    	validGPSLocation = status;
    }
    
	

	/**
	 * 
	 */
	public ReportInfo() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void clearImageSetting() 
	{
		this.imageUri = null;
		this.imageName = "";
		this.imageAbsolutePath = "";
		this.imageFullUriPath = "";
		this.imageWidth	= 0;
		this.imageHeight = 0;
		
	}
	
	public void clear() 
	{
		this.addressDescription = "";
		this.categoryId = "";
		//this.contactEmail = "";
		//this.contactName = "";
		//this.contactPhone = "";
		this.description = "";
		//this.deviceId = "";
		this.imageAbsolutePath = "";
		this.imageFullUriPath = "";
		this.imageName = "";
		this.verificationId = "";
		this.latitude = 0;
		this.longitude = 0;
		this.imageWidth	= 0;
		this.imageHeight = 0;
		this.imageUri = null;
		this.instanceId	= 0;
		this.validGPSLocation = false;
	}
	

}
