package com.zgld.mall.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zgld.mall.R;
import com.zgld.mall.beans.HishopUserShippingAddresses;
import com.zgld.mall.beans.Supplier;
import com.zgld.mall.fragment.ProductDescriptionFragment;
import com.zgld.mall.fragment.ProductParamFragment;
import com.zgld.mall.fragment.ProductReviewFragment;
import com.zgld.mall.indicator.FragmentPagerAdp;
import com.zgld.mall.indicator.TabInfoBean;
import com.zgld.mall.indicator.TitleIndicator;
import com.zgld.mall.pop.PublishSelectPicPopupWindow;
import com.zgld.mall.utils.Contents;

/**
 * 关于产品
 *
 */
public class ProductOptionFragmentActivity extends BaseFragmentActivity implements OnClickListener,
        OnPageChangeListener, PublishSelectPicPopupWindow.PublishSelectPicPopupWindowListener {
    private TextView title;
    private Intent intent;
    private Supplier info = new Supplier();
    Button correlation_add;
    TextView product_menu_1, product_menu_2, product_menu_3, product_menu_4;

    protected int mCurrentTab = 0;
    protected int mLastTab = -1;
    // 存放选项卡信息的列表
    protected ArrayList<TabInfoBean> mTabs = new ArrayList<TabInfoBean>();
    // viewpager adapter
    protected FragmentPagerAdp myAdapter = null;
    // viewpager
    protected ViewPager mPager;
    // 选项卡控件
    protected TitleIndicator mIndicator;

    public TitleIndicator getIndicator() {
        return mIndicator;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        initStyle();
        setContentView(R.layout.activity_product_option_fragment);
        intent = getIntent();
        info = (Supplier) intent.getSerializableExtra(Contents.INFO);
        initView();
        initFragment();
    }

    private void initView() {
        findViewById(R.id.add_car).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.cart).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_parameter_page));
        title.setTextSize(18);
        correlation_add = (Button) findViewById(R.id.correlation_add);
        correlation_add.setOnClickListener(this);

//        if (info != null || "3".equals(info.getPutawayType())) {
//            if (info.getPutawayType().equals("3")) {
//                Button add_car = (Button) findViewById(R.id.add_car);
//                add_car.setText("首付款");
//            }
//        }
    }

    private void initFragment() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (TitleIndicator) findViewById(R.id.pagerindicator);
        // 设置viewpager内部页面之间的间距
        // mPager.setPageMargin(0);
        // 这里初始化界面
        mCurrentTab = supplyTabs(mTabs);
        Intent intent = this.getIntent();
        if (intent != null) {
            mCurrentTab = intent.getIntExtra(Contents.POSITION, mCurrentTab);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("info",info);
        myAdapter = new FragmentPagerAdp(this, getSupportFragmentManager(), mTabs,bundle);

        mPager.setAdapter(myAdapter);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(1);

        mIndicator.init(mCurrentTab, mTabs, mPager);

        mPager.setCurrentItem(mCurrentTab);
        mLastTab = mCurrentTab;

    }

    private int supplyTabs(ArrayList<TabInfoBean> mTabs2) {
        // TODO Auto-generated method stub
        String[] titles = getResources().getStringArray(R.array.title_product_option);
        mTabs2.add(new TabInfoBean(Contents.FRAGMENT_ONE, titles[0], ProductParamFragment.class));
        mTabs2.add(new TabInfoBean(Contents.FRAGMENT_TWO, titles[1], ProductDescriptionFragment.class));
        mTabs2.add(new TabInfoBean(Contents.FRAGMENT_THREE, titles[2], ProductReviewFragment.class));
        return Contents.FRAGMENT_ONE;
    }

    /**
     * 添加一个选项卡
     *
     * @param tab
     */
    public void addTabInfo(TabInfoBean tab) {
        mTabs.add(tab);
        myAdapter.notifyDataSetChanged();
    }

    protected TabInfoBean getFragmentById(int tabId) {
        if (mTabs == null)
            return null;
        for (int index = 0, count = mTabs.size(); index < count; index++) {
            TabInfoBean tab = mTabs.get(index);
            if (tab.getId() == tabId) {
                return tab;
            }
        }
        return null;
    }

    /**
     * 跳转到任意选项卡
     *
     * @param tabId
     *            选项卡下标
     */
    public void navigate(int tabId) {
        for (int index = 0, count = mTabs.size(); index < count; index++) {
            if (mTabs.get(index).getId() == tabId) {
                mPager.setCurrentItem(index);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // for fix a known issue in support library
        // https://code.google.com/p/android/issues/detail?id=19917
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    /**
     * 以下三个方法是针对viewpager状态的监听设置
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            mLastTab = mCurrentTab;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TODO Auto-generated method stub
        mIndicator.onScrolled((mPager.getWidth() + mPager.getPageMargin()) * position + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        mIndicator.onSwitched(position);
        mCurrentTab = position;
    }

    /**
     * FragmentActivity向Fragemnt发出通知,在Fragment中必须实现这个接口，否则报类造型异常 将接口和Fragment绑定
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        try {
            super.onAttachFragment(fragment);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        Map<String, String> m = new HashMap<String, String>();
        switch (v.getId()) {
            case R.id.add_car:
                showPop();
                break;
            case R.id.cart:
                // intent.putExtra("car", true);
                // intent.setClass(getApplicationContext(), MainActivity.class);
                // startActivity(intent);
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;
            case R.id.back:
                finish();
                break;
            case R.id.correlation_add:
//                if (info != null && (info.getUserId().equals(Contents.getUser(this).getUserId()))) {
//                    Toast.makeText(this, getString(R.string.can_not_add_their_own_products), Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if (info != null) {
//                    getData(com.android.volley.Request.Method.GET,
//                            208,
//                            "UserOrProduct/UserOrProductCorrelationAdd?productId=" + info.getProductId() + "&userId="
//                                    + Contents.getUser(this).getUserId() + "&token=" + Contents.getUser(this).getToken()
//                                    + "&returnPrice=" + info.getReturnPrice() + "&purchaseDate="
//                                    + DateUtil.getDay(new Date().getTime()) + "&purchaseNum=1&accessTimes=&remark=", null,
//                            null, true);
//                }
                break;

        }

    }

    @Override
    public void handleMsg(Message msg) {
        // TODO Auto-generated method stub
        Bundle bundle = msg.getData();
        String json = "";
        if (bundle == null) {
            return;
        }
        json = bundle.getString(Contents.JSON);
        switch (msg.what) {
            case 202:// 加入购物车
//                if (json != null) {
//                    if (json.trim().equals("1")) {
//                        Toast.makeText(this, getString(R.string.add_shopping_cart_success), Toast.LENGTH_SHORT).show();
//                        BroadcastUtils.sendCarProduct(this);
//                    } else if (json.trim().equals("-2")) {
//                        Toast.makeText(this, getString(R.string.can_not_sell_their_own_products), Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, getString(R.string.add_shopping_cart_failed), Toast.LENGTH_SHORT).show();
//                }
                break;
            case 208:
//                if (json != null) {
//                    if (json.trim().equals("1")) {
//                        Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
//                    } else if (json.trim().equals("0")) {
//                        Toast.makeText(this, getString(R.string.can_not_add_their_own_products), Toast.LENGTH_SHORT).show();
//                    } else if (json.trim().equals("-1")) {
//                        Toast.makeText(this, getString(R.string.add_shopping_myshop_exis), Toast.LENGTH_SHORT).show();
//                    } else if (json.trim().equals("2")) {
//                        Toast.makeText(this, getString(R.string.add_shopping_myshop_use), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, getString(R.string.add_shopping_myshop_failed), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, getString(R.string.add_shopping_myshop_failed), Toast.LENGTH_SHORT).show();
//                }
                break;
            case 305:
                if (json != null && Integer.parseInt(json) > 0) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, BuyersOrdersFragmentActivity.class));// -2已有訂單//
                    // -3購物車務產品//
                    // -4產品庫存量不足
                    finish();
                } else if (json.equals("-1")) {
                    Toast.makeText(this, "已有订单", Toast.LENGTH_SHORT).show();
                } else if (json.equals("-2")) {
                    Toast.makeText(this, "已有订单", Toast.LENGTH_SHORT).show();
                } else if (json.equals("-3")) {
                    Toast.makeText(this, "产品库存量不足", Toast.LENGTH_SHORT).show();
                } else if (json.equals("-4")) {
                    Toast.makeText(this, "产品库存量不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    PublishSelectPicPopupWindow menuWindow;

    private void showPop() {
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5F; // 0.0-1.0
        getWindow().setAttributes(lp);
        // 实例化SelectPicPopupWindow
        menuWindow = new PublishSelectPicPopupWindow(this, info, this);
        // 显示窗口
        menuWindow.showAtLocation(this.findViewById(R.id.add_car), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        menuWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                lp.alpha = 1.0F; // 0.0-1.0
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                HishopUserShippingAddresses addressInfo = (HishopUserShippingAddresses) data.getSerializableExtra("info");

            }
        }
    }

    int number = 1;
    String strNorms = "";
    String ids = "";

    @Override
    public void confirm(int number, String strNorms, String ids) {
        // TODO Auto-generated method stub
        this.number = number;
        this.strNorms = strNorms;
        this.ids = ids;
        if (info != null && (info.getUserId().equals(Contents.getUser(this).getUserId()))) {
            Toast.makeText(this, getString(R.string.can_not_sell_their_own_products), Toast.LENGTH_SHORT).show();
            return;
        }
    }

}
