package com.bobby.okhttp.util;

import android.support.annotation.Keep;
import android.text.TextUtils;

import com.bobby.okhttp.annotations.SerializedRepair;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 作者 Bobby on 2017/10/27.
 */
@Keep
public class AnnotationProcessor
{
    @Keep
    public static void procesResetValues(List<?> objects)
    {
        if (null == objects || objects.isEmpty())
            return;
        for (Object target : objects)
            procesResetValue(target);
    }

    @Keep
    public static void procesResetValue(Object target)
    {
        try
        {
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields)
            {
                if (field.isAnnotationPresent(SerializedRepair.class))
                {
                    Object oldValue = field.get(target);
                    SerializedRepair repair = field.getAnnotation(SerializedRepair.class);
                    switch (repair.condition())
                    {
                        case EMPTY:
                            field.set(target, exactEmptyTypeHandle(oldValue, repair));
                            break;
                        case NUMBER:
                            break;
                        case HTML:
                            field.set(target, exactHtmlTypeHandle(oldValue, repair));
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }
    }

    @Keep
    private static Object exactEmptyTypeHandle(Object type, SerializedRepair repair)
    throws InstantiationException, IllegalAccessException
    {
        if (repair.isObject())
        {
            if (type instanceof List)
            {
                List<Object> objects = (List<Object>) type;
                for (Object object : objects)
                    procesResetValue(object);
            } else
            {
                Object value = type;
                if (null == value)
                    value = repair.targetObj().newInstance();
                procesResetValue(value);
                return value;
            }
        } else
        {
            String value = (String) type;
            if (TextUtils.isEmpty(value))
                value = repair.targetValue();
            return value;
        }
        return type;
    }

    @Keep
    private static Object exactHtmlTypeHandle(Object oValue, SerializedRepair repair)
    {
        if (null == oValue)
            return repair.targetObj();
        String nValue = String.valueOf(oValue);
        if (nValue.contains("<") && nValue.contains(">"))
            nValue = repair.targetValue();
        return nValue;
    }
}
