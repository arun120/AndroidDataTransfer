package com.example.home.offlinetransfer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button transfer = (Button) findViewById(R.id.transfer);
        final EditText accNum = (EditText) findViewById(R.id.accNum);
        final EditText IFSC = (EditText) findViewById(R.id.IFSC);
        final EditText ack = (EditText) findViewById(R.id.ack);
        final EditText amt= (EditText) findViewById(R.id.amount);

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accNumber = accNum.getText().toString();
                String IFSCCode = IFSC.getText().toString();
                String acknowledgement = ack.getText().toString();
                String amount=amt.getText().toString();

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +"1500,"+Encoder.encode(amount,accNumber,IFSCCode,acknowledgement)));

                //Log.i("Number Dialled", String.valueOf(Uri.parse("tel:" +"1500,"+ Encoder.encode(amount,accNumber,IFSCCode,acknowledgement))));

                // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +"04422730897,"+"500*123456789*123456789*123456789"));

                if (ActivityCompat.checkSelfPermission(Payment.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
}
