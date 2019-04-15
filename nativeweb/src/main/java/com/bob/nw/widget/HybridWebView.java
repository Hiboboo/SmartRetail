package com.bob.nw.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bob.nw.R;
import com.bob.nw.listener.OnWebInterceptListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 提供H5和Native APIs互调/访问的WebView控件
 * </hr>
 * bob/2019/2/23-15:45
 */
public class HybridWebView extends WebView
{
    private final String TAG = HybridWebView.class.getSimpleName();

    private OnWebInterceptListener mInterceptListener;

    public HybridWebView(Context context)
    {
        super(context);
        this.initWebConfig(null);
    }

    public HybridWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.initWebConfig(attrs);
    }

    public HybridWebView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.initWebConfig(attrs);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebConfig(AttributeSet attrs)
    {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        // 设置加载进来的页面自适应手机屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        else
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        this.setupWebIntercept(attrs);
    }

    private void setupWebIntercept(AttributeSet attrs)
    {
        boolean isNeedIntercept = false;
        boolean isNeedChromeIntercept = false;
        if (attrs != null)
        {
            TypedArray a = getResources().obtainAttributes(attrs, R.styleable.HybridWebView);
            isNeedIntercept = a.getBoolean(R.styleable.HybridWebView_hw_isNeedWebClientIntercept, false);
            isNeedChromeIntercept = a.getBoolean(R.styleable.HybridWebView_hw_isNeedWebChromeClientIntercept, false);
            a.recycle();
        }
        Log.d(TAG, "isNeedIntercept=" + isNeedIntercept);
        if (isNeedIntercept)
            this.setWebIntercept();
        Log.d(TAG, "isNeedChromeIntercept=" + isNeedChromeIntercept);
        if (isNeedChromeIntercept)
            this.setWebChromeIntercept();
    }

    /**
     * 设置WebView拦截处理器
     *
     * @see InterceptHandleWebViewClient
     */
    public void setWebIntercept()
    {
        this.setWebViewClient(new InterceptHandleWebViewClient());
    }

    public void setWebChromeIntercept()
    {
        this.setWebChromeClient(new InterceptHandleWebChromeClient());
    }

    /**
     * 为当前页面设置拦截监听器
     *
     * @param listener 拦截监听器的实例对象
     */
    public void setOnWebInterceptListener(OnWebInterceptListener listener)
    {
        mInterceptListener = listener;
    }

    private void onInterceptHandleUrl(WebView webView, String url)
    {
        try
        {
            String decodeUrl = URLDecoder.decode(url, "UTF-8");
            Log.d(TAG, "onInterceptHandleUrl decode url=" + decodeUrl);
            if (mInterceptListener != null)
                mInterceptListener.onInterceptHandleUrl(webView, url);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * WebView跳转拦截处理器
     */
    private final class InterceptHandleWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                Log.d(TAG, "API>=24 的环境中执行shouldOverrideUrlLoading");
                String url = request.getUrl().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    view.stopLoading();
                onInterceptHandleUrl(view, url);
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            {
                Log.d(TAG, "API<24 的环境中执行shouldOverrideUrlLoading");
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    view.stopLoading();
                onInterceptHandleUrl(view, url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            Log.d(TAG, "onPageFinished url=" + url);
            if (mInterceptListener != null)
                mInterceptListener.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
        {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                final int errorCode = error.getErrorCode();
                Log.d(TAG, "API>=23 onReceivedError error code=" + errorCode + "| desc=" + error.getDescription());
                if (mInterceptListener != null)
                    mInterceptListener.onReceivedError(view, errorCode, error.getDescription(), request.getUrl().toString());
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            {
                Log.d(TAG, "API<23 onReceivedError error code=" + errorCode + "| desc=" + description);
                if (mInterceptListener != null)
                    mInterceptListener.onReceivedError(view, errorCode, description, failingUrl);
            }
        }
    }

    /**
     * 拦截由Javascript主动发起的各类事件，并做Native处理
     */
    private final class InterceptHandleWebChromeClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            Log.d(TAG, "onProgressChanged progress=" + newProgress);
            if (mInterceptListener != null)
                mInterceptListener.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result)
        {
            Log.d(TAG, "onJsAlert message=" + message);
            if (mInterceptListener != null)
                return mInterceptListener.onInterceptJsEvent(OnWebInterceptListener.WebJsEventCode.JS_ALERT,
                        view, url, message, result, "");
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result)
        {
            Log.d(TAG, "onJsConfirm message=" + message);
            if (mInterceptListener != null)
                return mInterceptListener.onInterceptJsEvent(OnWebInterceptListener.WebJsEventCode.JS_CONFIRM,
                        view, url, message, result, "");
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)
        {
            Log.d(TAG, "onJsPrompt message=" + message);
            if (mInterceptListener != null)
                return mInterceptListener.onInterceptJsEvent(OnWebInterceptListener.WebJsEventCode.JS_PROMPT,
                        view, url, message, result, defaultValue);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
        {
            if (mInterceptListener != null)
                return mInterceptListener.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }
}
