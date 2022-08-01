package com.example.tapal_qr_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setid();
        if(chechAndrequestpermission(MainActivity.this)){
            runCodeScanner();}

    }



    private void runCodeScanner() {
        codeScanner=new CodeScanner(this,codeScannerView);

        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setFlashEnabled(false);

        codeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String data = result.getText();

                try{
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                    startActivity(intent2);
                }
                catch (Exception e)
                {
                    Log.d("TAG", "run: 2112");
                    Toast.makeText(MainActivity.this,"No url detected "+data,Toast.LENGTH_LONG).show();
                    codeScanner.startPreview();
                }
            }
        }));
        codeScanner.setFlashEnabled(true);
       boolean val = codeScanner.isFlashEnabled();
        Log.d("TAG", "hello "+val);

        codeScanner.startPreview();


    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        codeScanner.stopPreview();
        codeScanner.releaseResources();
    }

    public void setid() {
        codeScannerView=findViewById(R.id.scanner_view);

    }
    private boolean chechAndrequestpermission(Context context){

        if (Build.VERSION.SDK_INT >=23){

            int cameraPermission= ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if(cameraPermission== PackageManager.PERMISSION_DENIED){

                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA },20);
                return false;
            }
        }
        ;
        return true;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,Context context) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 20 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){

            runCodeScanner();
        }
        else{
            Toast.makeText(context,"Permission not granted",Toast.LENGTH_LONG).show();
        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}