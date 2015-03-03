package es.molestudio.photochop.controller.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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

    public static String[] dateToStringInLetters(Date date) {

        String formatDate = null;
        String formatTime = null;

        String returnDate[] = {null, null};

        try {

            // Day of week
            String dayOfWeek = new SimpleDateFormat("E").format(date);

            // Day
            String day = new SimpleDateFormat("d").format(date);

            // Month
            String month = new SimpleDateFormat("MMM").format(date);

            // Year
            String year = new SimpleDateFormat("yyyy").format(date);

            formatDate = dayOfWeek + " " + day + " " + month + " " + year;

            formatTime = new SimpleDateFormat("HH:mm").format(date);

            returnDate[0] = formatDate;
            returnDate[1] = formatTime;

        } catch (Exception e) {

        }

        return returnDate;

    }


    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
        }
        cursor.close();
        return result;
    }



}
