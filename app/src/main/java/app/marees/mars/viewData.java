package app.marees.mars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import app.marees.mars.Singletons.Singleton;

public class viewData extends AppCompatActivity{
    String zodiac;
    RequestQueue mqueue;
    String time,zodTam;
    TextView status,date,tamilzodi,zodi;
    LinearLayout myconstraint;
    AnimationDrawable marsAnim;
    TextView wk,day,mnth,year;
    String url="";
    private final String TODAY="இன்றைய ராசி பலன்";
    private final String WEEK="வார ராசி பலன்";
    private final String MONTH="மாத ராசி பலன்";
    private final String YEAR="வருட ராசி பலன்";
    String params ="http://www.tamildailycalendar.com/tamil_rasi_palan_weekly.php?msg=Tamil%20Rasi%20Palan%20Weekly&&rasi=MITHUNAM";
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_layout);

         dialog = new ProgressDialog(viewData.this);
        status = findViewById(R.id.textView38);
        tamilzodi= findViewById(R.id.zodiac);
        myconstraint = findViewById(R.id.constraintx);
        marsAnim = (AnimationDrawable) myconstraint.getBackground();
        marsAnim.setEnterFadeDuration(2000);
        marsAnim.setExitFadeDuration(4000);
        marsAnim.start();


        zodi = findViewById(R.id.content);
        wk=findViewById(R.id.button8);
        mnth=findViewById(R.id.button10);
        day=findViewById(R.id.button9);
        year = findViewById(R.id.button11);
        wk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText(WEEK);
              url="http://www.tamildailycalendar.com/tamil_rasi_palan_weekly.php?msg=Tamil%20Rasi%20Palan%20Weekly&&rasi="+zodiac;
              changeBackground(false,true,false,false);
              //  findRasi(zodiac,time,zodTam);
                new AstroSync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
                dialog.setMessage("Processing...");
               dialog.show();
            }
        });

        mnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="http://www.tamildailycalendar.com/tamil_rasi_palan_monthly.php?msg=Tamil%20Rasi%20Palan%20Monthly&&rasi="+zodiac;
                status.setText(MONTH);
                //   findRasi(zodiac,time,zodTam);
                changeBackground(false,false,true,false);
                new AstroSync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
                dialog.setMessage("Processing...");
                dialog.show();
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackground(true,false,false,false);
                url="http://www.tamildailycalendar.com/tamil_rasi_palan_daily.php?msg=Tamil%20Rasi%20Palan%20Daily&&rasi="+zodiac;
                status.setText(TODAY);
                // findRasi(zodiac,time,zodTam);
                new AstroSync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
                dialog.setMessage("Processing...");
                dialog.show();


            }
        });

        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackground(false,false,false,true);
                url="http://www.tamildailycalendar.com/tamil_rasi_palan_yearly.php?msg=Tamil%20Rasi%20Palan%20Yearly&&rasi="+zodiac;
                status.setText(YEAR);
                //   findRasi(zodiac,time,zodTam);
                new AstroSync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
                dialog.setMessage("Processing...");
                dialog.show();
            }
        });






        mqueue = Singleton.getInstance(viewData.this).getRequestQueue();
        Intent goIntent = getIntent();
        zodiac = goIntent.getStringExtra("zodiac");
        zodTam = goIntent.getStringExtra("zoditam");
       // findRasi(zodiac,time,zodTam);
        findDay();
    }

    private void changeBackground(boolean b, boolean b1, boolean b2, boolean b3) {


        if(b){

            day.setBackgroundColor(Color.parseColor("#f1edb9"));
            day.setTextColor(Color.parseColor("#173045"));
        }
        else{
            day.setBackgroundColor(Color.parseColor("#263238"));
            day.setTextColor(Color.WHITE);
        }
        if(b1){
            wk.setBackgroundColor(Color.parseColor("#f1edb9"));
            wk.setTextColor(Color.parseColor("#173045"));
        }
        else{
            wk.setBackgroundColor(Color.parseColor("#263238"));
            wk.setTextColor(Color.WHITE);
        }
        if(b2){
            mnth.setBackgroundColor(Color.parseColor("#f1edb9"));
            mnth.setTextColor(Color.parseColor("#173045"));
        }
        else{
            mnth.setBackgroundColor(Color.parseColor("#263238"));
            mnth.setTextColor(Color.WHITE);
        }
        if(b3){
            year.setBackgroundColor(Color.parseColor("#f1edb9"));
            year.setTextColor(Color.parseColor("#173045"));
        }
        else{
            year.setBackgroundColor(Color.parseColor("#263238"));
            year.setTextColor(Color.WHITE);
        }

    }

    private void findDay() {
        url="http://www.tamildailycalendar.com/tamil_rasi_palan_daily.php?msg=Tamil%20Rasi%20Palan%20Daily&&rasi="+zodiac;
        status.setText(TODAY);
        changeBackground(true,false,false,false);
       // tamilzodi.setText();
        // findRasi(zodiac,time,zodTam);
        new AstroSync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        dialog.setMessage("Processing...");
        dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }






    class AstroSync extends AsyncTask<String,Integer,String> {

        String myText="App work aala...so Please Contact the developer";
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            tamilzodi.setText(zodTam);

            zodi.setText(s);
            dialog.dismiss();
            super.onPostExecute(s);
        }



        @Override
        protected String doInBackground(String... strings) {
            String t = strings[0];
            Document document = null;
            try {
                document = Jsoup.connect(t).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements latestNewss = document.select(".panel-body font");
            for(Element tech2:latestNewss){

                String url = tech2.toString();
                myText= Jsoup.parse(url).text();
                Log.e("",url);


            }

            return myText;
        }


    }

}


