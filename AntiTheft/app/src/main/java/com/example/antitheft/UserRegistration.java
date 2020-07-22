package com.example.antitheft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserRegistration extends AppCompatActivity {
    String name;
    String phone;
    String email3;
    String pass;
    String imei;
    String vehicle,email05,token;
    EditText namer, emailr, phoner, passr, email1, vehicler;
    Button userdetails;
    private static final String TAG_SUCCESS = "success";
    //String HttpUrl = "http://192.168.43.25/antitheft/records.php";
    String HttpUrl = "http://ranjan.rdihouse.com/records.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String Firstname1,Lastname1,Email1,Alternativemobileno1,Address1,City1,State1;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        namer = findViewById(R.id.usenamer);
        emailr = findViewById(R.id.c_password);
        phoner = findViewById(R.id.phoner);
        passr = findViewById(R.id.passwordr);
        email1 = findViewById(R.id.email10);
        vehicler = findViewById(R.id.vehiclenor);
        userdetails = findViewById(R.id.verify);
        requestQueue = Volley.newRequestQueue(UserRegistration.this);
        progressDialog = new ProgressDialog(UserRegistration.this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                           // Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                         token = task.getResult().getToken();
                        Log.e("token","token"+token);
                        // Log and toast
                      //  String msg = getString(R.string.msg_token_fmt, token);
                       // Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    });


        userdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = namer.getText().toString().trim();
                phone = phoner.getText().toString().trim();
                email3 = emailr.getText().toString().trim();//pasword check
                pass = passr.getText().toString().trim();
                email05 = email1.getText().toString().trim();
                vehicle = vehicler.getText().toString().trim();
                progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
                progressDialog.show();
                // Creating string request with post method.
                if (name.matches(""))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"please enter the username",Toast.LENGTH_LONG).show();
                }
                else if (phone.matches("")&&phone.length()==10)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"please enter the mobileno",Toast.LENGTH_LONG).show();
                }
                else if (email3.matches(""))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"please enter the password",Toast.LENGTH_LONG).show();
                }
                else if (pass.matches(""))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"please enter the password",Toast.LENGTH_LONG).show();
                }

                else if (vehicle.matches(""))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"please enter the vehicle no",Toast.LENGTH_LONG).show();
                }

                else if (email3.equals(pass)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {
                                    progressDialog.dismiss();
                                    //Intent i1 = new Intent(getApplicationContext(), OTP.class);
                                    //startActivity(i1);
                                    // Showing response message coming from server.
                                    System.setProperty("http.keepAlive", "false");
                                    Toast.makeText(UserRegistration.this, ServerResponse, Toast.LENGTH_LONG).show();
                                    Log.e("ServerResponse","server response"+ServerResponse);

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing error message if something goes wrong.
                                    Toast.makeText(UserRegistration  .this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams() {

                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();

                            // Adding All values to Params.
                            params.put("username", name);
                            params.put("email", email05);
                            params.put("password", pass);
                            params.put("vehicleno", vehicle);
                            params.put("phone",phone);
                            params.put("token",token);

                            return params;
                        }
                    };
                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(UserRegistration.this);
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"please enter correct password",Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}