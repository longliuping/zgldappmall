package com.zgld.mall.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.zgld.mall.R;
import com.zgld.mall.adapter.BuyersOrdersAdapter;
import com.zgld.mall.beans.Orders;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.volley.NetWorkTools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * 买家订单 待付款
 *
 * @author LLP
 *
 */
public class BuyersOrdersWaitPaymentFragment extends BuyersOrdersBaseFragment implements OnRefreshListener2,
        OnItemClickListener, OnClickListener, BuyersOrdersAdapter.BuyersOrdersAdapterListener {
    List<Orders> listInfo = new ArrayList<Orders>();
    PullToRefreshExpandableListView listview;
    BuyersOrdersAdapter infoAdapter;
    int pageIndex = 1;
    View view;
    Activity activity;
    View null_data_default, network_error;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.seller_orders_expandable, null);
            isPrepared = true;
            lazyLoad();
            registerBoradcastReceiver();
        } else {
            ViewGroup group = (ViewGroup) view.getParent();
            if (group != null) {
                group.removeView(view);
            }
        }

        return view;
    }

    private void initData() {
        network_error.setVisibility(View.GONE);
        null_data_default.setVisibility(View.GONE);
        if (pageIndex == 1 && !NetWorkTools.isHasNet(activity)) {
            null_data_default.setVisibility(View.GONE);
            network_error.setVisibility(View.VISIBLE);
            network_error.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    initData();
                }
            });
            listview.onRefreshComplete();
            return;
        }
//        getData(Method.GET, 202, "Orders/QueryOrdersNew?token=" + Contents.getUser(activity).getToken() + "&userId="
//                + Contents.getUser(activity).getUserId() + "&pageSize=10&pageIndex=" + pageIndex
//                + "&Areaflag=2&orderStatus=1", null, null, 1);
    }

    @Override
    public void handleMsg(Message msg) {
        try {
            Bundle bundle = msg.getData();
            String json = "";
            if (bundle == null) {
                return;
            }
            json = bundle.getString(Contents.JSON);
            Gson gson = new Gson();
            Type entityType = null;
            JSONArray jsonArray = new JSONArray();
            switch (msg.what) {
                case 201:
                    break;
                case 202:
                    try {
                        if (TextUtils.isEmpty(json)) {
                            if (pageIndex == 1) {
                                listInfo = new ArrayList<Orders>();
                                infoAdapter = new BuyersOrdersAdapter(activity, listInfo, false, this);
                                listview.getRefreshableView().setAdapter(infoAdapter);
                                null_data_default.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(json);
                        jsonArray = jsonObject.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    entityType = new TypeToken<List<Orders>>() {
                    }.getType();
                    if (pageIndex == 1) {
                        listInfo = new ArrayList<Orders>();
                        infoAdapter = new BuyersOrdersAdapter(activity, listInfo, false, this);
                        listview.getRefreshableView().setAdapter(infoAdapter);
                    }
                    List<Orders> list = gson.fromJson(jsonArray.toString(), entityType);
                    if (list == null || list.size() <= 0) {
                        Toast.makeText(activity, activity.getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    }
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            Orders info = list.get(i);
                            listInfo.add(info);
                        }
                        infoAdapter = new BuyersOrdersAdapter(activity, listInfo, false, this);
                        listview.getRefreshableView().setAdapter(infoAdapter);
                        int groupCount = listview.getRefreshableView().getCount();
                        for (int i = 0; i < groupCount; i++) {
                            listview.getRefreshableView().expandGroup(i);
                        }
                        infoAdapter.notifyDataSetChanged();
                        listview.invalidate();
                        pageIndex++;
                    }
                    if (listInfo == null || listInfo.size() <= 0) {
                        null_data_default.setVisibility(View.VISIBLE);
                    } else {
                        null_data_default.setVisibility(View.GONE);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listview.onRefreshComplete();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
//        Intent intent = new Intent(activity, OrderDetailsActivity.class);
//        intent.putExtra("orderId", listInfo.get(position).getOrderId());
//        startActivityForResult(intent, 200);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        initData();
    }

    @Override
    public void onClick(View v) {
    }

    int sendCargoPosition;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == activity.RESULT_OK) {
            if (requestCode == 209) {
                listInfo.remove(commentProductPosition);
                infoAdapter.notifyDataSetChanged();
            }
        }
    }

    int confirmOrderPosition;

    @Override
    public void confirmOrder(int groupPosition, int childPosition) {
        confirmOrderPosition = groupPosition;
//        getData(Method.GET, 307, "Orders/OrderStatusUpdate?orderId=" + listInfo.get(groupPosition).getOrderId()
//                + "&orderStatus=5&userId=" + Contents.getUser(activity).getUserId() + "", null, null, 1);
    }

    @Override
    public void cannerOrder(int groupPosition, int childPosition) {
    }

    int commentProductPosition;

    @Override
    public void commentProduct(int groupPosition, int childPosition) {
        commentProductPosition = groupPosition;
//        Intent intent = new Intent(activity, ProductEvaluationActivity.class);
//        intent.putExtra(Contents.INFO, listInfo.get(groupPosition));
//        startActivityForResult(intent, 209);
    }

    int applyRefundPosition;

    @Override
    public void applyRefund(int groupPosition, int childPosition) {
    }

    @Override
    public void payment(int groupPosition, int childPosition) {
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastUtils.BUYERS_ORDER_ACTION_NAME)) {
                // pageIndex = 1;
                // initData();
            }
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastUtils.BUYERS_ORDER_ACTION_NAME);
        // 注册广播
        activity.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            activity.unregisterReceiver(mBroadcastReceiver);
        }
    }

    private int mCurIndex = -1;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        listview = (PullToRefreshExpandableListView) view.findViewById(R.id.listview);
        listview.setMode(Mode.BOTH);
        listview.setOnRefreshListener(this);
        listview.setOnItemClickListener(this);
        listview.getRefreshableView().setGroupIndicator(null);
        listview.getRefreshableView().setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        pageIndex = 1;
        listInfo = new ArrayList<Orders>();
        infoAdapter = new BuyersOrdersAdapter(activity, listInfo, false, this);
        listview.getRefreshableView().setAdapter(infoAdapter);
        infoAdapter.notifyDataSetChanged();
        null_data_default = view.findViewById(R.id.null_data_default);
        null_data_default.setVisibility(View.GONE);
        network_error = view.findViewById(R.id.network_error);
        network_error.setVisibility(View.GONE);
        initData();
    }
}
