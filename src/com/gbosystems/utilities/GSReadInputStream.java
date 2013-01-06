/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbosystems.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class for simple InputStream handling. GSReadInputStream extends Thread and
 * handling reading of InputStreams, the onRead() listener method 
 * 
 * @author Geoff O'Donnell
 */
public class GSReadInputStream extends Thread {
 
    /* Declare read mode constants */
    public static final int MODE_BLOCK = 1;
    public static final int MODE_LINE = 2;

    /* Declare defaults */
    public static final int DEFAULT_BLOCK_SIZE = 256;
    
    /* Declare class members */
    private InputStream mInputStream;
    private Listener mListener;
    private int readMode;
    private int blockSize;

    /**
     * Public constructor.
     * 
     * @param in InputStream to read
     * @param listener event Listener
     * @param mode read mode
     * @param size block size in bytes
     */
    public GSReadInputStream(InputStream in, Listener listener, int mode, int size){

        /* Initialize class members */
        mInputStream = in;
        mListener = listener;
        readMode = mode;
        blockSize = size;
    }

    /**
     * Public constructor using default block size.
     * 
     * @param in InputStream to read
     * @param listener event Listener
     * @param mode read mode
     */
    public GSReadInputStream(InputStream in, Listener listener, int mode){
        this(in, listener, mode, DEFAULT_BLOCK_SIZE);
    }
    
    @Override
    public void run(){

        /* Declare local variables */
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int currentBlock = 0;
        int bytes = 0;

        /* Notify listener that the thread has been started */
        if (mListener != null){
            mListener.onStart();
        }
        
        /* Handle the InputStream */
        try {

            switch (readMode){
            case MODE_BLOCK:
                
                while ( (bytes = mInputStream.read(data, 0, data.length)) != -1 ) {
                    
                    /* Write results to buffer */
                    result.write(data, 0, bytes);
                    
                    /* Increment current byte count */
                    currentBlock += bytes;
                    
                    /* Handle if the blocksize has been met */
                    if (currentBlock >= blockSize){
                        
                        if (mListener != null){
                            mListener.onRead( result.toByteArray() );
                        }
                        
                        /* Reset count and buffer */
                        result.reset();
                        currentBlock = 0;
                    }
                }

                break;

            case MODE_LINE:
                
                while ( (bytes = mInputStream.read()) != -1 ) {

                    /* If not a newline character, write results to buffer */
                    if (bytes != '\n'){
                        result.write(bytes);
                    }
                    /* Handle line read */
                    else {
                        
                        if (mListener != null){
                            mListener.onRead( result.toByteArray() );
                        }

                        result.reset();
                    }
                }

                break;
            default:
                /* Do nothing */
                break;
            }
        } catch (IOException e) {
             //TODO Send error to callback
        } finally {
            if (mListener != null){
                mListener.onFinish();
            }
        }
    }
    
    /**
     * An event listener interface for GSReadInputStream.
     * 
     * @author Geoff O'Donnell
     */
    public interface Listener {
    
        /**
         * Called when the task is started.
         */
        public void onStart();
        
        /**
         * Called on either a complete line read or when the minimum block size
         * has been reached.
         */
        public void onRead(byte[] data);
        
        /**
         * Called when the task is finished.
         */
        public void onFinish();
        
    }
    
}
