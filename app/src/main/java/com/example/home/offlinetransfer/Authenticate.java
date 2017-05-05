package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class Authenticate extends AppCompatActivity {
    SharedPreferences sharedPreferences=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        sharedPreferences= getSharedPreferences("Data", Context.MODE_PRIVATE);
        final String password=sharedPreferences.getString("Password",null);
        Log.i("Password",password);
        EditText auth= (EditText) findViewById(R.id.AuthPassword);
        auth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("Input", String.valueOf(s));
                if(password.contentEquals(s)){
                    Intent i=new Intent(Authenticate.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
