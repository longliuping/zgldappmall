package com.zgld.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.beans.HishopAttributeValues;
import com.zgld.mall.beans.HishopAttributes;

import java.util.List;

public class SelectedBaseInfoAdapter extends BaseAdapter {
	Context context;
	List<HishopAttributes> listInfo;
	LayoutInflater layoutInflater;

	public SelectedBaseInfoAdapter(Context context, int attributeId, List<HishopAttributes> listInfo) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listInfo = listInfo;
		layoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder {
		TextView title;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_selected_base_info, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setTextAppearance(context, R.style.item_text_default);
		convertView.setBackgroundResource(R.drawable.item_text_default_shape);
		HishopAttributes info = listInfo.get(position);
		if (info != null) {
//			if (info.isSelected()) {
//				holder.title.setTextAppearance(context, R.style.item_text_selected);
//				convertView.setBackgroundResource(R.drawable.item_text_selected_shape);
//			}
//			holder.title.setText(info.getValueStr());
		}
		return convertView;
	}

}
