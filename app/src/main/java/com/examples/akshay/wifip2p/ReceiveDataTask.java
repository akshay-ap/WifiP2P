package com.examples.akshay.wifip2p;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ash on 14/2/18.
 */

public class ReceiveDataTask extends AsyncTask {
    public static final String TAG = "===ReceiveDataTask";
    String receivedData;
    String data = "Yes...data received";
    Context context;
    public ReceiveDataTask(Context context) {
    this.context = context;

    }

    @Override
    protected String doInBackground(Object[] objects) {
        Log.d(ReceiveDataTask.TAG,"Starting receiveing data");
        ServerSocket serverSocket = null;
        try {

            Log.d(ReceiveDataTask.TAG," started DoInBackground");
            serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            Log.d(ReceiveDataTask.TAG,"Accepted Connection");
            InputStream inputstream = client.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
            }
            bufferedReader.close();
            Log.d(ReceiveDataTask.TAG,"Completed ReceiveDataTask");
            receivedData = sb.toString();
            serverSocket.close();
            return receivedData;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(ReceiveDataTask.TAG,"IOException occurred");
        }


        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.d(ReceiveDataTask.TAG,"Completed receiveing data");
        super.onPostExecute(o);
        Toast.makeText(context,receivedData,Toast.LENGTH_SHORT).show();
    }
}
