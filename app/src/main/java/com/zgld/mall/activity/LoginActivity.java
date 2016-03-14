package com.zgld.mall.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zgld.mall.R;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.beans.GsonObject;
import com.zgld.mall.utils.Contents;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
        if(msg.getData().getInt("status")==200){
            switch (msg.what){
                case 201:
                    String gson = msg.getData().getString("data");
                    AspnetUsers user = new Gson().fromJson(gson, new TypeToken<AspnetUsers>() {
                    }.getType());
                    Toast.makeText(getApplicationContext(),user.getUserName(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                if(et_user_name.getText()==null || et_user_name.getText().toString().isEmpty())
                {
                    Toast.makeText(this,"用户名不能为空!",Toast.LENGTH_SHORT).show();
                }else
                if(et_user_password.getText()==null || et_user_password.getText().toString().isEmpty())
                {
                    Toast.makeText(this,"密码不能为空!",Toast.LENGTH_SHORT).show();
                }else{
                    Map<String,String> m = new HashMap();
                    m.put("name",et_user_name.getText().toString());
                    m.put("password",et_user_password.getText().toString());
                    m.put("id","1");
                    getData(Request.Method.POST, 201, "user_login.html", m, null,1);
                }
                break;
        }
    }
}
