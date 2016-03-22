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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zgld.mall.R;
import com.zgld.mall.SysApplication;
import com.zgld.mall.UserDataShare;
import com.zgld.mall.adapter.PersonalDataAdapter;
import com.zgld.mall.beans.AspnetUsers;
import com.zgld.mall.beans.Personal;
import com.zgld.mall.beans.ProductImageUpload;
import com.zgld.mall.utils.BitmapUtil;
import com.zgld.mall.utils.BroadcastUtils;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.CustomDialog;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener, CustomDialog.CustomDialogListener,AdapterView.OnItemClickListener {
    ProductImageUpload productImageUpload;
    int regionID = 0;
    CustomDialog dialog;
    private Context mContext=null;
    ListView listview;
    List<Personal> listInfo = new ArrayList<>();
    PersonalDataAdapter infoAdapter;
    @Override
    public void handleMsg(Message msg) {
        try {
            String json = msg.getData().getString(Contents.JSON);
            if (json == null) {
                return;
            }
            switch (msg.what) {
                case 201:
                    if(msg.getData().getInt("status")==200){
                        JSONObject jsonObject = new JSONObject(json).getJSONObject("data").getJSONObject("info");
                        Gson gson = new Gson();
                        AspnetUsers user = gson.fromJson(jsonObject.toString(), new TypeToken<AspnetUsers>() {
                        }.getType());
                        new UserDataShare(this).saveUserData(user);
                        initData();
                        setResult(RESULT_OK);
                    }
                    break;
                case 402:
                    if (json != null && json.trim().equals("1")) {
                        Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
//                    Contents.getUser(this).setRegionId(regionID + "");
//                    new UserDataShare(this).saveUserData(Contents.getUser(this));
                        initData();
                        setResult(RESULT_OK);
                    } else {
                        Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
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
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.head_base:// 上传头像
//                dialog = new CustomDialog(mContext, R.style.mystyle, R.layout.customdialog, R.array.title_upload_image, this);
//                dialog.show();
//                break;
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
        initData();
    }
    private void uploadPhoto(Bundle extras) {
        Map<String,String> m = new HashMap<>();
        AspnetUsers user = Contents.getUser(this);
        Bitmap bitmap = extras.getParcelable("data");
        String result = BitmapUtil.bitmapToBase64(bitmap);
        m.put("userinfo.head",result);
        m.put("token", user.getUserToken().getAccountToken());
        m.put("userId", user.getUserId() + "");
        getData(Request.Method.POST, 201, "user/update_user_head.html",m,null,1);
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


    void initData() {
        AspnetUsers user = new UserDataShare(this).getUserData();
        if(user!=null){
            int types[] = new int[]{2,1,1,1,1,1,1,1,1,1};
            String names[] = new String[]{"上传头像","登录名","昵称","性别","个性签名","手机号码","座机","地址","QQ"};
            String values[] = new String[]{user.getHead()+"",user.getUserName(),"哈哈",Contents.getSex(user.getGender()),"布置了","18888552254","0851220011","大学生创业园","1001010"};
            listInfo = new ArrayList<>();
            for (int i=0;i<8;i++)
            {
                Personal info = new Personal();
                info.setType(types[i]);
                info.setName(names[i]);
                info.setValue(values[i]);
                listInfo.add(info);
            }
            infoAdapter = new PersonalDataAdapter(this,listInfo);
            listview.setAdapter(infoAdapter);
        }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,String> m = new HashMap<>();
        switch (position){
            case 0:
                dialog = new CustomDialog(mContext, R.style.mystyle, R.layout.customdialog, R.array.title_upload_image, this);
                dialog.show();
                break;
        }
    }
}
