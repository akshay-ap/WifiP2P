package com.examples.akshay.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaActionSound;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

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
            Log.d(WifiBroadcastReceiver.TAG,"WIFI_P2P_PEERS_CHANGED_ACTION");
            if (mManager != null) {
                MyPeerListener myPeerListener = new MyPeerListener(mActivity);
                mManager.requestPeers(mChannel, myPeerListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (mManager == null) {
                return;
            }
            NetworkInfo networkInfo = intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            WifiP2pInfo p2pInfo = intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);

            if (p2pInfo != null && p2pInfo.groupOwnerAddress != null) {
                String goAddress = Utils.getDottedDecimalIP(p2pInfo.groupOwnerAddress
                        .getAddress());
                boolean isGroupOwner = p2pInfo.isGroupOwner;
                Log.d(WifiBroadcastReceiver.TAG,"I am a group owner");
            }
            if (networkInfo.isConnected()) {
                mActivity.setStatusView(Constants.NETWORK_CONNECT);
                // we are connected with the other device, request connection
                // info to find group owner IP
                //mManager.requestConnectionInfo(mChannel, mActivity);
            } else {
                // It's a disconnect
                Log.d(WifiBroadcastReceiver.TAG,"Its a disconnect");
                mActivity.setStatusView(Constants.NETWORK_DISCONNECT);

                //activity.resetData();
            }
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
