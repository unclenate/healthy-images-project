/**
 * 
 */
package org.codeforhealth.hip.android.data;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class ConfigSetting {

	
	private String disclaimerText;
	private String disclaimerFrequency;
	private String gpsAccuracyThreshold;
	private String gpsSampleCount;
	private String gpsSampleInterval;
	private String helpPage;
	private String photoCompression;
	private String photoMaxPixels;
	
	private int androidGpsSampleCount;
	private int maxRetryAttempts;
	
	private int sampleSizeExtra;
	private int sampleSizeLarge;
	private int sampleSizeMedium;
	private int sampleSizeSmall;

	private int androidTimeout;
	private int androidCategoryRefresh;
	
	
		
	/**
	 * @return the androidCategoryRefresh
	 */
	public int getAndroidCategoryRefresh() {
		return androidCategoryRefresh;
	}

	/**
	 * @return the androidTimeout
	 */
	public int getAndroidTimeout() {
		return androidTimeout;
	}

	
	/**
	 * @return the sampleSizeExtra
	 */
	public int getSampleSizeExtra() {
		return sampleSizeExtra;
	}



	/**
	 * @return the sampleSizeLarge
	 */
	public int getSampleSizeLarge() {
		return sampleSizeLarge;
	}



	/**
	 * @return the sampleSizeMedium
	 */
	public int getSampleSizeMedium() {
		return sampleSizeMedium;
	}



	/**
	 * @return the sampleSizeSmall
	 */
	public int getSampleSizeSmall() {
		return sampleSizeSmall;
	}


	/**
	 * @return the androidGpsSampleCount
	 */
	public int getAndroidGpsSampleCount() {
		return androidGpsSampleCount;
	}



	/**
	 * @return the disclaimerText
	 */
	public String getDisclaimerText() {
		return disclaimerText;
	}




	/**
	 * @return the disclaimerFrequency
	 */
	public String getDisclaimerFrequency() {
		return disclaimerFrequency;
	}




	/**
	 * @return the gpsAccuracyThreshold
	 */
	public String getGpsAccuracyThreshold() {
		return gpsAccuracyThreshold;
	}




	/**
	 * @return the gpsSampleCount
	 */
	public String getGpsSampleCount() {
		return gpsSampleCount;
	}




	/**
	 * @return the gpsSampleInterval
	 */
	public String getGpsSampleInterval() {
		return gpsSampleInterval;
	}




	/**
	 * @return the helpPage
	 */
	public String getHelpPage() {
		return helpPage;
	}

	
	/**
	 * @return the maxRetryAttempts
	 */
	public int getMaxRetryAttempts() {
		return maxRetryAttempts;
	}


	/**
	 * @return the photoCompression
	 */
	public String getPhotoCompression() {
		return photoCompression;
	}




	/**
	 * @return the photoMaxPixels
	 */
	public String getPhotoMaxPixels() {
		return photoMaxPixels;
	}



	/**
	 * @param androidGpsSampleCount the androidGpsSampleCount to set
	 */
	public void setAndroidGpsSampleCount(int androidGpsSampleCount) {
		this.androidGpsSampleCount = androidGpsSampleCount;
	}


	/**
	 * @param disclaimerText the disclaimerText to set
	 */
	public void setDisclaimerText(String disclaimerText) {
		this.disclaimerText = disclaimerText;
	}




	/**
	 * @param disclaimerFrequency the disclaimerFrequency to set
	 */
	public void setDisclaimerFrequency(String disclaimerFrequency) {
		this.disclaimerFrequency = disclaimerFrequency;
	}




	/**
	 * @param gpsAccuracyThreshold the gpsAccuracyThreshold to set
	 */
	public void setGpsAccuracyThreshold(String gpsAccuracyThreshold) {
		this.gpsAccuracyThreshold = gpsAccuracyThreshold;
	}




	/**
	 * @param gpsSampleCount the gpsSampleCount to set
	 */
	public void setGpsSampleCount(String gpsSampleCount) {
		this.gpsSampleCount = gpsSampleCount;
	}




	/**
	 * @param gpsSampleInterval the gpsSampleInterval to set
	 */
	public void setGpsSampleInterval(String gpsSampleInterval) {
		this.gpsSampleInterval = gpsSampleInterval;
	}




	/**
	 * @param helpPage the helpPage to set
	 */
	public void setHelpPage(String helpPage) {
		this.helpPage = helpPage;
	}


	/**
	 * @param maxRetryAttempts the maxRetryAttempts to set
	 */
	public void setMaxRetryAttempts(int maxRetryAttempts) {
		this.maxRetryAttempts = maxRetryAttempts;
	}


	/**
	 * @param photoCompression the photoCompression to set
	 */
	public void setPhotoCompression(String photoCompression) {
		this.photoCompression = photoCompression;
	}




	/**
	 * @param photoMaxPixels the photoMaxPixels to set
	 */
	public void setPhotoMaxPixels(String photoMaxPixels) {
		this.photoMaxPixels = photoMaxPixels;
	}
	
	
	/**
	 * @param sampleSizeExtra the sampleSizeExtra to set
	 */
	public void setSampleSizeExtra(int sampleSizeExtra) {
		this.sampleSizeExtra = sampleSizeExtra;
	}



	/**
	 * @param sampleSizeLarge the sampleSizeLarge to set
	 */
	public void setSampleSizeLarge(int sampleSizeLarge) {
		this.sampleSizeLarge = sampleSizeLarge;
	}



	/**
	 * @param sampleSizeMedium the sampleSizeMedium to set
	 */
	public void setSampleSizeMedium(int sampleSizeMedium) {
		this.sampleSizeMedium = sampleSizeMedium;
	}



	/**
	 * @param sampleSizeSmall the sampleSizeSmall to set
	 */
	public void setSampleSizeSmall(int sampleSizeSmall) {
		this.sampleSizeSmall = sampleSizeSmall;
	}	
	
	
	/**
	 * @param androidTimeout the androidTimeout to set
	 */
	public void setAndroidTimeout(int androidTimeout) {
		this.androidTimeout = androidTimeout;
	}


	/**
	 * @param androidCategoryRefresh the androidCategoryRefresh to set
	 */
	public void setAndroidCategoryRefresh(int androidCategoryRefresh) {
		this.androidCategoryRefresh = androidCategoryRefresh;
	}

	

	/**
	 * 
	 */
	public ConfigSetting() {
		// TODO Auto-generated constructor stub
	}


}



