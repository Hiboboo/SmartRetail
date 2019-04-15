package com.bob.nw.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * </hr>
 * bob/2019/2/23-18:07
 */
public final class JavascriptInterface
{
    private Context context;

    public JavascriptInterface(Context context)
    {
        this.context = context;
    }

    @SuppressLint("JavascriptInterface")
    @android.webkit.JavascriptInterface
    public void alert(String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
