package com.zgld.mall.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.zgld.mall.R;
import com.zgld.mall.utils.ConfirmDialog;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.volley.RequestListenr;

/**
 * Created by ILoveYou on 2016/3/4.
 */
public abstract class BaseFragment extends Fragment implements RequestListenr {
    protected ConfirmDialog confirmDialog = null;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            handleMsg(msg);
        };
    };

    public abstract void handleMsg(android.os.Message msg);
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onCompelete(int tag, String json) {
        Message msg = handler.obtainMessage();
        msg.what = tag;
        Bundle data = new Bundle();
        data.putString(Contents.JSON, json);
        data.putBoolean("cache", false);
        msg.setData(data);
        if (confirmDialog != null && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
        }
        handler.sendMessage(msg);
    }

    /**
     * 请求出现异常时
     */

    @Override
    public void onException(String exception) {
        if (confirmDialog != null && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
        }
        handler.sendEmptyMessage(Contents.TAG_ERROES);
        if (exception != null && exception.equals("com.android.volley.ServerError")) {
            Toast.makeText(activity, activity.getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, activity.getString(R.string.network_connection_timeout), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
