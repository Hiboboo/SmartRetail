package com.bobby.okhttp.service;


import com.bobby.okhttp.entitys.ResponseEntity;
import com.bobby.okhttp.ex.NetworkResponseException;
import com.bobby.okhttp.util.AnnotationProcessor;

import java.util.List;

import okhttp3.Response;

import static com.bobby.okhttp.ex.NetworkResponseException.*;

/**
 * JSON数组数据的结果回调
 * <p>
 * 作者 Bobby on 2017/9/11.
 */
public abstract class ArrayCallback<T> extends DialogCallback<List<T>>
{
    private String tag;
    private ArrayConvert mConvert;

    protected ArrayCallback(Class<?> targetCls)
    {
        tag = targetCls.getName();
        mConvert = new ArrayConvert(targetCls);
    }

    @Override
    public String getOnlyTag()
    {
        return tag;
    }

    @Override
    public List<T> convertResponse(Response response) throws Throwable
    {
        ResponseEntity entity = mConvert.convertResponse(response);
        response.close();
        if (entity.state == 0)
        {
            List<T> objects = (List<T>) entity.data;
            AnnotationProcessor.procesResetValues(objects);
            return objects;
        } else
            throw new NetworkResponseException(ErrorState.valueOf(entity.state), entity.message);
    }
}
