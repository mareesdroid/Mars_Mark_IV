package app.marees.mars.Singletons;

import android.app.Activity;
import android.app.Application;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;


import androidx.lifecycle.ViewModel;

import org.json.JSONArray;

import java.util.ArrayList;

import app.marees.mars.Utilites.AccessHelper;
import app.marees.mars.room.MyViewModel;


public class Myapp extends Application {


    private int result;
    private Intent intent;
    private static MediaProjectionManager mMediaProjectionManager;
    private static MediaProjection mediaProjection;
    static boolean first = true;
    private static Handler mhandler;
    private static JobParameters myjob;
    private static String Command="";
    private static JSONArray myjs;
    private static View myView;
    private static VirtualDisplay myDisplay = null;
    private static ArrayList<String> latest=new ArrayList<>();
    private static ArrayList<String> others=new ArrayList<>();
    private static ArrayList<String> trends=new ArrayList<>();
    private static ArrayList<String> tech=new ArrayList<>();
    private static ArrayList<String> astro=new ArrayList<>();
    private static ArrayList<String> study=new ArrayList<>();
    private static ArrayList<String> kovil=new ArrayList<>();
    private static ArrayList<String> health=new ArrayList<>();
    private static ArrayList<String> tour=new ArrayList<>();
    private static ArrayList<String> recipe=new ArrayList<>();
    private static ArrayList<String> cinema=new ArrayList<>();
    private static ArrayList<String> sports=new ArrayList<>();
    private static ArrayList<String> life=new ArrayList<>();
    private static ArrayList<String> mukiyaNews=new ArrayList<>();
    private static ArrayList<String> latestu=new ArrayList<>();
    private static ArrayList<String> othersu=new ArrayList<>();
    private static ArrayList<String> trendsu=new ArrayList<>();
    private static ArrayList<String> techu=new ArrayList<>();
    private static ArrayList<String> astrou=new ArrayList<>();
    private static ArrayList<String> studyu=new ArrayList<>();
    private static ArrayList<String> kovilu=new ArrayList<>();
    private static ArrayList<String> healthu=new ArrayList<>();
    private static ArrayList<String> touru=new ArrayList<>();
    private static ArrayList<String> recipeu=new ArrayList<>();
    private static ArrayList<String> cinemau=new ArrayList<>();
    private static ArrayList<String> sportsu=new ArrayList<>();
    private static ArrayList<String> lifeu=new ArrayList<>();
    private static ArrayList<String> mukiyaNewsu=new ArrayList<>();
    private static boolean isSent = true;
    private static MyViewModel marsModel = null;
    private static MyViewModel connectModel = null;
    public static void setLatest(ArrayList json){

        latest = json;

    }

    public static ArrayList getLatest(){

        return latest;

    }
    public static void setOthers(ArrayList json){

        others = json;

    }



    public static ArrayList getOthers(){

        return others;

    }
    public static void setCommand(String command){

        Command =command;

    }

    public static String getCommand(){

        return Command;

    }

    public static void setTrends(ArrayList json){

        trends = json;

    }

    public static ArrayList getTrends(){

        return trends;

    } public static void setTech(ArrayList json){

        tech = json;

    }

    public static ArrayList getTech(){

        return tech;

    }

    public static void setAstro(ArrayList json){

        astro = json;

    }

    public static ArrayList getAstro(){

        return astro;

    }
    public static void setKovil(ArrayList json){

        kovil = json;

    }

    public static ArrayList getKovil(){

        return kovil;

    }
    public static void setStudy(ArrayList json){

        study = json;

    }

    public static ArrayList getStudy(){

        return study;

    }
    public static void setHealth(ArrayList json){

        health = json;

    }

    public static ArrayList getHealth(){

        return health;

    }
    public static void setTour(ArrayList json){

        tour = json;

    }

    public static ArrayList getTour(){

        return tour;

    }
    public static void setRecipe(ArrayList json){

        recipe = json;

    }

    public static ArrayList getRecipe(){

        return recipe;

    }

    public static void setCinema(ArrayList json){

        cinema = json;

    }

    public static ArrayList getCinema(){

        return cinema;

    }
    public static void setMukiyaNews(ArrayList json){

        mukiyaNews = json;

    }

    public static ArrayList getMukiyanews(){

        return mukiyaNews;

    }
    public static void setSports(ArrayList json){

        sports = json;

    }

    public static ArrayList getSports(){

        return sports;

    }
    public static void setLife(ArrayList json){

        life = json;

    }

    public static ArrayList getLife(){

        return life;

    }



    public static void setLatestu(ArrayList json){

        latestu = json;

    }

    public static ArrayList getLatestu(){

        return latestu;

    }
    public static void setOthersu(ArrayList json){

        othersu = json;

    }

    public static ArrayList getOthersu(){

        return othersu;

    } public static void setTrendsu(ArrayList json){

        trendsu = json;

    }

    public static ArrayList getTrendsu(){

        return trendsu;

    } public static void setTechu(ArrayList json){

        techu = json;

    }

    public static ArrayList getTechu(){

        return techu;

    }

    public static void setAstrou(ArrayList json){

        astrou = json;

    }

    public static ArrayList getAstrou(){

        return astrou;

    }
    public static void setKovilu(ArrayList json){

        kovilu = json;

    }

    public static ArrayList getKovilu(){

        return kovilu;

    }
    public static void setStudyu(ArrayList json){

        studyu = json;

    }

    public static ArrayList getStudyu(){

        return studyu;

    }
    public static void setHealthu(ArrayList json){

        healthu = json;

    }

    public static ArrayList getHealthu(){

        return healthu;

    }
    public static void setTouru(ArrayList json){

        touru = json;

    }

    public static ArrayList getTouru(){

        return touru;

    }
    public static void setRecipeu(ArrayList json){

        recipeu = json;

    }

    public static ArrayList getRecipeu(){

        return recipeu;

    }

    public static void setCinemau(ArrayList json){

        cinemau = json;

    }

    public static ArrayList getCinemau(){

        return cinemau;

    }
    public static void setMukiyaNewsu(ArrayList json){

        mukiyaNewsu = json;

    }

    public static ArrayList getMukiyanewsu(){

        return mukiyaNewsu;

    }
    public static void setSportsu(ArrayList json){

        sportsu = json;

    }

    public static ArrayList getSportsu(){

        return sportsu;

    }
    public static void setLifeu(ArrayList json){

        lifeu = json;

    }

    public static ArrayList getLifeu(){

        return lifeu;

    }


    private static Context mContext;

    public static void setMyView(View view) {
        myView = view;
    }

    public static View getMyView() {
        return myView;
    }

    public static void setVirtualDisplay(VirtualDisplay mVirtualDisplay) {
      myDisplay = mVirtualDisplay;
    }

    public static VirtualDisplay getVirtualDisplay() {
        return myDisplay;
    }

    public static boolean getIsSent() {
        return isSent;
    }

    public static void setisSent(boolean b) {
        isSent = b;
    }

    public static void setMyViewModel(MyViewModel mars) {

        marsModel = mars;

    }

    public static MyViewModel getMyViewModel() {
        return marsModel;
    }

    public static MyViewModel getMyViewModel2() {
        return connectModel;
    }
    public static void setMyViewModel2(MyViewModel mars) {

        connectModel = mars;

    }
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

    }

    public static Context getAppContext() {
        return mContext;
    }

    public int getResult() {
        return result;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setResult(int result1) {
        this.result = result1;
    }

    public void setIntent(Intent intent1) {
        this.intent = intent1;
    }


    public static MediaProjectionManager getMediaProjectionManager() {
        return mMediaProjectionManager;
    }


    public void setMediaProjectionManager(MediaProjectionManager mMediaProjectionManager) {
        this.mMediaProjectionManager = mMediaProjectionManager;
    }


    private static Intent screenshotPermission = null;
    private static MediaProjectionManager mediaProjectionManager;

    public static MediaProjection getScreenshotPermission() {
        try {
            if (hasScreenshotPermission()) {
                if (null != mediaProjection) {
                    mediaProjection.stop();
                    mediaProjection = null;
                }
                mediaProjectionManager = Myapp.getMediaProjectionManager();
                mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, (Intent) screenshotPermission.clone());
            } else {
                openScreenshotPermissionRequester();
            }
        } catch (final RuntimeException ignored) {
            openScreenshotPermissionRequester();
        }
        return mediaProjection;
    }

    private static boolean hasScreenshotPermission() {

        if (first) {
            first = false;
            return false;
        } else {
            return true;
        }

    }

    protected static void openScreenshotPermissionRequester() {
        final Intent intent = new Intent(Myapp.getAppContext(), AccessHelper.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Myapp.getAppContext().startActivity(intent);
    }


    public static void setScreenshotPermission(final Intent permissionIntent) {
        screenshotPermission = permissionIntent;
    }

    public static Handler getHandler() {
        if (mhandler != null) {
            Log.e("",mhandler+"");
            return mhandler;
        } else {
            // start capture handling thread
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    mhandler = new Handler();
                    Log.e("",mhandler+"");
                    Looper.loop();
                }
            }.start();
            return mhandler;
        }

    }


    public static void setHandler(Handler handler){

        mhandler = handler;
    }

    public static JobParameters getMyjob(){
        return myjob;
    }

    public static void setMyjob(JobParameters job){

        myjob = job;
    }


    public static void setMyJS(JSONArray json){

        myjs = json;

    }

    public static JSONArray getMyjs(){

        return myjs;

    }



}
