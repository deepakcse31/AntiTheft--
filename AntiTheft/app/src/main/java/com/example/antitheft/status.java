package com.example.antitheft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class status extends AppCompatActivity {
EditText Bike,tampering1,petro1l,time1,date1;
    Handler handler = new Handler();
    Runnable refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Bike=findViewById(R.id.bike_id);
        tampering1=findViewById(R.id.tampering);
        petro1l=findViewById(R.id.petrol);
        time1=findViewById(R.id.time);
        date1=findViewById(R.id.date);
        refresh = new Runnable() {
            public void run() {
                // Do something
                login();
                handler.postDelayed(refresh, 5000);
            }
        };
        handler.post(refresh);

    }
    public void login()
    {

        StringRequest request=new StringRequest(Request.Method.POST, "http://ranjan.rdihouse.com/readData.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            Log.e("response","response"+response);
                            JSONObject jobj = new JSONObject(response);
                            String status = jobj.getString("st");
//                                String result=jobj.getString("data");
                            JSONObject songsObject = jobj.getJSONObject("data");
                            JSONArray songsArray = songsObject.toJSONArray(songsObject.names());
//                                JSONArray jsonArray=jobj.getJSONArray("data");
                            Log.e("Result->","Result->"+songsArray);
//                                Log.e("Result->","Result->"+jsonArray);

                            //String phone1=jobj.getString("phone");

                            if (status.equals("true")) {
                                String id = songsObject.getString("id");
                                String bike_id = songsObject.getString("bike_id");
                                String tampering = songsObject.getString("tampering");
                                String petrol = songsObject.getString("petrol");
                                String time = songsObject.getString("time");
                                String date = songsObject.getString("date");

                                if (tampering.equals("1"))
                                {
                                    tampering1.setText("Tampering Occured");
                                }
                                else {
                                    tampering1.setText("Okay");
                                }
                                if (petrol.equals("1"))
                                {
                                    petro1l.setText("Leakage Detected");
                                }
                                else {
                                    petro1l.setText("Okay");
                                }
                                Bike.setText(bike_id);
                                //tampering1.setText(tampering);
                                //petro1l.setText(petrol);
                                time1.setText(time);
                                date1.setText(date);
                                //String vehicle = songsObject.getString("vehicleno");
                                //String status1 = songsObject.getString("status");
                                //Toast.makeText(getApplicationContext(),"Loginhave done",Toast.LENGTH_LONG).show();
                                // Intent i2=new Intent(getApplicationContext(),MapsActivity.class);
                                //i2.putExtra("AnyKeyName",phone);
                                //startActivity(i2);
                               // Toast.makeText(getApplicationContext(), "id->"+id+"name->"+name+"phone->"+phone+"email"+email+"password"+password+"imei"+imei+"vehicle"+vehicle, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "this is response" + response, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("exception","exception"+e);
                            Toast.makeText(getApplicationContext(),"There is some problem in link",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volleyError","volleyError"+error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
               // params.put("phone",username1.getText().toString());
                //params.put("pass",password1.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);


    }

}
