package app.marees.mars.NewsFrag;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import app.marees.NewsAdapter;
import app.marees.mars.R;
import app.marees.mars.Singletons.Myapp;


public class Latest extends Fragment {

    RecyclerView recycler;
   ArrayList<String> late = new ArrayList<>();
    ArrayList<String> late2 = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View myNewsView = inflater.inflate(R.layout.news_one,container,false);

        recycler= myNewsView.findViewById(R.id.trends);
        LinearLayoutManager myLine = new LinearLayoutManager(Myapp.getAppContext());
        myLine.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(myLine);


        late = Myapp.getLatest();
        late2 = Myapp.getLatestu();
        if(late.size()>0){

            NewsAdapter l = new NewsAdapter(late,late2,getActivity());
            recycler.setAdapter(l);

        }

        return myNewsView;

    }


}
