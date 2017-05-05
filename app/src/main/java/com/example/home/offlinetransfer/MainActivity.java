package com.example.home.offlinetransfer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    ListView clientlist;
    public WifiManager wifiManager;
    public Context context;
    Boolean connectedtoSender=false;
    WifiManager mainWifi;
    WifiReceiver receiverWifi=null;
    ArrayList<String> Clients;
    RelativeLayout wifilayout;
    String amount=null;
    String SendAmount=null;

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
        clientlist=(ListView) findViewById(R.id.clientList);
        wifilayout=(RelativeLayout) findViewById(R.id.wifi);
                wifilayout.setVisibility(View.GONE);

        sharedPreferences= getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();

        if(sharedPreferences.getInt("canSend",-1)==-1){
            edit.putInt("canSend",500);
            edit.putInt("received",100);
            edit.commit();
        }

        cansend.setText(String.valueOf(sharedPreferences.getInt("canSend",0)));
        received.setText(String.valueOf(sharedPreferences.getInt("received",0)));

        clientlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ssid=Clients.get(position);
                Log.i("Connect to ",ssid);
                if(!connectedtoSender)
                    connect(ssid);

                wifilayout.setVisibility(View.GONE);


            }
        });


        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"TopUP",Toast.LENGTH_SHORT).show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter the Amount");

                final EditText input = new EditText( context);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        amount = input.getText().toString();

                        if(true)
                        SendAmount=amount;

                        wifilayout.setVisibility(View.VISIBLE);
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

                builder.setNegativeButton("Cancel",null);

                builder.show();
            }
        });



        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotspotController.createWifiAccessPoint("AccessPoint",context);
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
        new ServerSocketHandler(this,received).execute();
        Log.i("Status ","Free");
        return;
    }
    public void showReceivers(ArrayList<String> SSID){

        Clients=new ArrayList<>();

        for(String s:SSID) {
            Log.i("Wifi", s);
            if(s.matches("1.*1") || true)
                Clients.add(s);
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,Clients);
        clientlist.setAdapter(adapter);



        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        Log.i("Status",wifiManager.getConnectionInfo().getSupplicantState().name());


        return;
    }

    public void connect(String SSID){

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + SSID + "\"";
        conf.preSharedKey = "\""+ "logitech" +"\"";
       // conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        Log.i("Inside Connect",SSID);

                wifiManager.disconnect();
                wifiManager.enableNetwork(wifiManager.addNetwork(conf),true);
                wifiManager.reconnect();
                connectedtoSender=true;
                Log.i("Status","Unregister Service");

                while (!wifiManager.getConnectionInfo().getSSID().equals("\""+SSID+"\""))
                    Log.i("SSID",wifiManager.getConnectionInfo().getSSID());

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {


                       Log.i("Service","Connected");
                       unregisterReceiver(receiverWifi);
                        startClientSocket();
                   }
               },1000);





        return;
    }

    public void  startClientSocket(){

        new ClientSocketHandler(this,cansend).execute(SendAmount);
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
