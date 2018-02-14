package com.examples.akshay.wifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by ash on 14/2/18.
 */

public class MyPeerListener implements WifiP2pManager.PeerListListener {
    public static final String TAG = "===MyPeerListener";
    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        Log.d(MyPeerListener.TAG,"OnPeerAvailable()");
        if(wifiP2pDeviceList != null ) {
            for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                Log.d(MyPeerListener.TAG,"Device name :"+device.deviceName);
            }
        }
        else {
            Log.d(MyPeerListener.TAG," wifiP2pDeviceList is null");

        }
    }

}
