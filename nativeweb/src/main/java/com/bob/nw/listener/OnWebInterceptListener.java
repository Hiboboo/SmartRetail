package com.bob.nw.listener;

import android.net.Uri;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 对Web页面的内容加载和Jscript事件拦截监听器。
 * </hr>
 * bob/2019/3/4-10:06
 */
public interface OnWebInterceptListener
{
    /**
     * 监听<code>{@link WebView}</code>即将要加载的URL地址，在目标URL加载前，调用者可以在该实现方法内进行业务逻辑的处理。
     *
     * @param webView 当前的<code>{@link WebView}</code>对象
     * @param url     即将要加载/被拦截的URL地址
     */
    void onInterceptHandleUrl(WebView webView, String url);

    /**
     * 监听当前页面加载的进度
     *
     * @param view        当前的<code>{@link WebView}</code>对象
     * @param newProgress 加载进度的具体数值，进度值范围：0~100
     */
    void onProgressChanged(WebView view, int newProgress);

    /**
     * 监听由Jscript引起的各类事件。所有的参数并非实时有效，它们受到参数{@code code}值的影响
     *
     * @param code         具体被拦截后的事件标记。参见：<code>{@link WebJsEventCode}</code>
     * @param view         当前的<code>{@link WebView}</code>对象
     * @param url          被拦截的URL地址
     * @param message      被拦截后的提示消息
     * @param result       用于将用户的响应发送到javascript的处理对象。当仅当参数{@code code}值为<code>{@link WebJsEventCode#JS_PROMPT}</code>时，该对象被自动实例化为<code>{@link android.webkit.JsPromptResult}</code>
     * @param defaultValue 提示对话框中显示的默认值
     * @return 如果调用者需要在该实现方法内处理自己的业务逻辑，并且要求进一步的操作由调用者自己来完成，则返回{@code true}，否则需要返回{@code false}
     */
    boolean onInterceptJsEvent(WebJsEventCode code, WebView view, String url, String message, JsResult result, String defaultValue);

    /**
     * Jscript事件标记常量
     */
    enum WebJsEventCode
    {
        /**
         * 对应Jscript的alert()函数
         */
        JS_ALERT,
        /**
         * 对应Jscript的confirm()函数
         */
        JS_CONFIRM,
        /**
         * 对应Jscript的prompt()函数
         */
        JS_PROMPT
    }

    /**
     * 监听页面已加载完成后的事件
     *
     * @param view 当前的<code>{@link WebView}</code>对象
     * @param url  被拦截的URL地址
     */
    void onPageFinished(WebView view, String url);

    /**
     * 监听页面加载失败时的事件
     *
     * @param view        当前的<code>{@link WebView}</code>对象
     * @param errorCode   错误码
     * @param description 错误描述
     * @param failingUrl  失败的URL地址
     */
    void onReceivedError(WebView view, int errorCode, CharSequence description, String failingUrl);

    /**
     * 监听页面调用本地文件选择器时的事件
     *
     * @param view              当前的<code>{@link WebView}</code>对象
     * @param filePathCallback  包含有所上传的所有文件列表
     * @param fileChooserParams 调取本地文件选择管理器的相关参数（选项）
     */
    boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);
}
