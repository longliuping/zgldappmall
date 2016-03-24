package com.zgld.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.zgld.mall.R;
import com.zgld.mall.beans.Region;
import com.zgld.mall.utils.XMLAddressHandler;
import com.zgld.mall.utils.XMLContentHandler;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class LoadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        startActivity(new Intent(this, MainActivity.class));
        finish();
        try {
            InputStream inStream = this.getResources().getAssets().open("address.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();// 创建解析器
            SAXParser saxParser = spf.newSAXParser();
            // 设置解析器的相关特性，true表示开启命名空间特性
            // saxParser.setProperty("http://xml.org/sax/features/namespaces",
            // true);
            XMLAddressHandler handler = new XMLAddressHandler();
            saxParser.parse(inStream, handler);
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMsg(Message msg) {

    }

}
