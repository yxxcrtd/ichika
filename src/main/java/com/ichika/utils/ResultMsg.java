package com.ichika.utils;

import lombok.Data;

import static com.ichika.utils.Constants.INT_200;
import static com.ichika.utils.Constants.INT_MINUS_200;

@Data
public class ResultMsg<T> {

    private int code;

    private String message;

    private T data;

    private ResultMsg() {

    }

    public static final <T> ResultMsg<T> resultSuccess(String message, T t) {
        ResultMsg<T> resultVo = new ResultMsg<>();
        resultVo.setCode(INT_200);
        resultVo.setMessage(message);
        resultVo.setData(t);
        return resultVo;
    }

    public static final <T> ResultMsg<T> resultSuccess(String message) {
        return resultSuccess(message, null);
    }

    public static final <T> ResultMsg<T> resultSuccess(T t) {
        return resultSuccess(null, t);
    }

    public static final <T> ResultMsg<T> resultSuccess() {
        return resultSuccess(null, null);
    }

    public static final <T> ResultMsg<T> resultFail(int code, String message) {
        ResultMsg<T> resultVo = new ResultMsg<>();
        resultVo.setCode(code);
        resultVo.setMessage(message);
        return resultVo;
    }

    public static final <T> ResultMsg<T> resultFail(String message) {
        return resultFail(INT_MINUS_200, message);
    }

    public boolean checkSuccess() {
        return this.getCode() == INT_200;
    }

}
