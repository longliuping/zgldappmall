package com.zgld.mall.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.zgld.mall.AppManager;
import com.zgld.mall.DataCleanManager;
import com.zgld.mall.R;
import com.zgld.mall.UserDataShare;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.ConfirmDialog;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.CustomDialog;

/**
 * 设置
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener, CustomDialog.CustomDialogListener {
	CustomDialog dialog;

	View check_app_update, logout, about, clear, server_center, wechat, update_pwd, message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initStyle();
		setContentView(R.layout.activity_setting);
		AppManager.getAppManager().addActivity(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("设置");
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		check_app_update = findViewById(R.id.check_app_update);
		check_app_update.setOnClickListener(this);
		logout = findViewById(R.id.logout);
		logout.setOnClickListener(this);
		about = findViewById(R.id.about);
		about.setOnClickListener(this);
		clear = findViewById(R.id.clear);
		clear.setOnClickListener(this);
		server_center = findViewById(R.id.server_center);
		server_center.setOnClickListener(this);
		wechat = findViewById(R.id.wechat);
		wechat.setOnClickListener(this);
		update_pwd = findViewById(R.id.update_pwd);
		update_pwd.setOnClickListener(this);
		message = findViewById(R.id.message);
		message.setOnClickListener(this);
		if (!new UserDataShare(this).isLogin()) {
			logout.setVisibility(View.GONE);
		} else {
			logout.setVisibility(View.VISIBLE);
		}
		registerBoradcastReceiver();
	}

	@Override
	public void handleMsg(Message msg) {

	}

	private void bindData() {
		// TODO Auto-generated method stub
		if (new UserDataShare(this).getUserData() == null) {
			logout.setVisibility(View.GONE);
		} else {
			logout.setVisibility(View.VISIBLE);
		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BroadcastUtils.UPDATE_HOME_USER)) {
				bindData();
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(BroadcastUtils.UPDATE_HOME_USER);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.check_app_update:
			if (confirmDialog == null) {
				confirmDialog = new ConfirmDialog(this, getString(R.string.check_for_updates));
			}
			if (confirmDialog.isShowing()) {
				confirmDialog.dismiss();
			}
			confirmDialog.show();
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int arg0, UpdateResponse arg1) {
					// TODO Auto-generated method stub
					if (confirmDialog != null && confirmDialog.isShowing()) {
						confirmDialog.dismiss();
					}
					if (arg0 == 1) {
						Toast.makeText(SettingActivity.this, getString(R.string.no_new_version), Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			UmengUpdateAgent.forceUpdate(this);
			break;
		case R.id.logout:
			dialog = new CustomDialog(this, R.style.mystyle, R.layout.customdialog, R.array.title_logout, this);
			dialog.show();
			break;
		case R.id.about:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.clear:
			DataCleanManager.clearAllData(this);
			Toast.makeText(this, getString(R.string.cache_success), Toast.LENGTH_SHORT).show();
			break;
		case R.id.server_center:

			break;
		case R.id.wechat:

			break;
		case R.id.update_pwd:
			if (Contents.getUser(this) == null) {
				startActivityForResult(new Intent(this, LoginActivity.class), 200);
				return;
			}
			startActivity(new Intent(this, ModifyUserPasswordActivity.class));
			break;
		case R.id.message:

			break;
		}
	}

	@Override
	public void customDialogClickLeft() {
		// TODO Auto-generated method stub
		dialog.dismiss();
	}

	@Override
	public void customDialogClickRight() {
		// TODO Auto-generated method stub
		dialog.dismiss();
		new UserDataShare(this).logout();
		BroadcastUtils.sendUserLogout(this);
//		startActivity(new Intent(this, LoginActivity.class));
		Contents.loginPage(this,null,200);
		// AppManager.getAppManager().finishAllActivity();
	}
}
