package com.zgld.mall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.beans.City;
import com.zgld.mall.beans.County;
import com.zgld.mall.beans.HotCategory;
import com.zgld.mall.beans.Province;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.XMLAddressHandler;
import com.zgld.mall.widget.NiceSpinner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CategoryProductActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    HotCategory info = null;

    Province province;
    NiceSpinner niceSpinnerProvince;
    List<String> datasetProvince = new LinkedList<>();

    List<City> cityList = new ArrayList<>();
    NiceSpinner niceSpinnerCity;
    List<String> datasetCity = new LinkedList<>();

    List<County> countyList = new ArrayList<>();
    NiceSpinner niceSpinnerCounty;
    List<String> datasetCounty = new LinkedList<>();

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
        try {
            InputStream inStream = this.getResources().getAssets().open("address.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();// 创建解析器
            SAXParser saxParser = spf.newSAXParser();
            // 设置解析器的相关特性，true表示开启命名空间特性
            // saxParser.setProperty("http://xml.org/sax/features/namespaces",
            // true);
            XMLAddressHandler handler = new XMLAddressHandler();
            saxParser.parse(inStream, handler);
            province = handler.getProvinces().get(0);
            inStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        niceSpinnerProvince = (NiceSpinner) findViewById(R.id.nice_spinner_province);
        datasetProvince.add(province.getName());
        niceSpinnerProvince.attachDataSource(datasetProvince);

        niceSpinnerCity = (NiceSpinner) findViewById(R.id.nice_spinner_city);
        cityList = province.getCitys();
        for (int i=0;i<province.getCitys().size();i++){
            datasetCity.add(province.getCitys().get(i).getName());
        }
        niceSpinnerCity.attachDataSource(datasetCity);
        niceSpinnerCity.setOnItemSelectedListener(this);

        niceSpinnerCounty = (NiceSpinner) findViewById(R.id.nice_spinner_county);
        countyList = cityList.get(0).getCountys();
        datasetCounty = new LinkedList<>();
        for (int i=0;i<countyList.size();i++){
            datasetCounty.add(countyList.get(i).getName());
        }
        niceSpinnerCounty.attachDataSource(datasetCounty);
        niceSpinnerCounty.setOnItemSelectedListener(this);
    }

    @Override
    public void handleMsg(Message msg) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.nice_spinner_province:

                break;
            case R.id.nice_spinner_city:
                countyList = cityList.get(position).getCountys();
                datasetCounty = new LinkedList<>();
                for (int i=0;i<countyList.size();i++){
                    datasetCounty.add(countyList.get(i).getName());
                }
                niceSpinnerCounty.attachDataSource(datasetCounty);
                break;
            case R.id.nice_spinner_county:

                break;
        }
    }
    void initData(){

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
