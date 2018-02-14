package com.examples.akshay.wifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    public static final String TAG = "===MainActivity";
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiBroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    public static WifiP2pDevice device = null;
    public static final String DEVICE_TO_SEARCH = "moto m";


    Button buttonDiscover = null;
    Button buttonConnect = null;
    Button buttonSend = null;
    Button buttonReceive = null;
    TextView textViewGroupOwner = null;
    ListView listViewDevices = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpIntentFilter();
        setUpUI();
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void setUpIntentFilter() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void setUpUI() {
        buttonDiscover = findViewById(R.id.main_activity_button_discover);
        buttonConnect = findViewById(R.id.main_activity_button_connect);
        buttonReceive = findViewById(R.id.main_activity_button_receive);
        buttonSend = findViewById(R.id.main_activity_button_send);
        textViewGroupOwner = findViewById(R.id.main_activity_textview_owner);
        listViewDevices = findViewById(R.id.main_activity_list_view_devices);

        buttonDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discoverPeers();
            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SendDataTask sendDataTask = new SendDataTask(getApplicationContext());
            sendDataTask.execute();
            }
        });

        buttonReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceiveDataTask receiveDataTask = new ReceiveDataTask(getApplicationContext());
                receiveDataTask.execute();
            }
        });


    }

    private void discoverPeers()
    {
        Log.d(MainActivity.TAG,"Discovering peers");

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(MainActivity.TAG," peer discovery successful");
                MyPeerListener myPeerListener = new MyPeerListener();
                mManager.requestPeers(mChannel,myPeerListener);

            }

            @Override
            public void onFailure(int i) {
                if (i == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d(MainActivity.TAG," peer discovery failed :" + "P2P_UNSUPPORTED");
                } else if (i == WifiP2pManager.ERROR) {
                    Log.d(MainActivity.TAG," peer discovery failed :" + "ERROR");
                } else if (i == WifiP2pManager.BUSY) {
                    Log.d(MainActivity.TAG," peer discovery failed :" + "BUSY");
                }
            }
        });

    }

    public void connect () {
        // Picking the first device found on the network.

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.d(MainActivity.TAG, "Connected to :" + DEVICE_TO_SEARCH);
                Toast.makeText(getApplication(),"Connection successful with " + DEVICE_TO_SEARCH,Toast.LENGTH_SHORT).show();

              /*  mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                    @Override
                    public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                        WifiP2pDevice wifiP2pDevice = wifiP2pGroup.getOwner();
                        Log.d(MainActivity.TAG,"onGroupInfoAvailable " + wifiP2pDevice.deviceName);
                        textViewGroupOwner.setText(wifiP2pDevice.deviceName);
                    }
                });*/
            }

            @Override
            public void onFailure(int reason) {
                Log.d(MainActivity.TAG, "Connect failed. Retry.");
            }
        });
    }



}
