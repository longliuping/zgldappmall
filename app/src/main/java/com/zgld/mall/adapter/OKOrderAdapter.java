package com.zgld.mall.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgld.mall.R;
import com.zgld.mall.SysApplication;
import com.zgld.mall.beans.ShopingCar;
import com.zgld.mall.beans.ShopingCartItem;
import com.zgld.mall.utils.PriceUtil;

public class OKOrderAdapter extends BaseExpandableListAdapter {
	public interface BaseExpandableListAdapterListener {
		void remark(int groupPosition, int childPosition, String remark);
	}

	List<ShopingCar> listInfo;
	LayoutInflater layoutInflater;
	Context context;
	int totalProductNumber = 0;// 总数量
	int totalMarketPrice = 0;// 价格
	int totalPostage = 0;// 运费
	BaseExpandableListAdapterListener listener;

	public OKOrderAdapter(Context context, List<ShopingCar> listInfo, int totalProductNumber, int totalMarketPrice,
			int totalPostage, BaseExpandableListAdapterListener listener) {
		// TODO Auto-generated constructor stub
		this.listInfo = listInfo;
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.totalMarketPrice = totalMarketPrice;
		this.totalPostage = totalPostage;
		this.totalProductNumber = totalProductNumber;
		this.listener = listener;
	}

	/**
	 * 获取一级标签总数
	 */
	@Override
	public int getGroupCount() {
		return listInfo.size();
	}

	/**
	 * 获取一级标签内容
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return listInfo.get(groupPosition);
	}

	/**
	 * 获取一级标签的ID
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * 获取一级标签下二级标签的总数
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// return listInfo.get(groupPosition).getProducts().size();
		return listInfo.get(groupPosition).getCartItems().size();
	}

	/**
	 * 获取一级标签下二级标签的内容
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// return listInfo.get(groupPosition).getProducts().get(childPosition);
		return listInfo.get(groupPosition).getCartItems().get(childPosition);
	}

	/**
	 * 获取二级标签的ID
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * 指定位置相应的组视图
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	class GroupViewHolder {
		TextView item_car_manufactor_name;
	}

	/**
	 * 对一级标签进行设置
	 */
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder holder = null;
		if (convertView == null) {
			holder = new GroupViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_ok_order_group, null);
			holder.item_car_manufactor_name = (TextView) convertView.findViewById(R.id.item_car_manufactor_name);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		ShopingCar info = listInfo.get(groupPosition);
		if (info != null) {
			holder.item_car_manufactor_name.setText(info.getSupplierName() + ">");
		}
		return convertView;
	}

	class ChildViewHoldeer {
		ImageView item_image;
		TextView item_title, item_detail, item_price, item_market_price, item_number;
		TextView item_number_base, item_postage_base, item_price_base;
		EditText item_remake;
		View bottom;
	}

	/**
	 * 对一级标签下的二级标签进行设置
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ChildViewHoldeer holder = null;
		if (convertView == null) {
			holder = new ChildViewHoldeer();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_ok_order, null);
			holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
			holder.item_detail = (TextView) convertView.findViewById(R.id.item_detail);
			holder.item_price = (TextView) convertView.findViewById(R.id.item_price);
			holder.item_market_price = (TextView) convertView.findViewById(R.id.item_market_price);
			holder.item_image = (ImageView) convertView.findViewById(R.id.item_image);
			holder.item_number = (TextView) convertView.findViewById(R.id.item_number);
			holder.item_number_base = (TextView) convertView.findViewById(R.id.item_number_base);
			holder.item_postage_base = (TextView) convertView.findViewById(R.id.item_postage_base);
			holder.item_price_base = (TextView) convertView.findViewById(R.id.item_price_base);
			holder.item_remake = (EditText) convertView.findViewById(R.id.item_remake);
			holder.bottom = convertView.findViewById(R.id.bottom);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHoldeer) convertView.getTag();
		}
		final ChildViewHoldeer h = holder;
		holder.bottom.setVisibility(View.GONE);
		final ShopingCartItem info = listInfo.get(groupPosition).getCartItems().get(childPosition);
		if (info != null) {
			if (isLastChild) {
				holder.bottom.setVisibility(View.VISIBLE);
			}
			holder.item_title.setText(info.getProductName());
			if (info.getNorms() != null) {
				holder.item_detail.setText(String.valueOf(info.getNorms().trim()));
			}
			// imageLoader.DisplayImage(info.getImageUrl1(), holder.item_image,
			// false);
			SysApplication.DisplayImage(info.getImageUrl1(), holder.item_image);
			holder.item_number_base.setText(totalProductNumber + "");
			if (TextUtils.isEmpty(info.getMarketPrice())) {
				info.setMarketPrice("0");
			}
			if (TextUtils.isEmpty(info.getQuantity())) {
				info.setQuantity("1");
			}
			if (TextUtils.isEmpty(info.getPostage())) {
				info.setPostage("0");
			}
			holder.item_number.setText("X" + info.getQuantity());
			holder.item_price.setText(PriceUtil.priceY(info.getSalePrice()));
			holder.item_market_price.setText(PriceUtil.priceY(info.getSalePrice()));
			holder.item_postage_base.setText(PriceUtil.priceY(info.getPostage()));
			holder.item_price_base.setText(PriceUtil.priceY(totalMarketPrice + ""));
			holder.item_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Intent intent = new Intent(context,
					// ProductDetailActivity.class);
					// intent.putExtra("ProductId", info.getProductId());
					// context.startActivity(intent);
				}
			});
			holder.item_remake.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					listener.remark(groupPosition, childPosition, s.toString());
				}
			});
		}
		return convertView;
	}

	/**
	 * 当选择子节点的时候，调用该方法
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
