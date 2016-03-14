package com.zgld.mall.adapter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zgld.mall.R;
import com.zgld.mall.SysApplication;
import com.zgld.mall.beans.OrderItems;
import com.zgld.mall.beans.OrderStatus;
import com.zgld.mall.beans.Orders;
import com.zgld.mall.sync.OrderAsync;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.CustomDialog;
import com.zgld.mall.utils.DateUtil;
import com.zgld.mall.utils.PriceUtil;

public class BuyersOrdersAdapter extends BaseExpandableListAdapter {
	private  String orderId="";
	public interface BuyersOrdersAdapterListener {
		/**
		 * 确认收货
		 * 
		 * @param groupPosition
		 * @param childPosition
		 */
		void confirmOrder(int groupPosition, int childPosition);

		/**
		 * 取消订单
		 * 
		 * @param groupPosition
		 * @param childPosition
		 */
		void cannerOrder(int groupPosition, int childPosition);

		/**
		 * 产品评论
		 * 
		 * @param groupPosition
		 * @param childPosition
		 */
		void commentProduct(int groupPosition, int childPosition);

		/**
		 * 申请退款
		 * 
		 * @param groupPosition
		 * @param childPosition
		 */
		void applyRefund(int groupPosition, int childPosition);

		/**
		 * 付款
		 * 
		 * @param groupPosition
		 * @param childPosition
		 */
		void payment(int groupPosition, int childPosition);
	}

	List<Orders> listInfo;
	LayoutInflater layoutInflater;
	Context context;
	BuyersOrdersAdapterListener listener;
	CustomDialog dialog;
	boolean display = false;// 处理完成后，是否显示当前item

	public BuyersOrdersAdapter(Context context, List<Orders> listInfo, boolean display,
			BuyersOrdersAdapterListener listener) {
		this.listInfo = listInfo;
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
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
		return listInfo.get(groupPosition).getOrderItems().size();
	}

	/**
	 * 获取一级标签下二级标签的内容
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// return listInfo.get(groupPosition).getProducts().get(childPosition);
		return listInfo.get(groupPosition).getOrderItems().get(childPosition);
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
		TextView item_name;
		TextView item_status;
	}

	/**
	 * 对一级标签进行设置
	 */
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder holder = null;
		if (convertView == null) {
			holder = new GroupViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_buyers_orderes_group, null);
			holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
			holder.item_status = (TextView) convertView.findViewById(R.id.item_status);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		Orders info = listInfo.get(groupPosition);
		if (info != null) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(context, OrderDetailsActivity.class);
//					intent.putExtra("orderId", listInfo.get(groupPosition).getOrderId());
//					context.startActivity(intent);
				}
			});
			String result = "";
			holder.item_name.setText(info.getSupplierName() + ">");
			String str = "";
			switch (info.getOrderStatus()) {
			case OrderStatus.A:
				str = "待付款";
				break;
			case OrderStatus.B:
				str = "待付款";
				break;
			case OrderStatus.C:
				switch (info.getRefundStatus()) {
				case 0:
					str = "待发货";
					break;
				case 1:
					str = "申请退款";
					break;
				case 2:
					str = "已退款";
					break;
				case 3:
					str = "拒绝退款";
					break;
				}
				break;
			case OrderStatus.D:
				str = "待收货";
				break;
			case OrderStatus.E:
				str = "已取消";
				break;
			case OrderStatus.F:
				str = "已完成";
				break;
			case OrderStatus.G:
				str = "待评价";
				break;
			case OrderStatus.H:
				str = "待退款";
				break;
			}
			holder.item_status.setText(str);
		}
		return convertView;
	}

	class ChildViewHolder {
		ImageView item_image;
		TextView item_title, item_detail, item_price, item_market_price, item_number, item_postage, item_list_price,
				item_number_all;
		TextView item_confirm, item_cancel, item_refund, item_evaluation, item_pay,item_view_logistics;
		View item_base_bottom, item_pre_base, tem_price_base;
		TextView item_date;
		TextView item_start_price, item_end_price;
	}

	/**
	 * 对一级标签下的二级标签进行设置
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ChildViewHolder holder = null;
		if (convertView == null) {
			holder = new ChildViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_buyers_orders, null);
			holder.item_image = (ImageView) convertView.findViewById(R.id.item_image);
			holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
			holder.item_detail = (TextView) convertView.findViewById(R.id.item_detail);
			holder.item_price = (TextView) convertView.findViewById(R.id.item_price);
			holder.item_market_price = (TextView) convertView.findViewById(R.id.item_market_price);
			holder.item_number = (TextView) convertView.findViewById(R.id.item_number);
			// holder.item_send = convertView.findViewById(R.id.item_send);
			holder.item_postage = (TextView) convertView.findViewById(R.id.item_postage);
			holder.item_list_price = (TextView) convertView.findViewById(R.id.item_list_price);
			holder.item_number_all = (TextView) convertView.findViewById(R.id.item_number_all);
			holder.item_confirm = (TextView) convertView.findViewById(R.id.item_confirm);
			holder.item_cancel = (TextView) convertView.findViewById(R.id.item_cancel);
			holder.item_refund = (TextView) convertView.findViewById(R.id.item_refund);
			holder.item_evaluation = (TextView) convertView.findViewById(R.id.item_evaluation);
			holder.item_pay = (TextView) convertView.findViewById(R.id.item_pay);
			holder.item_base_bottom = convertView.findViewById(R.id.item_base_bottom);
			holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
			holder.item_pre_base = convertView.findViewById(R.id.item_pre_base);
			holder.item_start_price = (TextView) convertView.findViewById(R.id.item_start_price);
			holder.item_end_price = (TextView) convertView.findViewById(R.id.item_end_price);
			holder.tem_price_base = convertView.findViewById(R.id.tem_price_base);
			holder.item_view_logistics = (TextView) convertView.findViewById(R.id.item_view_logistics);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		holder.item_confirm.setVisibility(View.GONE);
		holder.item_pay.setVisibility(View.GONE);
		holder.item_cancel.setVisibility(View.GONE);
		holder.item_refund.setVisibility(View.GONE);
		holder.item_evaluation.setVisibility(View.GONE);
		holder.item_base_bottom.setVisibility(View.GONE);
		holder.item_pre_base.setVisibility(View.GONE);
		holder.item_view_logistics.setVisibility(View.GONE);
		final ChildViewHolder h = holder;
		final OrderItems item = listInfo.get(groupPosition).getOrderItems().get(childPosition);
		final Orders info = listInfo.get(groupPosition);
		if (info != null) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(context, OrderDetailsActivity.class);
//					intent.putExtra("orderId", listInfo.get(groupPosition).getOrderId());
//					context.startActivity(intent);
				}
			});
			if (isLastChild) {
//				if (item.getBuyCategories().equals("3")) {
//					holder.item_pre_base.setVisibility(View.VISIBLE);
//					holder.item_start_price.setText(PriceUtil.priceY(item.getAdvancePrice()));
//					holder.item_end_price.setText(PriceUtil.priceY(item.getRemaining()));
//					holder.tem_price_base.setVisibility(View.GONE);
//					if ("0".equals(info.getIsPayBalancePayment())) {
//						holder.item_pay.setText("立即支付(首款)");
//					} else {
//						holder.item_pay.setText("立即支付(尾款)");
//					}
//				} else {
//					holder.item_pre_base.setVisibility(View.GONE);
//					holder.item_pay.setText("立即支付");
//					holder.tem_price_base.setVisibility(View.VISIBLE);
//				}
				holder.item_base_bottom.setVisibility(View.VISIBLE);
				// 就按共几件商品
				int itemQuantityCount = 0;
				for (OrderItems itemQuantity : listInfo.get(groupPosition).getOrderItems()) {
//					itemQuantityCount += Integer.parseInt(itemQuantity.getQuantity());
				}
				holder.item_number_all.setText(itemQuantityCount + "");
				holder.item_date.setText(DateUtil.getSMillon(info.getOrderDate()) + "");
			}
			
			switch (info.getOrderStatus()) {
			case OrderStatus.A:
				// holder.item_cancel.setVisibility(View.VISIBLE);
				holder.item_pay.setVisibility(View.VISIBLE);
				holder.item_cancel.setVisibility(View.VISIBLE);
				break;
			case OrderStatus.B:
				holder.item_pay.setVisibility(View.VISIBLE);
				holder.item_cancel.setVisibility(View.VISIBLE);
				break;
			case OrderStatus.C:
				if (info.getRefundStatus() == 2 || info.getRefundStatus() == 3) {
					holder.item_refund.setVisibility(View.GONE);
				} else {
					holder.item_refund.setVisibility(View.VISIBLE);
				}

				break;
			case OrderStatus.D:
				holder.item_confirm.setVisibility(View.VISIBLE);
				break;
			case OrderStatus.E:
				break;
			case OrderStatus.F:
				holder.item_evaluation.setVisibility(View.VISIBLE);
				break;
			case OrderStatus.G:
				holder.item_evaluation.setVisibility(View.VISIBLE);
				break;
			}
			holder.item_postage.setText(PriceUtil.priceY(info.getAdjustedFreight()));
//			holder.item_title.setText(item.getItemDescription());
//			holder.item_detail.setText(item.getNorms());
//			holder.item_list_price.setText(PriceUtil.priceY(info.getOrderTotal()));
//			holder.item_number.setText("X" + item.getQuantity());
//			String categories=item.getBuyCategories();
//			if (categories.equals("3")) {
//				if(item.getAdvancePrice()!=null && !item.getAdvancePrice().trim().isEmpty() && item.getRemaining()!=null && !item.getRemaining().trim().isEmpty()){
//					double salePrice=Double.parseDouble(item.getAdvancePrice())+Double.parseDouble(item.getRemaining());
//					holder.item_price.setText(PriceUtil.priceY(String.valueOf(salePrice)));//实际销售价额
//				}
//			} else {
//				holder.item_price.setText(PriceUtil.priceY(item.getItemAdjustedPrice()));//实际销售价额
//			}
//			holder.item_market_price.setText(PriceUtil.priceY(item.getSalePrice()));//市场价格
			holder.item_view_logistics.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(context, ViewLogisticsActivity.class);
//					context.startActivity(intent);
				}
			});
//			SysApplication.DisplayImage(item.getThumbnailsUrl(), holder.item_image);
			holder.item_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(context, ProductDetailActivity.class);
//					intent.putExtra(Contents.PRODUCTID, item.getProductId());
//					context.startActivity(intent);
				}
			});
			holder.item_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					confirmOrder(groupPosition, childPosition);
				}
			});
			holder.item_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					cancelOrder(groupPosition, childPosition);
				}
			});
			holder.item_refund.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					applyRefund(groupPosition, childPosition);
				}
			});
			holder.item_evaluation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// listener.commentProduct(groupPosition, childPosition);
//					Intent intent = new Intent(context, ProductEvaluationActivity.class);
//					intent.putExtra(Contents.INFO, listInfo.get(groupPosition));
//					context.startActivity(intent);
				}
			});
			holder.item_pay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					payOrder(groupPosition, childPosition);
				}
			});
		}
		return convertView;
	}

	/**
	 * 确认收货
	 * 
	 * @param groupPosition
	 * @param childPosition
	 */
	public void confirmOrder(final int groupPosition, final int childPosition) {
//		String url = "Orders/OrderStatusUpdate?orderId=" + listInfo.get(groupPosition).getOrderId()
//				+ "&orderStatus=5&token" + Contents.getUser(context).getToken() + "&userId="
//				+ Contents.getUser(context).getUserId() + "";
//		new OrderAsync(context, Method.GET, 307, url, null, null, 1, new OrderAsyncListener() {
//
//			@Override
//			public void complete(int tag, String json) {
//				if (tag == 307 && json.trim().equals("1")) {
//					Toast.makeText(context, context.getString(R.string.confirm_sign_success), Toast.LENGTH_SHORT)
//							.show();
//					listInfo.get(groupPosition).setOrderStatus(5);
//					if (!display) {
//						listInfo.remove(groupPosition);
//					}
//					notifyDataSetChangedAdapter();
//				} else {
//					Toast.makeText(context, context.getString(R.string.confirm_sign_failed), Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
	}

	/**
	 * 申请退款
	 * @param groupPosition
	 * @param childPosition
	 */
	public void applyRefund(final int groupPosition, final int childPosition) {
//		Map<String, String> m = new HashMap<String, String>();
//		m.put("token", "123");
//		m.put("operateType", "3");
//		m.put("userId", Contents.getUser(context).getUserId());
//		m.put("orderId", listInfo.get(groupPosition).getOrderId());
//		m.put("Reason", "");
//		new OrderAsync(context, Method.POST, 304, "Orders/OrderStatusUpdateNew", m, null, 1, new OrderAsyncListener() {
//
//			@Override
//			public void complete(int tag, String json) {
//				if (tag == 304) {
//					Log.i("TAG", "json="+json);
//					if (json.trim().equals("1")) {
//						Toast.makeText(context, "申请成功", Toast.LENGTH_SHORT).show();
//						if (!display) {
//							listInfo.remove(groupPosition);
//						}
//						notifyDataSetChangedAdapter();
//					} else if (json.trim().equals("21")) {
//						Toast.makeText(context, "已审请退款", Toast.LENGTH_SHORT).show();
//					}else {
//						Toast.makeText(context, "申请失败", Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//		});
	}

	/**
	 * 支付订单
	 * @param groupPosition
	 * @param childPosition    
	 */
	public void payOrder(final int groupPosition, final int childPosition) {
		Map<String, String> m = new HashMap<String, String>();
		final Orders orderInfo = listInfo.get(groupPosition);
		if (orderInfo.getIsPayBalancePayment().equals("2")) {
			orderId = orderInfo.getSecondTimePayNo();
		} else {
			orderId = orderInfo.getOrderId();
		}
		m.put("out_trade_no", orderId);
		
//		new OrderAsync(context, Method.POST, 2010, "Alipay/AlipayConfig", m, null, 1,
//				new OrderAsyncListener() {
//
//					@Override
//					public void complete(int tag, String json) {
//						Gson gson = new Gson();
//						Type entityType = null;
//						entityType = new TypeToken<Config>() {
//						}.getType();
//						Config config = gson.fromJson(json.trim(), entityType);
//						if (config != null && config.getConfig() != null) {
//							Alipay alipay = null;
//							for (Alipay a : config.getConfig()) {
//								alipay = a;
//							}
//							if (alipay != null) {
//								if (orderInfo.getIsPayBalancePayment().equals("2")) {
//									alipay.setProductName(orderInfo.getOrderItems().get(childPosition).getItemDescription());
//									alipay.setOrderTotal(orderInfo.getOrderItems().get(childPosition).getRemaining());
//								}
//								new AlipayUtil(context, orderId, alipay, new AlipayUtilListener() {
//
//									@Override
//									public void alipaySuccess() {
//										listInfo.get(groupPosition).setOrderStatus(5);
//										if (!display) {
//											listInfo.remove(groupPosition);
//										}
//										notifyDataSetChangedAdapter();
//									}
//								});
//							}
//						}
//					}
//				});
	}

	/**
	 * 取消订单
	 * @param groupPosition
	 * @param childPosition
	 */
	public void cancelOrder(final int groupPosition, final int childPosition) {
		dialog = new CustomDialog(context, R.style.mystyle, R.layout.customdialog, R.array.title_cancel_order,
				new CustomDialog.CustomDialogListener() {

					@Override
					public void customDialogClickRight() {
						dialog.dismiss();
//						String url = "Orders/OrderStatusUpdate?orderId=" + listInfo.get(groupPosition).getOrderId()
//								+ "&orderStatus=4&token=" + Contents.getUser(context).getToken() + "&userId="
//								+ Contents.getUser(context).getUserId() + "";
//						new OrderAsync(context, Method.GET, 306, url, null, null, 1, new OrderAsync.OrderAsyncListener() {
//
//							@Override
//							public void complete(int tag, String json) {
//								if (tag == 306 && json.trim().equals("1")) {
//									Toast.makeText(context, context.getString(R.string.cancel_order_success),
//											Toast.LENGTH_SHORT).show();
//									listInfo.get(groupPosition).setOrderStatus(4);
//									if (!display) {
//										listInfo.remove(groupPosition);
//									}
//									notifyDataSetChangedAdapter();
//								} else {
//									Toast.makeText(context, context.getString(R.string.cancel_order_failed),
//											Toast.LENGTH_SHORT).show();
//								}
//							}
//						});
					}

					@Override
					public void customDialogClickLeft() {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	public void notifyDataSetChangedAdapter() {
		this.notifyDataSetChanged();
	}

	/**
	 * 当选择子节点的时候，调用该方法
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
