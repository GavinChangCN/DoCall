package com.xbongbong.docall.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.xbongbong.docall.R;
import com.xbongbong.docall.util.callback.INoDataCallback;

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc:
 * Date: 2017-10-11
 * Time: 09:56
 */
public class PermissionAdviceDialogFragment extends DialogFragment {
    protected static final String TAG = "PermissionAdviceDialogFragment";

    private boolean mIsLocationPermission = true;
    private boolean mIsPhonePermission = true;
    private boolean mIsStoragePermission = true;
    private INoDataCallback mCallback;

    private ImageView mIvStoragePermission;
    private ImageView mIvPhonePermission;
    private ImageView mIvLocationPermission;
    private Button mBtnPermissionAdvice;

    private static PermissionAdviceDialogFragment newInstance(boolean isStoragePermission, boolean isPhonePermission, boolean isLocationPermission, INoDataCallback callback) {
        PermissionAdviceDialogFragment _dialogFragment = new PermissionAdviceDialogFragment();
        _dialogFragment.setIsLocationPermision(isLocationPermission);
        _dialogFragment.setIsPhonePermission(isPhonePermission);
        _dialogFragment.setIsStoragePermission(isStoragePermission);
        _dialogFragment.setCallback(callback);
        return _dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.dialog_permission_advice, container);
//        ButterKnife.bind(this, root);
        mIvStoragePermission = (ImageView) root.findViewById(R.id.ivStoragePermission);
        mIvPhonePermission = (ImageView) root.findViewById(R.id.ivPhonePermission);
        mIvLocationPermission = (ImageView) root.findViewById(R.id.ivLocationPermission);
        mBtnPermissionAdvice = (Button) root.findViewById(R.id.btnPermissionAdvice);
        initUI();

        return root;
    }

    protected void initUI() {
        mIvStoragePermission.setVisibility(mIsStoragePermission? View.GONE : View.VISIBLE);
        mIvPhonePermission.setVisibility(mIsPhonePermission? View.GONE : View.VISIBLE);
        mIvLocationPermission.setVisibility(mIsLocationPermission ? View.GONE : View.VISIBLE);
        mBtnPermissionAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mCallback.call();
            }
        });
    }

    public static void showImpl(FragmentManager manager, boolean isStoragePermission, boolean isPhonePermission, boolean isLocationPermision, INoDataCallback callback) {
        PermissionAdviceDialogFragment fragment = PermissionAdviceDialogFragment.newInstance(isStoragePermission, isPhonePermission, isLocationPermision, callback);
        fragment.show(manager, TAG);
    }

    public void setIsLocationPermision(boolean IsLocationPermision) {
        this.mIsLocationPermission = IsLocationPermision;
    }

    public void setIsPhonePermission(boolean IsPhonePermission) {
        this.mIsPhonePermission = IsPhonePermission;
    }

    public void setIsStoragePermission(boolean IsStoragePermission) {
        this.mIsStoragePermission = IsStoragePermission;
    }

    public void setCallback(INoDataCallback Callback) {
        this.mCallback = Callback;
    }
}

