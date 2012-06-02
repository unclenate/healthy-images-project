/**
 * 
 */
package gis.cityreports.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.Base64OutputStream;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 * 
 * Portions Copyright 1994-2006 W Randolph Franklin (WRF)
 * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
 *
 */
public class Utils {

	private double[] yCoords = new double[35];
	private double[] xCoords = new double[35];
		
	
	/**
	 * 
	 */
	public Utils() {
		// TODO Auto-generated constructor stub
		yCoords[0] = -122.764273482324; 
		xCoords[0] = 45.655554637746;
		yCoords[1] = -122.702403202537;
		xCoords[1] = 45.6187116938937;
		yCoords[2] = -122.690711190107;
		xCoords[2] = 45.6286944080437;
		yCoords[3] = -122.641795627773;
		xCoords[3] = 45.6129210615254;
		yCoords[4] = -122.612876805976;
		xCoords[4] = 45.6126851140898;
		yCoords[5] = -122.586994389802;
		xCoords[5] = 45.6054526451392;
		yCoords[6] = -122.57715828134;
		xCoords[6] = 45.5966417838354;
		yCoords[7] = -122.532402904163;
		xCoords[7] = 45.5727862029936;
		yCoords[8] = -122.470970578188;
		xCoords[8] = 45.5622985782448;
		yCoords[9] = -122.469277572091;
		xCoords[9] = 45.5451207366167;
		yCoords[10] = -122.488325836407;
		xCoords[10] = 45.5463337694745;
		yCoords[11] = -122.48665806736;
		xCoords[11] = 45.5404557261804;
		yCoords[12] = -122.49323247247;
		xCoords[12] = 45.5403419093664;
		yCoords[13] = -122.49292185098;
		xCoords[13] = 45.5210856321073;
		yCoords[14] = -122.478308035901;
		xCoords[14] = 45.521081342262;
		yCoords[15] = -122.474715037096;
		xCoords[15] = 45.4702921381664;
		yCoords[16] = -122.491887344921;
		xCoords[16] = 45.4707659490151;
		yCoords[17] = -122.491230865052;
		xCoords[17] = 45.4520289702228;
		yCoords[18] = -122.653630250684;
		xCoords[18] = 45.4524351309282;
		yCoords[19] = -122.663469556816;
		xCoords[19] = 45.4424900663915;
		yCoords[20] = -122.661197825358;
		xCoords[20] = 45.4307181232791;
		yCoords[21] = -122.747637425353;
		xCoords[21] = 45.4303412860042;
		yCoords[22] = -122.747801772923;
		xCoords[22] = 45.4621873876939;
		yCoords[23] = -122.75691953741;
		xCoords[23] = 45.4620084640207;
		yCoords[24] = -122.757286470332;
		xCoords[24] = 45.4712478064152;
		yCoords[25] = -122.749980917379;
		xCoords[25] = 45.4711343774928;
		yCoords[26] = -122.749926111197;
		xCoords[26] = 45.5250731077031;
		yCoords[27] = -122.786447516748;
		xCoords[27] = 45.5246090412258;
		yCoords[28] = -122.787002500818;
		xCoords[28] = 45.5474573397718;
		yCoords[29] = -122.821273297381;
		xCoords[29] = 45.5899207334168;
		yCoords[30] = -122.839570533467;
		xCoords[30] = 45.5900637663305;
		yCoords[31] = -122.838944662987;
		xCoords[31] = 45.6103673568999;
		yCoords[32] = -122.82395280584;
		xCoords[32] = 45.6106710794386;
		yCoords[33] = -122.766408163769;
		xCoords[33] = 45.6541968585544;
		yCoords[34] = -122.764273482324;
		xCoords[34] = 45.655554637746;

	}
	
	public static String cleanXml(String s) 
    {
    	StringBuffer sb = new StringBuffer();
        int len = s.length();
        
        for(int i = 0; i < len; i++) 
        {
        	char c = s.charAt(i);
            
        	switch(c) 
        	{
            	case '<': sb.append("&lt;"); break;
            	case '>': sb.append("&gt;"); break;
            	case '&': sb.append("&amp;"); break;
            	case '"': sb.append("&quot;"); break;
            	case '\'': sb.append("&apos;"); break;
            	default: sb.append(c); break;
            	
            }
        }
        return sb.toString().trim();
    }
	
	
	public boolean locationIsInside(double latitude, double longitude)
	{

		
		boolean isValidLocation = false;
				
	    if (latitude != 0 && longitude != 0)
	    {
	        int i, j = 0, vertexCount = 35;
	        
	        double textX = latitude;
			double testY = longitude;
	        
	        for (i = 0, j = vertexCount-1; i < vertexCount; j = i++) {
	        	if ((((yCoords[i] <= testY) && (testY < yCoords[j])) || ((yCoords[j] <= testY) && (testY < yCoords[i]))) && (textX < (xCoords[j]-xCoords[i]) * (testY-yCoords[i]) / (yCoords[j]-yCoords[i]) + xCoords[i]))
	        		isValidLocation = !isValidLocation;
	        }
	    }
	    return isValidLocation;
	}
	
	public static String objectToString(Serializable object) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try {
	        new ObjectOutputStream(out).writeObject(object);
	        byte[] data = out.toByteArray();
	        out.close();

	        out = new ByteArrayOutputStream();
	        Base64OutputStream b64 = new Base64OutputStream(out);
	        b64.write(data);
	        b64.close();
	        out.close();

	        return new String(out.toByteArray());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public static Object stringToObject(String encodedObject) {
	    try {
	        return new ObjectInputStream(new Base64InputStream(
	                new ByteArrayInputStream(encodedObject.getBytes()))).readObject();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static boolean isNetworkAvailable(Activity mActivity) {
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static String appendDelimiter(Iterable<?> elements, String delimiter) {  
		StringBuilder sb = new StringBuilder();  

		for (Object e : elements) {  
			
			if (sb.length() > 0)  
				sb.append(delimiter);   
			
			sb.append(e);  
		}  
		return sb.toString();  
	}  


}










