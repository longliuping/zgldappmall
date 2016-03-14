package com.zgld.mall.beans;

import java.io.Serializable;

/**
 * Created by LongLiuPing on 2016/3/14.
 */
public class GsonObject implements Serializable{
    int flag;
    String msg;
    int status;
//    boolean timeout;
    ObjectData data;

    public ObjectData getData() {
        return data;
    }

    public void setData(ObjectData data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

//    public boolean isTimeout() {
//        return timeout;
//    }
//
//    public void setTimeout(boolean timeout) {
//        this.timeout = timeout;
//    }
    private class ObjectData{

    }
}
