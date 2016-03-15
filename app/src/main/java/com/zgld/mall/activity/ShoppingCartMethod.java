package com.zgld.mall.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.zgld.mall.R;
import com.zgld.mall.adapter.ShoppingCarExpandableListAdapter;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.beans.HishopProductTypes;
import com.zgld.mall.beans.HishopProducts;
import com.zgld.mall.beans.HishopShoppingCarts;
import com.zgld.mall.beans.ShopingCar;
import com.zgld.mall.beans.ShopingCartItem;
import com.zgld.mall.beans.UserToken;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.ConfirmDialog;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.CustomDialog;
import com.zgld.mall.utils.PriceUtil;
import com.zgld.mall.volley.AsyncGameRunner;
import com.zgld.mall.volley.NetWorkTools;
import com.zgld.mall.volley.RequestListenr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车公共方法
 */
public class ShoppingCartMethod implements RequestListenr, OnRefreshListener2, OnItemClickListener, OnClickListener,
		OnCheckedChangeListener, ShoppingCarExpandableListAdapter.ShoppingCarExpandableListAdapterListener {

	protected ConfirmDialog confirmDialog = null;
	Activity activity;
//	List<ShopingCar> listInfo;
	List<HishopShoppingCarts> listInfo;
	PullToRefreshExpandableListView listview;
	ShoppingCarExpandableListAdapter infoAdapter;
	int pageIndex = 1;
	CheckBox item_car_checkbox;
	TextView item_pay;
	TextView item_payment_amount, item_total_amount;
	RelativeLayout bottom;
	List<String> indexAll = new ArrayList<String>();
	View null_data_default;
	ShopingCar shopingCar = new ShopingCar();
	CustomDialog dialog;
	LayoutInflater inflater;
	View back;

	public ShoppingCartMethod(Activity activity) {
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
	}

	View view;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_car, null);
		activity.setContentView(view);
		initView();
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_car, null);
			view.findViewById(R.id.back).setVisibility(View.GONE);
			TextView title = (TextView) view.findViewById(R.id.title_center);
			title.setText("购物车");
			initView();
			back.setVisibility(View.GONE);
		} else {
			ViewGroup group = (ViewGroup) view.getParent();
			if (group != null) {
				group.removeView(view);
			}
		}

		return view;

	}

	private void initView() {
		// TODO Auto-generated method stub
		back = view.findViewById(R.id.back);
		listview = (PullToRefreshExpandableListView) view.findViewById(R.id.listview);
		listview.setOnItemClickListener(this);
		listview.getRefreshableView().setGroupIndicator(null);
		listview.getRefreshableView().setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		// listview.setMode(Mode.BOTH);
		listview.setOnRefreshListener(this);
		item_car_checkbox = (CheckBox) view.findViewById(R.id.item_car_checkbox);
		item_car_checkbox.setOnCheckedChangeListener(this);
		item_pay = (TextView) view.findViewById(R.id.item_pay);
		item_pay.setOnClickListener(this);
		item_payment_amount = (TextView) view.findViewById(R.id.item_payment_amount);
		item_total_amount = (TextView) view.findViewById(R.id.item_total_amount);
		bottom = (RelativeLayout) view.findViewById(R.id.bottom);
		bottom.setVisibility(View.GONE);
		view.findViewById(R.id.go).setOnClickListener(this);
		null_data_default = view.findViewById(R.id.null_data_default);
		null_data_default.setVisibility(View.GONE);
		registerBoradcastReceiver();
		initData();
	}
	/**
	 * 请求成功
	 */
	@Override
	public void onCompelete(int tag, String json) {
		Message msg = handler.obtainMessage();
		msg.what = tag;
		Bundle data = new Bundle();
		data.putString("json", json);
		msg.setData(data);
		if (confirmDialog != null && confirmDialog.isShowing()) {
			confirmDialog.dismiss();
		}
		handler.sendMessage(msg);
	}

	/**
	 * 请求出现异常�?
	 */

	@Override
	public void onException(String exception) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (confirmDialog != null && confirmDialog.isShowing()) {
				confirmDialog.dismiss();
			}
		}
		if (exception != null && exception.equals("com.android.volley.ServerError")) {
			Toast.makeText(activity, activity.getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(activity, activity.getString(R.string.network_connection_timeout), Toast.LENGTH_SHORT)
					.show();
		}
	}

	public RequestQueue getData(int method, int tag, String url, Map m, String title, int pageIndex) {
		// RequestManager.init(this);
		if (NetWorkTools.isHasNet(activity)) {
			if (pageIndex == 1) {
				if (confirmDialog == null || !confirmDialog.isShowing()) {
					confirmDialog = new ConfirmDialog(activity, title);
				}
				if (confirmDialog.isShowing()) {
					confirmDialog.dismiss();
				}
				confirmDialog.show();
			}
			return AsyncGameRunner.request(method, tag, Contents.BASE_URL + url, this, activity, m);
		} else {
			Toast.makeText(activity, activity.getString(R.string.no_wifi_or_open_mobile_data), Toast.LENGTH_SHORT)
					.show();
		}
		return null;
	}

	private void initData() {
		listInfo = new ArrayList<>();
		if (NetWorkTools.isHasNet(activity)) {
			listview.onRefreshComplete();
		}
		if (Contents.getUser(activity) == null) {
			listview.onRefreshComplete();
			activity.startActivityForResult(new Intent(activity, LoginActivity.class),200);
			bottom.setVisibility(View.GONE);
			null_data_default.setVisibility(View.VISIBLE);
		} else {

//			getData(Method.GET, 202, "ShopingCart/Query?token=1&UserId=" + Contents.getUser(activity).getUserId()
//					+ "&size=100&pageIndex=1&&startDate=", null, null, 1);
//			ShopingCar car = new ShopingCar();
//			List<ShopingCartItem> listItem = new ArrayList<>();
//			listItem.add(new ShopingCartItem());
//			listItem.add(new ShopingCartItem());
//			listItem.add(new ShopingCartItem());
//			listItem.add(new ShopingCartItem());
//			listItem.add(new ShopingCartItem());
//			listItem.add(new ShopingCartItem());
//			car.setCartItems(listItem);
//			listInfo = new ArrayList<>();
//			listInfo.add(car);
//			infoAdapter = new ShoppingCarExpandableListAdapter(activity, listInfo, ShoppingCartMethod.this);
//			listview.getRefreshableView().setAdapter(infoAdapter);
//			int groupCount = listview.getRefreshableView().getCount();
//			for (int i = 0; i < groupCount; i++) {
//				listview.getRefreshableView().expandGroup(i);
//			}
//
//			item_car_checkbox.setChecked(false);
//			infoAdapter.notifyDataSetChanged();
//			getData(int method, int tag, String url, Map m, String title, int pageIndex) {
			AspnetUsers user = Contents.getUser(activity);
			Map<String,String> m = new HashMap<>();
			UserToken userToken = user.getUserToken();
			m.put("token",userToken.getAccountToken());
			m.put("userId",user.getUserId()+"");
			getData(Method.POST, 201, "user_car_product.html", m, null, 1);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				Bundle bundle = msg.getData();
				String json = "";
				if (bundle == null) {
					return;
				}
				json = bundle.getString(Contents.JSON);
				Gson gson = new Gson();
				Type entityType = null;
				JSONArray jsonArray = new JSONObject(json).getJSONObject("data").getJSONArray("listInfo");
				switch (msg.what) {
				case 201:
					listInfo = new Gson().fromJson(jsonArray.toString(),new TypeToken<List<HishopShoppingCarts>>() {
				}.getType());
					System.out.println("");
					System.out.println("");
					infoAdapter = new ShoppingCarExpandableListAdapter(activity, listInfo, ShoppingCartMethod.this);
			listview.getRefreshableView().setAdapter(infoAdapter);
			int groupCount = listview.getRefreshableView().getCount();
			for (int i = 0; i < groupCount; i++) {
				listview.getRefreshableView().expandGroup(i);
			}

			item_car_checkbox.setChecked(false);
			infoAdapter.notifyDataSetChanged();
					System.out.println("");
					System.out.println("");
					break;
				case 202:
					try {
						if (!TextUtils.isEmpty(json)) {
							JSONObject jsonObject = new JSONObject(json);
							jsonArray = jsonObject.getJSONArray(Contents.DATA);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (TextUtils.isEmpty(json)) {
						if (pageIndex == 1) {
							listInfo = new ArrayList<HishopShoppingCarts>();
							infoAdapter = new ShoppingCarExpandableListAdapter(activity, listInfo,
									ShoppingCartMethod.this);
							listview.getRefreshableView().setAdapter(infoAdapter);
							bindData();
						}
						return;
					}
					entityType = new TypeToken<List<HishopShoppingCarts>>() {
					}.getType();
					if (pageIndex == 1) {
						listInfo = new ArrayList<HishopShoppingCarts>();
						infoAdapter = new ShoppingCarExpandableListAdapter(activity, listInfo, ShoppingCartMethod.this);
						listview.getRefreshableView().setAdapter(infoAdapter);
					}
					List<HishopShoppingCarts> list = gson.fromJson(jsonArray.toString(), entityType);
					shopingCar = new ShopingCar();
					String OwnerUserId = "";
					if (list == null || list.size() <= 0) {
						// Toast.makeText(activity,
						// activity.getString(R.string.no_data),
						// Toast.LENGTH_SHORT).show();
					}
					if (list != null && list.size() > 0) {
//						OwnerUserId = list.get(0).getOwnerUserId();
//						for (int i = 0; i < list.size(); i++) {
//							HishopShoppingCarts info = list.get(i);
//							if (OwnerUserId == null) {
//								OwnerUserId = "";
//							}
//							if (info.getOwnerUserId() == null) {
//								info.setOwnerUserId("");
//							}
//							if (info.getOwnerUserId().equals(OwnerUserId + "")) {
//								shopingCar.setChecked(false);
//								shopingCar.setBrandName(info.getBrandName());
//								shopingCar.setOwnerUserId(info.getOwnerUserId());
//								shopingCar.setSupplierId(info.getSupplierId());
//								shopingCar.setSupplierName(info.getSupplierName());
//								shopingCar.getCartItems().add(info);
//							} else {
//								listInfo.add(shopingCar);
//								shopingCar = new ShopingCar();
//								OwnerUserId = info.getOwnerUserId();
//								shopingCar.setChecked(false);
//								shopingCar.setBrandName(info.getBrandName());
//								shopingCar.setOwnerUserId(info.getOwnerUserId());
//								shopingCar.setSupplierId(info.getSupplierId());
//								shopingCar.setSupplierName(info.getSupplierName());
//								shopingCar.getCartItems().add(info);
//							}
//						}
//						listInfo.add(shopingCar);
					}
					infoAdapter = new ShoppingCarExpandableListAdapter(activity, listInfo, ShoppingCartMethod.this);
					listview.getRefreshableView().setAdapter(infoAdapter);
					int groupCount1 = listview.getRefreshableView().getCount();
					for (int i = 0; i < groupCount1; i++) {
						listview.getRefreshableView().expandGroup(i);
					}

					item_car_checkbox.setChecked(false);
					infoAdapter.notifyDataSetChanged();
					totalMoney();

					infoAdapter.notifyDataSetChanged();
					pageIndex++;
					bindData();
					break;
				case 203:
					if (json != null && json.trim().equals("1")) {
						// listInfo.remove(deleteGroupPosition);
//						listInfo.get(deleteGroupPosition).getCartItems().remove(deleteChildPosition);
//						if (listInfo.get(deleteGroupPosition).getCartItems().size() <= 0) {
//							listInfo.remove(deleteGroupPosition);
//						}
						infoAdapter.notifyDataSetChanged();
						Toast.makeText(activity, activity.getString(R.string.delete_cart_product_success),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(activity, activity.getString(R.string.delete_cart_product_failed),
								Toast.LENGTH_SHORT).show();
					}
					// pageIndex = 1;
					totalMoney();
					// initData();
					bindData();
					break;
				case 209:
					if (json != null && Integer.parseInt(json) > 0) {
//						Intent intent = new Intent(activity, OKOrderActivity.class);
//						activity.startActivityForResult(intent, 200);
					} else {
						Toast.makeText(activity, activity.getString(R.string.network_connection_timeout),
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 210:
					if (json != null && json.trim().equals("1")) {
						Toast.makeText(activity, activity.getString(R.string.success), Toast.LENGTH_SHORT).show();
					} else if (json != null && json.trim().equals("-1")) {
						Toast.makeText(activity, activity.getString(R.string.already_exists), Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
					}
					break;
				}
				bindData();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				listview.onRefreshComplete();
			}

			super.handleMessage(msg);
		}
	};

	private void bindData() {
		if (listInfo != null && listInfo.size() > 0) {
			bottom.setVisibility(View.VISIBLE);
			null_data_default.setVisibility(View.GONE);
		} else {
			bottom.setVisibility(View.GONE);
			null_data_default.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		pageIndex = 1;
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		initData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.item_pay:
			if (!checkedProduct()) {
				Toast.makeText(activity, "请选择要结算的产品", Toast.LENGTH_SHORT).show();
			} else {
				String stockstr = "";
				int totalPrice = 0;// 总价格
				int totalMarketPrice = 0;
				int totalProductNumber = 0;// 总数量
				int favorablePrice = 0;
				StringBuffer param = new StringBuffer();
//				OKOrderActivity.listInfo = new ArrayList<ShopingCar>();
				ShopingCar shopingCar = new ShopingCar();
				String OwnerUserId = "";
				Map<String, String> muser = new HashMap<String, String>();
				for (int i = 0; i < listInfo.size(); i++) {
//					if (OwnerUserId.equals(listInfo.get(i).getOwnerUserId())) {
//						shopingCar = new ShopingCar();
//						OKOrderActivity.listInfo.add(shopingCar);
//					}
//					for (int j = 0; j < listInfo.get(i).getCartItems().size(); j++) {
//						ShopingCartItem info = listInfo.get(i).getCartItems().get(j);
//						if (listInfo.get(i).getCartItems().get(j).isChecked()) {
//							shopingCar.setBrandName(listInfo.get(i).getBrandName());
//							shopingCar.setOwnerUserId(listInfo.get(i).getOwnerUserId());
//							shopingCar.setSupplierId(listInfo.get(i).getSupplierId());
//							shopingCar.setSupplierName(listInfo.get(i).getSupplierName());
//							if (info.getStock() < Integer.parseInt(info.getQuantity())) {
//								stockstr += "产品" + info.getProductName() + ",库存不足";
//							}
//							if (!muser.containsKey(info.getOwnerUserId().trim())) {
//								muser.put(info.getOwnerUserId().trim(), info.getOwnerUserId().trim());
//							}
//							indexAll.add(info.getProductId());
//							shopingCar.getCartItems().add(info);
//							// param.append(info.getProductId() + "," +
//							// info.getQuantity() + ",");
//							param.append(info.getCartsId() + "," + info.getQuantity() + ",");
//							totalProductNumber += Integer.parseInt(info.getQuantity());
//							if (info.getSalePrice() == null) {
//								info.setSalePrice("1");
//							}
//							if (info.getQuantity() == null) {
//								info.setQuantity("1");
//							}
//							totalPrice += Double.parseDouble(info.getSalePrice())
//									* Integer.parseInt(info.getQuantity());
//							if (info.getMarketPrice() == null) {
//								info.setMarketPrice("1");
//							}
//							if (info.getQuantity() == null) {
//								info.setQuantity("1");
//							}
//							totalMarketPrice += Double.parseDouble(info.getMarketPrice())
//									* Integer.parseInt(info.getQuantity());
//						}
//					}
				}
				if (!TextUtils.isEmpty(stockstr)) {
					Toast.makeText(activity, stockstr, Toast.LENGTH_SHORT).show();
					return;
				}
//				if (muser.size() > 1) {
//					Toast.makeText(activity, "不能选择多个商家的产品进行结算", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				OKOrderActivity.listInfo.add(shopingCar);
				if (param.length() > 0) {
					param.deleteCharAt(param.length() - 1);// 删除最后一个,符号
				}
				Map<String, String> m = new HashMap<String, String>();
//				m.put("userId", Contents.getUser(activity).getUserId());
//				m.put("token", Contents.getUser(activity).getToken());
				m.put("proQuantityStr", param.toString());
//				getData(Method.POST, 209, "ShopingCart/ShopingCartUpdateALL", m, null, 1);
				Intent intent = new Intent(activity, OKOrderActivity.class);
				activity.startActivityForResult(intent, 200);
			}
			break;
		case R.id.go:
//			MenuType menuType = new MenuType();
//			Intent intent = new Intent();
//			menuType.setText("云端销售中心");
//			menuType.setId(-1);
//			intent.setClass(activity, ProductListFragmentActivity.class);
//			intent.putExtra(Contents.INFO, menuType);
//			activity.startActivity(intent);
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		if (Contents.getUser(activity) == null) {
//			activity.startActivityForResult(new Intent(activity, LoginActivity.class),200);
//			return;
//		}
		if (resultCode == activity.RESULT_OK) {
			if (requestCode == 200) {
				initView();
				listInfo = new ArrayList<>();
				infoAdapter.notifyDataSetChanged();
				bindData();
				pageIndex = 1;
				initData();
			}
		}
	}

	public void carUpdate() {
		pageIndex = 1;
		initData();
	}

	@Override
	public void childViewOnCheckedChangeListener(int groupPosition, int childPosition, boolean isChecked) {
		// TODO Auto-generated method stub
//		listInfo.get(groupPosition).getCartItems().get(childPosition).setChecked(isChecked);
//		if (!isChecked) {
//			listInfo.get(groupPosition).setChecked(isChecked);
//			item_car_checkbox.setChecked(isChecked);
//		} else {
//			// 如果全部选中,，分组也选中
//			if (groupChecked(groupPosition)) {
//				listInfo.get(groupPosition).setChecked(isChecked);
//				if (checkAllChecked(groupPosition)) {
//					item_car_checkbox.setChecked(isChecked);
//				}
//			}
//		}
		totalMoney();
		infoAdapter.notifyDataSetChanged();
	}

	@Override
	public void groupViewOnCheckedChangeListener(int groupPosition, boolean isChecked) {
		// TODO Auto-generated method stub
//		listInfo.get(groupPosition).setChecked(isChecked);
//		for (int i = 0; i < listInfo.get(groupPosition).getCartItems().size(); i++) {
//			listInfo.get(groupPosition).getCartItems().get(i).setChecked(isChecked);
//		}
//		if (!isChecked) {
//			item_car_checkbox.setChecked(isChecked);
//		} else {
//			if (groupChecked(groupPosition)) {
//				listInfo.get(groupPosition).setChecked(isChecked);
//				if (checkAllChecked(groupPosition)) {
//					item_car_checkbox.setChecked(isChecked);
//				}
//			}
//		}
		totalMoney();
		infoAdapter.notifyDataSetChanged();
	}

	/**
	 * 判断分组是否全部选中
	 * 
	 * @param groupPosition
	 * @return
	 */
	boolean groupChecked(int groupPosition) {
		boolean result = false;
//		for (int i = 0; i < listInfo.get(groupPosition).getCartItems().size(); i++) {
//			if (!listInfo.get(groupPosition).getCartItems().get(i).isChecked()) {
//				result = listInfo.get(groupPosition).isChecked();
//				break;
//			} else {
//				result = true;
//			}
//		}
		return result;
	}

	/**
	 * 判断是否全部选中
	 * 
	 * @param groupPosition
	 * @return
	 */
	boolean checkAllChecked(int groupPosition) {
		boolean result = true;
		for (int i = 0; i < listInfo.size(); i++) {
			if (result) {
//				if (!listInfo.get(i).isChecked()) {
//					result = false;
//					break;
//				} else {
//					for (int j = 0; j < listInfo.get(i).getCartItems().size(); j++) {
//						if (result) {
//							if (!listInfo.get(i).getCartItems().get(j).isChecked()) {
//								result = false;
//								break;
//							}
//						}
//					}
//				}
			}
		}
		return result;
	}

	/**
	 * 判断是否有选中结算的产品
	 * 
	 * @return
	 */
	boolean checkedProduct() {
		boolean result = false;
		for (int i = 0; i < listInfo.size(); i++) {
//			for (int j = 0; j < listInfo.get(i).getCartItems().size(); j++) {
//				if (listInfo.get(i).getCartItems().get(j).isChecked()) {
//					result = true;
//					return true;
//				}
//			}
		}
		return result;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		for (int i = 0; i < listInfo.size(); i++) {
//			listInfo.get(i).setChecked(isChecked);
//			for (int j = 0; j < listInfo.get(i).getCartItems().size(); j++) {
//				listInfo.get(i).getCartItems().get(j).setChecked(isChecked);
//			}
		}
		infoAdapter.notifyDataSetChanged();
		totalMoney();
	}

	@Override
	public void viewDrawComplete() {
		// TODO Auto-generated method stub
	}

	int deleteGroupPosition = 0;
	int deleteChildPosition = 0;

	@Override
	public void deleteProduct(final int groupPosition, final int childPosition) {
		// TODO Auto-generated method stub
		deleteGroupPosition = groupPosition;
		deleteChildPosition = childPosition;
		dialog = new CustomDialog(activity, R.style.mystyle, R.layout.customdialog, R.array.title_car_delete_product,
				new CustomDialog.CustomDialogListener() {

					@Override
					public void customDialogClickRight() {
						// TODO Auto-generated method stub
						dialog.dismiss();
						deleteProduct();
					}

					private void deleteProduct() {
						Map<String, String> m = new HashMap<String, String>();
//						m.put("token", Contents.getUser(activity).getToken());
//						m.put("UserId", Contents.getUser(activity).getUserId() + "");
						// m.put("productId",
						// listInfo.get(groupPosition).getCartItems().get(childPosition).getProductId());
//						m.put("CartId", listInfo.get(groupPosition).getListHishopProducts().get(childPosition).getCartsId());
						getData(Method.POST, 203, "ShopingCart/ShopingCartDel", m, null, 1);
					}

					@Override
					public void customDialogClickLeft() {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	@Override
	public void loveProduct(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
//		getData(com.android.volley.Request.Method.GET, 210, "UserOrProduct/ProductLikeUpdate?userId="
//						+ Contents.getUser(activity).getUserId() + "&token=" + Contents.getUser(activity).getToken()
//						+ "&productId=" + listInfo.get(groupPosition).getCartItems().get(childPosition).getProductId(), null,
//				null, 1);
	}

	@Override
	public void reduceNumber(int groupPosition, int childPosition, int newNumber) {
		// TODO Auto-generated method stub
//		listInfo.get(groupPosition).getListHishopProducts().get(childPosition).setQuantity(newNumber + "");
		totalMoney();
		infoAdapter.notifyDataSetChanged();
	}

	@Override
	public void addNumber(int groupPosition, int childPosition, int newNumber) {
		// TODO Auto-generated method stub
//		listInfo.get(groupPosition).getListHishopProducts().get(childPosition).setQuantity(newNumber + "");
		totalMoney();
		infoAdapter.notifyDataSetChanged();
	}

	/**
	 * 统计钱和产品数量
	 * 
	 */
	void totalMoney() {
		Map<String, String> m = new HashMap<String, String>();// 商家
		int totalMarketPrice = 0;// 价格
		int totalProductNumber = 0;// 总数量
		int totalPostage = 0;// 运费
		if (listInfo != null && listInfo.size() > 0) {
			bottom.setVisibility(View.VISIBLE);
		} else {
			bottom.setVisibility(View.GONE);
		}
		for (int j = 0; j < listInfo.size(); j++) {
			for (int i = 0; i < listInfo.get(j).getListHishopProducts().size(); i++) {
				HishopProducts info = listInfo.get(j).getListHishopProducts().get(i);
//				if (info.isChecked()) {
//					if (!m.containsKey(info.getSupplierName())) {
//						m.put(info.getSupplierName(), info.getSupplierName());
//					}
//					if (!TextUtils.isEmpty(info.getPostage())) {
//						totalPostage += Integer.parseInt(info.getPostage());
//					}
//					totalProductNumber += Integer.parseInt(info.getQuantity());
//					if (info.getSalePrice() == null) {
//						info.setSalePrice("1");
//					}
//					if (info.getQuantity() == null) {
//						info.setQuantity("1");
//					}
//					if (info.getMarketPrice() == null) {
//						info.setMarketPrice("1");
//					}
//					if (info.getQuantity() == null) {
//						info.setQuantity("1");
//					}
//					totalMarketPrice += Double.parseDouble(info.getSalePrice()) * Double.parseDouble(info.getQuantity());
//				}
			}
		}
		item_payment_amount.setText(activity.getString(R.string.pay_price)
				+ PriceUtil.priceY((totalMarketPrice + totalPostage) + ""));
		item_total_amount.setText(activity.getString(R.string.total_price)
				+ PriceUtil.priceY((totalMarketPrice + totalPostage) + ""));

	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BroadcastUtils.UPDATE_CAR_PRODUCT)) {
				pageIndex = 1;
				initData();
			}
			if(action.equals(BroadcastUtils.USER_LOGOUT)){
				listInfo.clear();
				infoAdapter.notifyDataSetChanged();
				bottom.setVisibility(View.GONE);
				null_data_default.setVisibility(View.VISIBLE);
			}
			if(action.equals(BroadcastUtils.USER_LOGIN))
			{
				pageIndex=1;
//				initView();
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(BroadcastUtils.UPDATE_CAR_PRODUCT);
		myIntentFilter.addAction(BroadcastUtils.USER_LOGOUT);
		myIntentFilter.addAction(BroadcastUtils.USER_LOGIN);
		// 注册广播
		activity.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mBroadcastReceiver != null) {
			activity.unregisterReceiver(mBroadcastReceiver);
		}
	}
}
