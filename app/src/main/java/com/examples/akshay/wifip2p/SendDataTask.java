package com.examples.akshay.wifip2p;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by ash on 14/2/18.
 */

public class SendDataTask extends AsyncTask {

    private static final String TAG = "===SendDataTask";
    private Context context;
    private final DeviceLink deviceLink;

    public SendDataTask(Context context, DeviceLink deviceLink) {
        this.context = context;
        this.deviceLink = deviceLink;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d(SendDataTask.TAG, "Starting SendDataTask");
        sendData();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d(SendDataTask.TAG, "SendDataTask Completed");
    }


    public void sendData() {
        String data = "===========";
        deviceLink.send(data);
    }
}
