package com.bobby.okhttp.type;

import com.bobby.okhttp.ex.TypeException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 * 作者：孙博
 * <p>
 * 时间：2017/7/26 11:35
 */
public abstract class TypeToken<T>
{
    private final Type type;

    public TypeToken()
    {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class)
            throw new TypeException("No generics found!");
        ParameterizedType type = (ParameterizedType) superclass;
        this.type = type.getActualTypeArguments()[0];
    }

    public Type getType()
    {
        return type;
    }
}