package com.example.home.offlinetransfer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Signup extends AppCompatActivity {

    Button confirm;
    ListView name;
    RelativeLayout select,view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name=(ListView) findViewById(R.id.accountlist);
        select=(RelativeLayout) findViewById(R.id.layoutselect);
        view=(RelativeLayout) findViewById(R.id.layoutview);
        select.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

        new GetCustomerDetails(Signup.this,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,getIntent().getExtras().getString("custid"));






    }


}
