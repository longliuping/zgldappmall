package com.zgld.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.beans.HotCategory;
import com.zgld.mall.utils.Contents;

public class CategoryProductActivity extends BaseActivity {
    HotCategory info = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_category_product);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ;
            }
        });
        info = (HotCategory) this.getIntent().getSerializableExtra(Contents.INFO);
        if(info==null){
            finish();
            return;
        }
        TextView title = (TextView) findViewById(R.id.title_center);
        title.setText(info.getHotname());
    }

    @Override
    public void handleMsg(Message msg) {

    }
}
