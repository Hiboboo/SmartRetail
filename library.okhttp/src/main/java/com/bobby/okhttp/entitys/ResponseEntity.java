package com.bobby.okhttp.entitys;

import com.google.gson.annotations.SerializedName;

/**
 * 作者 Bobby on 2017/9/11.
 */
public class ResponseEntity<T>
{
    @SerializedName("ResponseID")
    public int state;
    @SerializedName("Message")
    public String message;
    @SerializedName("Data")
    public T data;
    public String errContent;
}
