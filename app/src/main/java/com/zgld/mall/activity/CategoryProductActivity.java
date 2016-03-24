package com.zgld.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.beans.HotCategory;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.widget.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner_province);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(dataset);
    }

    @Override
    public void handleMsg(Message msg) {

    }
}
