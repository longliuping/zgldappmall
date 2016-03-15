package com.zgld.mall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zgld.mall.UserDataShare;
import com.zgld.mall.activity.LoginActivity;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.beans.Province;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ILoveYou on 2016/3/4.
 */
public class Contents {
    public static final String BASE_URL = "http://114.215.198.143:89/webapi/json/";
    public static final String BASE_IMAGE_PATH = "http://image.kuaidiantong.cn/";
    public static final String UPLOAD_USER_IMAGE = "http://www.zncdk.com/ComImgUpload.ashx";// upload
    public static final String DATA = "data";
    public static final String INFO = "info";
    public static final String PRODUCTID = "product_id";
    public static final String NAME = "name";
    public static final String JSON = "json";
    public static final String IMAGE_URL = "image_url";// 图片数组
    public static final String POSITION = "position";// 选择索引
    public static final String URLS = "urls";
    // fragment 标识
    public static final int FRAGMENT_ONE = 0;
    public static final int FRAGMENT_TWO = 1;
    public static final int FRAGMENT_THREE = 2;
    public static final int FRAGMENT_FOUR = 3;
    public static final int FRAGMENT_FIVE = 4;
    // 产品参数
    public static final int TAB_PRODUCT_PARAM = 0;
    // 产品详情
    public static final int TAB_PRODUCT_DETAIL = 1;
    // 产品评论
    public static final int TAB_PRODUCT_REVIEW = 2;

    public static final String CACHE = "cache";

    public static final String CAR_UPDATE = "car_update";
    public static final int TAG_ERROES = 500;
    public File picture;
    public static final int PHOTO = 1;// 相册
    public static final int PHOTOHRAPH = 2;// 拍照
    public static final int PHOTOZOOM = 3; // 缩放
    public static final int PHOTORESOULT = 4;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int VEDIO = 5;
    public static final int VIDEO_CAPTURE = 6;
    public String videoPath = null;
    public static String picFileFullName = "";
    public static String result = null;
    public static List<Province> listProvince = new ArrayList<Province>();
    public static int selectedProvincePosition = 0;// 选中省份
    public static int selectedCityPosition = 0;// 选中城市
    public static int selectedCountyPosition = 0;// 选中的县
    static AspnetUsers user = null;

    public static AspnetUsers getUser(Context context) {
//		UserInfo u = null;
//		if(context==null)
//		{
//			return u;
//		}
//		if (new UserDataShare(context).isLogin()) {
//			u = new UserInfo();
//			if (u == null || TextUtils.isEmpty(u.getUserId())) {
//				u = new UserDataShare(context).getUserData();
//			}
//		} else {
//			return null;
//		}
//		return u;

        if(new UserDataShare(context).isLogin()){
            if (user == null) {
                user = new UserDataShare(context).getUserData();
            }
        }else{
            user = null;
        }
        //test user login
//        Contents.setUser(new UserInfo());
        return user;

    }
    /**
     * 跳转到登录页面
     * @param activity
     * @param bundle 要传递的参数
     * @param flag 标识  startActivityForResult 处理完毕后，返回的标识码
     */
    public static void loginPage(Activity activity,Bundle bundle,int flag){
        Intent intent = new Intent(activity,LoginActivity.class);
        intent.putExtra("data",bundle);
        activity.startActivityForResult(intent,flag);
    }
    public static void setUser(AspnetUsers u) {
        user = u;
    }
    public static void updateCarProduct(Context context) {
        Intent intent = new Intent(Contents.CAR_UPDATE);
        context.sendBroadcast(intent);
    }
}
