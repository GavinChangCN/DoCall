package com.xbongbong.docall.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc: 获取去电号码服务
 * Date: 2017-10-10
 * Time: 23:44
 */
public class OutCallService extends Service {

    // 去电 Action
    private static final String OUTGOING_ACTION = "android.intent.action.NEW_OUTGOING_CALL";

    // 去电广播接收器
    private MyPhoneStateReceiver myPhoneStateReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取去电号码
        getOutPhoneNumber();
    }

    @Override
    public void onDestroy() {
        // 不获取去电号码
        getOutPhoneNumberCancel();
        super.onDestroy();
    }

    /**
     * 获取去电号码
     */
    private void getOutPhoneNumber() {
        IntentFilter intentFilter = new IntentFilter();
        // 监听去电广播
        intentFilter.addAction(OUTGOING_ACTION);
        myPhoneStateReceiver = new MyPhoneStateReceiver();
        // 动态注册去电广播接收器
        registerReceiver(myPhoneStateReceiver, intentFilter);
    }

    /**
     * 不获取去电号码
     */
    private void getOutPhoneNumberCancel() {
        // 取消注册去电广播接收器
        unregisterReceiver(myPhoneStateReceiver);
    }

    /**
     * 监听去电广播
     */
    private class MyPhoneStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取去电号码
            String outgoingNumber = getResultData();
            Toast.makeText(context, "销帮帮来电宝\n去电号码是：" + outgoingNumber, Toast.LENGTH_LONG).show();
        }
    }
}
