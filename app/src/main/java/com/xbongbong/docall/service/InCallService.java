package com.xbongbong.docall.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc: 获取来电号码服务
 * Date: 2017-10-10
 * Time: 23:30
 */
public class InCallService extends Service {

    //  电话服务管理器
    private TelephonyManager mTelephonyManager;
    // 电话状态监听器
    private MyPhoneStateListener myPhoneStateListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取来电号码
        getInPhoneNumber();
    }

    @Override
    public void onDestroy() {
        // 不获取来电号码
        getInPhoneNumberCancel();
        super.onDestroy();
    }

    /**
     * 获取来电号码
     */
    private void getInPhoneNumber() {
        // 获取电话系统服务
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 不获取来电号码
     */
    private void getInPhoneNumberCancel() {
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * 电话状态监听器
     */
    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                // 如果电话铃响
                case TelephonyManager.CALL_STATE_RINGING:
                    Toast.makeText(InCallService.this, "销帮帮来电宝\n来电号码是：" + incomingNumber, Toast
                            .LENGTH_LONG).show();
            }
        }
    }
}
