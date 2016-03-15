package com.zgld.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zgld.mall.R;
import com.zgld.mall.SysApplication;
import com.zgld.mall.UserDataShare;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.beans.ProductImageUpload;
import com.zgld.mall.utils.BitmapUtil;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.CustomDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener, CustomDialog.CustomDialogListener {
    View user_data_address_base, user_address_base, wechat_base, name_base, sex_base, single_base, phone_base, head_base;
    TextView sex, single, phone, nick, wechat, address;
    ImageView head;
    ProductImageUpload productImageUpload;
    int regionID = 0;
    CustomDialog dialog;
    private Context mContext=null;
    @Override
    public void handleMsg(Message msg) {
        if (msg.getData() == null) {
            return;
        }
        String json = msg.getData().getString(Contents.JSON);
        if (json == null) {
            return;
        }
        switch (msg.what) {
            case 201:
                if (json != null && json.trim().equals("1")) {
                    Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
//                    Contents.getUser(this).setHeadImg(productImageUpload.getImageUrl());
//                    new UserDataShare(this).saveUserData(Contents.getUser(this));
                    bindData();
//                    SysApplication.DisplayUserImageClick(productImageUpload.getImageUrl(), head);
                    BroadcastUtils.sendUpdateHomeUser(this);
                    setResult(RESULT_OK);

                } else {
                    Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                }
                break;
            case 402:
                if (json != null && json.trim().equals("1")) {
                    Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
//                    Contents.getUser(this).setRegionId(regionID + "");
//                    new UserDataShare(this).saveUserData(Contents.getUser(this));
                    bindData();
                    setResult(RESULT_OK);
                } else {
                    Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_personal_data);
        mContext=PersonalDataActivity.this;
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_personal_data));
        user_data_address_base = findViewById(R.id.user_data_address_base);
        user_data_address_base.setOnClickListener(this);
        user_address_base = findViewById(R.id.user_address_base);
        user_address_base.setOnClickListener(this);
        wechat_base = findViewById(R.id.wechat_base);
        wechat_base.setOnClickListener(this);
        name_base = findViewById(R.id.name_base);
        name_base.setOnClickListener(this);
        sex_base = findViewById(R.id.sex_base);
        sex_base.setOnClickListener(this);
        single_base = findViewById(R.id.single_base);
        single_base.setOnClickListener(this);
        phone_base = findViewById(R.id.phone_base);
        phone_base.setOnClickListener(this);
        head_base = findViewById(R.id.head_base);
        head_base.setOnClickListener(this);

        head = (ImageView) findViewById(R.id.head);
        sex = (TextView) findViewById(R.id.sex);
        single = (TextView) findViewById(R.id.single);
        phone = (TextView) findViewById(R.id.phone);
        nick = (TextView) findViewById(R.id.nick);
        wechat = (TextView) findViewById(R.id.wechat);
        address = (TextView) findViewById(R.id.address);
        bindData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_base:// 上传头像
                dialog = new CustomDialog(mContext, R.style.mystyle, R.layout.customdialog, R.array.title_upload_image, this);
                dialog.show();
                break;
//            case R.id.name_base:// 昵称
//                startActivityForResult(new Intent(mContext, ModifyUserNickActivity.class), 202);
//                break;
//            case R.id.sex_base:// 性别
//                startActivityForResult(new Intent(mContext, ModifyUserSexActivity.class), 203);
//                break;
//            case R.id.single_base:// 个性签名
//                startActivityForResult(new Intent(mContext, ModifyUserSingleActivity.class), 204);
//                break;
//            case R.id.phone_base:// 手机号
//                startActivityForResult(new Intent(mContext, ModifyUserPhoneActivity.class), 205);
//                break;
//            case R.id.user_address_base:// 所在地
//                Contents.selectedCityPosition=0;
//                Contents.selectedCountyPosition=0;
//                Contents.selectedProvincePosition=0;
//                startActivityForResult(new Intent(mContext, ProvinceActivity.class), 206);
//                break;
//            case R.id.wechat_base:// 绑定微信号
//                startActivityForResult(new Intent(mContext, ModifyUserWechatActivity.class), 207);
//                break;
//            case R.id.user_data_address_base:// 收货地址
//                startActivityForResult(new Intent(mContext, AddressManagerActivity.class), 208);
//                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Contents.PHOTORESOULT) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    uploadPhoto(extras);
                }
            }
            if (data != null && requestCode == Contents.PHOTO) {
                startPhotoZoom(data.getData(), true);
            }
            File file = new File(Contents.picFileFullName);
            if (requestCode == Contents.PHOTOHRAPH) {
                startPhotoZoom(Uri.fromFile(file), true);
            }
        }
        if (requestCode == 206) {
            setProvince();
        }
        bindData();
    }
    Bitmap bitmap = null;
    private void uploadPhoto(Bundle extras) {
        bitmap = extras.getParcelable("data");
        Contents.result = BitmapUtil.bitmapToBase64(bitmap);
//        new AsyncUpImgTask(this, new AsyncUpImgTask.CallbackPic() {
//
//            @Override
//            public void success(String json) {
//                Gson gson = new Gson();
//                Type entityType = null;
//                entityType = new TypeToken<ProductImageUpload>() {
//                }.getType();
//                productImageUpload = gson.fromJson(json, entityType);
//                if (productImageUpload == null) {
//                    Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
//                } else {
//                    saveData();
//                }
//            }
//
//        }, System.currentTimeMillis() + ".png", ViewUtil.Bitmap2Bytes(bitmap), Contents.UPLOAD_USER_IMAGE
//                + "?token=" + Contents.getUser(this).getToken() + "&userid="
//                + Contents.getUser(this).getUserId()).execute("");
    }

    private void setProvince() {
        if(Contents.selectedProvincePosition<=0){
            return;
        }
//        List<City> listCity = Contents.listProvince.get(Contents.selectedProvincePosition).getCitys();
//        if (listCity == null || listCity.size() <= 0) {
//            if(Contents.listProvince.size()>=Contents.selectedProvincePosition){
//                regionID = Contents.listProvince.get(Contents.selectedProvincePosition).getId();
//            }
//        } else {
//            List<County> listCounty = listCity.get(Contents.selectedCityPosition).getCountys();
//            if (listCounty == null || listCounty.size() <= 0) {
//                if(listCity.size()>=Contents.selectedCityPosition){
//                    regionID = listCity.get(Contents.selectedCityPosition).getId();
//                }
//            } else {
//                if(listCounty.size()>=Contents.selectedCountyPosition){
//                    regionID = listCounty.get(Contents.selectedCountyPosition).getId();
//                }
//            }
//        }
//        Map<String, String> m = new HashMap<String, String>();
//        m.put("gender", Contents.getUser(this).getGender());
//        m.put("token", Contents.getUser(this).getToken());
//        m.put("userId", Contents.getUser(this).getUserId());
//        m.put("nickName", Contents.getUser(this).getNickname());
//        m.put("personalSignature", Contents.getUser(this).getPersonalSignature());
//        m.put("cellPhone", Contents.getUser(this).getCellPhone());
//        m.put("regionId", regionID + "");
//        m.put("wechat", Contents.getUser(this).getWeChat());
//        m.put("address", Contents.getUser(this).getAddress());
//        m.put("headImg", Contents.getUser(this).getHeadImg());
//        getData(com.android.volley.Request.Method.POST, 402, "User/UserUpdateInfo", m, null, 1);
//        Contents.listProvince=null;
    }

    private void saveData() {
        SysApplication.DisplayUserImageClick(productImageUpload.getImageUrl(), head);
        Map<String, String> m = new HashMap<String, String>();
//        m.put("gender", Contents.getUser(this).getGender());
//        m.put("token", Contents.getUser(this).getToken());
//        m.put("userId", Contents.getUser(this).getUserId());
//        m.put("nickName", Contents.getUser(this).getNickname());
//        m.put("personalSignature", Contents.getUser(this).getPersonalSignature());
//        m.put("cellPhone", Contents.getUser(this).getCellPhone());
//        m.put("regionId", Contents.getUser(this).getRegionId());
//        m.put("wechat", Contents.getUser(this).getWeChat());
//        m.put("address", Contents.getUser(this).getAddress());
//        m.put("headImg", productImageUpload.getImageUrl());
        getData(com.android.volley.Request.Method.POST, 201, "User/UserUpdateInfo", m, null, 1);
    }

    void bindData() {
        AspnetUsers user = new UserDataShare(this).getUserData();
//        if (TextUtils.isEmpty(user.getGender())) {
//            sex.setText("男");
//        } else if (user.getGender().equals("1")) {
//            sex.setText("男");
//        } else if (user.getGender().equals("0")) {
//            sex.setText("女");
//        } else {
//            sex.setText("女");
//        }
//        single.setText(user.getPersonalSignature());
//        phone.setText(user.getCellPhone());
//        SysApplication.DisplayUserImageClick(user.getHeadImg(), head);
//        nick.setText(user.getNickname());
//        wechat.setText(user.getWeChat());
//        address.setText(user.getAddress());
//        DatabaseHelper databaseHelper = null;
//        SQLiteDatabase db = null;
//        try {
//            databaseHelper = new DatabaseHelper(this);
//            db = databaseHelper.getWritableDatabase();
//            if (TextUtils.isEmpty(user.getRegionId())) {
//                return;
//            }
//            int regionid = Integer.parseInt(user.getRegionId());
//            Cursor cursor = db.rawQuery("select * from " + Table.county + " where id = " + regionid, null);
//            String ad = "";
//            if (cursor.moveToNext()) {
//                ad = cursor.getString(cursor.getColumnIndex(Table.County.name));
//                regionid = cursor.getInt(cursor.getColumnIndex(Table.County.id));
//            }
//            cursor = db.rawQuery("select * from " + Table.city + " where id = " + regionid, null);
//            if (cursor.moveToNext()) {
//                ad = cursor.getString(cursor.getColumnIndex(Table.City.name)) + ad;
//                regionid = cursor.getInt(cursor.getColumnIndex(Table.City.id));
//            }
//            cursor = db.rawQuery("select * from " + Table.province + " where id = " + regionid, null);
//            if (cursor.moveToNext()) {
//                ad = cursor.getString(cursor.getColumnIndex(Table.Province.name)) + ad;
//                regionid = cursor.getInt(cursor.getColumnIndex(Table.Province.id));
//            }
//            address.setText(ad);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//            if (databaseHelper != null) {
//                databaseHelper.close();
//            }
//        }
    }

    @Override
    public void customDialogClickLeft() {
        dialog.dismiss();
        Intent intent = new Intent();
        intent.setType("image/*");/* 开启Pictures画面Type设定为image */
        intent.setAction(Intent.ACTION_GET_CONTENT);/* 使用Intent.ACTION_GET_CONTENT这个Action */
        startActivityForResult(intent, Contents.PHOTO);/* 取得相片后返回本画面 */
    }

    @Override
    public void customDialogClickRight() {
        dialog.dismiss();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            Contents.picFileFullName = outFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, Contents.PHOTOHRAPH);
        } else {
            Toast.makeText(PersonalDataActivity.this, getString(R.string.selected_sdcard_not_exists), Toast.LENGTH_SHORT).show();
        }
    }
}
