package com.example.home.offlinetransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;
    TextView cansend;
    TextView received;
    Button send,receive,topup,transfer;
    public WifiManager wifiManager;
    public Context context;
    Boolean connectedtoSender=false;
    WifiManager mainWifi;
    WifiReceiver receiverWifi=null;

    StringBuilder sb = new StringBuilder();

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=getApplicationContext();
        cansend=(TextView) findViewById(R.id.canSend);
        received=(TextView) findViewById(R.id.pendingTransefer);
        send=(Button) findViewById(R.id.send);
        receive=(Button) findViewById(R.id.receive);
        topup=(Button) findViewById(R.id.topup);
        transfer=(Button) findViewById(R.id.completeTransfer);

        sharedPreferences= getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();

        if(sharedPreferences.getInt("canSend",-1)==-1){
            edit.putInt("canSend",500);
            edit.putInt("received",100);
            edit.commit();
        }

        cansend.setText(String.valueOf(sharedPreferences.getInt("canSend",0)));
        received.setText(String.valueOf(sharedPreferences.getInt("received",0)));

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"TopUP",Toast.LENGTH_SHORT).show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                connectedtoSender=false;
                if (receiverWifi==null) {
                    receiverWifi = new WifiReceiver();
                }
                Log.i("Service ID",String.valueOf(receiverWifi));
                registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                Log.i("Service ID",String.valueOf(receiverWifi));
                if(mainWifi.isWifiEnabled()==false)
                {
                    mainWifi.setWifiEnabled(true);
                }


                doInback();
            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWifiAccessPoint("AccessPoint");
                transferDataReceiver();
            }
        });
    }


    @Override
    protected void onPause()
    {
        super.onPause();
     //    Log.i("Service ID",String.valueOf(receiverWifi));
      //  unregisterReceiver(receiverWifi);

    }

    public void transferDataReceiver(){

        Log.i("Start","Socket");
        new ServerSocketHandler(this).execute();
        Log.i("Status ","Free");
        return;
    }
    public void showReceivers(ArrayList<String> SSID){

        for(String s:SSID)
        Log.i("Wifi",s);

        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        Log.i("Status",wifiManager.getConnectionInfo().getSupplicantState().name());
        if(!connectedtoSender)
                connect("AccessPoint");
        return;
    }

    public void connect(String SSID){

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + SSID + "\"";
        //conf.preSharedKey = "\""+ "logitech" +"\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);


        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + SSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                connectedtoSender=true;
                Log.i("Status","Unregister Service");

                while (!wifiManager.getConnectionInfo().getSSID().equals("\""+SSID+"\""))
                    Log.i("SSID",wifiManager.getConnectionInfo().getSSID());

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {

                       ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                       NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                      // while (!mWifi.isConnected()) ;
                       Log.i("Service","Connected");
                       unregisterReceiver(receiverWifi);
                        startClientSocket();
                   }
               },1000);

                break;
            }
        }

        return;
    }

    public void  startClientSocket(){

        new ClientSocketHandler(this).execute("250");
    }

    public void doInback()
    {
        handler.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if(receiverWifi==null) {
                    receiverWifi = new WifiReceiver();

                    registerReceiver(receiverWifi, new IntentFilter(
                            WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                }
                mainWifi.startScan();
                doInback();
            }
        }, 1000);

    }


    private void createWifiAccessPoint(String name) {
        WifiManager wifiManager = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();

        for(Method method: wmMethods){
            if(method.getName().equals("setWifiApEnabled")){

                WifiConfiguration netConfig = new WifiConfiguration();

                netConfig.SSID = name;
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                try {
                    Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled",  WifiConfiguration.class, boolean.class);
                    setWifiApMethod.invoke(wifiManager, netConfig,true);

                    Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
                    while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){};
                    Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
                    getWifiApStateMethod.invoke(wifiManager);
                    Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
                    netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(wifiManager);
                    Log.e("CLIENT", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        }

    }




    class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {

            ArrayList<String> connections=new ArrayList<String>();


            sb = new StringBuilder();
            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            for(int i = 0; i < wifiList.size(); i++)
            {

                connections.add(wifiList.get(i).SSID);

            }

            showReceivers(connections);
        }
    }

}
