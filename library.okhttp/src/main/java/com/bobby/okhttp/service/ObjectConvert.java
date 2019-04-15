package com.bobby.okhttp.service;

import android.util.Log;

import com.bobby.okhttp.entitys.ResponseEntity;
import com.bobby.okhttp.type.TypeBuilder;
import com.google.gson.Gson;
import com.lzy.okgo.convert.Converter;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 对象类型的结果数据
 * <p>
 * 作者 Bobby on 2017/9/11.
 */
class ObjectConvert implements Converter<ResponseEntity>
{
    private Class<?> targetCls;

    ObjectConvert(Class<?> targetCls)
    {
        this.targetCls = targetCls;
    }

    @Override
    public ResponseEntity convertResponse(Response response) throws Throwable
    {
        ResponseBody body = response.body();
        if (null == body) return null;
        String json = body.string();
        Log.d(ObjectConvert.class.getSimpleName(), json);
        JSONObject responseJson = new JSONObject(json);
        Gson gson = new Gson();
        if (!(responseJson.opt("Data") instanceof JSONObject))
        {
            if (Number.class.isAssignableFrom(targetCls))
                responseJson.put("Data", responseJson.opt("Data"));
            else if (String.class.isAssignableFrom(targetCls))
                responseJson.put("Data", responseJson.optString("Data"));
            else
                responseJson.put("Data", new JSONObject());
        }
        Type type = TypeBuilder.newInstance(ResponseEntity.class)
                .addTypeParam(targetCls)
                .build();
        ResponseEntity entity = gson.fromJson(responseJson.toString(), type);
        entity.errContent = json;
        return entity;
    }
}
