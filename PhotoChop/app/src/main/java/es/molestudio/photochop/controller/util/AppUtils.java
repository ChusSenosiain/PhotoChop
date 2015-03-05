package es.molestudio.photochop.controller.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.security.MessageDigest;
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


    /**
     * Obtiene la clave de la aplicación para FB
     * @return Key de la applicación
     */
    public static void getAppKey(Context _context) {

        String keyhash = null;
        String packageName = _context.getPackageName().toString();

        try {

            PackageInfo info = _context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyhash = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                Log.d("Obtengo Key: " + keyhash);
            }
        } catch (Exception e) {
            Log.d("Error al obtener el hashkey: " + e.toString());
        }

    }

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

        String formatDate = "";
        String formatTime = "";

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

            if (formatDate == null) {
                formatDate = "";
            }

            if (formatTime == null) {
                formatTime = "";
            }

            returnDate[0] = formatDate;
            returnDate[1] = formatTime;

        } catch (Exception e) {

        }

        return returnDate;

    }






}
