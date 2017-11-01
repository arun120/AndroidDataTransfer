package com.example.home.offlinetransfer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WifiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class WifiFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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
    Dialog dialog;


    StringBuilder sb = new StringBuilder();

    private final Handler handler = new Handler();



    public WifiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        context=getActivity().getApplicationContext();
        cansend=(TextView) getActivity().findViewById(R.id.canSend);
        received=(TextView) getActivity().findViewById(R.id.pendingTransefer);
        send=(Button) getActivity().findViewById(R.id.send);
        receive=(Button) getActivity().findViewById(R.id.receive);
        topup=(Button) getActivity().findViewById(R.id.topup);
        transfer=(Button) getActivity().findViewById(R.id.completeTransfer);
        dialog = new Dialog(getActivity());
        //clientlist=(ListView) getActivity().findViewById(R.id.clientList);
        wifilayout=(RelativeLayout) getActivity().findViewById(R.id.wifi);
        wifilayout.setVisibility(View.GONE);

        sharedPreferences= context.getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();

        if(sharedPreferences.getInt("canSend",-1)==-1){
            edit.putInt("canSend",0);
            edit.putInt("received",0);
            edit.commit();
        }

        cansend.setText(String.valueOf(sharedPreferences.getInt("canSend",0)));
        received.setText(String.valueOf(sharedPreferences.getInt("received",0)));





        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TransferToAccount(context,received).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter the Amount");

                final EditText input = new EditText( context);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                input.setTextColor(Color.BLACK);
                builder.setView(input);


                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String  amt = input.getText().toString();
                        new Topup(context,cansend).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,amt);

                    }
                });

                builder.setNegativeButton("Cancel",null);

                builder.show();


            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter the Amount");

                final EditText input = new EditText( context);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                input.setTextColor(Color.BLACK);
                builder.setView(input);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        amount = input.getText().toString();

                        if(true)
                            SendAmount=amount;

                        wifilayout.setVisibility(View.VISIBLE);
                        mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        connectedtoSender=false;
                        if (receiverWifi==null) {
                            receiverWifi = new WifiReceiver();
                        }
                        Log.i("Service ID",String.valueOf(receiverWifi));
                        context.registerReceiver(receiverWifi, new IntentFilter(
                                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                        Log.i("Service ID",String.valueOf(receiverWifi));
                        if(mainWifi.isWifiEnabled()==false)
                        {
                            mainWifi.setWifiEnabled(true);
                        }

                        List<String> permissionsList = new ArrayList<String>();

                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                        }
                        if (permissionsList.size() > 0) {
                            ActivityCompat.requestPermissions( getActivity(), permissionsList.toArray(new String[permissionsList.size()]),
                                    124);
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
                SharedPreferences sp=context.getSharedPreferences("Data", Context.MODE_PRIVATE);
                HotspotController.createWifiAccessPoint("1"+sp.getString("Name","Unknown")+"1",context);
                transferDataReceiver();
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void transferDataReceiver(){

        Log.i("Start","Socket");
        new ServerSocketHandler(context,received,getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.i("Status ","Free");
        return;
    }
    public void showReceivers(ArrayList<String> SSID){

        Clients=new ArrayList<>();

        for(String s:SSID) {
            Log.i("Wifi", s);
            if(s.matches("1.*1"))
                Clients.add(s);
        }

      //  ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,Clients);
      //  clientlist.setAdapter(adapter);


        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Choose receiver...");
        clientlist= (ListView) dialog.findViewById(R.id.List);

        ArrayAdapter<String> adapter = new ArrayAdapter(context,R.layout.customtext, Clients);
        clientlist.setAdapter(adapter);
        if(!dialog.isShowing())
            dialog.show();

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


        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        Log.i("Status",wifiManager.getConnectionInfo().getSupplicantState().name());


        return;
    }

    public void connect(String SSID){

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + SSID + "\"";
        //conf.preSharedKey = "\""+ "logitech" +"\"";
         conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);

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
                context.unregisterReceiver(receiverWifi);
                dialog.dismiss();
                startClientSocket();
            }
        },1000);





        return;
    }

    public void  startClientSocket(){

        new ClientSocketHandler(context,cansend).execute(SendAmount);
    }

    public void doInback()
    {
        handler.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if(receiverWifi==null) {
                    receiverWifi = new WifiReceiver();

                    context.registerReceiver(receiverWifi, new IntentFilter(
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
            Log.i("Number of Wifis",String.valueOf(wifiList.size()));
            for(int i = 0; i < wifiList.size(); i++)
            {

                connections.add(wifiList.get(i).SSID);

            }

            showReceivers(connections);
        }
    }


}
