package com.zgld.mall.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.activity.BuyersOrdersFragmentActivity;
import com.zgld.mall.activity.SettingActivity;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends BaseFragment {
    String menuString[] = {"我的订单","我的收藏","设置"};
    int menuInt[] = {R.drawable.me_order,R.drawable.me_love,R.drawable.me_setting};
    ListView list_menu;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_mine, container, false);
        view.findViewById(R.id.back).setVisibility(View.GONE);
        TextView title = (TextView) view.findViewById(R.id.title_center);
        title.setText("我的");
        list_menu = (ListView) view.findViewById(R.id.list_menu);
        list_menu.setAdapter(new MenuAdapter());
        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    startActivity(new Intent(getActivity(), BuyersOrdersFragmentActivity.class));
                }
                if(position==2)
                {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return menuString.length;
        }

        @Override
        public Object getItem(int position) {
            return menuString[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if(convertView==null){
                vh = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_me_menu, null);
                vh.item_name = (TextView) convertView.findViewById(R.id.item_name);
                vh.item_image = (ImageView) convertView.findViewById(R.id.item_image);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            if(menuString[position]!=null) {
                vh.item_name.setText(menuString[position]);
                vh.item_image.setImageResource(menuInt[position]);
            }
            return convertView;
        }
        class ViewHolder{
            TextView item_name;
            ImageView item_image;
        }
    }
}
