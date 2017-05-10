package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Home on 10-05-2017.
 */
public class CreateAccount extends AsyncTask<String,Void,Void> {

    Context context;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;

    CreateAccount(Context c){
        context=c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Intent i=new Intent(context,MainActivity.class);
        context.startActivity(i);
    }

    @Override
    protected Void doInBackground(String... params) {

        String name=params[0];
        sharedPreferences= context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        String number=sharedPreferences.getString("Number",null);

        String res=HTTPClient.post(ServerDetails.BaseURL+"signup","name="+name+"&number="+number);
        if(res.equals("success")){

            edit.putString("Name",params[0]);
            edit.commit();
        }

        return null;
    }
}
