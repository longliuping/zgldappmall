package com.zgld.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.zgld.mall.R;
import com.zgld.mall.beans.UserInfo;
import com.zgld.mall.utils.Contents;

public class LoadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void handleMsg(Message msg) {

    }

}
