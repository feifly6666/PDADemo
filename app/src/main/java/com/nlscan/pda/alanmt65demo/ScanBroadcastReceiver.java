package com.nlscan.pda.alanmt65demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nlscan.pda.alanmt65demo.aimiddemo.AimidInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alan
 * @Company nlscan
 * @date 2017/12/19 15:23
 * @Description:
 */
public class ScanBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ScanBroadcastReceiver";
    // 判断广播是否已经注册的tag
    public static boolean registeredTag = false;
    EditText eText;
    private List<AimidInfo> mAIMIDInfoList = new ArrayList<AimidInfo>();

    public ScanBroadcastReceiver(View view) {
        eText = (EditText) view;
        loadPresionInfo();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (NLScanConstant.SCANNER_RESULT.equals(action)) {
            final String scanResult_1 = intent.getStringExtra("SCAN_BARCODE1");
            final String scanResult_2 = intent.getStringExtra("SCAN_BARCODE2");
            final int barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown
            final String scanStatus = intent.getStringExtra("SCAN_STATE");
            StringBuilder sb = new StringBuilder();
            if ("ok".equals(scanStatus)) {
                if (!TextUtils.isEmpty(scanResult_1)) {
                    sb.append("[SCAN_BARCODE1:").append(scanResult_1).append("]");
                }
                if (!TextUtils.isEmpty(scanResult_2)) {
                    sb.append("[SCAN_BARCODE2:]").append(scanResult_2).append("]");
                }
                if (!TextUtils.isEmpty(scanStatus)) {
                    sb.append("[SCAN_STATE:").append(scanStatus).append("]");
                }

                sb.append("[SCAN_BARCODE_TYPE:").append(barcodeType).append("]");

                String barcodeTypeName = getCodeTypeName(barcodeType);
                if (TextUtils.isEmpty(barcodeTypeName)) {
                    barcodeTypeName = NLScanConstant.NLSCAN_UNKNOW_CODETYPE_NAME;
                }
                sb.append("[SCAN_BARCODE_TYPE_NAME:").append(barcodeTypeName).append("]");

            } else {
                sb.append("[SCAN_STATE:").append(scanStatus).append("]");
            }
            eText.append(sb.toString());
            eText.setSelection(eText.getText().length());
        }
    }

    private String getCodeTypeName(int value) {
        for (AimidInfo info : mAIMIDInfoList) {
            if (info.getCodeTypeValue() == value) {
                return info.getCodeTypeName();
            }
        }
        return null;
    }

    private void loadPresionInfo() {
        mAIMIDInfoList.clear();
        AimidInfo info = null;
        try {
            XmlResourceParser xrp = eText.getResources().getXml(R.xml.img3_codetpye);

            // 如果没有到文件尾继续执行
            int eventype = xrp.getEventType();
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {

                // 如果是开始标签
                switch (eventype) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml analysis start");
                        break;
                    case XmlPullParser.START_TAG:
                        // 一般都是获取标签的属性值，所以在这里数据你需要的数据
                        if (xrp.getName().equals("Code")) {
                            // 两种方法获取属性值
                            info = new AimidInfo();
                            info.setCodeTypeName(xrp.getAttributeValue(0));
                            Log.d(TAG, "AimidInfo CodeTypeName is " + xrp.getAttributeValue(0));
                        }
                        break;
                    case XmlPullParser.TEXT:
                        Log.d(TAG, "AimidInfo CodeTypeValue is " + xrp.getText());
                        info.setCodeTypeValue(Integer.valueOf(xrp.getText()));
                        break;
                    case XmlPullParser.END_TAG:
                        mAIMIDInfoList.add(info);
                        break;
                    default:
                        break;
                }
                eventype = xrp.next(); // 将当前解析器光标往下一步移
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
