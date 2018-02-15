package com.examples.akshay.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaActionSound;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by ash on 14/2/18.
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "===WifiBReceiver";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    public WifiBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.d(WifiBroadcastReceiver.TAG,"WIFI P2P ENABLED");
                mActivity.setStatusView(Constants.P2P_WIFI_ENABLED);
            } else {
                Log.d(WifiBroadcastReceiver.TAG,"WIFI P2P NOT ENABLED");
                mActivity.setStatusView(Constants.P2P_WIFI_DISABLED);

            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                Log.d(WifiBroadcastReceiver.TAG,"Peer action changed");

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            Log.d(WifiBroadcastReceiver.TAG,"WIFI_P2P_CONNECTION_CHANGED_ACTION");

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(WifiBroadcastReceiver.TAG,"WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
            // Respond to this device's wifi state changing
        } else if(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, 10000);
            if( state == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED ) {
                mActivity.setStatusView(Constants.DISCOVERY_INITATITED);
            } else if(state == WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED) {
                mActivity.setStatusView(Constants.DISCOVERY_STOPPED);
            }


        }
    }
}
