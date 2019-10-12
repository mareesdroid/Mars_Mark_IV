package app.marees.mars;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.marees.mars.Singletons.Myapp;
import app.marees.mars.Singletons.Singleton;

public class NotificationFrag extends Fragment {

    RequestQueue myqueue;
   EditText msg,ID,title;
   Button mybt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cloud, container, false);


        msg = rootView.findViewById(R.id.editText7);
        ID = rootView.findViewById(R.id.editText5);
        title = rootView.findViewById(R.id.editText6);
        mybt = rootView.findViewById(R.id.mybt);
        mybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mymsg,myID,mytitle;

                mymsg = msg.getText().toString().trim();
                myID = ID.getText().toString().trim();
                mytitle = title.getText().toString().trim();

                sendtoDrive(mymsg,myID,mytitle);
            }
        });




        return rootView;
    }



    private void sendtoDrive(String mymsg, String myID, String mytitle) {


        myqueue = Singleton.getInstance(Myapp.getAppContext()).getRequestQueue();
        String url = "https://fcm.googleapis.com/fcm/send";





        JSONObject js = new JSONObject();
        JSONObject mybody = new JSONObject();
        try {
            mybody.put("body",mymsg);
            mybody.put("title",mytitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            js.put("to",myID);
            js.put("notification",mybody);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String success = response.getString("success");
                    String failure = response.getString("failure");
                    if(failure == "0"){
                        Toast.makeText(Myapp.getAppContext(),"Something went Wrong",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Myapp.getAppContext(),"Something went Wrong",Toast.LENGTH_LONG).show();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(Myapp.getAppContext(),"Something went Wrong",Toast.LENGTH_LONG).show();

            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "key=AIzaSyCWZ_qX7VnlK89HXw8msU4d4Ht6H11SE_Q");

                return params;
            }
        };


        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myqueue.add(myRequest);

    }



}
