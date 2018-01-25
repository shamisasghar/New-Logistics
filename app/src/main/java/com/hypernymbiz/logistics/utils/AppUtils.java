package com.hypernymbiz.logistics.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.R;

import java.io.File;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class AppUtils {

    public static int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(50, new float[]{
                rgen.nextInt(359), 1, 1
        });
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
//        if (version >= Build.VERSION_CODES.LOLLIPOP) {
//            return ContextCompat.getColor(context, id);
//        } else {
        return context.getResources().getColor(id);
//        }
    }

    public static String getDayOfMonth(String date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date serverDate = simpleDateFormat.parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
                return dateFormat.format(serverDate);
            } catch (Exception e) {
                Log.e(">> Date Exception", e.getMessage());
            }
        }
        return "0";
    }

    public static String getDayOfWeek(String date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date serverDate = simpleDateFormat.parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
                return dateFormat.format(serverDate);
            } catch (Exception e) {
                Log.e(">> Date Exception", e.getMessage());
            }
        }
        return "Monday";
    }

    public static String getFormattedDate(String date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date serverDate = simpleDateFormat.parse(date);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTime(serverDate);
                calendar.add(Calendar.MILLISECOND,
                        TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                return dateFormat.format(serverDate);
            } catch (Exception e) {
                Log.e(">> Date Exception", e.getMessage());
            }
        }
        return "Monday, Jan 01, 2016";
    }

    public static String getTime(String date) {
        if (date != null) {
            date = date.replace("T", " ");
            date = date.replace("z", "");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date serverDate = simpleDateFormat.parse(date);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTime(serverDate);
                calendar.add(Calendar.MILLISECOND,
                        TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                return dateFormat.format(calendar.getTime());
            } catch (Exception e) {
                Log.e(">> Date Exception", e.getMessage());
            }
        }
        return "00:00 am";
    }

    public static boolean isCurrentOrder(String date) {
        date = date.replace("T", " ");
        date = date.replace("z", "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date serverDate = null;
        try {
            serverDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }
        Calendar calendarServer = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendarServer.setTime(serverDate);
        calendarServer.add(Calendar.MILLISECOND,
                TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());

        Calendar calendarCurrent = Calendar.getInstance(TimeZone.getDefault());
        if (calendarCurrent.after(calendarServer)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isExpiredOrder(String date) {
        date = date.replace("T", " ");
        date = date.replace("z", "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date serverDate = null;
        try {
            serverDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }
        Calendar calendarServer = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendarServer.setTime(serverDate);
        calendarServer.add(Calendar.MILLISECOND,
                TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());

        Calendar calendarCurrent = Calendar.getInstance(TimeZone.getDefault());
        long miliSeconds = calendarCurrent.getTimeInMillis()-calendarServer.getTimeInMillis();
        if(miliSeconds > 5400000){
            return true;
        }else {
            return false;
        }
    }

    public static String getDateTimeRaw(String date) {
        if (date != null) {
            String rawDate = "";
            rawDate = date.substring(0, date.indexOf("."));
            Log.e("Date", rawDate);
            rawDate = rawDate.replace("-", "");
            rawDate = rawDate.replace(":", "");
            rawDate = rawDate.replace(" ", "");
            Log.e("Date", rawDate);
            return rawDate;
        }
        return "";
    }

    public static Calendar getCalendar(String date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date serverDate1 = simpleDateFormat.parse(date);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTime(serverDate1);
                calendar.add(Calendar.MILLISECOND,
                        TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
                // Zero out the hour, minute, second, and millisecond
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar;
            } catch (Exception e) {
                Log.e(">> Date Exception", e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    public static String getDateAndTime(String date) {
        if (date != null) {
            date = date.replace("T", " ");
            date = date.replace("z", "");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date serverDate = simpleDateFormat.parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
                return dateFormat.format(serverDate);
            } catch (Exception e) {
                Log.e(">> Date Exception", e.getMessage());
            }
        }
        return "00:00 am";
    }

//    public static String getErrorMessage(Context context, RetrofitError error) {
//        if (error == null || error.getResponse() == null)
//            return context.getString(R.string.connection_error);
//        return error.getResponse().getReason();
//    }

    public static boolean isInternetAvailable(final Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conn.getActiveNetworkInfo();
        if (activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public static void requestFocus(Activity activity, View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void makeToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void makeNotification(Context context, Class<?> class_, String fragmentName, Bundle bundle, String title, boolean isUpdateCurrent, int requestCode) {
        Intent intent = new Intent(context, class_);
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);

        intent.putExtra(Constants.DATA, bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setTicker(title)
                .setColor(ContextCompat.getColor(context, R.color.colorNotificationIcon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(requestCode, notification);
    }

    public static void hideKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String realPath;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = context.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        realPath = cursor.getString(columnIndex);
        cursor.close();
        return realPath;
    }

    public static boolean isRunningInForeground(final Context context) {
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(1);
        if (tasks.isEmpty()) {
            return false;
        }
        String topActivityName = tasks.get(0).topActivity.getPackageName();
        return topActivityName.equalsIgnoreCase(context.getPackageName());
    }

    public static void showSnackBar(View v, String message) {
        if (v != null && !TextUtils.isEmpty(message)) {
            Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundResource(R.color.colorPrimary);
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
            snackbar.show();
        }
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_check_circle_36dp : R.mipmap.ic_launcher;
    }
////
//    public static String getErrorMessage(Context context, int statusCode) {
//        String message = null;
//        if (context != null) {
//            message = context.getString(R.string.default_error);
//            switch (statusCode) {
//                case 1:
//                    message = context.getString(R.string.network_error);
//                    break;
//
//                case 204:
//                    message = context.getString(R.string.error_204);
//                    break;
//
//                case 400:
//                    message = context.getString(R.string.error_400);
//                    break;
//
//                case 401:
//                    message = context.getString(R.string.error_401);
//                    break;
//
//                case 406:
//                    message = context.getString(R.string.error_406);
//                    break;
//
//                case 500:
//                    message = context.getString(R.string.error_500);
//                    break;
//            }
//        }
//        return message;
//    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    public static Drawable getColorDrawable(int image, Context context) {

        Drawable mDrawable = ContextCompat.getDrawable(context, image);
        mDrawable.setColorFilter(new
                PorterDuffColorFilter(ContextCompat.getColor(context,R.color.tracking_stpi_indicatorColor),
                PorterDuff.Mode.MULTIPLY));
        return mDrawable;
    }

}
