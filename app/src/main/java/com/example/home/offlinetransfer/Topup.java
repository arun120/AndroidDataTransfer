package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
        String number=sharedPreferences.getString("Number",null);
        sharedPreferences= context.getSharedPreferences("Balance", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        Integer amount=sharedPreferences.getInt("canSend",0);
        Log.i("Can spend ",String.valueOf(amount));
        amount=500-amount;
        Log.i("request",String.valueOf(amount));

        String res=HTTPClient.post(ServerDetails.BaseURL+"withdraw","amount="+amount+"&number="+number);
        if(res.equals("success")){

            edit.putInt("canSend",500);
            edit.commit();
        }

        return null;
    }
}
