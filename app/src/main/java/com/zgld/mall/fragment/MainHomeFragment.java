package com.zgld.mall.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kevin.loopview.AdLoopView;
import com.kevin.loopview.internal.BaseLoopAdapter;
import com.zgld.mall.R;
import com.zgld.mall.adapter.MaintypeAdapter;
import com.zgld.mall.beans.HishopProductTypes;
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.pulltorefresh.LocalFileUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainHomeFragment extends BaseFragment {
    PullToRefreshScrollView home_fragment_scrollview;
    ListView listview;
    private AdLoopView mAdLoopView;
    int pageNumber = 1;
    MaintypeAdapter infoAdapter;
    public MainHomeFragment() {
        // Required empty public constructor
    }
    Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this.getActivity();
    }
    @Override
    public void handleMsg(Message msg) {
        home_fragment_scrollview.onRefreshComplete();
        try{
            String json = msg.getData().getString("data");
            switch (msg.what){
                case  201:
                    mAdLoopView = (AdLoopView) view.findViewById(R.id.home_frag_rotate_vp);
                    // 初始化RotateView数据
//                    String json = LocalFileUtils.getStringFormAsset(activity.getApplicationContext(), "loopview.json");
                    mAdLoopView.refreshData(json);
                    mAdLoopView.startAutoLoop();mAdLoopView.setOnClickListener(new BaseLoopAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(PagerAdapter parent, View view, int position, int realPosition) {
                        Log.i("", "");
                        Log.i("", "");
                    }
                });
                    break;
                case 202:
                    json = new JSONObject(json).getJSONArray("listInfo").toString();
                    List<HishopProductTypes> listHishopProductTypes = new Gson().fromJson(json,new TypeToken<List<HishopProductTypes>>() {
                    }.getType());
                    infoAdapter = new MaintypeAdapter(getContext(),listHishopProductTypes);
                    listview.setAdapter(infoAdapter);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();;
        }
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_home, container, false);
            view.findViewById(R.id.back).setVisibility(View.GONE);
            TextView title = (TextView) view.findViewById(R.id.title_center);
            title.setText("首页");
            initViews();
        }else{
            ViewGroup group = (ViewGroup) view.getParent();
            if (group != null) {
                group.removeView(view);
            }
        }
        return view;
    }

    private void initViews() {
        listview = (ListView) view.findViewById(R.id.listview);
        home_fragment_scrollview = (PullToRefreshScrollView) view.findViewById(R.id.home_fragment_scrollview);
        home_fragment_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNumber = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
        });

        initLoopView();
        getDataCache(Request.Method.GET, 201, "home_banner.html", null, null, 1);
        initData();
    }
    public void initData(){
        getData(Request.Method.GET, 201, "home_banner.html", null, null, 1);
        getData(Request.Method.GET, 202, "product/home_all_product.html", null, null, 1);
    }
    /**
     * 初始化LoopView
     *
     * 这里使用的是LoopView开源项目，项目地址：https://github.com/xuehuayous/Android-LoopView
     *
     * @return void
     */
    private void initLoopView() {
        mAdLoopView = (AdLoopView) view.findViewById(R.id.home_frag_rotate_vp);

        // 初始化RotateView数据
        String json = LocalFileUtils.getStringFormAsset(activity.getApplicationContext(), "loopview.json");
        mAdLoopView.refreshData(json);
        mAdLoopView.startAutoLoop();mAdLoopView.setOnClickListener(new BaseLoopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PagerAdapter parent, View view, int position, int realPosition) {
                Log.i("", "");
                Log.i("", "");
            }
        });
    }

}
