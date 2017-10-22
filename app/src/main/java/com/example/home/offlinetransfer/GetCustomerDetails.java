package com.example.home.offlinetransfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fluffy on 22-10-2017.
 */
public class GetCustomerDetails extends AsyncTask<String,Void,List<String>> {

    Context context;
    Activity activity;
    RelativeLayout select,viewData;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;

    GetCustomerDetails(Context c,Activity a){
        context=c;
        activity=a;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final List<String> list) {
        super.onPostExecute(list);

        final ListView name;
                name=(ListView) activity.findViewById(R.id.accountlist);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
        name.setAdapter(arrayAdapter);

        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                select=(RelativeLayout) activity.findViewById(R.id.layoutselect);
                viewData=(RelativeLayout) activity.findViewById(R.id.layoutview);
                select.setVisibility(View.GONE);
                Toast.makeText(context,list.get(position),Toast.LENGTH_SHORT).show();
                new GetAccountDetails(context,activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,list.get(position));
                viewData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected List<String> doInBackground(String... params) {

        List<String> list=new ArrayList<>();
        String res=HTTPClient.postBOB("GetCustAccList","{" + "\"Customer_Id\":"+ params[0]+    "}");
        Log.i("Result",res);
        JSONParser parser=new JSONParser();
        try {
            JSONArray array= (JSONArray) parser.parse(res);
            for(Object obj:array){
                JSONObject json=(JSONObject) obj;
                list.add((String)json.get("Account_Number"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }


    public class GetAccountDetails extends AsyncTask<String,Void,String> {

        Context context;
        Activity activity;
        RelativeLayout select,viewData;

        GetAccountDetails(Context c,Activity a){
            context=c;
            activity=a;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final String str) {
            super.onPostExecute(str);

            TextView details=(TextView) activity.findViewById(R.id.viewdetails);
            details.setText(str);

            final Button confirm=(Button) activity.findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences= context.getSharedPreferences("Data", Context.MODE_PRIVATE);
                    edit=sharedPreferences.edit();
                    edit.putString("acnumber",str.substring(str.indexOf("Account Number ")+"Account Number ".length(),str.indexOf("\nAccount Name ")));
                    edit.commit();
                    Toast.makeText(context,str.substring(str.indexOf("Account Number ")+"Account Number ".length(),str.indexOf("\nAccount Name ")),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,HomeActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });


        }

        @Override
        protected String doInBackground(String... params) {

            String str=null;
            String res=HTTPClient.postBOB("GetAccDetails","{" +" \"Account_Number\": \""+ params[0] +"\"" +"}");
            Log.i("Result",res);
            JSONParser parser=new JSONParser();
            try {
                JSONArray array= (JSONArray) parser.parse(res);

                    JSONObject json=(JSONObject) array.get(0);
                    str="Account Number "+params[0]+"\nAccount Name "+json.get("Account_Name")+"\nCustomer Name "+json.get("Account_Name")+"\nBalance "+json.get("Available_Balance");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }



    }
}