package com.gbosystems.utilities;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * A class for various data conversion methods.
 * 
 * @author Geoff O'Donnell
 */
public class GSData {

    /* DEBUG */
    private final static String TAG = "GSData";
    private final static boolean D = false;

    /* Character sets */
    public final static String CHAR_SET_ASCII = "US-ASCII";
    public final static String CHAR_SET_UTF8 = "UTF-8";

    public final static String CHAR_SET = CHAR_SET_ASCII;
    public final static String AUX_CHAR_SET = CHAR_SET_UTF8;

    private final static String HEXES = "0123456789ABCDEF";
	
    /**
     * Converts a byte array to a String using the default character set.
     * 
     * @param input byte array containing encoded text
     * @return a String holding the converted text
     */
    static public String stringFromBytes(byte[] input){
        return stringFromBytes(input, CHAR_SET);
    }
	
    /**
     * Converts a byte array to a String using the specified character set.
     * 
     * @param input byte array containing encoded text
     * @param charSet character set (CHAR_SET -or- AUX_CHAR_SET)
     * @return a String holding the converted text
     */
    static public String stringFromBytes(byte[] input, String charSet){
    	
    	/* Declare local variables */
    	String returnString = "";
    	
    	/* Convert */
    	if (input != null){
            try {
                returnString = new String(input, 0, input.length, charSet );
            } catch (UnsupportedEncodingException e) {
                if (D) { System.out.println(TAG + ": Data error:" + e.getMessage()); }
            }
    	}
		
	return returnString;
    }
    
    /**
     * Converts a String to a byte array using the default character set
     * 
     * @param input a String containing text to convert
     * @return byte array containing encoded text
     */
    
    static public byte[] bytesFromString(String input){
	return bytesFromString(input, CHAR_SET);
    }
	
    /**
     * Converts a String to a byte array using the specified character set
     * 
     * @param input input a String containing text to convert
     * @param charSet character set (CHAR_SET -or- AUX_CHAR_SET)
     * @return byte array containing encoded text
     */
    
    static public byte[] bytesFromString(String input, String charSet){
    	
    	/* Declare local variables */
    	byte[] returnBytes = null;
    	
    	/* Convert */
    	if ( (input != null) && (!input.equals("") )){
            try {
                returnBytes = input.getBytes(charSet);
            } catch (UnsupportedEncodingException e) {
                if (D) { System.out.println(TAG + ": Data error:" + e.getMessage()); }
            }
    	}
        
        return returnBytes;
    }
    
    /**
     * Returns a String of Hex characters from a byte[] , i.e., 
     * raw = {0x09, 0xFF, 0x32, 0x8D}, return = "09FF328D"
     * 
     * @param raw byte array to be converted
     * @return a String representation of the hex characters
     */
    static public String hexCharsFromBytes( byte [] raw ) {
	    
    	/* Safety check */
    	if ( raw == null ) {
            return null;
	}
    	
    	/* Declare local variables */
        StringBuilder hex = new StringBuilder( 2 * raw.length );

        /* Convert */
        for ( final byte b : raw ) {
          hex.append(HEXES.charAt((b & 0xF0) >> 4))
             .append(HEXES.charAt((b & 0x0F)));
        }

	return hex.toString();
    }
    
    /**
     * Returns a byte[] from a String of Hex characters, i.e., 
     * raw = "09FF328D", return = {0x09, 0xFF, 0x32, 0x8D}
     * 
     * @param raw String to be converted
     * @return a byte representation of the hex characters
     */
    static public byte[] bytesFromHexChars( String raw){
    	
    	/* Declare local variables */
        int len = raw.length();
        byte[] data = new byte[len / 2];
        
        /* Convert */
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(raw.charAt(i), 16) << 4)
                                 + Character.digit(raw.charAt(i+1), 16));
        }
        
        return data;
    }
    
    /**
     * Combine two byte arrays. The new byte[] {A[0],...,A[n],B[0],...,B[n]}
     * 
     * @param A byte array one
     * @param B byte array two
     * @return a byte array: byte[] {A[0],...,A[n],B[0],...,B[n]}
     */
    static public byte[] concatBytes(byte[] A, byte[] B) {
    	
    	/* Declare local variables */
    	byte[] C= new byte[A.length+B.length];
    	
    	/* Combine */
    	System.arraycopy(A, 0, C, 0, A.length);
    	System.arraycopy(B, 0, C, A.length, B.length);
	
    	return C;
    }
 
    /**
     * Converts a 4 byte wide section of a byte array to an (32-bit) integer.
     * 
     * @param array input array
     * @param start start of integer
     * @return the integer result of the conversion
     */
    static public int byteArraySectionToInt(byte[] array, int start){
    	
        ByteBuffer bb = ByteBuffer.allocate(4);
        if (array.length > (start)){ bb.put((byte) array[start]); }
        if (array.length > (start + 1)) { bb.put((byte) array[start + 1]); }
        if (array.length > (start + 2)) { bb.put((byte) array[start + 2]); }
        if (array.length > (start + 3)) { bb.put((byte) array[start + 3]); }
        return bb.getInt(0);
    }
	
}
