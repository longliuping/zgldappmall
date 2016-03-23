package com.zgld.mall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zgld.mall.R;
import com.zgld.mall.beans.GsonObject;
import com.zgld.mall.utils.ConfirmDialog;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.volley.AsyncGameRunner;
import com.zgld.mall.volley.NetWorkTools;
import com.zgld.mall.volley.RequestListenr;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * Created by ILoveYou on 2016/3/3.
 */
public abstract class BaseActivity extends Activity  implements RequestListenr {
    protected ConfirmDialog confirmDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initStyle();
    }
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            handleMsg(msg);
        };
    };

    public abstract void handleMsg(android.os.Message msg);
    /**
     * 初始化透明状态栏
     */
    protected void initStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 透明导航栏
        }
    }

    /**
     * 请求成功
     */
    @Override
    public void onCompelete(int tag, String json) {
        Message msg = handler.obtainMessage();
        msg.what = tag;
       try{
           JSONObject object = new JSONObject(json);
           String msgStr = object.getString(Contents.MSG);
           if(!msgStr.equals(Contents.SUCCESS)) {
               Toast.makeText(this, msgStr, Toast.LENGTH_SHORT).show();
           }
           Bundle data = new Bundle();
           data.putInt(Contents.STATUS,object.getInt(Contents.STATUS));
           data.putString(Contents.JSON, json);
           data.putString(Contents.DATA,object.getJSONObject(Contents.DATA).toString());
           msg.setData(data);
           if (confirmDialog != null && confirmDialog.isShowing()) {
               confirmDialog.dismiss();
           }
       }catch (Exception e){
        e.printStackTrace();
       }
        handler.sendMessage(msg);
    }

    /**
     * 请求出现异常
     */

    @Override
    public void onException(String exception) {
        handler.sendEmptyMessage(Contents.TAG_ERROES);
        if (confirmDialog != null && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
        }
        if (exception != null && exception.equals("com.android.volley.ServerError")) {
            Toast.makeText(this, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.network_connection_timeout), Toast.LENGTH_SHORT).show();
        }
    }
    long time = 0;
    /**
     * @param method 请求方式
     * @param tag  请求标识
     * @param url  请求地址
     * @param m  请求参数
     * @param title  请求过程中对话框的标题
     * @param pageIndex  请求页数
     * @return
     */
    public RequestQueue getData(int method, int tag, String url, Map m, String title, int pageIndex) {
        // RequestManager.init(this);
        if (NetWorkTools.isHasNet(getApplicationContext())) {
            if (pageIndex == 1) {
                if (confirmDialog == null || !confirmDialog.isShowing()) {
                    confirmDialog = new ConfirmDialog(this, title);
                }
                if (confirmDialog.isShowing()) {
                    confirmDialog.dismiss();
                }
                confirmDialog.show();
            }
            return AsyncGameRunner.request(method, tag, Contents.BASE_URL + url, this, this, m);
        } else {
            if (time + 2000 < new Date().getTime()) {
                time = new Date().getTime();
                Toast.makeText(this, getString(R.string.no_wifi_or_open_mobile_data), Toast.LENGTH_SHORT).show();
            }
            handler.sendEmptyMessage(Contents.TAG_ERROES);
        }
        return null;
    }
    /**
     * 没有连接网络，加载缓存json
     *
     * @param method
     * @param tag
     * @param url
     * @param m
     * @param title
     * @param pageIndex
     */
    public void getDataCache(int method, int tag, String url, Map m, String title, int pageIndex) {
        if (NetWorkTools.isHasNet(getApplicationContext())) {
            if (pageIndex == 1) {
                if (confirmDialog == null || !confirmDialog.isShowing()) {
                    confirmDialog = new ConfirmDialog(this, title);
                }
                if (confirmDialog.isShowing()) {
                    confirmDialog.dismiss();
                }
                confirmDialog.show();
            }
            AsyncGameRunner.request(method, tag, Contents.BASE_URL + url, this, this, m);
        } else {
            if (time + 2000 < new Date().getTime()) {
                time = new Date().getTime();
                Toast.makeText(this, getString(R.string.no_wifi_or_open_mobile_data), Toast.LENGTH_SHORT).show();
            }
            handler.sendEmptyMessage(Contents.TAG_ERROES);
        }
        if (pageIndex == 1) {
            RequestQueue queue = Volley.newRequestQueue(this);
            Cache.Entry en = queue.getCache().get(Contents.BASE_URL + url);
            if (en != null && en.data != null) {
                String json = new String(en.data);
                Message msg = handler.obtainMessage();
                msg.what = tag;
                Bundle data = new Bundle();
                data.putString(Contents.JSON, json);
                msg.setData(data);
                if (confirmDialog != null && confirmDialog.isShowing()) {
                    confirmDialog.dismiss();
                }
                handler.sendMessage(msg);
            }
        }
    }

    public void getDataAddress(int method, int tag, String url, Map m, String title, boolean isshowDialog) {
        if (NetWorkTools.isHasNet(getApplicationContext())) {
            if (isshowDialog) {
                if (confirmDialog == null || !confirmDialog.isShowing()) {
                    confirmDialog = new ConfirmDialog(this, title);
                }
                if (confirmDialog.isShowing()) {
                    confirmDialog.dismiss();
                }
                confirmDialog.show();
            }
            AsyncGameRunner.request(method, tag, url, this, this, m);
        } else {
            if (time + 2000 < new Date().getTime()) {
                time = new Date().getTime();
                Toast.makeText(this, getString(R.string.no_wifi_or_open_mobile_data), Toast.LENGTH_SHORT).show();
            }
            handler.sendEmptyMessage(Contents.TAG_ERROES);
        }
    }
    /**
     * 图片缩放剪切
     *
     * @param uri
     * @param aspect
     */
    public void startPhotoZoom(Uri uri, boolean aspect) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, Contents.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (aspect) {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Contents.PHOTORESOULT);
    }

    /**
     * 打开相机或者相册
     */
    protected void openCameraOrAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.selected_please));
        builder.setTitle(getString(R.string.selected_hit));

        builder.setNegativeButton(getString(R.string.selected_camera), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                    Toast.makeText(BaseActivity.this, getString(R.string.selected_sdcard_not_exists),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setPositiveButton(getString(R.string.selected_photo), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
                startActivityForResult(intent, Contents.PHOTO);
            }
        });
        builder.show();
    }

}
