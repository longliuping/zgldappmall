package com.zgld.mall.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgld.mall.R;

public class LoginActivity extends BaseActivity implements  View.OnClickListener{

    EditText et_user_name,et_user_password;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 200);
            }
        });
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        et_user_password = (EditText) findViewById(R.id.et_user_password);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
    }

    @Override
    public void handleMsg(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                if(et_user_name.getText()==null || !et_user_name.getText().toString().isEmpty())
                {
                    Toast.makeText(this,"用户名不能为空!",Toast.LENGTH_SHORT).show();
                }else
                if(et_user_password.getText()==null || !et_user_password.getText().toString().isEmpty())
                {
                    Toast.makeText(this,"密码不能为空!",Toast.LENGTH_SHORT).show();
                }else{

                }
                break;
        }
    }
}
