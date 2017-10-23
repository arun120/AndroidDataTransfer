package com.example.home.offlinetransfer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Home on 04-05-2017.
 */
public class ServerSocketHandler extends AsyncTask<Void,Void,String> {
    private Context context;
    private Activity activity;
    private  ServerSocket serverSocket;
    TextView received;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;

    ServerSocketHandler(Context c,TextView tv,Activity a){
        context=c;
        activity=a;
        received=tv;
    }
    ProgressDialog mProgressDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog=new ProgressDialog(activity);
        mProgressDialog.setMessage("Waiting for Sender...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        String transferedAmount=res;
        sharedPreferences= context.getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        int a=sharedPreferences.getInt("received",-1);
        a+=Integer.valueOf(transferedAmount);
        edit.putInt("received",a);
        edit.commit();
        received.setText(String.valueOf(a));


       HotspotController.turnoffWifiAccessPoint(context);
        mProgressDialog.dismiss();
        Toast.makeText(context,"Transfered "+res,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected String doInBackground(Void... params) {
    String res=null;
        try {
            serverSocket = new ServerSocket(5101);
           // serverSocket.setSoTimeout(10000);


            while (true){
                System.out.println("Waiting for client ");
                Socket server=serverSocket.accept();
                System.out.println("Connected");
                DataInputStream in = new DataInputStream(server.getInputStream());

                res=in.readUTF();
                Log.i("Received",res);
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                Integer.valueOf(res);
                out.writeUTF("Success "+res);
                server.close();

                break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }
}
