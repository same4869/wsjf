package com.dt.wsjf.json;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @Author:Lijj
 * @Date:2014-4-17上午10:33:54
 * @Todo:TODO
 */
public class JSONToBeanHandler {

    /**
     * 数据源转化为 对象bean
     *
     * @param data
     * @param cls
     * @return
     * @throws JSONFormatExcetion
     */
    public static <T> T fromJsonString(String data, Class<T> cls) throws JSONFormatExcetion {
        try {
            Gson gson = new Gson();
            return gson.fromJson(data, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JSONFormatExcetion("json format to " + cls.getName() + " exception :" + data);
        }
    }

    /**
     * 对象bean转化为json字符串
     *
     * @param value
     * @return
     * @throws JSONFormatExcetion
     */
    public static String toJsonString(Object value) throws JSONFormatExcetion {
        try {
            Gson gson = new Gson();
            return gson.toJson(value);
        } catch (JsonIOException e) {
            throw new JSONFormatExcetion(value.getClass().getName() + " to json exception");
        }
    }

}
