package com.xbongbong.docall.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.xbongbong.docall.R
import com.xbongbong.docall.base.BaseActivity
import com.xbongbong.docall.service.IncomingCallService
import com.xbongbong.docall.service.OutgoingCallService
import com.xbongbong.docall.widget.toast.CustomToast
import kotlinx.android.synthetic.main.activity_main.*

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc:
 * Date: 2017-10-11
 * Time: 10:30
 */
class MainActivity : BaseActivity() {

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }

        private val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1
        private val MY_PERMISSIONS_REQUEST_OUTGOING_PHONE_STATE = 2
        private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 3
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setupToolbar(toolbar)
        // 隐藏返回按钮
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val tvTitle = findViewById(R.id.tvTitle) as TextView
        tvTitle.text = getString(R.string.app_name)

        // 设置按键监听
        btnIncomingCall.setOnClickListener(mClickListener)
        btnIncomingCallCancel.setOnClickListener(mClickListener)
        btnOutgoingCall.setOnClickListener(mClickListener)
        btnOutgoingCallCancel.setOnClickListener(mClickListener)
        btnCall.setOnClickListener(mClickListener)
    }

    private var mClickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.btnIncomingCall ->
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                            Manifest.permission.READ_PHONE_STATE)) {
                        // 返回值：
                        //                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                        //                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                        //                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        CustomToast.INSTANCE.showErrorToast(this@MainActivity, "请授权！")

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else {
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.READ_PHONE_STATE),
                                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE)
                    }
                } else {
                    startInService()
                }
            R.id.btnIncomingCallCancel -> {
                val stopInService = Intent(this@MainActivity, IncomingCallService::class.java)
                stopService(stopInService)
                CustomToast.INSTANCE.showToast(this@MainActivity, "不再获取来电号码")
            }
            R.id.btnOutgoingCall ->
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                            Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                        // 返回值：
                        //                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                        //                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                        //                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        CustomToast.INSTANCE.showErrorToast(this@MainActivity, "请授权！")

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else {
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS),
                                MY_PERMISSIONS_REQUEST_OUTGOING_PHONE_STATE)
                    }
                } else {
                    startOutService()
                }
            R.id.btnOutgoingCallCancel -> {
                val stopOutgoingService = Intent(this@MainActivity, OutgoingCallService::class.java)
                stopService(stopOutgoingService)
                CustomToast.INSTANCE.showToast(this@MainActivity, "不再获取去电号码")
            }
            R.id.btnCall ->
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                            Manifest.permission.CALL_PHONE)) {
                        // 返回值：
                        //                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                        //                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                        //                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        CustomToast.INSTANCE.showErrorToast(this@MainActivity, "请授权！")

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else {
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.CALL_PHONE),
                                MY_PERMISSIONS_REQUEST_CALL_PHONE)
                    }
                } else {
                    callPhone()
                }
        }
    }


    // 处理权限申请的回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权成功
                startInService()
            } else {
                // 授权失败！
                CustomToast.INSTANCE.showErrorToast(this@MainActivity, "获取来电信息权限授权失败！")
            }
            MY_PERMISSIONS_REQUEST_OUTGOING_PHONE_STATE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权成功
                startOutService()
            } else {
                // 授权失败！
                CustomToast.INSTANCE.showErrorToast(this@MainActivity, "获取去电信息权限授权失败！")
            }
            MY_PERMISSIONS_REQUEST_CALL_PHONE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权成功，继续打电话
                callPhone()
            } else {
                // 授权失败！
                CustomToast.INSTANCE.showErrorToast(this@MainActivity, "拨号权限授权失败！")
            }
        }

    }

    /**
     * 开启获取来电信息服务
     */
    private fun startInService() {
        val startInService = Intent(this@MainActivity, IncomingCallService::class.java)
        startService(startInService)
        CustomToast.INSTANCE.showSuccessToast(this@MainActivity, "允许获取来电号码")
    }

    /**
     * 开启获取去电信息权限
     */
    private fun startOutService() {
        val startOutgoingService = Intent(this@MainActivity, OutgoingCallService::class.java)
        startService(startOutgoingService)
        CustomToast.INSTANCE.showSuccessToast(this@MainActivity, "允许获取去电号码")
    }

    /**
     * 拨打号码
     */
    private fun callPhone() {
        val number = "10010"
        if (TextUtils.isEmpty(number)) {
            // 提醒用户
            // 注意：在这个匿名内部类中如果用this则表示是View.OnClickListener类的对象，
            // 所以必须用MainActivity.this来指定上下文环境。
            CustomToast.INSTANCE.showErrorToast(this@MainActivity, "号码不能为空！")
        } else {
            // 拨号：激活系统的拨号组件
            val intent = Intent() // 意图对象：动作 + 数据
            intent.action = Intent.ACTION_CALL // 设置动作
            val data = Uri.parse("tel:" + number) // 设置数据
            intent.data = data
            startActivity(intent) // 激活Activity组件
        }
    }
}
