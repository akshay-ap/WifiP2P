package com.examples.akshay.wifip2p;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "===MainActivity";
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiBroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    WifiP2pDevice device;

    Button buttonDiscover;
    Button buttonConnect;
    Button buttonSend;
    Button buttonReceive;
    TextView textViewGroupOwner;
    ListView listViewDevices;
    RadioGroup radioGroup_send_receive;
    TextView textViewStatus;

    int state = 0;

    ArrayAdapter mAdapter;
    WifiP2pDevice[] deviceListItems;

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
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
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
        radioGroup_send_receive = findViewById(R.id.main_activity_radio_group_send_receive);
        textViewStatus = findViewById(R.id.main_activiy_textView_status);


        radioGroup_send_receive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.main_activity_radioButton_enumertor) {
                    Toast.makeText(getApplicationContext(),"You are a enumerator",Toast.LENGTH_SHORT).show();
                } else if (i == R.id.main_activity_radioButton_supervisor) {
                    Toast.makeText(getApplicationContext(),"You are a supervisor",Toast.LENGTH_SHORT).show();
                }

            }
        });


        listViewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            device = deviceListItems[i];
            Toast.makeText(MainActivity.this,"Selected device :"+ device.deviceName ,Toast.LENGTH_SHORT).show();
            }
        });


        //buttonReceive.setVisibility(View.INVISIBLE);
        //buttonSend.setVisibility(View.INVISIBLE);
        //buttonDiscover.setVisibility(View.INVISIBLE);
        //buttonReceive.setVisibility(View.INVISIBLE);

        buttonDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state == 0) {
                    discoverPeers();
                } else if (state == 1) {
                    mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            state = 0;
                            buttonDiscover.setText("Start discovery");
                            Log.d(MainActivity.TAG,"Discovery stooped");
                        }

                        @Override
                        public void onFailure(int i) {
                        }
                    });
                }
            }

        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(device == null) {
                    Toast.makeText(MainActivity.this,"Please discover and select a device",Toast.LENGTH_SHORT).show();
                    return;
                }
                connect(device);
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
                ReceiveDataTask receiveDataTask = new ReceiveDataTask(getApplicationContext(), serverSocket);
                receiveDataTask.execute();
            }
        });


    }

    private void discoverPeers()
    {
        Log.d(MainActivity.TAG,"Discovering peers");
        setDeviceList(new ArrayList<WifiP2pDevice>());
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                state = 1;
                buttonDiscover.setText("Stop discovery");
                Log.d(MainActivity.TAG," peer discovery started");
                MyPeerListener myPeerListener = new MyPeerListener(MainActivity.this);
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

    public void connect (final WifiP2pDevice device) {
        // Picking the first device found on the network.

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.d(MainActivity.TAG, "Connected to :" + device.deviceName);
                Toast.makeText(getApplication(),"Connection successful with " + device.deviceName,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                if(reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d(MainActivity.TAG, "P2P_UNSUPPORTED");

                }
                else if( reason == WifiP2pManager.ERROR) {
                    Log.d(MainActivity.TAG, "Conneciton falied : ERROR");

                }
                else if( reason == WifiP2pManager.BUSY) {
                    Log.d(MainActivity.TAG, "Conneciton falied : BUSY");

                }
            }
        });
    }

    public void setDeviceList(ArrayList<WifiP2pDevice> deviceDetails) {

        deviceListItems = new WifiP2pDevice[deviceDetails.size()];
        String[] deviceNames = new String[deviceDetails.size()];
        for(int i=0 ;i< deviceDetails.size(); i++){
            deviceNames[i] = deviceDetails.get(i).deviceName;
            deviceListItems[i] = deviceDetails.get(i);
        }
        mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,deviceNames);
        listViewDevices.setAdapter(mAdapter);
    }

    public void setStatusView(int status) {

        switch (status)
        {
            case Constants.DISCOVERY_INITATITED:
                state = 1;
                textViewStatus.setText("DISCOVERY_INITATITED");
                //buttonDiscover.setEnabled(false);
                break;
            case Constants.DISCOVERY_STOPPED:
                state = 0;
                textViewStatus.setText("DISCOVERY_STOPPED");
                //buttonDiscover.setEnabled(true);
                break;
            case Constants.P2P_WIFI_DISABLED:
                textViewStatus.setText("P2P_WIFI_DISABLED");
                buttonReceive.setEnabled(false);
                buttonSend.setEnabled(false);
                buttonDiscover.setEnabled(false);
                buttonReceive.setEnabled(false);

                break;
            case Constants.P2P_WIFI_ENABLED:
                textViewStatus.setText("P2P_WIFI_ENABLED");
                buttonReceive.setEnabled(true);
                buttonSend.setEnabled(true);
                buttonDiscover.setEnabled(true);
                buttonReceive.setEnabled(true);
                break;


            default:
                Log.d(MainActivity.TAG,"Unknown status");
                break;
        }
    }


}
