package com.zgld.mall.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zgld.mall.R;

public class RegisterActivity extends BaseActivity implements  View.OnClickListener
{
    Button user_register;
    EditText user_name,user_password,user_password_r,user_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("注册");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        user_name = (EditText) findViewById(R.id.user_name);
        user_password = (EditText) findViewById(R.id.user_password);
        user_password_r = (EditText) findViewById(R.id.user_password_r);
        user_code = (EditText) findViewById(R.id.user_code);
    }

    @Override
    public void handleMsg(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.user_register:
                if(user_name.getText()==null || !user_name.getText().toString().isEmpty())
                {
                    Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }else if(user_name.getText().toString().length()<6){
                    Toast.makeText(this,"用户名长度不能小于6",Toast.LENGTH_SHORT).show();
                }else if(user_password.getText().toString()==null || !user_password.getText().toString().isEmpty()){
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(user_password.getText().toString().length()<6)
                {
                    Toast.makeText(this,"密码长度不能小于6",Toast.LENGTH_SHORT).show();
                }else if(!user_password.getText().toString().equals(user_password_r.getText().toString()))
                {
                    Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }else{

                }
                break;
        }
    }
}
