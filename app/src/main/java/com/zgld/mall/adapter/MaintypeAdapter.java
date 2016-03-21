package com.zgld.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.activity.ProductDetailActivity;
import com.zgld.mall.beans.HishopProductTypes;
import com.zgld.mall.utils.Contents;

import java.util.List;

/**
 * Created by LongLiuPing on 2016/3/21.
 */
public class MaintypeAdapter extends BaseAdapter{
    Context context;
    List<HishopProductTypes> listInfo;
    public MaintypeAdapter(  Context context,List<HishopProductTypes> listInfo){
        this.context = context;
        this.listInfo = listInfo;
    }
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
    class ViewHolder{
        GridView item_gridview;
        TextView item_name;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_type,null);
            vh.item_gridview = (GridView) convertView.findViewById(R.id.item_gridview);
            vh.item_name = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final HishopProductTypes info = listInfo.get(position);
        if(info!=null){
            vh.item_name.setText(info.getTypeName());
            vh.item_gridview.setAdapter(new HomeProductAdapter(context, info.getHishopProducts()));
            vh.item_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(Contents.PRODUCTID,info.getHishopProducts().get(position).getProductId());
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }
}