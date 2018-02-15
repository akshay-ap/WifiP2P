package com.examples.akshay.wifip2p;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ash on 14/2/18.
 */

public class ReceiveDataTask extends AsyncTask {
    public static final String TAG = "===ReceiveDataTask";
    String receivedData;
    String data = "Yes...data received";
    Context context;
    private final DeviceLink deviceLink;

    public ReceiveDataTask(Context context, DeviceLink deviceLink) {
        this.context = context;
        this.deviceLink = deviceLink;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        receivedData = deviceLink.receive();
        return receivedData;
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.d(ReceiveDataTask.TAG, "Completed receiveing data");
        super.onPostExecute(o);
        Toast.makeText(context, receivedData, Toast.LENGTH_SHORT).show();
    }
}
