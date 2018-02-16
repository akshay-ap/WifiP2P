package com.examples.akshay.wifip2p;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ash on 16/2/18.
 */

public class ClientSocket extends AsyncTask<Void,Void,Void> {

    private static final String TAG = "===ClientSocket";
    private String ip = "192.168.49.1";
    private int port = 8888;
    private Socket socket;
    public ClientSocket() {

    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            socket = new Socket(ip,port);
            //socket.connect();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.bind();



        return null;
    }
}
