package com.xbongbong.docall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xbongbong.docall.base.BaseActivity;
import com.xbongbong.docall.service.InCallService;
import com.xbongbong.docall.service.OutCallService;

public class MainActivity extends BaseActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_OUTGOING_PHONE_STATE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar);
        // 隐藏返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.app_name));

        // 实例化控件
        Button btnInCall = (Button) findViewById(R.id.btn_in_call);
        Button btnInCallCancel = (Button) findViewById(R.id.btn_in_call_cancel);

        Button btnOutCall = (Button) findViewById(R.id.btn_out_call);
        Button btnOutCallCancel = (Button) findViewById(R.id.btn_out_call_cancel);

        Button btnCall = (Button) findViewById(R.id.call);

        // 设置按键监听
        btnInCall.setOnClickListener(mClickListener);
        btnInCallCancel.setOnClickListener(mClickListener);
        btnOutCall.setOnClickListener(mClickListener);
        btnOutCallCancel.setOnClickListener(mClickListener);
        btnCall.setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_in_call:
                    // 检查是否获得了权限（Android6.0运行时权限）
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // 没有获得授权，申请授权
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.READ_PHONE_STATE)) {
                            // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                            // 弹窗需要解释为何需要该权限，再次请求授权
                            Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                            // 帮跳转到该应用的设置界面，让用户手动授权
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        } else {
                            // 不需要解释为何需要该权限，直接请求授权
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    } else {
                        startInService();
                    }
                    break;
                case R.id.btn_in_call_cancel:
                    Intent stopInService = new Intent(MainActivity.this, InCallService.class);
                    stopService(stopInService);
                    Toast.makeText(MainActivity.this, "不再获取来电号码", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_out_call:
                    // 检查是否获得了权限（Android6.0运行时权限）
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        // 没有获得授权，申请授权
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                            // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                            // 弹窗需要解释为何需要该权限，再次请求授权
                            Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                            // 帮跳转到该应用的设置界面，让用户手动授权
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        } else {
                            // 不需要解释为何需要该权限，直接请求授权
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                                    MY_PERMISSIONS_REQUEST_OUTGOING_PHONE_STATE);
                        }
                    } else {
                        startOutService();
                    }
                    break;
                case R.id.btn_out_call_cancel:
                    Intent stopOutgoingService = new Intent(MainActivity.this, OutCallService.class);
                    stopService(stopOutgoingService);
                    Toast.makeText(MainActivity.this, "不再获取去电号码", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.call:
                    // 检查是否获得了权限（Android6.0运行时权限）
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // 没有获得授权，申请授权
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.CALL_PHONE)) {
                            // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                            // 弹窗需要解释为何需要该权限，再次请求授权
                            Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                            // 帮跳转到该应用的设置界面，让用户手动授权
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        } else {
                            // 不需要解释为何需要该权限，直接请求授权
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        }
                    } else {
                        callPhone();
                    }
                    break;
            }
        }
    };

    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功
                    startInService();
                } else {
                    // 授权失败！
                    Toast.makeText(this, "获取来电信息权限授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_OUTGOING_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功
                    startOutService();
                } else {
                    // 授权失败！
                    Toast.makeText(this, "获取去电信息权限授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    callPhone();
                } else {
                    // 授权失败！
                    Toast.makeText(this, "拨号权限授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    /**
     * 开启获取来电信息服务
     */
    private void startInService() {
        Intent startInService = new Intent(MainActivity.this, InCallService.class);
        startService(startInService);
        Toast.makeText(MainActivity.this, "允许获取来电号码", Toast.LENGTH_SHORT).show();
    }

    /**
     * 开启获取去电信息权限
     */
    private void startOutService() {
        Intent startOutgoingService = new Intent(MainActivity.this, OutCallService.class);
        startService(startOutgoingService);
        Toast.makeText(MainActivity.this, "允许获取去电号码", Toast.LENGTH_SHORT).show();
    }

    /**
     * 拨打号码
     */
    private void callPhone() {
        String number = "10010";
        if (TextUtils.isEmpty(number)) {
            // 提醒用户
            // 注意：在这个匿名内部类中如果用this则表示是View.OnClickListener类的对象，
            // 所以必须用MainActivity.this来指定上下文环境。
            Toast.makeText(MainActivity.this, "号码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            // 拨号：激活系统的拨号组件
            Intent intent = new Intent(); // 意图对象：动作 + 数据
            intent.setAction(Intent.ACTION_CALL); // 设置动作
            Uri data = Uri.parse("tel:" + number); // 设置数据
            intent.setData(data);
            startActivity(intent); // 激活Activity组件
        }
    }
}
