package app.marees;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.marees.mars.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.myHolder> {

    ArrayList<String> newslists = new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();
    Activity myact;




    public NewsAdapter(ArrayList<String> news, ArrayList<String> urlList, FragmentActivity activity) {
        this.newslists = news;
        this.urlList = urlList;
        this.myact = activity;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View myview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newscycle,viewGroup,false);


        return new myHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {

        myHolder.news.setText(newslists.get(i));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myHolder.news.setText(Html.fromHtml(newslists.get(i), Html.FROM_HTML_MODE_COMPACT));

        } else {
            myHolder.news.setText(Html.fromHtml(newslists.get(i)));
        }
        Linkify.addLinks( myHolder.news, Linkify.WEB_URLS);

        myHolder.news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent n = new Intent(myact, WebPage.class);
                n.putExtra("STRING_I_NEED", urlList.get(i));
               myact.startActivity(n);

            }
        });
    }

    @Override
    public int getItemCount() {
        return newslists.size();
    }

    class myHolder extends RecyclerView.ViewHolder{

        TextView news;


        public myHolder(@NonNull View itemView) {
            super(itemView);
            news = itemView.findViewById(R.id.newList);

        }
    }
}
