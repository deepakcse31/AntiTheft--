package com.example.gpstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Register extends AppCompatActivity {
    String server_url_insert = "http://192.168.0.11/test/register.php?name=";
    EditText namer, emailr, phoner, passr, imeir, vehicler;
    String name, phone, email, pass, imei, vehicle;
    Integer otp;
    Button userdetails;
    RequestQueue requestQueue;
    String HttpUrl = "http://192.168.0.9/test/register.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static String url_new_user = "C:/xampp/htdocs/test/register.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        namer = findViewById(R.id.usenamer);
        emailr = findViewById(R.id.c_password);
        phoner = findViewById(R.id.phoner);
        passr = findViewById(R.id.passwordr);
        imeir = findViewById(R.id.imeinor);
        vehicler = findViewById(R.id.vehiclenor);
        userdetails = findViewById(R.id.verify);
        requestQueue = Volley.newRequestQueue(Register.this);
        progressDialog = new ProgressDialog(Register.this);
        userdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //code.....

                progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
                progressDialog.show();

                // Calling method to get value from EditText.
                GetValueFromEditText();
                name = namer.getText().toString().trim();
                phone = phoner.getText().toString().trim();
                email = emailr.getText().toString().trim();
                pass = passr.getText().toString().trim();
                imei = imeir.getText().toString().trim();
                vehicle = vehicler.getText().toString().trim();
                Random rand = new Random();
                otp = rand.nextInt(10000);
                sharedPreferences = getSharedPreferences("save_data", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("AD_Login", phone);
                editor.putString("otp_", otp.toString());
                editor.commit();


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
                else if (email.matches(""))
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
                else if (email.equals(pass)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {

                                    //JSONObject jobj = new JSONObject(ServerResponse);
                                    //JSONObject songsObject = jobj.getJSONObject("otp");
                                    //SONArray songsArray = songsObject.toJSONArray(songsObject.names());
                                    //Log.e("Result->","Result->"+songsArray);
                                    // Hiding the progress dialog after all task complete.


                                    Log.e("saturday", "saturday" + otp);

                                    progressDialog.dismiss();
                                    Intent i1 = new Intent(getApplicationContext(), OTP.class);
                                    startActivity(i1);
                                    // Showing response message coming from server.
                                    Toast.makeText(Register.this, ServerResponse, Toast.LENGTH_LONG).show();


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing error message if something goes wrong.
                                    Toast.makeText(Register.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {

                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();

                            // Adding All values to Params.
                            params.put("username", name);
                            params.put("phone", phone);
                            params.put("email", email);
                            params.put("pass", pass);
                            params.put("imei", imei);
                            params.put("vehicle", vehicle);
                            params.put("otp", otp.toString());

                            return params;
                        }

                    };

                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(Register.this);

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

    public void GetValueFromEditText(){


    }
                //Intent user=new Intent(getApplicationContext(),UserDetails.class);
                //startActivity(user);
}


