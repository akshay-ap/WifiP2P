package com.examples.akshay.wifip2p;

import android.util.Log;

import java.net.ServerSocket;

/**
 * Created by ash on 16/2/18.
 */

public class ServerSocketThread implements Runnable {

    private static final String TAG = "ServerSocketThread";
    ServerSocket serverSocket;
    private int port = 8888;
    public ServerSocketThread() {

    }

    @Override
    public void run() {
        Log.d(ServerSocketThread.TAG,"In ServerSocketThread RUN");
    }
}
