package com.nlscan.pda.alanmt65demo.nfcdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2017/5/15.
 */
public class NfcBaseActivity extends AppCompatActivity {

    private final String LOGCAT_TAG = getClass().getSimpleName();

    private NfcAdapter mNfcAdapter = null;
    private PendingIntent mNfcPendingIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter!=null) {
            Intent nfcIntent = new Intent(this, getClass());
            // If set, the activity will not be launched if it is already running at the top of the history stack.
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mNfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        } else {
            Log.e(LOGCAT_TAG, "no NFC adapter exists");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter!=null && mNfcPendingIntent!=null) {
            mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}
