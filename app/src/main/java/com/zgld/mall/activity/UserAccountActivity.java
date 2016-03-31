package com.zgld.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.UserDataShare;
import com.zgld.mall.beans.AspnetUsers;

/**
 * 用户账户
 */
public class UserAccountActivity extends BaseActivity {
    AspnetUsers users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_user_account);
        TextView title = (TextView) findViewById(R.id.title_center);
        title.setText("我的账户");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        users = new UserDataShare(this).getUserData();
        if(users==null){
            finish();
            return;
        }

    }

    @Override
    public void handleMsg(Message msg) {

    }
}
