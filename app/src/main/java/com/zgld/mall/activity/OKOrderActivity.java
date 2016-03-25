package com.zgld.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zgld.mall.R;
import com.zgld.mall.UserDataShare;
import com.zgld.mall.adapter.OKOrderAdapter;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.beans.HishopShoppingCarts;
import com.zgld.mall.beans.HishopUserShippingAddresses;
import com.zgld.mall.beans.Supplier;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.PriceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OKOrderActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, View.OnClickListener,
        OKOrderAdapter.BaseExpandableListAdapterListener {

    ExpandableListView listview;
    OKOrderAdapter infoAdapter;
    int pageIndex = 1;

    TextView item_pay;
    TextView item_payment_amount;
    RelativeLayout bottom;
    ArrayList<HishopShoppingCarts> listInfo = new ArrayList<>();
    RelativeLayout next;
    TextView name, address, address_title;
    HishopUserShippingAddresses addressInfo;
    int totalProductNumber = 0;// 总数量
    int totalMarketPrice = 0;// 市场总价
    int totalSalePrice = 0;//销售总价
    String remark = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated com.android.volley.Request.Method stub
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_okorder);
        listInfo = (ArrayList<HishopShoppingCarts>)this.getIntent().getSerializableExtra("listInfo");
        if(listInfo==null || listInfo.size()<=0){
            finish();
            return;
        }
        for (int i = 0;i<listInfo.size();i++){
            HishopShoppingCarts car = listInfo.get(i);
            for (int j = 0;j<car.getListHishopProducts().size();j++){
                HishopProducts pro = car.getListHishopProducts().get(j);
                totalSalePrice+=pro.getHishopSkus().getSalePrice()*car.getQuantity();
                totalMarketPrice+=pro.getMarketPrice()*car.getQuantity();
            }
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollview);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.requestFocus();
        listview = (ExpandableListView) findViewById(R.id.listview);
        listview.setGroupIndicator(null);
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_ok_order));
        item_pay = (TextView) findViewById(R.id.item_pay);
        item_pay.setOnClickListener(this);
        item_payment_amount = (TextView) findViewById(R.id.item_payment_amount);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        totalMoney();
        infoAdapter = new OKOrderAdapter(this, listInfo, this);
        listview.setAdapter(infoAdapter);
        int groupCount = listview.getCount();
        for (int i = 0; i < groupCount; i++) {
            listview.expandGroup(i);
        }
        next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        address_title = (TextView) findViewById(R.id.address_title);
    }

    @Override
    public void handleMsg(Message msg) {
        Bundle bundle = msg.getData();
        String json = "";
        json = bundle.getString(Contents.JSON);
        switch (msg.what) {
            case 205:
                if(msg.getData().getInt(Contents.STATUS)==200){
                    BroadcastUtils.sendCarProduct(this);
                    setResult(RESULT_OK);
                    Intent intent = new Intent();
                    intent.setClass(this,BuyersOrdersFragmentActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                addressInfo = (HishopUserShippingAddresses) data.getSerializableExtra("info");
                bindAddress();
            }
        }
    }

    void bindAddress() {
        if (address != null) {
            name.setText("收货人:" + addressInfo.getShipTo() + "   " + addressInfo.getCellPhone());
            address.setText("" + addressInfo.getAddress());
            address_title.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.next:
                intent.setClass(this, AddressManagerActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.item_pay:
                AspnetUsers users = new UserDataShare(this).getUserData();
                if(users==null){
                    Contents.loginPage(this,null,200);
                    return;
                }
                if (addressInfo == null) {
                    Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
                    break;
                }
                Map<String, String> m = new HashMap<String, String>();
                StringBuffer skuId = new StringBuffer();
                StringBuffer nums = new StringBuffer();
                for (int i = 0; i < listInfo.size(); i++) {
                    skuId.append(listInfo.get(i).getSkuId() + ",");
                    nums.append(listInfo.get(i).getQuantity() + ",");
                }
                if (!TextUtils.isEmpty(skuId)) {
                    skuId.deleteCharAt(skuId.length() - 1);
                }
                if (!TextUtils.isEmpty(nums)) {
                    nums.deleteCharAt(nums.length() - 1);
                }
                m.put("shippingId", addressInfo.getShippingId()+"");
                m.put("skuId", skuId.toString());
                m.put("skuNumber", nums.toString());
                m.put("modeId","8");
                m.put("templateId","2");
                m.put(Contents.TOKEN,users.getUserToken().getAccountToken());
                m.put(Contents.USERID,users.getUserId()+"");
                getData(com.android.volley.Request.Method.POST, 205, "order/submit_order.html", m, null, 1);
                break;
        }
    }

    /**
     * 统计钱和产品数量
     *
     */
    void totalMoney() {
        item_payment_amount.setText(getString(R.string.pay_price)
                + PriceUtil.priceY((totalSalePrice) + ""));

    }

    @Override
    public void remark(int groupPosition, int childPosition, String remark) {
        this.remark = remark;
    }
}
