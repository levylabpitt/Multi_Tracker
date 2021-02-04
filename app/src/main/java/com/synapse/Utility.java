package com.synapse;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.util.Preconditions;

import com.google.api.client.http.HttpResponse;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.Util;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.help.TabbedActivity;
import com.room_db.Beacon;
//import com.shekhargulati.urlcleaner.UrlCleaner;
import com.synapse.model.BlackListedBeacon;
import com.synapse.model.TaskData;
import com.synapse.model.Task_data;
import com.synapse.model.ScannedData;
import com.synapse.model.SpreadsheetItem;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog.TAG;

public class Utility {

    public static String imgExt[] = {".JPG", ".jpg", ".jpeg", ".JPEG", ".png", ".PNG", ".bmp",
            ".BMP", ".gif", ".GIF", ".webp", ".WEBP"};
    public static String docExt[] = {".doc", ".DOC", ".psd", ".PSD", ".docx", ".PSD",
            ".docx", ".DOCX", ".pdf", ".PDF", ".xlsx", ".XLSX", ".pptx"};
    public static String mediaExt[] = {".aac", ".AAC", ".m4a", ".M4A",
            ".mp4", ".MP4", ".3gp", ".3GP", ".m4b", ".M4B", ".mp3", ".MP3",
            ".wave", ".WAVE"};
    public static String wordExt[] = {".doc", ".DOC", ".psd", ".PSD", ".docx", ".PSD",
            ".docx", ".DOCX"};
    public static String pdfExt[] = {".pdf", ".PDF"};
    public static String txtExt[] = {".txt", ".TXT"};
    public static String excelExt[] = {".xlsx", ".XLSX", ".xls", ".XLS"};


    public static Spanned getTitle(String title) {

        return Html.fromHtml("<small>" + title + "</small>");

    }

    public static File createDriveCacheFolder(String filenameWithExt) {
        File file = null;

        try {

            File folder1 = getPathDriveTempFiles();
            if (!folder1.exists()) {
                folder1.mkdir();
            }

            file = new File(folder1, filenameWithExt);
        } catch (Exception e) {
            e.getMessage();
        }
        return file;
    }

    public static File getPathDriveTempFiles() {
        File folder2 = null;
        try {
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Multitracker");
            if (!folder.exists()) {
                folder.mkdir();
            }
            folder2 = new File(folder, "Drive Cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder2;
    }

    public static void startNotification(Context context) {
        // TODO Auto-generated method stub
        NotificationCompat.Builder notification;
        PendingIntent pIntent;
        NotificationManager manager;
        Intent resultIntent;
        TaskStackBuilder stackBuilder;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            final String channelId = context.getResources().getString(R.string.default_notification_channel_id);
            final String channelName = "channel name";
            final NotificationChannel defaultChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN);
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(defaultChannel);
            }


            stackBuilder = TaskStackBuilder.create(context);

            resultIntent = new Intent(context, TabbedActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification2 = new Notification.Builder(context)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText("Beacon scanner running...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(channelId)
                    .setContentIntent(pIntent)
                    .setOngoing(true)
                    .build();

            manager.notify(9083150, notification2);

        } else {
            //Creating Notification Builder
            notification = new NotificationCompat.Builder(context);
            //Title for Notification
            notification.setContentTitle(context.getResources().getString(R.string.app_name));
            //Message in the Notification
            notification.setContentText("Beacon scanner running...");
            //Alert shown when Notification is received
            notification.setTicker("Beacon scanner running...");
            //Icon to be set on Notification
            notification.setSmallIcon(R.mipmap.ic_launcher);
            notification.setOngoing(true);
            /*nks*/
            //Creating new Stack Builder
            stackBuilder = TaskStackBuilder.create(context);
            /*  stackBuilder.addParentStack(Result.class);*/
            //Intent which is opened when notification is clicked
            resultIntent = new Intent(context, TabbedActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pIntent);
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(9083150, notification.build());

        }


    }

    public static File getPathWorkbook(Context context) {
        File folder2 = null;
        File file = null;
        try {
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder;
            folder = new File(extStorageDirectory, context.getString(R.string.app_name));
            if (!folder.exists()) {
                folder.mkdir();
            }
            folder2 = new File(folder, "Drive Cache");
            if (!folder2.exists()) {
                folder2.mkdir();
            }
            file = new File(folder2, context.getString(R.string.app_name) + ".xls");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String getLimitedCharFromString(String title) {

        int limit = 20;
        String s = title;
        if (title.length() > limit) {
            s = title.substring(0, limit);
            s = s + "...";
        }
        return s;

    }


//date covert

    public static String covertDateAndTimeFormat(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    // date greater than check method.

    public static boolean isToDateafterFromDate(String fromDate, String toDate, String format) {

        boolean isDate = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date _fromDate = sdf.parse(fromDate);
            Date _toDate = sdf.parse(toDate);


            if (_toDate.after(_fromDate)) {
                isDate = true;
            }


        } catch (Exception e) {
            e.getMessage();
        }
        return isDate;
    }


    // get date range between data

    public static boolean getFronDate_toDate_data(String fromDate, String toDate, String actualDate, String format) {

        boolean isDate = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date _fromDate = sdf.parse(fromDate);
            Date _toDate = sdf.parse(toDate);
            Date _actualDate = sdf.parse(actualDate);


            if (_actualDate.after(_fromDate) && _actualDate.before(_toDate)) {
                isDate = true;
            }


        } catch (Exception e) {
            e.getMessage();
        }
        return isDate;
    }


    public static String chageDateFormat(String inputDateFormat, String dateStr, String outputDateFormat) {
        String formattedDate = "";
        try {
            DateFormat fromFormat = new SimpleDateFormat(inputDateFormat);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat(outputDateFormat);
            toFormat.setLenient(false);
            Date date = fromFormat.parse(dateStr);
            formattedDate = toFormat.format(date);
        } catch (ParseException e) {
            e.getMessage();
        }
        return formattedDate;
    }


    public static String convertEpochToDate(Long epochTime) {
        Date date = new Date(epochTime);
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.US);
        return format.format(date);

    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String formatMonth(String month) {
        String mnth = "";
        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
            mnth = monthDisplay.format(monthParse.parse(month));
        } catch (ParseException e) {
            e.getMessage();
        }
        return mnth;
    }

    public static boolean isSpreadsheetListEmpty(Context context) {
        List<SpreadsheetItem> list = new ArrayList<>();
        String strItems = SharedPreferencesUtil.getSpreadsheetList(context);
        if (strItems.equals("")) return true;
        list = convertJSONStringToSpreadsheetListObject(strItems);
        if (list == null || list.isEmpty()) return true;
        else return false;
    }

    public static List<SpreadsheetItem> getSpreadsheetList(Context context) {
        List<SpreadsheetItem> list = new ArrayList<>();
        String strItems = SharedPreferencesUtil.getSpreadsheetList(context);
        if (strItems.equals("")) return list;
        list = convertJSONStringToSpreadsheetListObject(strItems);
        return list;
    }

    public static void addSheetToPreference(SpreadsheetItem item, Context context) {
        String strAllSheets = SharedPreferencesUtil.getSpreadsheetList(context);
        List<SpreadsheetItem> list = convertJSONStringToSpreadsheetListObject(strAllSheets);
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(item);

        } else {
            list.add(item);
        }
        String json = convertSpreadsheetObjectToJSONString(list);
        SharedPreferencesUtil.setSpreadsheetList(context, json);
    }

    public static void checkBluetoothSheetAvailability(Context context, ArrayList<GoogleDriveFileHolder> list) {
        for (GoogleDriveFileHolder googleDriveFileHolder : list) {
            if (googleDriveFileHolder.getName().equals(Constants.BLUETOOTH_SHEET)) {
                if (SharedPreferencesUtil.getDefaultBluetoothSheet(context).equals("")) {
                    SharedPreferencesUtil.setDefaultBluetoothSheet(context, String.valueOf(googleDriveFileHolder.getId()));
                }
                break;
            }
        }
    }

    public static String getCurrentTimestamp() {
        //  Long tsLong = System.currentTimeMillis()/1000;
        //   return tsLong.toString();


       /* final GregorianCalendar startDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        startDate.clear();
        startDate.set(1904, Calendar.JANUARY, 1);
        final long startMillis = startDate.getTimeInMillis();
      return  startMillis+"";*/


        long seconds = 0;
        long milliSeconds = 0;
        String toyBornTime = "1904-01-01 12:00:00.000 am";
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss.SSS aa");

        try {

            Date oldDate = dateFormat.parse(toyBornTime);
            Date currentDate = new Date();

            long diff = currentDate.getTime() - oldDate.getTime();
            seconds = diff / 1000;
            milliSeconds = diff;
            //  long minutes = seconds / 60;
            //  long hours = minutes / 60;
            //   long days = hours / 24;

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return String.valueOf(milliSeconds);

    }

    /* public static boolean QRAlreadyScanned(List<ScannedData> list, String QRData) {
         for (ScannedData scannedData : list) {
             if (scannedData.getQr_text().equals(QRData)) return true;
         }
         return false;
     }*/
    public static boolean QRAlreadyScanned(List<Task_data> AsanaTaskDataList, String QRData) {
        for (Task_data scannedData : AsanaTaskDataList) {
            if (scannedData.getQrText().equals(QRData)) return true;
        }
        return false;
    }

    public static Timestamp getCurrentTimestamp1() {
        Calendar calender = Calendar.getInstance();
        java.util.Date d = calender.getTime();
        return new Timestamp(d.getTime());
    }

    public static Timestamp getTime(long time) {
        if (time == 0 || time < 0) {
            return getCurrentTimestamp1();
        }
        return new Timestamp(time);
    }


    public static String convertSpreadsheetObjectToJSONString(List<SpreadsheetItem> list) {
        String json = "";
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(list);
        } catch (Exception e) {

        }
        return json;
    }

    public static List<SpreadsheetItem> convertJSONStringToSpreadsheetListObject(String json) {
        List<SpreadsheetItem> myObjects = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            myObjects = mapper.readValue(json, new TypeReference<List<SpreadsheetItem>>() {
            });

        } catch (Exception e) {
            e.getMessage();
        }
        return myObjects;
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        // System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static File getOutputMediaFolder() {
        File folder2 = null;
        try {
            String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
            File folder = new File(extStorageDirectory, "Multitracker");
            if (!folder.exists()) {
                folder.mkdir();
            }
            folder2 = new File(folder, "QR Scanner");
            if (!folder2.exists()) {
                folder2.mkdir();
            }


            File nomedia = new File(folder2.getPath() + "/" + ".Nomedia");
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return folder2;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder2;
    }

    public static File getOutputMediaFolderForSpreadsheet() {
        File folder2 = null;
        try {
            String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
            File folder = new File(extStorageDirectory, "Multitracker");
            if (!folder.exists()) {
                folder.mkdir();
            }
            folder2 = new File(folder, "Spreadsheet");
            if (!folder2.exists()) {
                folder2.mkdir();
            }


            File nomedia = new File(folder2.getPath() + "/" + ".Nomedia");
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return folder2;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder2;
    }

    public static File getOutputMediaFilePath() {
        File folder2 = getOutputMediaFolder();
        try {
            String timeStamp = getCurrentTimestamp();
            File mediaFile;
            String mImageName = "QR_" + timeStamp + ".jpg";
            mediaFile = new File(folder2.getPath() + File.separator + mImageName);
            return mediaFile;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder2;
    }

    public static File writeStreamToFile(InputStream input, File file) {
        try {
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File getExcelOutputMediaFilePath() {
        File folder2 = getOutputMediaFolderForSpreadsheet();
        try {
            File mediaFile;
            mediaFile = new File(folder2.getPath() + File.separator + "sample7392.xlsx");
            if (mediaFile.exists()) {
                boolean deleted = mediaFile.delete();
            }
            mediaFile.createNewFile();
            return mediaFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder2;
    }

    private File getOutputMediaFile(Context context) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    public static String saveImage(Bitmap image) {
        // File pictureFile = getOutputMediaFile();
        File pictureFile = getOutputMediaFilePath();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return pictureFile.getAbsolutePath();
    }

    public static void deleteQRScannerFiles() {


        try {
            File folder1 = getOutputMediaFolder();

            if (folder1.isDirectory()) {
                String[] children = folder1.list();
                for (int i = 0; i < children.length; i++) {
                    File file = new File(folder1, children[i]);
                    Date lastModDate = new Date(file.lastModified());
                    long daysOld = calculateDays(lastModDate);
                    if (daysOld >= 10) {
                        boolean deleted = file.delete();
                    }

                    String s = "";
                }
            }

            File folder2 = getPathDriveTempFiles();

            if (folder2.isDirectory()) {
                String[] children = folder2.list();
                for (int i = 0; i < children.length; i++) {
                    File file = new File(folder2, children[i]);
                    boolean deleted = file.delete();
                    String s = "";
                }
            }

        } catch (Exception e) {
        }
    }


    public static long calculateDays(Date created) {
        Date currentDate = new Date();
        long difference = Math.abs(currentDate.getTime() - created.getTime());
        long days = difference / (24 * 60 * 60 * 1000);
        return days;
    }

    public static void ReportNonFatalError(String Title, String Detail) {
        FirebaseCrashlytics.getInstance().recordException(new RuntimeException(Title + "---> " + Detail));
    }

    public static void logFeatureSelectedEvent(Context context, String Title, String Detail) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("Event_Name", Title);
        bundle.putString("Event_Desc", Detail);
        mFirebaseAnalytics.logEvent("CustomEvent", bundle);

    }

    public static void killAppFromBg() {
        android.os.Process.killProcess(android.os.Process.myPid()); // stopping timertask
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

/*    public static List<Task_data> addData(String qr_text, List<Task_data> qrDataList) {
        if (qrDataList.size() == 0) qrDataList.add(new Task_data("", qr_text, "", "", "", "","","",""));
        for (int i = 0; i < qrDataList.size(); i++) {
            if (qrDataList.get(i).getQrText().equals(qr_text)) {
                break;
            } else if (i == qrDataList.size() - 1) {
                qrDataList.add(new Task_data("", qr_text, "", "", "", "","","",""));
            }
        }
        return qrDataList;
    }*/


  /*  public static void addTimestampForMatching(String QR_Text,String timestamp,){

    }*/

    public static boolean AllQRMatchedWithTask(List<Task_data> AsanaTaskDataList) {
        for (Task_data task_data : AsanaTaskDataList) {
            if (task_data.getTaskId().equals(""))
                return false;
        }
        return true;
    }

    public static String getNonMatchedQR(List<Task_data> AsanaTaskDataList) {
        String nonMatched = "";
        for (Task_data task_data : AsanaTaskDataList) {
            if (task_data.getTaskId().equals(""))
                nonMatched = nonMatched + task_data.getQrText() + ",";
        }
        return nonMatched;
    }

    public static String getMimeType(Uri uri, Context context) {
        String mimeType = null;
        try {

            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = context.getApplicationContext().getContentResolver();
                mimeType = cr.getType(uri);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                        .toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        fileExtension.toLowerCase());
            }
        } catch (Exception e) {

        }


        return mimeType;
    }

    public static String getMimeTypeFile(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void show_preview(Context context, Drawable drawable) {
        final Dialog showd = new Dialog(context, R.style.Theme_Dialog);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_screenshot);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(true);
        showd.setCanceledOnTouchOutside(true);
        ImageView imgvw_screenshot = showd.findViewById(R.id.imgvw_screenshot);
        ImageView close = showd.findViewById(R.id.close);
        imgvw_screenshot.setImageDrawable(drawable);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showd.dismiss();
            }
        });
        showd.show();
    }

    public static List<BlackListedBeacon> convertJSONStringBeaconsList(String json) {
        List<BlackListedBeacon> data = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<BlackListedBeacon> beacons = mapper.readValue(json, new TypeReference<List<BlackListedBeacon>>() {
            });
            data.addAll(beacons);
        } catch (Exception e) {
            e.getMessage();
        }


        return data;
    }

    public static String convertBeaconListToJSONString(List<BlackListedBeacon> beaconList) {
        String json = "";
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(beaconList);
        } catch (Exception e) {

        }
        return json;
    }

    public static String getCellByIndex(int columnNum, int rowNum) {
        int col = columnNum + 65;
        char column = (char) col;
        rowNum = rowNum + 1;
        String val = column + "" + rowNum;
        return val;
    }


    public static List<List<Object>> convertToSheetCompatibleList(List<Beacon> beaconList) {
        List<List<Object>> BluetoothList = new ArrayList<>();
        for (int i = 0; i < beaconList.size(); i++) {
            List<Object> list1 = new ArrayList<>();
            Beacon beacon = beaconList.get(i);
            list1.add(beacon.getDateTime());
            list1.add(beacon.getName());
            list1.add(beacon.getTimestamp());
            list1.add(beacon.getRSSI());
            list1.add(beacon.getUUID());
            list1.add(beacon.getMajor());
            list1.add(beacon.getMinor());
            list1.add(beacon.getBattery());
            BluetoothList.add(list1);
        }
        return BluetoothList;
    }

    public static String getNearAnchorURL(List<TaskData> AsanaTaskDataList) {
        String barcode = "";
        for (TaskData taskData : AsanaTaskDataList) {
            if (taskData.isAnchor()) {  // if this is an Anchor
                barcode = taskData.getBarcode();
                barcode = "http://qlv.me/" + barcode;
                break;
            }
        }
        return barcode;
    }
    public static String getNearAnchorURL_v1(TaskData taskData) {
               String barcode = "";
                barcode = taskData.getBarcode();
                barcode = "http://qlv.me/" + barcode;

        return barcode;
    }
    public static String getNearAnchorURL_v2(String barcode) {
        String nearAnchorURL = "";

        nearAnchorURL = "http://qlv.me/" + barcode;

        return nearAnchorURL;
    }

    public static String getLastScannedAnchorTaskID(List<TaskData> AsanaTaskDataList) {
       String lastScannedAnchorGID="";
        for (TaskData taskData : AsanaTaskDataList) {
            if (taskData.isAnchor()) {  // if this is an Anchor
                lastScannedAnchorGID = taskData.getTaskId();
            }
        }
        return lastScannedAnchorGID;
    }

    public static int getRecentlyScannedAnchorPosition(List<TaskData> AsanaTaskDataList, int taskPosition) {
       int recentScannedAnchorPosition=-1;
        String recentScannedAnchorGID="";
        for (int i=0;i<taskPosition;i++) {
            TaskData taskData =AsanaTaskDataList.get(i);
            if (taskData.isAnchor()) {  // if this is an Anchor
                recentScannedAnchorGID = taskData.getTaskId();
                recentScannedAnchorPosition=i;
            }
        }
        return recentScannedAnchorPosition;
    }


    public static JsonObject getJSONForUpdatingNormalTask(String beacon1_gid, String beacon1_RSSI_gid, String UUID, int RSSI, String nearAnchor_gid, String nearAnchorURL) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(beacon1_gid, UUID);
            obj.addProperty(beacon1_RSSI_gid, RSSI);
            obj.addProperty(nearAnchor_gid, nearAnchorURL);
            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }

    public static JsonObject getJSONForUpdatingAnchorTask(String beacon1_gid, String beacon1_RSSI_gid, String UUID, int RSSI) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(beacon1_gid, UUID);
            obj.addProperty(beacon1_RSSI_gid, RSSI);

            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }


    public static JsonObject getJSONForUpdatingAnchorTask_v1(String beacon1_gid, String beacon1_RSSI_gid, String UUID, int RSSI, String nearAnchor_gid, String nearAnchorURL) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(beacon1_gid, UUID);
            obj.addProperty(beacon1_RSSI_gid, RSSI);
            obj.addProperty(nearAnchor_gid, nearAnchorURL);
            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }
    public static JsonObject getJSONForUpdatingOrdinaryTask_v2(String beacon1_gid, String beacon1_RSSI_gid, String UUID, int RSSI, String nearAnchor_gid, String nearAnchorURL) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(beacon1_gid, UUID);
            obj.addProperty(beacon1_RSSI_gid, RSSI);
            obj.addProperty(nearAnchor_gid, nearAnchorURL);
            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }
    public static JsonObject getJSONForUpdatingNormalTask_v1(String beacon1_gid, String beacon1_RSSI_gid, String UUID, int RSSI) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(beacon1_gid, UUID);
            obj.addProperty(beacon1_RSSI_gid, RSSI);

            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }

    public static JsonObject getJSONForUpdatingAnchorTask_v2(String beacon1_gid, String beacon1_RSSI_gid, String UUID, int RSSI) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(beacon1_gid, UUID);
            obj.addProperty(beacon1_RSSI_gid, RSSI);

            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI", uri + "");
        String result = uri + "";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length - 1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
            } else if ("audio".equals(type)) {
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


/*    public static void main(String[] srgs) throws IOException {
        // String s=    UrlCleaner.unshortenUrl("http://bit.ly/1pwuGdF");


        String s = unshortenUrl("http://bit.ly/1pwuGdF");
        System.out.println(s);
    }*/


  /*  public static String unshortenUrl(final String shortUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(shortUrl).openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("HEAD");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setReadTimeout(4000);
        int responseCode = connection.getResponseCode();
        String url = connection.getHeaderField("Location");
        if (responseCode / 100 == 3 && url != null) {
            String expandedUrl = unshortenUrl(new URL(new URL(shortUrl), url).toString());
            if (Objects.equals(expandedUrl, url))
                return url;
            else {
                return expandedUrl;
            }
        }
        return shortUrl;
    }*/


}


