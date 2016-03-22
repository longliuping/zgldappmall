package com.zgld.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.SysApplication;
import com.zgld.mall.adapter.HomeProductAdapter;
import com.zgld.mall.beans.Supplier;
import com.zgld.mall.utils.Contents;

import org.w3c.dom.Text;

public class SupperProductActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    Supplier info = null;
    GridView gridview;
    HomeProductAdapter infoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_supper_product);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ;
            }
        });
        TextView title = (TextView) findViewById(R.id.title_center);
        title.setText("商家店铺");
        info = (Supplier) this.getIntent().getSerializableExtra(Contents.INFO);
        if(info==null){
            finish();
            return;
        }
        ImageView head = (ImageView) findViewById(R.id.head);
        SysApplication.DisplayUserImage(info.getSupplierLogoUrl(), head);
        TextView name = (TextView) findViewById(R.id.name);
        name.setText("商家名称:"+info.getSupplierName());
        TextView cdk_number = (TextView) findViewById(R.id.cdk_number);
        cdk_number.setText("商家店铺号:"+info.getUserId());
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setOnItemClickListener(this);
    }

    @Override
    public void handleMsg(Message msg) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
