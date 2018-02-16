package com.examples.akshay.wifip2p;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ash on 16/2/18.
 */

public class ServerSocketThread extends AsyncTask<Void,Void,Void> {

    private static final String TAG = "===ServerSocketThread";
    ServerSocket serverSocket;
    private int port = 8888;
    public ServerSocketThread() {

    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            serverSocket = new ServerSocket(8888);
            Log.d(ServerSocketThread.TAG, "Trying to accept connection");
            Socket client = serverSocket.accept();

            Log.d(ServerSocketThread.TAG, "Client connection successful");
            serverSocket.close();

        } catch (IOException e) {
            Log.d(ServerSocketThread.TAG, e.toString());
            e.printStackTrace();
        }
        return null;
    }

}
