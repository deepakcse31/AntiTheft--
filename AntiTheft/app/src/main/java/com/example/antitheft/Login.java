package com.example.antitheft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView links,register,forgetpassword;
    EditText username1,password1;
    ProgressDialog progressDialog;
    Button login;
    HashMap<String,String> hashMap = new HashMap<>();
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.new_user_resigtration);
        username1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        login = findViewById(R.id.login);
        requestQueue = Volley.newRequestQueue(Login.this);
        progressDialog = new ProgressDialog(Login.this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(getApplicationContext(), UserRegistration.class);
                startActivity(reg);
                Toast.makeText(getApplicationContext(), "register ho gya tu", Toast.LENGTH_LONG).show();
            }
        });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
    }
    public void login()
    {
        StringRequest request=new StringRequest(Request.Method.POST, "http://ranjan.rdihouse.com/login.php",
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
                                String name = songsObject.getString("username");
                                String phone = songsObject.getString("phone");
                                String email = songsObject.getString("email");
                                String password = songsObject.getString("password");
                                String imei = songsObject.getString("email");
                                String vehicle = songsObject.getString("vehicleno");
                                //String status1 = songsObject.getString("status");
                                Toast.makeText(getApplicationContext(),"Loginhave done",Toast.LENGTH_LONG).show();
                                Intent i2=new Intent(getApplicationContext(),status.class);
                                //i2.putExtra("AnyKeyName",phone);
                                startActivity(i2);
                                Toast.makeText(getApplicationContext(), "id->"+id+"name->"+name+"phone->"+phone+"email"+email+"password"+password+"imei"+imei+"vehicle"+vehicle, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "this is response" + response, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("exception","exception"+e);
                            Toast.makeText(getApplicationContext(),"Please Enter Correct Username and Password",Toast.LENGTH_LONG).show();
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
                params.put("phone",username1.getText().toString());
                params.put("pass",password1.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
