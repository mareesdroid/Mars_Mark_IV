package app.marees.mars;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import app.marees.mars.Singletons.Myapp;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyviewHolder> {


    Context mycontext;
   JSONArray devices = new JSONArray();




    public DeviceAdapter(Context appContext, JSONArray js) {


        this.mycontext = appContext;
        this.devices = js;

    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle,viewGroup,false);
        return new MyviewHolder(myView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int i) {


        int finalI = i;
        myviewHolder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myviewHolder.copy_text.setText(devices.getString(finalI));
                    myviewHolder.copy_text.setVisibility(View.VISIBLE);
                    myviewHolder.copy.setVisibility(View.GONE);
                    Toast.makeText(Myapp.getAppContext(),"Ok copy your code",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            int i2 = i+1;
            myviewHolder.mytxt.setText("Device "+i2);
            myviewHolder.id.setText(devices.getString(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return devices.length();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView mytxt;
        TextView id;
        Button copy;
        EditText copy_text;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            mytxt = itemView.findViewById(R.id.textView43);
            id = itemView.findViewById(R.id.textView44);
            copy_text = itemView.findViewById(R.id.copy_txt);
            copy = itemView.findViewById(R.id.button12);

        }
    }
}
