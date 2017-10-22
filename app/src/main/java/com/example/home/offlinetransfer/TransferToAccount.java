package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        String acnumber=sharedPreferences.getString("acnumber",null);
        sharedPreferences= context.getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        Integer amount=sharedPreferences.getInt("received",0);

        String res=HTTPClient.postBOB("Cust2CustFundsTrf","{" +
                " \"Dr_Acct\":\" "+ServerDetails.serveraccount+" \"," +
                " \"Cr_ Acct\":\" "+ acnumber  +" \"," +
                " \"Tran_Amt\":\" "+ amount +"  \"," +
                " \"Tran_Msg\":\"Offline Wallet TopUp\"" +
                "}");

        Log.i("Response",res);
        JSONParser parser=new JSONParser();
        try {
                JSONArray array=(JSONArray) parser.parse(res);
                JSONObject json=(JSONObject) array.get(0);

                if( (json.get("Trans_Status")).equals("OK")){

                        edit.putInt("received",0);
                        edit.commit();
                }
        } catch (ParseException e) {
                e.printStackTrace();
        }



        return null;
        }
        }
