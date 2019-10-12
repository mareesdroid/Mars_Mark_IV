package app.marees.mars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import app.marees.mars.Singletons.Myapp;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

    ConstraintLayout myconstraint;
    AnimationDrawable marsAnim;
    ImageView newss,astros,translates,cloud,movie;
    final boolean[] doubleBackToExitPressedOnce = {false};
    ProgressDialog dialog;
    ImageView tr;



    //////////////////////for image zoom
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        myconstraint = findViewById(R.id.constraint);
        newss = findViewById(R.id.imageView5);
        astros = findViewById(R.id.astro);
        cloud = findViewById(R.id.imageView);
        movie = findViewById(R.id.imageView3);
        new ConnectNews().execute("https://tamil.samayam.com/");
        dialog = new ProgressDialog(SecondActivity.this);
        tr=findViewById(R.id.dailyCalendar);
       getTodaydate();
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(SecondActivity.this,Cloud.class);
                startActivity(n);
            }
        });

        newss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> l = new ArrayList<>();
                ArrayList<String> q = new ArrayList<>();
                ArrayList<String> w = new ArrayList<>();
                ArrayList<String> e = new ArrayList<>();
                l= Myapp.getLatest();
                q=Myapp.getOthers();
                w = Myapp.getLife();
                e = Myapp.getTrends();

                getTodaydate();
                new ConnectNews().execute("https://tamil.samayam.com/");
                if(l.size()>0&&q.size()>0&&w.size()>0&&e.size()>0){
                    Intent d = new Intent(SecondActivity.this,News.class);
                    startActivity(d);
                }
                else{
                    if (doubleBackToExitPressedOnce[0]) {

                        Intent d = new Intent(SecondActivity.this,News.class);
                        startActivity(d);

                    }
                    doubleBackToExitPressedOnce[0] =true;
                    Toast.makeText(getApplicationContext(),"Please wait News is still not loading....Do other work",Toast.LENGTH_LONG).show();                }



                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce[0] =false;
                    }
                }, 200);
            }
        });
        astros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hello = new Intent(SecondActivity.this,Astro.class);
                startActivity(hello);
            }
        });
        translates = findViewById(R.id.translate);
        marsAnim = (AnimationDrawable) myconstraint.getBackground();
        marsAnim.setEnterFadeDuration(2000);
        marsAnim.setExitFadeDuration(4000);
        marsAnim.start();
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this,Movies.class);
                startActivity(i);
            }
        });
        tr.setOnTouchListener(this);
    }

    private void getTodaydate() {

        dialog.setMessage("Finding Today date....");
        dialog.show();
        new TodaySync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://www.tamildailycalendar.com/tamil_daily_calendar.php");

    }






    @Override
    public void onClick(View v) {
        switch(v.getId()){


        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }
    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }

    class TodaySync extends AsyncTask<String,Integer,String> {

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

            Picasso.get().load(s).into(tr);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.setMessage("Date Found Generating calendar....");
                }
            });

            Element latestNewss = document.select(".col-sm-12 img").first();
            String Imagesrc = latestNewss.absUrl("src");


            return Imagesrc;
        }


    }

}
