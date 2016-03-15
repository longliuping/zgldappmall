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
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.beans.Product;
import com.zgld.mall.beans.Supplier;
import com.zgld.mall.utils.PriceUtil;

import org.json.JSONObject;

public class PublishSelectPicPopupWindow extends PopupWindow {
	public interface PublishSelectPicPopupWindowListener {
		void confirm(int number);
	}

	private View mMenuView;
	View close, ok, d_add, d_reduce;
	ImageView image;
	TextView title, price;
	EditText d_result;
	PublishSelectPicPopupWindowListener callBack;

	TextView style;

	@SuppressWarnings("deprecation")
	public PublishSelectPicPopupWindow(final Activity context, Supplier info,
			final PublishSelectPicPopupWindowListener callBack) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callBack = callBack;
		mMenuView = inflater.inflate(R.layout.product_detail_param, null);
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

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int number = Integer.parseInt(d_result.getText().toString());
				callBack.confirm(number);
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
