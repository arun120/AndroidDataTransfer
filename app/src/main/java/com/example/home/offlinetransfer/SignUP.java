package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup extends AppCompatActivity {

    Button signup;
    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup=(Button) findViewById(R.id.signup);
        name=(EditText) findViewById(R.id.name);

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                String uname=name.getText().toString();
                  new CreateAccount(Signup.this).execute(uname);

                }
            });



    }
}
