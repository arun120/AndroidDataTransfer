package com.example.home.offlinetransfer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button add = (Button) findViewById(R.id.add);
        final EditText acnumber = (EditText) findViewById(R.id.acnumber);
        final EditText IFSC = (EditText) findViewById(R.id.IFSC);
        final EditText branch = (EditText) findViewById(R.id.branch);
        final EditText type= (EditText) findViewById(R.id.type);
        final EditText bank=(EditText) findViewById(R.id.bank);
        final EditText name=(EditText) findViewById(R.id.name);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionDetails td=new TransactionDetails();

                td.setAcnumber(acnumber.getText().toString());
                td.setBank(bank.getText().toString());
                td.setBranch(branch.getText().toString());
                td.setIfsc(IFSC.getText().toString());
                td.setType(type.getText().toString());
                td.setName(name.getText().toString());
                Log.i("add",td.getJson());
                new AddBeneficiary(getApplicationContext(),getParent()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,td.getJson());
            }
        });





    }
}
