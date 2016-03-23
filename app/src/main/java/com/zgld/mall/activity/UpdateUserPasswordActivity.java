package com.zgld.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.zgld.mall.R;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserPasswordActivity extends BaseActivity implements View.OnClickListener{
    Button submit;
    EditText old_pwd,new_pwd,new_confirm_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_update_user_password);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String nameStr = this.getIntent().getStringExtra(Contents.NAME);
        if(nameStr==null || Contents.getUser(this) == null){
            finish();;
            return;
        }
        TextView name = (TextView) findViewById(R.id.title_center);
        name.setText(nameStr);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        old_pwd = (EditText) findViewById(R.id.old_pwd);
        new_pwd = (EditText) findViewById(R.id.new_pwd);
        new_confirm_pwd = (EditText) findViewById(R.id.new_confirm_pwd);
    }

    @Override
    public void handleMsg(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                if(StringUtils.isEmpty(old_pwd)){
                    Toast.makeText(this,"原始密码不能为空",Toast.LENGTH_LONG).show();
                }else if(StringUtils.isEmpty(new_pwd)){
                    Toast.makeText(this,"新密码不能为空",Toast.LENGTH_LONG).show();
                }else if(StringUtils.isEmpty(new_confirm_pwd)){
                    Toast.makeText(this,"确认密码不能为空",Toast.LENGTH_LONG).show();
                }else if(!new_pwd.getText().toString().equals(new_confirm_pwd.getText().toString())){
                    Toast.makeText(this,"新密码和确认密码不一致",Toast.LENGTH_LONG).show();
                }else{
                    Map<String,String> m = new HashMap<>();
                    AspnetUsers users = Contents.getUser(this);
                    m.put(Contents.TOKEN,users.getUserToken().getAccountToken());
                    m.put(Contents.USERID,users.getUserId()+"");
                    m.put("oldPassword",old_pwd.getText().toString());
                    m.put("password",new_pwd.getText().toString());
                    getData(Request.Method.POST,201,"user/update_user_password.html",m,null,1);
                }
                break;
        }
    }
}
