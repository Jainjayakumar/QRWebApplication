package com.example.qrwebapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanResultActivity extends AppCompatActivity {

    Button viewbutton;
    TextView txtScanResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        txtScanResult = (TextView) findViewById(R.id.txtScanResult); /* Find TextView and initialize it to object */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String scanResult = extras.getString("ScanResult"); /* Retrieving text of QR Code */
            final String url = extras.getString("url");
            txtScanResult.setText(scanResult);

            viewbutton = (Button) findViewById(R.id.gotourl);

            viewbutton.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    Intent intent = new Intent(ScanResultActivity.this, browserActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);

                }

            });
        }
    }
}