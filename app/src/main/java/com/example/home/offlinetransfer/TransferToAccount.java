package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Home on 10-05-2017.
 */
public class TransferToAccount extends AsyncTask<String,Void,Void> {

        Context context;
        SharedPreferences sharedPreferences=null;
        SharedPreferences.Editor edit=null;
        TextView receive;

        TransferToAccount(Context c,TextView tv){
        context=c;
        receive=tv;
        }

@Override
protected void onPreExecute() {
        super.onPreExecute();
        }

@Override
protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        receive.setText(String.valueOf(sharedPreferences.getInt("received",0)));
        Toast.makeText(context,"Transferd Amount",Toast.LENGTH_SHORT).show();
        }

@Override
protected Void doInBackground(String... params) {

        sharedPreferences= context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        String number=sharedPreferences.getString("Number",null);
        sharedPreferences= context.getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        Integer amount=sharedPreferences.getInt("received",0);

        String res=HTTPClient.post(ServerDetails.BaseURL+"deposite","amount="+amount+"&number="+number);
        if(res.equals("success")){

                edit.putInt("received",0);
                edit.commit();
        }

        return null;
        }
        }
