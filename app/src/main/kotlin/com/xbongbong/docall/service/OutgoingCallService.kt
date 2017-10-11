package com.xbongbong.docall.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc: 去电信息监听服务
 * Date: 2017-10-11
 * Time: 10:43
 */
class OutgoingCallService : Service() {

    // 去电广播接收器
    private val myPhoneStateReceiver: MyPhoneStateReceiver by lazy { MyPhoneStateReceiver() }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        // 获取去电号码
        getOutPhoneNumber()
    }

    override fun onDestroy() {
        // 不获取去电号码
        getOutPhoneNumberCancel()
        super.onDestroy()
    }

    /**
     * 获取去电号码
     */
    private fun getOutPhoneNumber() {
        val intentFilter = IntentFilter()
        // 监听去电广播
        intentFilter.addAction(OUTGOING_ACTION)
        // 动态注册去电广播接收器
        registerReceiver(myPhoneStateReceiver, intentFilter)
    }

    /**
     * 不获取去电号码
     */
    private fun getOutPhoneNumberCancel() {
        // 取消注册去电广播接收器
        unregisterReceiver(myPhoneStateReceiver)
    }

    /**
     * 监听去电广播
     */
    private inner class MyPhoneStateReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // 获取去电号码
            val outgoingNumber = resultData
            Toast.makeText(context, "销帮帮来电宝\n去电号码是：$outgoingNumber", Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        // 去电 Action
        private val OUTGOING_ACTION = "android.intent.action.NEW_OUTGOING_CALL"
    }
}
