package com.nlscan.pda.alanmt65demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.nlscan.pda.alanmt65demo.NLScanConstant.BOOT_COMPLETED;

/**
 * @author Alan
 * @Company nlscan
 * @date 2017/12/22 10:47
 * @Description:
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, FirstActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
