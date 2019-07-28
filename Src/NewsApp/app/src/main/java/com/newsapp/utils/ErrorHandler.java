package com.newsapp.utils;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.newsapp.R;
import com.newsapp.models.ErrorObject;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.http.HTTP;

public class ErrorHandler {

    public static ErrorObject buildErrorObject(Throwable e, Context context) {
        if (e instanceof HttpException) {
            String error = "";
            HttpException httpException = (HttpException) e;

            if (httpException.code() == 429) {
                error = context.getString(R.string.too_many_request);
            }else if (httpException.code()>=500){
                error = context.getString(R.string.something_went_wrong);
            }
            else {
                ResponseBody responseBody = ((HttpException) e).response().errorBody();
                error = getErrorMessage(responseBody);
            }
            return new ErrorObject(error, error, -1);
        } else if (e instanceof SocketTimeoutException) {
            return new ErrorObject(context.getString(R.string.time_out_message),
                    context.getString(R.string.time_out_message),
                    -1);
        } else if (e instanceof IOException) {
            return new ErrorObject(context.getString(R.string.something_went_wrong),
                    context.getString(R.string.something_went_wrong),
                    -1);
        } else {
            return new ErrorObject(context.getString(R.string.something_went_wrong),
                    context.getString(R.string.something_went_wrong)
                    , -1);
        }
    }

    private static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
