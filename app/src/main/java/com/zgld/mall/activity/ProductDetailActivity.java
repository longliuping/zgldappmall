package com.zgld.mall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zgld.mall.AppManager;
import com.zgld.mall.R;
import com.zgld.mall.adapter.LoveProductListAdapter;
import com.zgld.mall.adapter.ProductCommentAdapter;
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.beans.Product;
import com.zgld.mall.beans.ProductComment;
import com.zgld.mall.jazzy.JazzPageChangeListener;
import com.zgld.mall.jazzy.JazzProductDetailAdapter;
import com.zgld.mall.jazzy.JazzyProductDetailViewPager;
import com.zgld.mall.pop.PublishSelectPicPopupWindow;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.widget.MyGridView;
import com.zgld.mall.widget.MyListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener,
        PullToRefreshBase.OnRefreshListener2, PublishSelectPicPopupWindow.PublishSelectPicPopupWindowListener, JazzProductDetailAdapter.JazzProductDetailAdapterListener {
    HishopProducts info;
    String ProductId = null;
    MyGridView gridview;
    LoveProductListAdapter loveProductListAdapter;
    PullToRefreshScrollView scrollview;
    List<Product> listProduct = new ArrayList<Product>();

    MyListView listview;
    List<ProductComment> listProductComments = new ArrayList<ProductComment>();
    ProductCommentAdapter productCommentAdapter;

    ImageView right, right1, back;

    // product detail
    ImageView item_image;
    TextView item_title, item_detail, item_sale_price, item_market_price, item_return_price;

    Button add_car, cart, correlation_add;
    View immediately_pay;
    LinearLayout item_love;
    PublishSelectPicPopupWindow menuWindow;
    ImageView item_user_head;
    TextView item_user_name;
    View share, item_user_data_base;

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
    View product_pay;

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
                    JSONObject jsonObject = new JSONObject(json).getJSONObject("data").getJSONObject("info");
                    info = new Gson().fromJson(jsonObject.toString(),new TypeToken<HishopProducts>(){}.getType());

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
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_product_detail);
        ProductId = this.getIntent().getStringExtra(Contents.PRODUCTID);
        ProductId = "45";
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
        scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        gridview = (MyGridView) findViewById(R.id.gridview);
        gridview.setOnItemClickListener(this);
        loveProductListAdapter = new LoveProductListAdapter(this, listProduct);

        listProductComments = new ArrayList<ProductComment>();
        listview = (MyListView) findViewById(R.id.listview);
        productCommentAdapter = new ProductCommentAdapter(this, listProductComments);
        listview.setAdapter(productCommentAdapter);
        item_user_data_base = findViewById(R.id.item_user_data_base);
        item_user_data_base.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        // product detail
        item_image = (ImageView) findViewById(R.id.item_image);
        item_title = (TextView) findViewById(R.id.item_title);
        item_detail = (TextView) findViewById(R.id.item_detail);
        item_sale_price = (TextView) findViewById(R.id.item_sale_price);
        item_market_price = (TextView) findViewById(R.id.item_market_price);
        item_return_price = (TextView) findViewById(R.id.item_return_price);
        immediately_pay = findViewById(R.id.immediately_pay);
        immediately_pay.setOnClickListener(this);
        add_car = (Button) findViewById(R.id.add_car);
        add_car.setOnClickListener(this);
        cart = (Button) findViewById(R.id.cart);
        cart.setOnClickListener(this);
        correlation_add = (Button) findViewById(R.id.correlation_add);
        correlation_add.setOnClickListener(this);
        findViewById(R.id.item_product_param).setOnClickListener(this);
        item_love = (LinearLayout) findViewById(R.id.item_love);
        item_love.setOnClickListener(this);
        findViewById(R.id.product_norms).setOnClickListener(this);
        findViewById(R.id.product_details).setOnClickListener(this);
        findViewById(R.id.product_evaluation).setOnClickListener(this);
        item_user_head = (ImageView) findViewById(R.id.item_user_head);
        item_user_name = (TextView) findViewById(R.id.item_user_name);
        share = findViewById(R.id.share);
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);
        product_pay = findViewById(R.id.product_pay);
        product_pay.setOnClickListener(this);
        // initBData();
        initData();
    }

    private void initBData() {
        initJazzView();
    }

    private void initData() {
        // 产品详细
        getData(com.android.volley.Request.Method.GET, 205, "product_detail.html?id=" + ProductId, null,
                null, 1);// + "&userId=" + Contents.getUser(this).getUserId()
        // 喜欢
//		getData(com.android.volley.Request.Method.GET, 201,
//				"Products/GetProducts?putawayType=-1&pageSize=6&pageIndex=1&startDate=&sort=2", null, null, 1);
        // 评论
//        Map<String, String> m = new HashMap<String, String>();
//        m.put("productId", ProductId);
//        m.put("Size", 10 + "");
//        m.put("pageIndex", 1 + "");
//        getData(com.android.volley.Request.Method.POST, 666, "ProductReviews/QueryProductReview", m, null, 1);
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
//                    intent.setClass(this, ProductOptionFragmentActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.add_car:
                if (Contents.getUser(this) == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                showPop();
                break;
            case R.id.cart:
                if (Contents.getUser(this) == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                // Intent mIntent = new Intent("car");
                // mIntent.putExtra("shop", true);
                // mIntent.setClass(getApplicationContext(), MainActivity.class);
                // startActivity(mIntent);
                // finish();
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;
            case R.id.correlation_add:
                if (Contents.getUser(this) == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                if (info != null) {
                    Toast.makeText(this, getString(R.string.can_not_add_their_own_products), Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.item_love:
                if (Contents.getUser(this) == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.product_details:
                if (info != null) {
//                    bundle.putSerializable("info", info);
//                    bundle.putInt(Contents.POSITION, Contents.TAB_PRODUCT_DETAIL);
//                    intent.putExtras(bundle);
//                    intent.setClass(this, ProductOptionFragmentActivity.class);
//                    startActivity(intent);
                }
                break;
            case R.id.product_norms:
                if (info != null) {
                    Toast.makeText(this, getString(R.string.can_not_sell_their_own_products), Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    showPop();
                }
                break;
            case R.id.product_evaluation:
                if (info != null) {
//                    bundle.putSerializable("info", info);
//                    bundle.putInt(Contents.POSITION, Contents.TAB_PRODUCT_REVIEW);
//                    intent.putExtras(bundle);
//                    intent.setClass(this, ProductOptionFragmentActivity.class);
//                    startActivity(intent);
                }
                break;
            case R.id.immediately_pay:
                if (Contents.getUser(this) == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                if (info != null) {
                    Toast.makeText(this, getString(R.string.can_not_sell_their_own_products), Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    showPop();
                }
                break;
            case R.id.item_user_data_base:
                if (Contents.getUser(this) == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                if (info != null) {
//                    intent.setClass(this, MyShopActivity.class);
//                    intent.putExtra("UserId", info.getUserId());
//                    intent.putExtra("UserName", info.getUserName());
//                    intent.putExtra("HeadImg", info.getHeadImg());
                    startActivity(intent);
                }
                break;
            case R.id.product_pay:
                if (Contents.getUser(this) == null) {
                    Contents.loginPage(this,null,200);
                    return;
                }
                if (info != null) {
                    Toast.makeText(this, getString(R.string.can_not_sell_their_own_products), Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    showPop();
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
            menuWindow.showAtLocation(this.findViewById(R.id.product_norms),
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

    int number = 1;
    String strNorms = "";
    String ids = "";

    @Override
    public void confirm(int number) {
        // TODO Auto-generated com.android.volley.Request.Method stub
        this.number = number;

    }

    void initJazzView() {
        if (info != null) {
            mViewPager = (JazzyProductDetailViewPager) findViewById(R.id.index_product_images_container);
            mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);
            mImageUrls = new ArrayList<String>();
//            if (!TextUtils.isEmpty(info.getImageUrl1())) {
//                mImageUrls.add(Contents.BASE_IMAGE_PATH + info.getImageUrl1());
//            }
//            if (!TextUtils.isEmpty(info.getImageUrl2())) {
//                mImageUrls.add(Contents.BASE_IMAGE_PATH + info.getImageUrl2());
//            }
//            if (!TextUtils.isEmpty(info.getImageUrl3())) {
//                mImageUrls.add(Contents.BASE_IMAGE_PATH + info.getImageUrl3());
//            }
//            if (!TextUtils.isEmpty(info.getImageUrl4())) {
//                mImageUrls.add(Contents.BASE_IMAGE_PATH + info.getImageUrl4());
//            }

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
                    mIndicators[i].setBackgroundResource(R.drawable.jazz_default);
                } else {
                    mIndicators[i].setBackgroundResource(R.drawable.jazz_pressed);
                }
                mIndicator.addView(imageView);
            }

            mImageViews = new ImageView[mImageUrls.size()];

            for (int i = 0; i < mImageViews.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

}
