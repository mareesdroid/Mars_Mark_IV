package app.marees.mars;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;

import java.util.ArrayList;

import app.marees.mars.Singletons.Myapp;


public class DatabaseFrag extends Fragment {

    EditText pass;
    Button verify;
    String password;
    ConstraintLayout secure,back_view;
    RecyclerView cycle;
    DatabaseReference mydatabase;
    CardView myCard;
    ArrayList<String> DeviceList = new ArrayList<>();
    JSONArray js = new JSONArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.database, container, false);

        pass= rootView.findViewById(R.id.editText4);
        verify = rootView.findViewById(R.id.button11);
        secure = rootView.findViewById(R.id.secure);
        cycle = rootView.findViewById(R.id.cycle2);
        myCard = rootView.findViewById(R.id.mycard);

        LinearLayoutManager mylays=new LinearLayoutManager(Myapp.getAppContext()); /////////dummy(Model) layout
        mylays.setOrientation(LinearLayoutManager.VERTICAL);
        cycle.setLayoutManager(mylays);                                 /////set dummy layout to main layout
        back_view = rootView.findViewById(R.id.back_view);
        mydatabase = FirebaseDatabase.getInstance().getReference();

//        ///for future use
//        DatabaseReference cities = mydatabase.child("cities");
//

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = pass.getText().toString();
                if(password.equals("asdf")){

                    back_view.setVisibility(View.GONE);
                    cycle.setVisibility(View.VISIBLE);
                    myCard.setVisibility(View.VISIBLE);


                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Vibrator vibe = (Vibrator) Myapp.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        Vibrator vibe = (Vibrator) Myapp.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(500);
                    }

                }

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mydatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot myData : dataSnapshot.getChildren()){



                js.put(myData.getValue().toString());
                }

                DeviceAdapter ex2 =new DeviceAdapter(Myapp.getAppContext(),js);
                cycle.setAdapter(ex2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
