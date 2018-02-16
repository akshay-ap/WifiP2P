package com.examples.akshay.wifip2p;

import android.app.Activity;
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
 * Created by ash on 16/2/18.
 */

public class ClientSocket extends AsyncTask{
    String data = "This is a recieved string";
    private static final String TAG = "===ClientSocket";
    private Socket socket;
    private Context context;
    //private MainActivity activity;
    public ClientSocket(Context context, MainActivity activity) {
        this.context = context;
    //    this.activity= activity;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        sendData();
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d(ClientSocket.TAG,"SendDataTask Completed");
    }


    public void sendData()
    {
        String host = MainActivity.IP;
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
            Log.d(ClientSocket.TAG,"Trying to connect...");

            socket.connect((new InetSocketAddress(host, port)), 500);
            Log.d(ClientSocket.TAG,"Connected...");


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
            Log.d(ClientSocket.TAG,e.toString());
        } catch (IOException e) {
            //catch logic
            //activity.makeToast(ClientSocket.TAG + " " +e.toString());
            Log.d(ClientSocket.TAG,e.toString());
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


    public void connect(){

    }

    public void disconnect(){

    }


}