package es.molestudio.photochop.controller.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Chus on 03/01/15.
 */
public class AppUtils {


    public static String getStringFormatDate(Date date) {

        if (date == null) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }


    public static Date getDateFromString(String dateString) {

        DateFormat inputFormat = null;

        if (dateString == null) {
            return null;
        }

        if(dateString.length() == 19)
            inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        if(dateString.length() == 10)
            inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        if(inputFormat == null) {
            return null;
        }

        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = null;
        try {
            parsed = inputFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsed;
    }


}
