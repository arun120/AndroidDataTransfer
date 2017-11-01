package com.example.home.offlinetransfer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor edit=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CALL_PHONE);
        }
        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions( HomePage.this, permissionsList.toArray(new String[permissionsList.size()]),
                    123);
        }

        Button signUp=(Button) findViewById(R.id.signUp);
        final EditText number= (EditText) findViewById(R.id.number);
        final EditText password=(EditText) findViewById(R.id.password);
        final EditText confirm= (EditText) findViewById(R.id.confirm);
        final EditText name=(EditText) findViewById(R.id.name);
        sharedPreferences= getSharedPreferences("Data", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        Log.i("DB",sharedPreferences.getString("Password","null"));
        if(sharedPreferences.getString("Password",null)!=null&&sharedPreferences.getString("acnumber",null)!=null){
            Intent i=new Intent(HomePage.this,Authenticate.class);
            startActivity(i);
            finish();
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirm.getText().toString())){
                    edit.putString("Number",number.getText().toString());
                    edit.putString("Password",confirm.getText().toString());
                    edit.putString("Name",name.getEditableText().toString());
                    edit.commit();
                    Intent i=new Intent(HomePage.this,Signup.class);
                    i.putExtra("custid",number.getText().toString());
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
