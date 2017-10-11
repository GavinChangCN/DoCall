package com.xbongbong.docall.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc: 来电监视服务
 * Date: 2017-10-11
 * Time: 10:39
 */
class IncomingCallService : Service() {

    //  电话服务管理器
    private val mTelephonyManager: TelephonyManager by lazy { getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager }
    // 电话状态监听器
    private val myPhoneStateListener: MyPhoneStateListener by lazy { MyPhoneStateListener() }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        // 获取来电号码
        getIncomingPhoneNumber()
    }

    override fun onDestroy() {
        // 不获取来电号码
        getIncomingPhoneNumberCancel()
        super.onDestroy()
    }

    /**
     * 获取来电号码
     */
    private fun getIncomingPhoneNumber() {
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    /**
     * 不获取来电号码
     */
    private fun getIncomingPhoneNumberCancel() {
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE)
    }

    /**
     * 电话状态监听器
     */
    private inner class MyPhoneStateListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            super.onCallStateChanged(state, incomingNumber)
            when (state) {
            // 如果电话铃响
                TelephonyManager.CALL_STATE_RINGING -> Toast.makeText(this@IncomingCallService, "销帮帮来电宝\n来电号码是：$incomingNumber", Toast
                        .LENGTH_LONG).show()
            }
        }
    }
}
