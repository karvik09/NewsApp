package com.newsapp.utils;

import android.content.Context;


import com.newsapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TImeUtils {

    private static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
    private static SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    /**
     * return date in dd-MM-yyyy format
     * @param dateString
     * @return
     */
    public static String getDate(String dateString) {
        Date date = parseDate(dateString);
        return outputFormat.format(date);
    }

    public static Date parseDate(String date){
        try {
            return inputFormat.parse(date);
        } catch (ParseException e) {
            VLog.e("Error!","Unable to parse date");
            return new Date();
        }
    }

    public static String getArticleTime(Context context, String publishedAt) {
        Date date = parseDate(publishedAt);
        long milliseconds = new Date().getTime() - date.getTime();

        if (TimeUnit.MILLISECONDS.toMinutes(milliseconds) <= 10) {
            return context.getString(R.string.now);

        }else if (TimeUnit.MILLISECONDS.toMinutes(milliseconds)<60){

            return String.format(context.getString(R.string.minutes_ago),
                    TimeUnit.MILLISECONDS.toMinutes(milliseconds));

        }else if (TimeUnit.MILLISECONDS.toHours(milliseconds)<2){

            return String.format(context.getString(R.string.hour_ago),
                    TimeUnit.MILLISECONDS.toHours(milliseconds));

        }else if (TimeUnit.MILLISECONDS.toHours(milliseconds)<24){

            return String.format(context.getString(R.string.hours_ago),
                    TimeUnit.MILLISECONDS.toHours(milliseconds));

        }else if (TimeUnit.MILLISECONDS.toDays(milliseconds)<2){

            return String.format(context.getString(R.string.day_ago),
                    TimeUnit.MILLISECONDS.toDays(milliseconds));

        }else if (TimeUnit.MILLISECONDS.toDays(milliseconds)<7){

            return String.format(context.getString(R.string.days_ago),
                    TimeUnit.MILLISECONDS.toDays(milliseconds));
        }else {
            return outputFormat.format(date);
        }

    }
}
