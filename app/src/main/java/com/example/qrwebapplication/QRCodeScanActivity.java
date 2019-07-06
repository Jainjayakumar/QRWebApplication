package com.example.qrwebapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRCodeScanActivity extends AppCompatActivity {

    SurfaceView surfaceQRScanner;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    String scanResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        initStuff(); /* Calling this function to initialize components */
    }

    /* Function used to initialize components of activity */
    public void initStuff() {
        /* Initializing objects */
        surfaceQRScanner = (SurfaceView) findViewById(R.id.surfaceQRScanner);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true)
                .build();

        /* Adding Callback method to SurfaceView */
        surfaceQRScanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    /* Asking user to allow access of camera */
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        cameraSource.start(surfaceQRScanner.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QRCodeScanActivity.this, new
                                String[]{Manifest.permission.CAMERA}, 1024);
                    }
                } catch (IOException e) {
                    Log.e("Camera start error-->> ", e.getMessage().toString());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        /* Adding Processor to Barcode detector */
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                /* Retrieving QR Code */
                if (barcodes.size() > 0) {
                    barcodeDetector.release();
                    /* Releasing barcodeDetector */
                    ToneGenerator toneNotification = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    /* Setting beep sound */
                    toneNotification.startTone(ToneGenerator.TONE_PROP_BEEP, 150);
                    scanResult = barcodes.valueAt(0).displayValue.toString();
                    Barcode thisCode = barcodes.valueAt(0);
                    /* Retrieving text from QR Code */
                    Intent intent = new Intent(QRCodeScanActivity.this, ScanResultActivity.class);
                    intent.putExtra("ScanResult", thisCode.rawValue);
                    intent.putExtra("url",scanResult);
                    /* Sending text to next activity to display */
                    startActivity(intent);
                }
            }
        });
    }
    /* Initialize components again */
    @Override
    public void onResume() {
        super.onResume();
        initStuff();
    }
}