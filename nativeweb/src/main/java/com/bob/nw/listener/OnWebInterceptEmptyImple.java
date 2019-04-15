package com.bob.nw.listener;

import android.net.Uri;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 仅作为一个空实现，它的目的在于使调用者可以不用实现接口中所有的方法
 * </hr>
 * bob/2019/3/4-10:53
 */
public abstract class OnWebInterceptEmptyImple implements OnWebInterceptListener
{
    @Override
    public void onInterceptHandleUrl(WebView webView, String url)
    {
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {

    }

    @Override
    public boolean onInterceptJsEvent(WebJsEventCode code, WebView view, String url, String message, JsResult result, String defaultValue)
    {
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, CharSequence description, String failingUrl)
    {

    }

    @Override
    public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
    {
        return false;
    }
}
