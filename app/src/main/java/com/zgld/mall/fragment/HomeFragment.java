package com.zgld.mall.fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.zgld.mall.SysApplication;
import com.zgld.mall.activity.ProductDetailActivity;
import com.zgld.mall.beans.HishopProductTypes;
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.beans.Product;
import com.zgld.mall.beans.TypeInfo;
import com.zgld.mall.pulltorefresh.LocalFileUtils;
import com.zgld.mall.widget.FullyGridLayoutManager;
import com.zgld.mall.widget.FullyLinearLayoutManager;
import com.zgld.mall.widget.GridSpacingItemDecoration;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    PullToRefreshScrollView home_fragment_scrollview;
    private GridView home_fragment_gridview1,home_fragment_gridview2,home_fragment_gridview3,home_fragment_gridview4,home_fragment_gridview5,home_fragment_gridview6;
    // 自定义可添加头部、尾部 RecyclerView数据适配器包装类
//    private WrapAdapter<RecyclerViewAdapter> mWrapAdapter;
    private MyGridViewAdapter adapter1,adapter2,adapter3,adapter4,adapter5,adapter6;
    private AdLoopView mAdLoopView;
    TextView item_name1,item_name2,item_name3,item_name4,item_name5,item_name6;
    int pageNumber = 1;
    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
                    for (int i = 0;i<listHishopProductTypes.size();i++)
                    {
                        HishopProductTypes type = listHishopProductTypes.get(i);
                        List<HishopProducts> listInfo = type.getHishopProducts();
                        switch (i){
                            case 0:
                                adapter1 = new MyGridViewAdapter(listInfo);
                                home_fragment_gridview1.setAdapter(adapter1);
                                item_name1.setText(type.getTypeName());
                                break;
                            case 1:
                                adapter2 = new MyGridViewAdapter(listInfo);
                                home_fragment_gridview2.setAdapter(adapter2);
                                item_name2.setText(type.getTypeName());
                                break;
                            case 2:
                                adapter3 = new MyGridViewAdapter(listInfo);
                                home_fragment_gridview3.setAdapter(adapter3);
                                item_name3.setText(type.getTypeName());
                                break;
                            case 3:
                                adapter4 = new MyGridViewAdapter(listInfo);
                                home_fragment_gridview4.setAdapter(adapter4);
                                item_name4.setText(type.getTypeName());
                                break;
                            case 4:
                                adapter5 = new MyGridViewAdapter(listInfo);
                                home_fragment_gridview5.setAdapter(adapter5);
                                item_name5.setText(type.getTypeName());
                                break;
                            case 5:
                                adapter6 = new MyGridViewAdapter(listInfo);
                                home_fragment_gridview6.setAdapter(adapter6);
                                item_name6.setText(type.getTypeName());
                                break;
                        }
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();;
        }
    }
    Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this.getActivity();
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
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
    /**
     * 初始化View
     */
    private void initViews() {
        item_name1 = (TextView) view.findViewById(R.id.item_name1);
        item_name2 = (TextView) view.findViewById(R.id.item_name2);
        item_name3 = (TextView) view.findViewById(R.id.item_name3);
        item_name4 = (TextView) view.findViewById(R.id.item_name4);
        item_name5 = (TextView) view.findViewById(R.id.item_name5);
        item_name6 = (TextView) view.findViewById(R.id.item_name6);
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
        home_fragment_gridview1 = (GridView) view.findViewById(R.id.home_fragment_gridview1);
        home_fragment_gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ProductDetailActivity.class));
            }
        });
        home_fragment_gridview2 = (GridView) view.findViewById(R.id.home_fragment_gridview2);
        home_fragment_gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ProductDetailActivity.class));
            }
        });
        home_fragment_gridview3 = (GridView) view.findViewById(R.id.home_fragment_gridview3);
        home_fragment_gridview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ProductDetailActivity.class));
            }
        });
        home_fragment_gridview4 = (GridView) view.findViewById(R.id.home_fragment_gridview4);
        home_fragment_gridview4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ProductDetailActivity.class));
            }
        });
        home_fragment_gridview5 = (GridView) view.findViewById(R.id.home_fragment_gridview5);
        home_fragment_gridview5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ProductDetailActivity.class));
            }
        });
        home_fragment_gridview6 = (GridView) view.findViewById(R.id.home_fragment_gridview6);
        home_fragment_gridview6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ProductDetailActivity.class));
            }
        });
//        pageNumber++;
        adapter1 = new MyGridViewAdapter(new ArrayList<HishopProducts>());
        adapter2 = new MyGridViewAdapter(new ArrayList<HishopProducts>());
        adapter3 = new MyGridViewAdapter(new ArrayList<HishopProducts>());
        adapter4 = new MyGridViewAdapter(new ArrayList<HishopProducts>());
        adapter5 = new MyGridViewAdapter(new ArrayList<HishopProducts>());
        adapter6 = new MyGridViewAdapter(new ArrayList<HishopProducts>());
        home_fragment_gridview1.setAdapter(adapter1);
        home_fragment_gridview2.setAdapter(adapter2);
        home_fragment_gridview3.setAdapter(adapter3);
        home_fragment_gridview4.setAdapter(adapter4);
        home_fragment_gridview5.setAdapter(adapter5);
        home_fragment_gridview6.setAdapter(adapter6);

        initLoopView();
        getDataCache(Request.Method.GET, 201, "home_banner.html", null, null, 1);
        initData();
    }
    public void initData(){
        getData(Request.Method.GET,201,"home_banner.html",null,null,1);
        getData(Request.Method.GET,202,"home_all_product.html",null,null,1);
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

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            if(pageNumber==1){
//                listInfo.clear();
//            }
//            Product product = new Product();
//            product.setName("男装");
//            product.setName("JackSoney");
//            listInfo.add(product);
//            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            home_fragment_scrollview.onRefreshComplete();
            pageNumber ++;
            super.onPostExecute(result);
        }
    }

    class MyGridViewAdapter extends BaseAdapter {
        private  List<HishopProducts> listInfo;
        MyGridViewAdapter(List<HishopProducts> listInfo){
            this.listInfo = listInfo;
        }
        LayoutInflater layoutInflater;
        @Override
        public int getCount() {
            return listInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return listInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if(convertView==null){
                vh = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_product,null);
                vh.item_name = (TextView) convertView.findViewById(R.id.item_name);
                vh.item_image = (ImageView) convertView.findViewById(R.id.item_image);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            HishopProducts info = listInfo.get(position);
            if(info!=null){
                vh.item_name.setText(info.getProductName());
                SysApplication.DisplayImage(info.getImageUrl1(),vh.item_image);
            }
            return convertView;
        }
        class ViewHolder{
            TextView item_name;
            ImageView item_image;
        }
    }
}
