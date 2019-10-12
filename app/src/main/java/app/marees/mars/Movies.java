package app.marees.mars;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Movies extends AppCompatActivity {

    ArrayList<String> mylist = new ArrayList<>();
    RecyclerView mycycle;
    LinearLayout l;
    TextView tamill;
    ConstraintLayout sources,language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie);
        mycycle = findViewById(R.id.cycle);
        tamill = findViewById(R.id.tamill);
        l = findViewById(R.id.myline);
        language = findViewById(R.id.language);
        language.setOnClickListener(v -> {
            language.setVisibility(View.GONE);
            new connect().execute();

        });
        tamill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.setVisibility(View.GONE);
                new connect().execute();
            }
        });
        tamill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        sources = findViewById(R.id.sources);

        LinearLayoutManager line = new LinearLayoutManager(getApplicationContext());
        line.setOrientation(LinearLayoutManager.VERTICAL);
        mycycle.setLayoutManager(line);

    }



    class connect extends AsyncTask<Void,Void,Void> {





        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://hdmoviesda.me/";
            Document document = null;
            try {
                document = Jsoup.connect(url).get();




            Elements movieLatest = document.select("b"); //Get price




            for (Element answerer : movieLatest) {
                System.out.println("Answerer: " + answerer.text());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                      mylist.add(String.valueOf(answerer));


                    }
                });

            }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TammovieAdapter mv = new TammovieAdapter(mylist,getApplicationContext());
                        mycycle.setAdapter(mv);
                        l.setVisibility(View.VISIBLE);
                        sources.setVisibility(View.VISIBLE);

                    }
                });

//                sources.setVisibility(View.VISIBLE);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }


}