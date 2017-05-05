package com.example.home.offlinetransfer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created by Home on 04-05-2017.
 */
public class ClientSocketHandler extends AsyncTask<String,Void,String> {

    Context context;
    TextView canSend;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;

    ClientSocketHandler(Context c,TextView tv){
        context=c;
        canSend=tv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        String transferedAmount=res.split(" ")[1];
        sharedPreferences= context.getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        int a=sharedPreferences.getInt("canSend",-1);
        a-=Integer.valueOf(transferedAmount);
        edit.putInt("canSend",a);
        edit.commit();
        canSend.setText(String.valueOf(a));


        Toast.makeText(context,"Transfered "+transferedAmount,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected String doInBackground(String... params) {

        String res=null;


        String serverName = "192.168.1.4";
        int port = 5101;

        Socket client = null;
        while(true) {
            try {
                Thread.sleep(1000);
                client = new Socket(serverName, port);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {


            System.out.println("Connected");
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(params[0]);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            res=in.readUTF();
            System.out.println("Server  " + res);
            client.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
