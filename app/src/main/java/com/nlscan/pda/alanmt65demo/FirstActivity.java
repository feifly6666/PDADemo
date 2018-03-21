package com.nlscan.pda.alanmt65demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nlscan.pda.alanmt65demo.nfcdemo.NfcDemoActivity;
import com.nlscan.pda.alanmt65demo.scandemo.ScanDemoActivity;
import com.nlscan.pda.alanmt65demo.aimiddemo.AimidDemoActivity;
/**
 * @author Alan
 * @Company nlscan
 * @date 2017/12/16 23:02
 * @Description:
 */
public class FirstActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnScanDemo;
    Button btnAimidDemo;
    Button btnNfcDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        btnScanDemo = (Button)findViewById(R.id.btnScanDemo);
        btnScanDemo.setOnClickListener(this);
        btnAimidDemo = (Button)findViewById(R.id.btnAimidDemo);
        btnAimidDemo.setOnClickListener(this);
        btnNfcDemo = (Button)findViewById(R.id.btnNFCDemo);
        btnNfcDemo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnScanDemo:
                Intent openScanDemo = new Intent(FirstActivity.this,ScanDemoActivity.class);
                startActivity(openScanDemo);
                break;
            case R.id.btnAimidDemo:
                Intent openAimidDemo = new Intent(FirstActivity.this,AimidDemoActivity.class);
                startActivity(openAimidDemo);
                break;
            case R.id.btnNFCDemo:
                Intent openNFCDemo = new Intent(FirstActivity.this,NfcDemoActivity.class);
                startActivity(openNFCDemo);
                break;
        }
    }
}
