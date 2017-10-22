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

import java.io.Console;

/**
 * Created by Home on 10-05-2017.
 */
public class Topup extends AsyncTask<String,Void,Void> {

    Context context;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;
    TextView canSend;
    Topup(Context c, TextView tv){
        context=c;
        canSend=tv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        canSend.setText(String.valueOf(sharedPreferences.getInt("canSend",0)));
        Toast.makeText(context,"Topup Successful",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(String... params) {

        sharedPreferences= context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        String acnumber=sharedPreferences.getString("acnumber",null);
        sharedPreferences= context.getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        Integer amount=sharedPreferences.getInt("canSend",0);

        Log.i("Can spend ",String.valueOf(amount));


        String res=HTTPClient.postBOB("Cust2CustFundsTrf","{" +
                " \"Dr_Acct\":\" "+acnumber+" \"," +
                " \"Cr_ Acct\":\" "+ ServerDetails.serveraccount +" \"," +
                " \"Tran_Amt\":\" "+ amount +"  \"," +
                " \"Tran_Msg\":\"Offline Wallet TopUp\"" +
                "}");
        Log.i("Response",res);
        JSONParser parser=new JSONParser();
        try {
            JSONArray array=(JSONArray) parser.parse(res);
            JSONObject json=(JSONObject) array.get(0);

            if( (json.get("Trans_Status")).equals("OK")){
                amount+=Integer.valueOf(params[0]);
                edit.putInt("canSend",amount);
                edit.commit();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
