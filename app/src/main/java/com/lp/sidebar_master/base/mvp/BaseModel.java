package com.lp.sidebar_master.base.mvp;

import java.io.Serializable;

/**
 * File descripition:   mode基类
 *
 * @author lp
 * @date 2018/6/19
 */

public class BaseModel<T> implements Serializable {
    private String message;
    private int code;
    private T result;

    public BaseModel(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getErrcode() {
        return code;
    }

    public void setErrcode(int code) {
        this.code = code;
    }

    public String getErrmsg() {
        return message;
    }

    public void setErrmsg(String message) {
        this.message = message;
    }


    public T getData() {
        return result;
    }

    public void setData(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "code=" + code +
                ", msg='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
