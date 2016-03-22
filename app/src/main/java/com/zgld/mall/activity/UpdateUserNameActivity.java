package com.zgld.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.utils.Contents;

public class UpdateUserNameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_update_user_name);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String nameStr = this.getIntent().getStringExtra("name");
        if(nameStr==null || Contents.getUser(this) == null){
            finish();;
            return;
        }
        TextView name = (TextView) findViewById(R.id.title_center);
        name.setText(nameStr);
    }

    @Override
    public void handleMsg(Message msg) {

    }
}
