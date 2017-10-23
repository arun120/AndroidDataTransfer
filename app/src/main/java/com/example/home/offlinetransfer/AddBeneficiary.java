package com.example.home.offlinetransfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Fluffy on 22-10-2017.
 */
public class AddBeneficiary extends AsyncTask<String,Void,String> {

    Context context;
    Activity activity;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;

    AddBeneficiary(Context c,Activity a){
        context=c;
        activity=a;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(context,aVoid,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {


        sharedPreferences= context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        String number=sharedPreferences.getString("acnumber",null);

        String res=HTTPClient.post(ServerDetails.BaseURL+"Beneficiary/"+number,null,params[0]);
        //Log.i("Response",res);
        JSONParser parser=new JSONParser();
        try {
            JSONObject json=(JSONObject) parser.parse(res);
            res=(String)json.get("reponse");
        } catch (ParseException e) {
            e.printStackTrace();
        }


            String jsonarray=sharedPreferences.getString("transactiondetails","[]");
            params[0]=params[0].replace("}",", \"id\": \"" +res+"\" }" );

       // Log.i("Json",params[0]);
            if(jsonarray.equals("[]"))
                jsonarray=jsonarray.replace("]",params[0]+"]");
            else
            jsonarray=jsonarray.replace("]",","+params[0]+"]");
            edit.putString("transactiondetails",jsonarray);
            edit.commit();

          return "Beneficiary Added Successfully!!";



    }
}
