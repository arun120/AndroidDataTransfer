package com.example.home.offlinetransfer;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Home on 04-05-2017.
 */
public class ClientSocketHandler extends AsyncTask<String,Void,String> {

    Context context;

    ClientSocketHandler(Context c){
        context=c;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        Toast.makeText(context,"Transfered "+res,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {

        String res=null;


        String serverName = "192.168.43.1";
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
