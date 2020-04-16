package com.example.prudentdatalogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.util.NumberUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import android.os.Bundle;

public class main2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView End1,distance2;
    int count=0;
    TextView Lati1,Longe1;
    EditText feat;
    Button km,TP,stop2;
    int in,x1=0,x2=0;
    Button tp2,others1;
    int value=0,value1=0;
    String d1="20";
    LocationTrack locationTrack;
    Handler handler = new Handler();
    int num[]=new int[100];
    Runnable refresh;
    double latitude,longitude;
    double lastLongitude=0.0;
    double lastLatitude=0.0;
    String route,route_no;
    double dis=0.0;
    String[] routefeature={" ","Pt & Crossing 40","Level Crossing 26","Switch Expansion Joint 47","Buffer rails 50","Road Over Bridge 22"
            ,"Bridge(Steel Girder)In 20","Bridge(Steel Grinder) Out 21","Bridge(others)In 24","Bridge(Others) Out 25","Curve In 10","Curve Out 10",
            "Tunnel In 68","Tunnel Out 69","Cutting In 60","Cutting Out 61","Siding/loop In 18","Siding/loop Out 19",
            "Speed Restriction In 70","Speed Restriction Out 71","Transponder 77"
    };
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        End1=findViewById(R.id.end);
        km=findViewById(R.id.end1);
        tp2=findViewById(R.id.tp1);
        feat=findViewById(R.id.fea);
        Lati1=findViewById(R.id.latitude1);
        Longe1=findViewById(R.id.longitude1);
        distance2=findViewById(R.id.distance1);
        stop2=findViewById(R.id.stop);
        others1=findViewById(R.id.other);
        stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final String date1 = date+".csv";
        permissionsToRequest = findUnAskedPermissions(permissions);
       final Spinner spp1=findViewById(R.id.routefeature);
        SharedPreferences result = getSharedPreferences("save_data", Context.MODE_PRIVATE);
        String name = result.getString("name", "");//
        final String data = getIntent().getExtras().getString("selected");
        final String section = getIntent().getExtras().getString("section");
        final String start=getIntent().getExtras().getString("start");
        final String direction1=getIntent().getExtras().getString("direction");
        final String end=getIntent().getExtras().getString("end");
        Log.e("data","data"+data);
        Log.e("name","name"+name);
        Log.e("name1->","name1->"+section);
        Log.e("name2->","name2->"+start);
        spp1.setOnItemSelectedListener(this);
        spp1.setSelection(0);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,routefeature);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spp1.setAdapter(aa);
        d1=feat.getText().toString();
        End1.setText(start);
        in= Integer.parseInt(start);

//automaticlly refresh of activity
        refresh = new Runnable() {
            public void run() {
                // Do something
                locationTrack = new LocationTrack(main2.this);
                if (locationTrack.canGetLocation())
                {
                    longitude = locationTrack.getLongitude();
                    latitude = locationTrack.getLatitude();

                    Lati1.setText((Double.toString(longitude)));
                    Longe1.setText(Double.toString(latitude));
                }
                else {
                    locationTrack.showSettingsAlert();
                }
                handler.postDelayed(refresh, 5000);
            }
        };
        handler.post(refresh);

        others1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spp1.setSelection(0);
                if (data.equals("even")&&direction1.equals("up")) {
                    in = in ;
                    String x = Integer.toString(in);
                    //End1.setText(x);
                    //value = 2;
                    //String z = "TP/"+Integer.toString(value);
                    //feat.setText(z);


                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude->","latitude->"+latitude);
                        Log.e("latitude1->","latitude1->"+longitude);
                        Log.e("latitude2->","latitude2->"+lastLatitude);
                        Log.e("latitude3->","latitude3->"+lastLongitude);
                        float[] results = new float[1];
                        Location.distanceBetween(lastLatitude,lastLongitude,
                                latitude, longitude, results);
                        float distance9 = results[0];
                        Log.e("point","point"+distance9);
                        double km1=dis/0.62137;
                        Log.e("kilometer","kilometer"+km1);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (data.equals("odd")&&direction1.equals("up"))
                {
                    in=in;
                    String x = Integer.toString(in);
                    //End1.setText(x);
                    //value1 = 1;
                    //String z = "TP/"+Integer.toString(value1);
                    //feat.setText(z);


                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route;
                            String a="0.0";
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }


                }
                else if (direction1.equals("down")&&data.equals("even"))
                {
                    in = in;
                    String x = Integer.toString(in);
                    //  End1.setText(x);
                    //value = 0;
                    //String z = "TP/"+Integer.toString(value);
                    //feat.setText(z);

                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route;
                            String a="0.0";
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (direction1.equals("down")&&data.equals("odd"))
                {
                    in=in;
                    String x = Integer.toString(in);
                    // End1.setText(x);
                    //value1 = 1;
                    //String z = "TP/"+Integer.toString(value1);
                    //feat.setText(z);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route;
                            String a="0.0";
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (direction1.equals("up")&&data.equals("continue"))
                {
                    in=in;
                    String x = Integer.toString(in);
                    //End1.setText(x);
                    //value1 = 1;
                    //String z = "TP/"+Integer.toString(value1);
                    //feat.setText(z);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route;
                            String a="0.0";
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (direction1.equals("down")&&data.equals("continue"))
                {
                    in=in;
                    String x = Integer.toString(in);
                    //End1.setText(x);
                    //value1 = 1;
                    // String z = "TP/"+Integer.toString(value1);
                    //feat.setText(z);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route;
                            String a="0.0";
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }

            }
        });
        km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spp1.setSelection(0);
                if (data.equals("even")&&direction1.equals("up")) {
                    in = in + 1;
                    String x = Integer.toString(in);
                    End1.setText(x);
                    value = 2;
                    String z = "TP/"+Integer.toString(value);
                    feat.setText(z);


                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (data.equals("odd")&&direction1.equals("up"))
                {
                    in=in+1;
                    String x = Integer.toString(in);
                    End1.setText(x);
                    value1 = 1;
                    String z = "TP/"+Integer.toString(value1);
                    feat.setText(z);


                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;
                            distance2.setText(Double.toString(dis));
                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }


                }
                else if (direction1.equals("down")&&data.equals("even"))
                {
                    in = in - 1;
                    String x = Integer.toString(in);
                    End1.setText(x);
                    value = 0;
                    String z = "TP/"+Integer.toString(value);
                    feat.setText(z);

                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (direction1.equals("down")&&data.equals("odd"))
                {
                    in=in-1;
                    String x = Integer.toString(in);
                    End1.setText(x);
                    value1 = 1;
                    String z = "TP/"+Integer.toString(value1);
                    feat.setText(z);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (direction1.equals("up")&&data.equals("continue"))
                {
                    in=in+1;
                    String x = Integer.toString(in);
                    End1.setText(x);
                    value1 = 1;
                    String z = "TP/"+Integer.toString(value1);
                    feat.setText(z);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
                else if (direction1.equals("down")&&data.equals("continue"))
                {
                    in=in-1;
                    String x = Integer.toString(in);
                    End1.setText(x);
                    value1 = 1;
                    String z = "TP/"+Integer.toString(value1);
                    feat.setText(z);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=x+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=x+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }

            }
        });

        if (data.equals("even")&&direction1.equals("up"))
        {
            String y=Integer.toString(value);

            feat.setText(y);
            tp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spp1.setSelection(0);
                    value=value+2;
                    String z=Integer.toString(value);
                    String a2="TP/"+z;
                    feat.setText(a2);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=in+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=in+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }
                }
            });
        }
        else if (data.equals("odd")&&direction1.equals("up")){
            String a1=Integer.toString(value1);
            feat.setText(a1);
            tp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spp1.setSelection(0);
                    value1=value1+2;
                    String a2=Integer.toString(value1);
                    String a3="TP/"+a2;
                    feat.setText(a3);

                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+a2;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=in+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=in+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }


                }
            });
        }
        else if (data.equals("even")&&direction1.equals("down")){

            String y=Integer.toString(value);

            feat.setText(y);
            tp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spp1.setSelection(0);
                    value=value+2;
                    String z=Integer.toString(value);
                    String a2="TP/"+z;
                    feat.setText(a2);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+z;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=in+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=in+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }
                }
            });

        }
        else if (data.equals("odd")&&direction1.equals("down")){
            String a1=Integer.toString(value1);
            feat.setText(a1);
            tp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spp1.setSelection(0);
                    value1=value1+2;
                    String a2=Integer.toString(value1);
                    String a3="TP/"+a2;
                    feat.setText(a3);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+a3;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=in+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=in+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
            });
        }
        else if (data.equals("continue")&&direction1.equals("up"))
        {
            String a1=Integer.toString(value1);
            feat.setText(a1);
            tp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spp1.setSelection(0);
                    value1=value1+1;
                    String a2=Integer.toString(value1);
                    String a3="TP/"+a2;
                    feat.setText(a3);


                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+a3;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=in+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=in+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
            });
        }
        else if (data.equals("continue")&&direction1.equals("down"))
        {
            String a1=Integer.toString(value1);
            feat.setText(a1);
            tp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spp1.setSelection(0);
                    value1=value1+1;
                    String a2=Integer.toString(value1);
                    String a3="TP/"+a2;
                    feat.setText(a3);
                    String name1=section+" "+start+"-"+end+" "+direction1;
                    String name3=name1+".csv";
                    locationTrack = new LocationTrack(main2.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        double dis = distance(lastLatitude, lastLongitude, latitude, longitude);
                        Log.e("latitude","latitude"+latitude);
                        Log.e("latitude1","latitude1"+longitude);
                        Log.e("latitude2","latitude2"+lastLatitude);
                        Log.e("latitude3","latitude3"+lastLongitude);
                        Log.e("dis","dis"+dis);
                        lastLatitude = latitude;
                        lastLongitude = longitude;

                        FileOutputStream fos = null;
                        ActivityCompat.requestPermissions(main2.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE},23);


                        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+name3);
                        Log.e("path1","path1"+folder);
                        try {
                            FileOutputStream fstream = new FileOutputStream(folder, true);
                            OutputStreamWriter out = new OutputStreamWriter(fstream);
                            BufferedWriter fbw = new BufferedWriter(out);
                            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
                            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            //fos.write(text.getBytes());
                            FileInputStream fis=new FileInputStream(folder);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            // CSVReader reader=new CSVReader(br);
                            StringBuilder sb = new StringBuilder();
                            String [] nextLine;
                            String distance="0";
                            String Tp=route+"TP/OHE Mast;"+in+"/"+a3;
                            String a="0.0";
                            distance2.setText(Double.toString(dis));
                            String text2=in+","+a+","+latitude+","+longitude+","+route_no+","+Tp;

                            //String text=date+","+currentTime+","+longitude+","+latitude+","+Double.toString(dis)+","+route+","+route_no+","+Tp;
                            String text=in+","+Double.toString(dis)+","+latitude+","+longitude+","+route_no+","+Tp;
                            String text1="Kilometer"+","+"Distance"+","+"Latitude"+","+"Longitude"+","+"Feature Code"+","+"Feature Details"+","+"Remark";
                            //Log.e("read line","read line"+br.readLine());

                            if (count==0 )
                            {
                                fbw.write('\n');

                                fbw.write(text1);
                                fbw.write('\n');
                                fbw.write(text2);
                                fbw.close();
                                count++;
                            }
                            else {
                                fbw.write('\n');
                                fbw.write(text);
                                //fbw.write('\n');

                                //close file

                                fbw.close();

                            }
                            String line ="";
                            // text1.getText().clear();
                            line = br.readLine();
                            String[] lastlocation = line.split(",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                    else {
                        locationTrack.showSettingsAlert();
                    }

                }
            });
        }
        else {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

    }
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(Object permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(main2.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position==0)
        {
            route="";
            route_no="2";
        }
        if (position==1)
        {
            route="pt & Crossing";
            route_no="40";
        }
        else if (position==2)
        {
            route="Level Crossing";
            route_no="26";
        }
        else if (position==3)
        {
            route="Switch Expainsion Joint";
            route_no="47";
        }
        else if (position==4)
        {
            route="Buffer rails";
            route_no="50";
        }
        else if (position==5)
        {
            route="Road Over Bridge";
            route_no="22";
        }
        else if (position==6)
        {
            route="Bridge(Steel Girder) In";
            route_no="20";
        }
        else if (position==7)
        {
            route="Bridge(Steel Girder) Out";
            route_no="21";
        }
        else if (position==8)
        {
            route="Bridge(Others) In";
            route_no="24";
        }
        else if (position==9)
        {
            route="Bridge(Others) Out";
            route_no="25";
        }
        else if (position==10)
        {
            route="Curve In";
            route_no="10";
        }
        else if (position==11)
        {
            route="Curve Out";
            route_no="11";
        }
        else if (position==12)
        {
            route="Tunnel In";
            route_no="68";
        }
        else if (position==13)
        {
            route="Tunnel Out";
            route_no="69";
        }
        else if (position==14)
        {
            route="Cutting In";
            route_no="60";
        }
        else if (position==15)
        {
            route="Cutting Out";
            route_no="61";
        }
        else if (position==16)
        {
            route="Siding/loop In";
            route_no="18";
        }
        else if (position==17)
        {
            route="Sliding/loop Out";
            route_no="19";
        }
        else if (position==18)
        {
            route="Speed Restriction In";
            route_no="70";
        }
        else if (position==19)
        {
            route="Speed Restiction Out";
            route_no="71";
        }
        else if (position==20)
        {
            route="Transponder";
            route_no="77";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        route="";
        return;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                      * Math.sin(deg2rad(lat2))
                      + Math.cos(deg2rad(lat1))
                      * Math.cos(deg2rad(lat2))
                      * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515* 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}

