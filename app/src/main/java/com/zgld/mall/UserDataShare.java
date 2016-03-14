package com.zgld.mall;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.TextUtils;

import com.zgld.mall.beans.UserInfo;
import com.zgld.mall.utils.Contents;

/**
 * 用户数据保存
 * 
 * @author Administrator
 * 
 */
public class UserDataShare implements OnSharedPreferenceChangeListener {
	final String NAME_SHARE = "loginUserInfo";
	final String NAME = "name";
	final String PWD = "pwd";
	final String USERID = "userId";
	final String ISLOGIN = "islogin";
	Context context;
	SharedPreferences haredPreferences;
	UserDataShareChangeListener listener;

	public interface UserDataShareChangeListener {
		void update(SharedPreferences arg0, String arg1);
	}

	public UserDataShare(Context context) {
		this.context = context;
		this.haredPreferences = context.getSharedPreferences(NAME_SHARE, Activity.MODE_PRIVATE);
		this.haredPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	public UserDataShare(Context context, UserDataShareChangeListener listener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.haredPreferences = context.getSharedPreferences(NAME_SHARE, Activity.MODE_PRIVATE);
		this.haredPreferences.registerOnSharedPreferenceChangeListener(this);
		this.listener = listener;
	}

	/**
	 * 保存登录用户名密码
	 * 
	 * @return
	 */
	public void saveLoginInfo(String name, String pwd, String userId) {
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = haredPreferences.edit();
		// 用putString的方法保存数据
		editor.putString(NAME, name);
		editor.putString(PWD, pwd);
		editor.putString(USERID, userId);
		editor.putBoolean(ISLOGIN, true);
		// 提交当前数据
		editor.commit();
	}

	/**
	 * 读取登录用户名密码
	 * 
	 * @return
	 */
	public String[] getLoginInfo() {
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String name = haredPreferences.getString(NAME, "");
		String pwd = haredPreferences.getString(PWD, "");
		String userId = haredPreferences.getString(USERID, "0");
		if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(userId)) {
			return new String[] { name, pwd, userId };
		} else {
			return null;
		}
	}

	/**
	 * 获取登录用户信息
	 * 
	 * @return
	 */
	public UserInfo getUserData() {
		UserInfo info = null;
		if (isLogin()) {
			info = new UserInfo();
//			info.setCellPhone(haredPreferences.getString("CellPhone", ""));
//			info.setExamineStuart(haredPreferences.getBoolean("ExamineStuart", false));
//			info.setGender(haredPreferences.getString("Gender", "0"));
//			info.setHeadImg(haredPreferences.getString("HeadImg", ""));
//			info.setLoweredUserName(haredPreferences.getString("LoweredUserName", ""));
//			info.setNickname(haredPreferences.getString("Nickname", ""));
//			info.setPersonalSignature(haredPreferences.getString("PersonalSignature", ""));
//			info.setRegionId(haredPreferences.getString("RegionId", "0"));
//			info.setUserId(haredPreferences.getString("UserId", "0"));
//			info.setToken(haredPreferences.getString("Token", "0"));
//			info.setWeChat(haredPreferences.getString("WeChat", ""));
//			info.setAddress(haredPreferences.getString("address", ""));
			Contents.setUser(info);
		}
		return info;
	}

	/**
	 * 保存登录用户信息
	 * 
	 * @param info
	 */
//	public void saveUserData(UserInfo info) {
//		// 实例化SharedPreferences.Editor对象（第二步）
//		SharedPreferences.Editor editor = haredPreferences.edit();
//		// 用putString的方法保存数据
//		editor.putString("CellPhone", info.getCellPhone());
//		editor.putBoolean("ExamineStuart", info.isExamineStuart());
//		editor.putString("Gender", info.getGender());
//		editor.putString("HeadImg", info.getHeadImg());
//		editor.putString("LoweredUserName", info.getLoweredUserName());
//		editor.putString("Nickname", info.getNickname());
//		editor.putString("PersonalSignature", info.getPersonalSignature());
//		editor.putString("RegionId", info.getRegionId());
//		editor.putString("UserId", info.getUserId());
//		editor.putString("Token", info.getToken());
//		editor.putString("WeChat", info.getWeChat());
//		editor.putString("address", info.getAddress());
//		editor.putBoolean(ISLOGIN, true);
//		// 提交当前数据
//		editor.commit();
//		Contents.setUser(info);
//	}

	/**
	 * 是否登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		return haredPreferences.getBoolean(ISLOGIN, false);
	}

	public void logout() {
		Contents.setUser(null);
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = haredPreferences.edit();
		editor.putBoolean(ISLOGIN, false);
		// 提交当前数据
		editor.commit();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		if (listener != null) {
			listener.update(arg0, arg1);
		}
	}
}
