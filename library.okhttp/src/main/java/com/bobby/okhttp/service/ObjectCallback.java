package com.bobby.okhttp.service;


import android.text.TextUtils;

import com.bobby.okhttp.entitys.ResponseEntity;
import com.bobby.okhttp.ex.NetworkResponseException;
import com.bobby.okhttp.util.AnnotationProcessor;

import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.bobby.okhttp.ex.NetworkResponseException.ErrorState;

/**
 * JSON对象数据的结果回调
 * <p>
 * 作者 Bobby on 2017/9/11.
 */
public abstract class ObjectCallback<T> extends DialogCallback<T>
{
    private ObjectConvert mConvert;
    private String tag;

    protected ObjectCallback(Class<?> targetCls)
    {
        tag = targetCls.getName();
        mConvert = new ObjectConvert(targetCls);
    }

    @Override
    public String getOnlyTag()
    {
        return tag;
    }

    @Override
    public T convertResponse(Response response) throws Throwable
    {
        ResponseEntity entity = mConvert.convertResponse(response);
        response.close();
        if (entity.state == 0)
        {
            T target = (T) entity.data;
            AnnotationProcessor.procesResetValue(target);
            return target;
        } else
        {
            if (TextUtils.isEmpty(entity.errContent))
                throw new NetworkResponseException(ErrorState.valueOf(entity.state), entity.message);
            else
                throw new NetworkResponseException(ErrorState.valueOf(entity.state), entity.message, entity.errContent);
        }
    }
}
