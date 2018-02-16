package com.examples.akshay.wifip2p;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ash on 16/2/18.
 */

public class ServerSocketThread extends AsyncTask {

    private static final String TAG = "===ServerSocketThread";
    ServerSocket serverSocket;
    String receivedData = "null";
    private int port = 8888;
    private boolean interrupted = false;
    OnUpdateListener listener;

    public ServerSocketThread() {
    }

    public interface OnUpdateListener {
        public void onUpdate(String data);
    }

    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Object[] objects) {
        try {

            Log.d(ServerSocketThread.TAG," started DoInBackground");
            serverSocket = new ServerSocket(8888);

            while (!interrupted) {
                Socket client = serverSocket.accept();

                Log.d(ServerSocketThread.TAG,"Accepted Connection");
                InputStream inputstream = client.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                Log.d(ServerSocketThread.TAG,"Completed ReceiveDataTask");
                receivedData = sb.toString();

                if (listener != null) {
                    listener.onUpdate(receivedData);
                }

                Log.d(ServerSocketThread.TAG," ================ " + receivedData);
            }
            serverSocket.close();

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(ServerSocketThread.TAG,"IOException occurred");
        }
        return null;
    }


    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }



}

