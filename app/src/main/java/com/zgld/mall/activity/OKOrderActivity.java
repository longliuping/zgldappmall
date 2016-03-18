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
import com.zgld.mall.adapter.OKOrderAdapter;
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.beans.HishopShoppingCarts;
import com.zgld.mall.beans.HishopUserShippingAddresses;
import com.zgld.mall.beans.Supplier;
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
        if (bundle == null) {
            return;
        }
        json = bundle.getString(Contents.JSON);
        if (json == null) {
            return;
        }
        switch (msg.what) {
            case 205:
                if (json != null && Integer.parseInt(json) > 0) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, BuyersOrdersFragmentActivity.class));// -2已有訂單
                    // -3購物車務產品
                    // -4產品庫存量不足
                    Contents.updateCarProduct(this);
                    finish();
                } else if (json.equals("-1")) {
                    Toast.makeText(this, "已有订单", Toast.LENGTH_SHORT).show();
                } else if (json.equals("-2")) {
                    Toast.makeText(this, "已有订单", Toast.LENGTH_SHORT).show();
                } else if (json.equals("-3")) {
                    Toast.makeText(this, "购物车没有该产品", Toast.LENGTH_SHORT).show();
                } else if (json.equals("-4")) {
                    Toast.makeText(this, "产品库存量不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
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
                if (addressInfo == null) {
                    Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
                    break;
                }
                Map<String, String> m = new HashMap<String, String>();
                StringBuffer productId = new StringBuffer();
                StringBuffer cartsId = new StringBuffer();
//                for (int i = 0; i < listInfo.size(); i++) {
//                    for (int j = 0; j < listInfo.get(i).getCartItems().size(); j++) {
//                        productId.append(listInfo.get(i).getCartItems().get(j).getProductId() + ",");
//                        cartsId.append(listInfo.get(i).getCartItems().get(j).getCartsId() + ",");
//                    }
//                }
                if (!TextUtils.isEmpty(productId)) {
                    productId.deleteCharAt(productId.length() - 1);
                }
                if (!TextUtils.isEmpty(cartsId)) {
                    cartsId.deleteCharAt(cartsId.length() - 1);
                }
                m.put("productId", productId.toString());
                m.put("remark", remark);
                m.put("Remark", remark);
                m.put("shippingModeId", "3");
                m.put("paymentTypeId", "1");
                m.put("cartsId", cartsId.toString());
                getData(com.android.volley.Request.Method.POST, 205, "Orders/OrdersAdd", m, null, pageIndex);
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
