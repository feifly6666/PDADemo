package com.nlscan.pda.alanmt65demo.aimiddemo;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nlscan.pda.alanmt65demo.NLScanConstant;
import com.nlscan.pda.alanmt65demo.NLScanIntent;
import com.nlscan.pda.alanmt65demo.R;
import com.nlscan.pda.alanmt65demo.ScanBroadcastReceiver;

public class AimidDemoActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "AimidDemoActivityTag";

    // EditText
    EditText txtAimidOutputText;
    ScanBroadcastReceiver resultReceiver;
    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aimid_demo);

        txtAimidOutputText = (EditText)findViewById(R.id.txtAimidOutputText);
        resultReceiver = new ScanBroadcastReceiver(txtAimidOutputText);
        btnScan = (Button)findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        setApiScanMode();


    }

    // set the scan mode to API mode
    private void setApiScanMode(){
        NLScanIntent intent = new NLScanIntent(NLScanConstant.ACTION_BAR_SCANCFG, NLScanConstant.EXTRA_SCAN_MODE, 3);
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnScan:
                btnScanClicked();
                break;
        }
    }

    private void btnScanClicked() {

        txtAimidOutputText.requestFocus();
        NLScanIntent intent = new NLScanIntent(NLScanConstant.SCANNER_TRIG);
        sendBroadcast(intent);
        txtAimidOutputText.setSelection(txtAimidOutputText.getText().length());

    }

    private void registerReceiver()
    {
        if(!ScanBroadcastReceiver.registeredTag){
            IntentFilter mFilter= new IntentFilter(NLScanConstant.SCANNER_RESULT);
            registerReceiver(resultReceiver, mFilter);
            ScanBroadcastReceiver.registeredTag = true;
            Log.d(TAG,"registerReceiver is called。registeredTag："+ScanBroadcastReceiver.registeredTag);
        }

    }

    private void unRegisterReceiver()
    {
        if(ScanBroadcastReceiver.registeredTag){
            try {
                unregisterReceiver(resultReceiver);
                ScanBroadcastReceiver.registeredTag = false;
                Log.d(TAG,"unRegisterReceiver is called。registeredTag："+ScanBroadcastReceiver.registeredTag);
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }
}
