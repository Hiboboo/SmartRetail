package com.bobby.okhttp.ex;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * <p>
 * 作者：孙博
 * <p>
 * 时间：2017/7/26 11:36
 */
public class TypeException extends RuntimeException
{
    public TypeException()
    {
    }

    public TypeException(String message)
    {
        super(message);
    }

    public TypeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TypeException(Throwable cause)
    {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
