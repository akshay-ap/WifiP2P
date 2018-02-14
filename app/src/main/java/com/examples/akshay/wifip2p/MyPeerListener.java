package com.examples.akshay.wifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ash on 14/2/18.
 */

public class MyPeerListener implements WifiP2pManager.PeerListListener {
    public static final String TAG = "===MyPeerListener";
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

        Log.d(MyPeerListener.TAG, "OnPeerAvailable()");
        if(wifiP2pDeviceList != null ) {

            if(wifiP2pDeviceList.getDeviceList().size() == 0) {
                Log.d(MyPeerListener.TAG, "wifiP2pDeviceList size is zero");
                return;
            }
            Log.d(MainActivity.TAG,"");
            for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                if(device.deviceName.equals(MainActivity.DEVICE_TO_SEARCH)) {
                    MainActivity.device = device;
                } else {
                    Log.d(MyPeerListener.TAG, "Found device :" + device.deviceName + " " + device.deviceAddress);
                }
            }
        }
        else {
            Log.d(MyPeerListener.TAG, "wifiP2pDeviceList is null");

        }
    }
}
