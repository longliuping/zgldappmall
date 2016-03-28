package com.zgld.mall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zgld.mall.AppManager;
import com.zgld.mall.R;
import com.zgld.mall.SysApplication;
import com.zgld.mall.UserDataShare;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.beans.HishopSkus;
import com.zgld.mall.beans.Supplier;
import com.zgld.mall.jazzy.JazzPageChangeListener;
import com.zgld.mall.jazzy.JazzProductDetailAdapter;
import com.zgld.mall.jazzy.JazzyProductDetailViewPager;
import com.zgld.mall.pop.PublishSelectPicPopupWindow;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.PriceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener,
        PullToRefreshBase.OnRefreshListener2, PublishSelectPicPopupWindow.PublishSelectPicPopupWindowListener, JazzProductDetailAdapter.JazzProductDetailAdapterListener {
    Supplier info;
    int productId = 0;
    PullToRefreshScrollView scrollview;

    ImageView back;

    // product detail
    TextView item_title, item_sale_price,item_market_price;

    Button add_car, cart, correlation_add;
    PublishSelectPicPopupWindow menuWindow;
    ImageView item_user_head;
    TextView item_user_name;
    TextView item_user_shop_address;
    View item_user_data_base;

    // TextView title;

    /**
     * 装ViewPager中ImageView的数组
     */
    private ImageView[] mImageViews;
    private ArrayList<String> mImageUrls = new ArrayList<String>();
    private LinearLayout mIndicator = null;
    private static final int MSG_CHANGE_PHOTO = 1;
    /** 图片自动切换时间 */
    private static final int PHOTO_CHANGE_TIME = 5000;
    // ============== 广告切换 ===================
    private JazzyProductDetailViewPager mViewPager = null;
    /**
     * 装指引的ImageView数组
     */
    private ImageView[] mIndicators;
    protected Handler mHandler = null;

    @Override
    public void handleMsg(Message msg) {
        // TODO Auto-generated com.android.volley.Request.Method stub
        try {
            Bundle bundle = msg.getData();
            String json = "";
            if (bundle == null) {
                return;
            }
            json = bundle.getString(Contents.JSON);
            Gson gson = new Gson();
            switch (msg.what){
                case 205:
                    JSONObject jsonObject = new JSONObject(json).getJSONObject(Contents.DATA).getJSONObject(Contents.INFO);
                    String gsonStr = jsonObject.toString();
                    info = new Gson().fromJson(gsonStr,new TypeToken<Supplier>(){}.getType());
                    initJazzView();
                    initBData();
                    break;
                case 207:
                    BroadcastUtils.sendCarProduct(this);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scrollview.onRefreshComplete();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_product_detail);
        productId = this.getIntent().getIntExtra(Contents.PRODUCTID,0);
        if(productId<=0)
        {
            finish();
            return;
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_product_details));
        scrollview = (PullToRefreshScrollView) findViewById(R.id.product_detailscrollview);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.requestFocus();
        scrollview.setOnRefreshListener(this);

        item_user_data_base = findViewById(R.id.item_user_data_base);
        item_user_data_base.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        // product detail
        item_title = (TextView) findViewById(R.id.item_title);
        item_sale_price = (TextView) findViewById(R.id.item_sale_price);
        item_market_price = (TextView) findViewById(R.id.item_market_price);
        add_car = (Button) findViewById(R.id.add_car);
        add_car.setOnClickListener(this);
        cart = (Button) findViewById(R.id.cart);
        cart.setOnClickListener(this);
        correlation_add = (Button) findViewById(R.id.correlation_add);
        correlation_add.setOnClickListener(this);
        findViewById(R.id.item_product_param).setOnClickListener(this);
        findViewById(R.id.product_details).setOnClickListener(this);
        findViewById(R.id.product_evaluation).setOnClickListener(this);
        item_user_head = (ImageView) findViewById(R.id.item_user_head);
        item_user_name = (TextView) findViewById(R.id.item_user_name);
        item_user_shop_address = (TextView) findViewById(R.id.item_user_shop_address);
        initData();
    }

    private void initBData() {
        item_user_name.setText(info.getSupplierName());
        item_user_shop_address.setText(info.getSupplierDescribe());
        item_title.setText(info.getHishopProducts().getProductName());
        item_sale_price.setText("销售价："+ PriceUtil.priceY(info.getHishopProducts().getListHishopSkus().get(0).getSalePrice()+""));
        item_market_price.setText("市场价:"+PriceUtil.priceY(info.getHishopProducts().getMarketPrice()+""));
        SysApplication.DisplayUserImage(info.getSupplierUrl1(), item_user_head);
    }

    private void initData() {
        // 产品详细
        getData(com.android.volley.Request.Method.GET, 205, "product/product_detail.html?id=" + productId, null,
                null, 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, ProductDetailActivity.class);
//        intent.putExtra(Contents.PRODUCTID, listProduct.get(position).getProductId());
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        scrollview.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Map<String, String> m = new HashMap<String, String>();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.item_product_param:
                if (info != null) {
                    bundle.putSerializable("info", info);
                    intent.putExtras(bundle);
                    intent.setClass(this, ProductOptionFragmentActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.add_car:
                if (new UserDataShare(this).getUserData() == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                showPop();
                break;
            case R.id.cart:
                if (new UserDataShare(this).getUserData() == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;
            case R.id.back:
                finish();
                break;
            case R.id.product_details:
                if (info != null) {
                    bundle.putSerializable("info", info);
                    bundle.putInt(Contents.POSITION, Contents.TAB_PRODUCT_DETAIL);
                    intent.putExtras(bundle);
                    intent.setClass(this, ProductOptionFragmentActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.product_evaluation:
                if (info != null) {
                    bundle.putSerializable("info", info);
                    bundle.putInt(Contents.POSITION, Contents.TAB_PRODUCT_REVIEW);
                    intent.putExtras(bundle);
                    intent.setClass(this, ProductOptionFragmentActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.item_user_data_base:
                if (info != null) {
                    intent.setClass(this, SupperProductActivity.class);
                    bundle.putSerializable(Contents.INFO,info);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    private void showPop() {
        if (info != null) {
            final WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.5F; // 0.0-1.0
            getWindow().setAttributes(lp);
            // 实例化SelectPicPopupWindow
            menuWindow = new PublishSelectPicPopupWindow(this, info, this);
            // 显示窗口
            menuWindow.showAtLocation(this.findViewById(R.id.item_product_param),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated com.android.volley.Request.Method
                    // stub
                    lp.alpha = 1.0F; // 0.0-1.0
                    getWindow().setAttributes(lp);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {

            }
        }
    }

    void initJazzView() {
        if (info != null) {
            mViewPager = (JazzyProductDetailViewPager) findViewById(R.id.index_product_images_container);
            mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);
            mImageUrls = new ArrayList<String>();
            if (!TextUtils.isEmpty(info.getHishopProducts().getImageUrl1())) {
                mImageUrls.add(info.getHishopProducts().getImageUrl1());
            }
            if (!TextUtils.isEmpty(info.getHishopProducts().getImageUrl2())) {
                mImageUrls.add(info.getHishopProducts().getImageUrl2());
            }
            if (!TextUtils.isEmpty(info.getHishopProducts().getImageUrl3())) {
                mImageUrls.add(info.getHishopProducts().getImageUrl3());
            }
            if (!TextUtils.isEmpty(info.getHishopProducts().getImageUrl4())) {
                mImageUrls.add(info.getHishopProducts().getImageUrl4());
            }

            /////


            ////////////////////////////////////////////////////////////////

            mIndicators = new ImageView[mImageUrls.size()];
            // mIndicator.setBackgroundColor(Color.argb(200, 135, 135, 152));
            mIndicator.setGravity(Gravity.CENTER_HORIZONTAL);
            if (mImageUrls.size() <= 1) {
                mIndicator.setVisibility(View.GONE);
            }
            mIndicator.removeAllViews();
            for (int i = 0; i < mIndicators.length; i++) {
                ImageView imageView = new ImageView(this);
                android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 0);
                if (i != 0) {
                    params.leftMargin = 10;
                }
                imageView.setLayoutParams(params);
                mIndicators[i] = imageView;
                if (i == 0) {
                    mIndicators[i].setImageResource(R.drawable.jazz_default);
                } else {
                    mIndicators[i].setImageResource(R.drawable.jazz_pressed);
                }
                mIndicator.addView(imageView);
            }

            mImageViews = new ImageView[mImageUrls.size()];

            for (int i = 0; i < mImageViews.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setAdjustViewBounds(true);
                mImageViews[i] = imageView;
            }
            mViewPager.setTransitionEffect(JazzyProductDetailViewPager.TransitionEffect.Standard);
            mViewPager.setCurrentItem(0);
            if (mHandler != null) {
                mHandler.removeMessages(MSG_CHANGE_PHOTO);
            }
            mHandler = new Handler(this.getMainLooper()) {

                @Override
                public void handleMessage(Message msg) {
                    // TODO Auto-generated method stub
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case MSG_CHANGE_PHOTO:
                            int index = mViewPager.getCurrentItem();
                            if (index == mImageUrls.size() - 1) {
                                index = -1;
                            }
                            mViewPager.setCurrentItem(index + 1);
                            mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
                            break;
                    }
                }
            };
            mHandler.removeMessages(MSG_CHANGE_PHOTO);
            mHandler.removeCallbacks(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                }
            });
            mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);

            mViewPager.setAdapter(new JazzProductDetailAdapter(this, mViewPager, mImageUrls, mImageViews, this));
            mViewPager.setOnPageChangeListener(new JazzPageChangeListener(mIndicators));

            mViewPager.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (mImageUrls.size() == 0 || mImageUrls.size() == 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    @Override
    public void jazzOnItemClick(int position) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, ImageBrowserActivity.class);
        intent.putStringArrayListExtra(Contents.URLS, mImageUrls);
        intent.putExtra(Contents.POSITION, position);
        startActivity(intent);
    }

    @Override
    public void confirm(int number, String strNorms,HishopSkus hishopSkus, Integer valueId,Integer attributeId) {
        Map<String,String> m = new HashMap<>();
        AspnetUsers users = new UserDataShare(this).getUserData();
        if(users!=null) {
            m.put("skuId", hishopSkus.getSkuId());
            m.put("productId", hishopSkus.getProductId()+"");
            m.put("number", number+"");
            m.put("token", users.getUserToken().getAccountToken());
            m.put("userId", users.getUserId()+"");
            getData(Request.Method.POST, 207, "car/add_product_car.html", m, null
                    , 1);
        }else{
            Contents.loginPage(this,null,200);
        }
    }
}
