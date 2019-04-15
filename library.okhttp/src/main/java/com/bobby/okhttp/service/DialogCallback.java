package com.bobby.okhttp.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.StringRes;

import com.bobby.okhttp.ex.NetworkResponseException;
import com.bobby.okhttp.view.MaterialProgressDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * 带有进度提示对话框的结果回调
 * <p>
 * 作者 Bobby on 2017/9/12.
 */
public abstract class DialogCallback<T> extends AbsCallback<T>
{
    private MaterialProgressDialog dialog;

    public DialogCallback showProgressDialog(Context context, @StringRes int message)
    {
        dialog = MaterialProgressDialog.getInstance(context);
        dialog.setProgressStyle(true);
        dialog.setProgressMessage(message)
                .setProgressDialogCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        OkGo.getInstance().cancelTag(getOnlyTag());
                    }
                });
        return this;
    }

    public DialogCallback showProgressHorizontalDialog(Context context, @StringRes int message)
    {
        dialog = MaterialProgressDialog.getInstance(context);
        dialog.setProgressStyle(false);
        dialog.setProgressMessage(message);
        dialog.setCancelButton(new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                OkGo.getInstance().cancelTag(getOnlyTag());
            }
        });
        return this;
    }

    private Context context;

    public void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

    public abstract String getOnlyTag();

    @Override
    public void onStart(Request<T, ? extends Request> request)
    {
        if (dialog != null)
            dialog.show();
    }

    @Override
    public void uploadProgress(Progress progress)
    {
        if (dialog != null)
        {
            dialog.setMax((int) (progress.totalSize));
            dialog.setProgress((int) (progress.currentSize));
        }
    }

    @Override
    public void onFinish()
    {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onError(Response<T> response)
    {
        Throwable throwable = response.getException();
        if (throwable instanceof NetworkResponseException)
        {
            NetworkResponseException exception = (NetworkResponseException) throwable;
            NetworkResponseException.ErrorState state = exception.getState();
            if (state != null)
                switch (state)
                {
                    case NOT_LOGGEDIN:
                    case LOGING_TIMEOUT:
                        String packname = context.getPackageName();
                        Intent intent = new Intent(Libapps.LibActions.ACTION_RE_LOGIN);
                        intent.setComponent(new ComponentName(packname, String.format("%s.service.ReloginReceiver", packname)));
                        context.sendBroadcast(intent, Libapps.LibRequiresPermission.PERMISSION_RE_LOGIN);
                        break;
                }
        }
        this.onFailure(response);
    }

    /**
     * 所有的子类不建议直接重写<code>{@link #onError(Response)}</code>，因为子类重写后其超类就无法正常完成工作
     *
     * @param response 结果
     */
    public void onFailure(Response<T> response)
    {
        // TODO
    }
}
