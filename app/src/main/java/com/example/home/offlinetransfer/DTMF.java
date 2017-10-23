package com.example.home.offlinetransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DTMF extends AppCompatActivity {

    Spinner type,payee;
    EditText amount;
    EditText snumber;
    Button transfer;
    String sType;
    String sPayee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtmf);

        type=(Spinner) findViewById(R.id.type);
        payee=(Spinner) findViewById(R.id.beneficiary);
        amount=(EditText) findViewById(R.id.amout);
        transfer=(Button) findViewById(R.id.transfer);
        snumber=(EditText) findViewById(R.id.servernumber);

        final String[] ttype={"NEFT","RTGS"};

        ArrayAdapter<String> typeadapter=new ArrayAdapter<String>(this,R.layout.customtext,ttype);
        type.setAdapter(typeadapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sType=ttype[position];
                Log.i("sType",sType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        SharedPreferences sharedPreferences=null;
        sharedPreferences= getSharedPreferences("Data", Context.MODE_PRIVATE);
        final List<TransactionDetails> list=new ArrayList<>();
        final List<String> listid=new ArrayList<>();
        JSONParser parser=new JSONParser();
        try {
            Log.i("Array",sharedPreferences.getString("transactiondetails","[]"));
            JSONArray jsonArray=(JSONArray) parser.parse(sharedPreferences.getString("transactiondetails","[]"));
            for(Object obj:jsonArray){
                JSONObject jsonObject=(JSONObject)obj;
                TransactionDetails td=TransactionDetails.fromJson(jsonObject.toJSONString());
                Log.i("data",td.getName());
                list.add(td);
                listid.add((String)jsonObject.get("id"));
            }

           ArrayAdapter<TransactionDetails> payeeadapter=new ArrayAdapter<>(getApplicationContext(),R.layout.customtext,list);
            payee.setAdapter(payeeadapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        payee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPayee= listid.get(position);
                Log.i("sPayee",sPayee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = snumber.getText().toString();
                String amt=amount.getText().toString();
                String type;
                if(sType.equals("NEFT"))
                    type="0";
                else
                    type="1";
                number="1500";
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +number+","+Encoder.encode(sPayee,amt,type)));

                //Log.i("Number Dialled", String.valueOf(Uri.parse("tel:" +"1500,"+ Encoder.encode(amount,accNumber,IFSCCode,acknowledgement))));

                // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +"04422730897,"+"500*123456789*123456789*123456789"));

                if (ActivityCompat.checkSelfPermission(DTMF.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    String[] permission={Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions(DTMF.this,permission,125);
                    // ActivityCompat.requestPermissions( , permissionsList.toArray(new String[permissionsList.size()]),
                     //       124);
                  //  return;
                }
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
}
