package com.zgld.mall.activity;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zgld.mall.R;
import com.zgld.mall.adapter.AddressAdapter;
import com.zgld.mall.beans.Address;
import com.zgld.mall.utils.Contents;
import com.zgld.mall.utils.CustomDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 地址管理
 *
 */
public class AddressManagerActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener2,
        AddressAdapter.AddressAdapterListener {
    PullToRefreshListView listview;
    List<Address> listInfo = new ArrayList<Address>();
    AddressAdapter infoAdapter;

    @Override
    public void handleMsg(Message msg) {
        // TODO Auto-generated method stub
        listview.onRefreshComplete();
        Bundle bundle = msg.getData();
        String json = "";
        if (bundle == null) {
            return;
        }
        json = bundle.getString(Contents.JSON);
        if (json == null) {
            return;
        }
        Gson gson = new Gson();
        Type entityType = null;
        switch (msg.what) {
            case 201:
                listInfo = new ArrayList<Address>();
                entityType = new TypeToken<List<Address>>() {
                }.getType();
                List<Address> list = gson.fromJson(json, entityType);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Address info = list.get(i);
                        listInfo.add(info);
                    }
                }
                infoAdapter = new AddressAdapter(this, listInfo, this);
                listview.setAdapter(infoAdapter);
                infoAdapter.notifyDataSetChanged();
                if (listInfo == null || listInfo.size() <= 0) {
                    startActivityForResult(new Intent(AddressManagerActivity.this, AddAddressActivity.class), 200);
                }
                break;
            case 202:
                initData();
                if (json != null && json.trim().equals("1")) {
                    listInfo.remove(deletePosition);
                    Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_address_manager);
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_manager_address));
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnRefreshListener(this);
        TextView add = (TextView) findViewById(R.id.add);
        add.setVisibility(View.VISIBLE);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(AddressManagerActivity.this, AddAddressActivity.class), 200);
            }
        });
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                initData();
            }
        }
    }

    void initData() {
//        getData(com.android.volley.Request.Method.POST, 201, "User/GetUserShipping?token="
//                + Contents.getUser(this).getToken() + "&UserId=" + Contents.getUser(this).getUserId(), null, null, 1);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        // TODO Auto-generated method stub
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        // TODO Auto-generated method stub
        listview.onRefreshComplete();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectedChecked(int position) {
        // TODO Auto-generated method stub
        Address info = listInfo.get(position);
        Intent intent = new Intent();
        intent.putExtra(Contents.INFO, info);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void updateAddress(int position) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, ModifyUserAddressActivity.class);
        intent.putExtra(Contents.INFO, listInfo.get(position));
        startActivityForResult(intent, 200);
    }

    int deletePosition;
    CustomDialog dialog;

    @Override
    public void deleteAddress(final int position) {
        // TODO Auto-generated method stub
        deletePosition = position;
        dialog = new CustomDialog(this, R.style.mystyle, R.layout.customdialog, R.array.title_car_delete_address,
                new CustomDialog.CustomDialogListener() {

                    @Override
                    public void customDialogClickRight() {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
//                        getData(com.android.volley.Request.Method.GET, 202, "User/UserShippingAddressesDel?token="
//                                + Contents.getUser(AddressManagerActivity.this).getToken() + "&userId="
//                                + Contents.getUser(AddressManagerActivity.this).getUserId() + "&shippingId="
//                                + listInfo.get(position).getShippingId(), null, null, 1);
                    }

                    @Override
                    public void customDialogClickLeft() {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
