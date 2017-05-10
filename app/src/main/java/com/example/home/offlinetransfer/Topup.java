package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * Created by Home on 10-05-2017.
 */
public class Topup extends AsyncTask<String,Void,Void> {

    Context context;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;

    Topup(Context c){
        context=c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... params) {

        String amount=params[0];
        sharedPreferences= context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        String number=sharedPreferences.getString("Number",null);

        String res=HTTPClient.post(ServerDetails.BaseURL+"withdraw","amount="+amount+"&number="+number);


        return null;
    }
}
