package com.bobby.okhttp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.bobby.okhttp.R;

/**
 * Material风格的圆形进度提示框
 * <HR>
 * 创建者 Bobby
 * <p>
 * 时间 2017/8/16 11:41
 */
public class MaterialProgressDialog
{
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public static MaterialProgressDialog getInstance(Context context)
    {
        return new MaterialProgressDialog(context);
    }

    private MaterialProgressDialog(Context context)
    {
        this.mContext = context;
        mProgressDialog = new ProgressDialog(context);
    }

    public MaterialProgressDialog setProgressStyle(boolean isSpinner)
    {
        mProgressDialog.setProgressStyle(isSpinner ? ProgressDialog.STYLE_SPINNER : ProgressDialog.STYLE_HORIZONTAL);
        if (!isSpinner)
        {
            mProgressDialog.setProgressNumberFormat("%1$d bit/%2$d bit");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        return this;
    }

    public MaterialProgressDialog setProgressMessage(CharSequence message)
    {
        mProgressDialog.setMessage(message);
        return this;
    }

    public MaterialProgressDialog setProgressMessage(@StringRes int res)
    {
        mProgressDialog.setMessage(mContext.getResources().getString(res));
        return this;
    }

    public MaterialProgressDialog setMax(int max)
    {
        mProgressDialog.setMax(max);
        return this;
    }

    public MaterialProgressDialog setProgress(int progress)
    {
        mProgressDialog.setProgress(progress);
        return this;
    }

    public MaterialProgressDialog setProgressDialogCancelable(boolean isCancelable)
    {
        mProgressDialog.setCancelable(isCancelable);
        return this;
    }

    public MaterialProgressDialog setCancelButton(DialogInterface.OnClickListener listener)
    {
        String butText = mContext.getResources().getString(R.string.lib_label_cancel);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, butText, listener);
        return this;
    }

    public MaterialProgressDialog setOnCancelListener(DialogInterface.OnCancelListener listener)
    {
        mProgressDialog.setOnCancelListener(listener);
        return this;
    }

    public MaterialProgressDialog setOnDismissListener(DialogInterface.OnDismissListener listener)
    {
        mProgressDialog.setOnDismissListener(listener);
        return this;
    }

    public void show()
    {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void dismiss()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
