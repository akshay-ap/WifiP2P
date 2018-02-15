package com.examples.akshay.wifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by ash on 14/2/18.
 */

public class SendDataTask extends AsyncTask {

    private static final String TAG = "===SendDataTask";
    private Context context;
    private String data = "";
    public SendDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d(SendDataTask.TAG,"Starting SendDataTask");
        sendData();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d(SendDataTask.TAG,"SendDataTask Completed");
    }


    public void sendData()
    {


        String host = "192.168.49.1";
        String data = "===========";
        int port = 8888;
        int len;
        Socket socket = new Socket();
        byte buf[]  = new byte[1024];

        try {
            /**
             * Create a client socket with the host,
             * port, and timeout information.
             */
            socket.bind(null);
            Log.d(SendDataTask.TAG,"Trying to connect...");

            socket.connect((new InetSocketAddress(host, port)), 500);
            Log.d(SendDataTask.TAG,"Connected...");
            /**
             * Create a byte stream from a JPEG file and pipe it to the output stream
             * of the socket. This data will be retrieved by the server device.
             */
            OutputStream outputStream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            inputStream = new ByteArrayInputStream(data.getBytes());

            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            //catch logic
            Log.d(SendDataTask.TAG,e.toString());
        } catch (IOException e) {
            //catch logic
            Log.d(SendDataTask.TAG,e.toString());
        }

        /**
         * Clean up any open sockets when done
         * transferring or if an exception occurred.
         */
        finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        //catch logic
                    }
                }
            }
        }


    }
}
