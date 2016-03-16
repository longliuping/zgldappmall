package com.zgld.mall.pop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zgld.mall.R;
import com.zgld.mall.SysApplication;
import com.zgld.mall.adapter.SelectedInfoAdapter;
import com.zgld.mall.beans.HishopAttributeValues;
import com.zgld.mall.beans.HishopAttributes;
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.beans.HishopSkuitems;
import com.zgld.mall.beans.HishopSkus;
import com.zgld.mall.beans.Product;
import com.zgld.mall.beans.Supplier;
import com.zgld.mall.utils.PriceUtil;

import org.json.JSONObject;

public class PublishSelectPicPopupWindow extends PopupWindow implements SelectedInfoAdapter.SelectedInfoAdapterListener {
	@Override
	public void deleteInfo(int attributeId, int position) {

	}
	private Integer valueId;
	private Integer attributeId;
	public interface PublishSelectPicPopupWindowListener {
		void confirm(int number, String strNorms, Integer valueId,Integer attributeId);
	}
	private View mMenuView;
	View close, ok, d_add, d_reduce;
	ImageView image;
	TextView title, price;
	EditText d_result;
	PublishSelectPicPopupWindowListener callBack;
	TextView gridview_color_name,gridview_size_name;
	GridView gridview_color,gridview_size;
	TextView style;
	SelectedInfoAdapter selectedInfoAdapter1;
	SelectedInfoAdapter selectedInfoAdapter2;
	@SuppressWarnings("deprecation")
	public PublishSelectPicPopupWindow(final Activity context, Supplier info,
			final PublishSelectPicPopupWindowListener callBack) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callBack = callBack;
		mMenuView = inflater.inflate(R.layout.product_detail_param, null);

		List<HishopSkus> listHishopSkus = info.getHishopProducts().getListHishopSkus();
		List<HishopSkuitems> listHishopSkuitems = info.getHishopProducts().getListHishopSkuitems();
		final List<HishopAttributes> listHishopAttributes = info.getHishopProducts().getListHishopAttributes();
		List<HishopAttributeValues> listHishopAttributeValues = info.getHishopProducts().getListHishopAttributeValues();
		if(listHishopSkus!=null && listHishopAttributes!=null && listHishopAttributeValues!=null){
			for (int i = 0;i<listHishopAttributes.size();i++){
				HishopAttributes hishopAttributes = listHishopAttributes.get(i);
				List<HishopAttributeValues> lv = new ArrayList<>();
				for (int j=1;j<listHishopAttributeValues.size();j++){
					HishopAttributeValues hishopAttributeValues = listHishopAttributeValues.get(j);
					if(hishopAttributeValues.getAttributeId().equals(hishopAttributes.getAttributeId())){
						lv.add(hishopAttributeValues);
					}
				}
				hishopAttributes.setListHishopAttributeValues(lv);
				listHishopAttributes.set(i,hishopAttributes);

				switch (i){
					case 0:
						gridview_color_name = (TextView) mMenuView.findViewById(R.id.gridview_color_name);
						gridview_color_name.setText(hishopAttributes.getAttributeName());
						gridview_color = (GridView) mMenuView.findViewById(R.id.gridview_color);
						selectedInfoAdapter1 = new SelectedInfoAdapter(context,hishopAttributes.getAttributeId(),lv,this);
						gridview_color.setAdapter(selectedInfoAdapter1);
						gridview_color.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								valueId = selectedInfoAdapter1.getListInfo().get(position).getValueId();
								attributeId = selectedInfoAdapter1.getListInfo().get(position).getAttributeId();
								Toast.makeText(context,selectedInfoAdapter1.getListInfo().get(position).getAttributeId()+"  "+selectedInfoAdapter1.getListInfo().get(position).getValueId(),Toast.LENGTH_SHORT).show();
								for (int i=0;i<selectedInfoAdapter1.getListInfo().size();i++){
									selectedInfoAdapter1.getListInfo().get(i).setSelected(false);
								}
								if (selectedInfoAdapter1.getListInfo().get(position).isSelected()) {
									selectedInfoAdapter1.getListInfo().get(position).setSelected(false);
								} else {
									selectedInfoAdapter1.getListInfo().get(position).setSelected(true);
								}
								selectedInfoAdapter1.notifyDataSetChanged();
							}
						});
						break;
					case 1:
						gridview_size_name = (TextView) mMenuView.findViewById(R.id.gridview_size_name);
						gridview_size_name.setText(hishopAttributes.getAttributeName());
						gridview_size = (GridView) mMenuView.findViewById(R.id.gridview_size);
						selectedInfoAdapter2 = new SelectedInfoAdapter(context,hishopAttributes.getAttributeId(),lv,this);
						gridview_size.setAdapter(selectedInfoAdapter2);
						gridview_size.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								valueId = selectedInfoAdapter1.getListInfo().get(position).getValueId();
								attributeId = selectedInfoAdapter1.getListInfo().get(position).getAttributeId();
								Toast.makeText(context,selectedInfoAdapter2.getListInfo().get(position).getAttributeId()+"  "+selectedInfoAdapter2.getListInfo().get(position).getValueId(),Toast.LENGTH_SHORT).show();
								for (int i=0;i<selectedInfoAdapter2.getListInfo().size();i++){
									selectedInfoAdapter2.getListInfo().get(i).setSelected(false);
								}
								if (selectedInfoAdapter2.getListInfo().get(position).isSelected()) {
									selectedInfoAdapter2.getListInfo().get(position).setSelected(false);
								} else {
									selectedInfoAdapter2.getListInfo().get(position).setSelected(true);
								}
								selectedInfoAdapter2.notifyDataSetChanged();
							}
						});
						break;
				}
			}
		}


		d_result = (EditText) mMenuView.findViewById(R.id.d_result);
		style = (TextView) mMenuView.findViewById(R.id.style);
		close = mMenuView.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		ok = mMenuView.findViewById(R.id.ok);
		d_add = mMenuView.findViewById(R.id.d_add);

		d_reduce = mMenuView.findViewById(R.id.d_reduce);
		image = (ImageView) mMenuView.findViewById(R.id.image);
		title = (TextView) mMenuView.findViewById(R.id.title);
		price = (TextView) mMenuView.findViewById(R.id.price);
		title.setText(info.getHishopProducts().getProductName());
		price.setText(PriceUtil.priceY(info.getHishopProducts().getListHishopSkus().get(0).getSalePrice() + ""));
		SysApplication.DisplayImage(info.getHishopProducts().getImageUrl1(),image);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean sa = false;
				String seleStr = "";
				for (int i=0;i<selectedInfoAdapter1.getListInfo().size();i++)
				{
					if(selectedInfoAdapter1.getListInfo().get(i).isSelected()){
						sa = true;

						for (HishopAttributes ha : listHishopAttributes) {
							if(selectedInfoAdapter1.getListInfo().get(i).getAttributeId().equals(ha.getAttributeId())){
								seleStr += ha.getAttributeName()+":";
							}
						}
						seleStr += selectedInfoAdapter1.getListInfo().get(i).getValueStr()+";";
					}
				}
				if(!sa){
					Toast.makeText(context,"请选择属性",Toast.LENGTH_SHORT).show();
					return;
				}
				sa = false;
				for (int i=0;i<selectedInfoAdapter2.getListInfo().size();i++)
				{
					if(selectedInfoAdapter2.getListInfo().get(i).isSelected()){
						sa = true;
						for (HishopAttributes ha : listHishopAttributes) {
							if(selectedInfoAdapter2.getListInfo().get(i).getAttributeId().equals(ha.getAttributeId())){
								seleStr += ha.getAttributeName()+":";
							}
						}
						seleStr += selectedInfoAdapter2.getListInfo().get(i).getValueStr()+";";
					}
				}
				if(!sa){
					Toast.makeText(context,"请选择属性",Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(context,seleStr,Toast.LENGTH_SHORT).show();
				int number = Integer.parseInt(d_result.getText().toString());
				callBack.confirm(number,"",1,1);
				dismiss();
			}
		});
		d_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int number = Integer.parseInt(d_result.getText().toString());
				number = number + 1;
				d_result.setText(number + "");
			}
		});

		d_reduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int number = Integer.parseInt(d_result.getText().toString());
				if (number > 1) {
					number = number - 1;
					d_result.setText(number + "");
				}
			}
		});

		// 设置按钮监听
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);// #BFBFBF 0xb0000000
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
