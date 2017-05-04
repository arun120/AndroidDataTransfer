package com.example.home.offlinetransfer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
    private  ServerSocket serverSocket;

    ServerSocketHandler(Context c){
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
                out.writeUTF("Success");
                server.close();

                break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }
}
