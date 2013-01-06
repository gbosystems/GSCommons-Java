package com.gbosystems.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A class for system specific methods and information.
 * 
 * @author Geoff O'Donnell
 */
public class GSSystem {

    /* Debug */
    private final static String TAG = "GSSystem";
    private final static boolean D = true;

    /**
     * Copy a File to the specified path.
     * 
     * @param in the File to be copied
     * @param outPath a path copy file
     * @return the newly created File
     */
    public static File fileCopy(File in, String outPath){

        /* Declare local variables */
        File out = new File(outPath);
        InputStream mInputStream;
        OutputStream mOutputStream;
        byte[] buf = new byte[1024];
        int len = -1;

        try{

            /* Create the output file */
            out.createNewFile();

            /* Initialize streams */
            mInputStream = new FileInputStream(in);
            mOutputStream = new FileOutputStream(out);

            /* Transfer the file contents */
            while ((len = mInputStream.read(buf)) > 0){
                mOutputStream.write(buf, 0, len);
            }

            /* Close resources */
            mInputStream.close();
            mOutputStream.close();

        }
        catch(FileNotFoundException ex){
            if (D) { System.out.println(ex.getMessage() + " in the specified directory."); }
        }
        catch(IOException e){
            if (D) { System.out.println(e.getMessage()); }  
        }

        return out;
    }
	
    /**
     * Execute a system command.
     * 
     * @param args a String array containing the command
     * @param listener a listener for the results of the command,
     *   Listener.onRead() is called on each line read
     * @return the Process representing the command, null if an error occurred
     */
    public static Process runSystemCommand(String[] args, GSReadInputStream.Listener listener){

        /* Declare local variables */
        Process mProcess = null;

        /* Execute Command */
        try {

            mProcess = new ProcessBuilder()
            .command(args)
            .redirectErrorStream(true)
            .start();

            /* Get the input stream */
            InputStream in = mProcess.getInputStream();

            /* Hand off the stream for reading */
            new GSReadInputStream(in, listener, GSReadInputStream.MODE_LINE).start();

        } catch (IOException e) {
            /* Do nothing */
        }

        return mProcess;
    }

}
