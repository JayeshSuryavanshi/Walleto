package com.edubank.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This is a utility class used to convert a 
 * string into its corresponding hashed format 
 * so that we can store the hashed data and not the actual data
 * 
 * This technique is used for storing the secured 
 * information about user like password, pin, CVV.
 * 
 * @author ETA_JAVA
 *
 */
public class Hashing {

	/**
	 * This method is to hash the given data using a hashing algorithm.
	 * 
	 * @param data
	 * 
	 * @return hashed data
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHashValue(String data)
			throws NoSuchAlgorithmException {
		
		/*
		 * This MessageDigest class provides applications the functionality of a
		 * message digest algorithm, such as SHA-1 or SHA-256. Message digests
		 * are secure one-way hash functions that take arbitrary-sized data and
		 * output a fixed-length hash value.
		 */
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		/*
		 * md.update:- Updates the digest using the specified array of bytes.
		 */
		md.update(data.getBytes());
		
		/*
		 * md.didgest():- returns an array of 32 bytes if SHA-256 algorithm is
		 * used, and array of 20 bytes if SHA-1 algorithm is used
		 */
		byte byteData[] = md.digest();

		/*
		 * creating a StringBuffer object for easy append
		 */
		StringBuffer hexString = new StringBuffer();

		/*
		 * convert the byte to hexadecimal format
		 */
		for (int i = 0; i < byteData.length; i++) {
			
			/*
			 * converting the bytes received from md.digest() to positive values
			 * by switching the bit position and converting the result to
			 * hexadecimal format using toHexString method
			 */
			String hex = Integer.toHexString(0xff & byteData[i]);
			
			/*
			 * checking if the size of hexadecimal value is one, the appending a
			 * character '0' (Zero) to it.
			 */
			if (hex.length() == 1)
				hexString.append('0');
			
			/*
			 * appending the result to the hexString which is a StringBuffer
			 */
			hexString.append(hex);
		}
		
		/*
		 * converting the StringBuffer to String value and returning the same.
		 */
		return hexString.toString();
	}
}
