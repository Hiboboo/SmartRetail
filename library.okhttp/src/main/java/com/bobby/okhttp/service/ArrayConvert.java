package com.bobby.okhttp.service;

import android.util.Log;

import com.bobby.okhttp.entitys.ResponseEntity;
import com.bobby.okhttp.type.TypeBuilder;
import com.google.gson.Gson;
import com.lzy.okgo.convert.Converter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 数组类型的结果数据
 * <p>
 * 作者 Bobby on 2017/9/11.
 */
class ArrayConvert implements Converter<ResponseEntity>
{
    private Class<?> targetCls;

    ArrayConvert(Class<?> targetCls)
    {
        this.targetCls = targetCls;
    }

    @Override
    public ResponseEntity convertResponse(Response response) throws Throwable
    {
        ResponseBody body = response.body();
        if (null == body) return null;
        String json = body.string();
        Log.d(ArrayConvert.class.getSimpleName(), json);
        JSONObject responseJson = new JSONObject(json);
        Gson gson = new Gson();
        Type type = TypeBuilder.newInstance(ResponseEntity.class)
                .beginSubType(List.class)
                .addTypeParam(targetCls)
                .endSubType()
                .build();
        ResponseEntity entity = gson.fromJson(responseJson.toString(), type);
        entity.errContent = json;
        return entity;
    }
}
