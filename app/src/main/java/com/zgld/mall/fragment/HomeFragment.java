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
import android.widget.ScrollView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kevin.loopview.AdLoopView;
import com.kevin.loopview.internal.BaseLoopAdapter;
import com.zgld.mall.R;
import com.zgld.mall.activity.ProductDetailActivity;
import com.zgld.mall.beans.Product;
import com.zgld.mall.beans.TypeInfo;
import com.zgld.mall.pulltorefresh.LocalFileUtils;
import com.zgld.mall.widget.FullyGridLayoutManager;
import com.zgld.mall.widget.FullyLinearLayoutManager;
import com.zgld.mall.widget.GridSpacingItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    PullToRefreshScrollView home_fragment_scrollview;
    private GridView home_fragment_gridview1,home_fragment_gridview2;
//    private LinkedList<String> mListItems;
    private  LinkedList<Product> listInfo;
    // 自定义可添加头部、尾部 RecyclerView数据适配器包装类
//    private WrapAdapter<RecyclerViewAdapter> mWrapAdapter;
    private MyGridViewAdapter mAdapter;
    private AdLoopView mAdLoopView;
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
        home_fragment_scrollview = (PullToRefreshScrollView) view.findViewById(R.id.home_fragment_scrollview);
        home_fragment_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNumber = 1;
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
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
        listInfo = new LinkedList<>();
        Product product = new Product();
        product.setName("韩版男装12春秋装新款男士夹克男修身男式夹克休闲");
        listInfo.add(product);
        listInfo.add(product);
        listInfo.add(product);
        listInfo.add(product);
        listInfo.add(product);
        listInfo.add(product);
        pageNumber++;
        mAdapter = new MyGridViewAdapter();
        home_fragment_gridview1.setAdapter(mAdapter);
        home_fragment_gridview2.setAdapter(mAdapter);
        initLoopView();
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
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            Product info = listInfo.get(position);
            if(info!=null){
                vh.item_name.setText(info.getName());
            }
            return convertView;
        }
        class ViewHolder{
            TextView item_name;
        }
    }
}
