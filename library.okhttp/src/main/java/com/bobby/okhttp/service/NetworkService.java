package com.bobby.okhttp.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * 网络请求服务
 * <p>
 * 作者 Bobby on 2017/9/11.
 */
public class NetworkService
{
    private Context context;

    private NetworkService(Context context)
    {
        this.context = context;
    }

    public static NetworkService newInstance(Context context)
    {
        return new NetworkService(context);
    }

    public static final String BASE_URL = "http://api.ccoder.vip";
    public static final String BASE_UPLOAD_URL = "https://imgapi.jdhui.com";
    public static final String BASE_H5_URL = "http://zmm.ccoder.vip";
    public static final String BASE_SHOP_H5_URL = "http://shop.ccoder.vip";

    private String url;

    public NetworkService onPost(String url)
    {
        this.url = url;
        return this;
    }

    public NetworkService onGet(String url)
    {
        this.url = url;
        return this;
    }

    private boolean sslManager = false;

    public NetworkService sslRequest(boolean sslManager)
    {
        this.sslManager = sslManager;
        return this;
    }

    private Map<String, Object> params = new HashMap<>();

    public NetworkService addParams(String key, Object value)
    {
        params.put(key, (null == value || "null".equals(value)) ? "" : value instanceof CharSequence ? String.valueOf(value) : value);
        return this;
    }

    public static String getToken(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("zk", Context.MODE_PRIVATE);
        return preferences.getString("com.zol.zkep.t", "");
    }

    private void buildParams()
    {
        if (!params.containsKey("token"))
            params.put("token", getToken(context));
        params.put("Version", "and" + this.getCurrentVersionCode(context));
    }

    /**
     * 获取当前程序的版本号
     *
     * @param context 运行中的上下文对象
     * @return 当前程序的版本号码
     */
    private int getCurrentVersionCode(Context context)
    {
        PackageManager mPackageManager = context.getPackageManager();
        try
        {
            PackageInfo mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return mPackageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public enum NetworkDomType
    {
        BASIC,
        NO_BASIC
    }

    public <T> void onPostRequest(DialogCallback<T> callback)
    {
        this.onPostRequest(callback, NetworkDomType.BASIC);
    }

    public <T> void onPostRequest(DialogCallback<T> callback, NetworkDomType type)
    {
        String requestUrl;
        switch (type)
        {
            case BASIC:
                requestUrl = BASE_URL.concat(url);
                break;
            case NO_BASIC:
            default:
                requestUrl = url;
                break;
        }
        this.buildParams();
        callback.setContext(context);
        PostRequest<T> request = OkGo.post(requestUrl);
        request.tag(callback.getOnlyTag());
        request.upJson(new JSONObject(params));
        Log.d("NetworkService", requestUrl + ", url params = " + params);
        request.execute(callback);
    }

    public <T> void onUploadFiles(DialogCallback<T> callback, List<File> files)
    {
        this.buildParams();
        OkGo okGo = OkGo.getInstance();
        if (sslManager)
        {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            okGo.setOkHttpClient(builder.build());
        }
        callback.setContext(context);
        String requestUrl = BASE_UPLOAD_URL.concat(url);
        for (File file : files)
        {
            PostRequest<T> request = OkGo.post(requestUrl);
            request.tag(callback.getOnlyTag()).params("file", file).execute(callback);
        }
    }
}
