package app.marees.mars;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TammovieAdapter extends RecyclerView.Adapter<TammovieAdapter.myHolder> {


    ArrayList<String> str = new ArrayList<>();
    Context cntxt;


    public TammovieAdapter(ArrayList<String> mylist, Context applicationContext) {

        this.cntxt = applicationContext;
        this.str = mylist;

    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list,viewGroup,false);

        return new myHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {

        int i2= i+1;
        myHolder.counts.setText("Movie"+i2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myHolder.names.setText(Html.fromHtml(str.get(i), Html.FROM_HTML_MODE_COMPACT));
        } else {
            myHolder.names.setText(Html.fromHtml(str.get(i)));
        }

    }

    @Override
    public int getItemCount() {
        return str.size();
    }

    class myHolder extends RecyclerView.ViewHolder{
TextView counts ,names;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            counts = itemView.findViewById(R.id.textView49);
            names = itemView.findViewById(R.id.nameList);
        }
    }
}
