package com.bobby.okhttp.ex;

/**
 * 作者 Bobby on 2017/9/12.
 */
public class NetworkResponseException extends RuntimeException
{
    private ErrorState state;
    private String content;

    public NetworkResponseException(ErrorState state, String message)
    {
        super(message);
        this.state = state;
    }

    public NetworkResponseException(ErrorState state, String message, String content)
    {
        super(message);
        this.state = state;
        this.content = content;
    }

    public ErrorState getState()
    {
        return state;
    }

    public String getErrContent()
    {
        return content;
    }

    private int code;

    public NetworkResponseException(int code, String message)
    {
        super(message);
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public NetworkResponseException(String message)
    {
        super(message);
    }

    public enum ErrorState
    {
        /**
         * 未登录状态
         */
        NOT_LOGGEDIN(1),
        /**
         * 登录过时
         */
        LOGING_TIMEOUT(1),
        /**
         * 未知错误
         */
        UNKNOWN_ERROR(2),
        /**
         * 需要验证登录身份
         */
        VERIFIC_LOGIN(1);

        public int code;

        ErrorState(int code)
        {
            this.code = code;
        }

        public static ErrorState valueOf(int code)
        {
            for (ErrorState state : ErrorState.values())
                if (state.code == code)
                    return state;
            return ErrorState.UNKNOWN_ERROR;
        }
    }
}
