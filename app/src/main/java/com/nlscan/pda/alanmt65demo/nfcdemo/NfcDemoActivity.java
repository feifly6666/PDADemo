package com.nlscan.pda.alanmt65demo.nfcdemo;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.nlscan.pda.alanmt65demo.R;

public class NfcDemoActivity extends NfcBaseActivity {
    private static final String TAG = "NfcDemoActivityTag";
    TextView txtNfcOutputText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_demo);
        txtNfcOutputText = (TextView) findViewById(R.id.txtNfcOutputText);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            String cardNr = bytesToHexString(tag.getId());
            Log.d(TAG, "card Number:" + cardNr);

            processIntent(intent);
        }
    }

    //字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (byte aSrc : src) {
            buffer[0] = Character.forDigit((aSrc >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(aSrc & 0x0F, 16);

            stringBuilder.append(buffer);
        }
        Log.d(TAG, "bytesToHexString:" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * Parses the NDEF Message from the intent and prints to the EditView
     */
    private void processIntent(final Intent intent) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //取出封装在intent中的TAG
                Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                for (String tech : tagFromIntent.getTechList()) {
                    System.out.println(tech);
                }
                boolean auth;
                //读取TAG
                MifareClassic mfc = MifareClassic.get(tagFromIntent);
                try {
                    String metaInfo = "";
                    //Enable I/O operations to the tag from this TagTechnology object.
                    mfc.connect();
                    int type = mfc.getType();//获取TAG的类型
                    int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
                    String typeS = "";
                    switch (type) {
                        case MifareClassic.TYPE_CLASSIC:
                            typeS = "TYPE_CLASSIC";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            typeS = "TYPE_PLUS";
                            break;
                        case MifareClassic.TYPE_PRO:
                            typeS = "TYPE_PRO";
                            break;
                        case MifareClassic.TYPE_UNKNOWN:
                            typeS = "TYPE_UNKNOWN";
                            break;
                    }
                    metaInfo += getResources().getString(R.string.nfcCardType) + typeS + getResources().getString(R.string.nfcAll) + sectorCount + getResources().getString(R.string.nfcSectors)+getResources().getString(R.string.nfcAll)
                            + mfc.getBlockCount() + getResources().getString(R.string.nfcBolcks)+getResources().getString(R.string.nfcStorageSpace)+ mfc.getSize() + getResources().getString(R.string.nfcByte);
                    for (int j = 0; j < sectorCount; j++) {
                        //Authenticate a sector with key A.
                        // 关于MifareClassic卡的背景介绍：数据分为16个区(Sector) ,每个区有4个块(Block) ，每个块可以存放16字节的数据。
                        // 每个区最后一个块称为Trailer ，主要用来存放读写该区Block数据的Key ，可以有A，B两个Key，每个Key 长度为6个字节，缺省的Key值一般为全FF或是0. 由 MifareClassic.KEY_DEFAULT 定义。
                        // 因此读写Mifare Tag 首先需要有正确的Key值（起到保护的作用），如果鉴权成功
                        //然后才可以读写该区数据。
                        auth = mfc.authenticateSectorWithKeyA(j,
                                MifareClassic.KEY_DEFAULT);
                        int bCount;
                        int bIndex;
                        if (auth) {
                            metaInfo += getResources().getString(R.string.nfcSector) + j + getResources().getString(R.string.nfcVerifySuccessful);
                            // 读取扇区中的块
                            bCount = mfc.getBlockCountInSector(j);
                            bIndex = mfc.sectorToBlock(j);
                            for (int i = 0; i < bCount; i++) {
                                byte[] data = mfc.readBlock(bIndex);
                                // bytesToHexString:获得块内数据
                                metaInfo += getResources().getString(R.string.nfcBolck) + bIndex + " : "
                                        + bytesToHexString(data) + getResources().getString(R.string.nfcN);
                                bIndex++;
                            }
                        } else {
                            metaInfo += getResources().getString(R.string.nfcSector)+ j + getResources().getString(R.string.nfcVerifyFail);
                        }
                    }
                    Log.d(TAG,"metaInfo:"+metaInfo);
                    txtNfcOutputText.setText(metaInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}  
