package com.example.programmabletimer;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Bluetooth_device extends AppCompatActivity {
    private ListView lstvw;
    private ArrayAdapter aAdapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device);
        if(bAdapter==null){
            Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
        }
        else if (bAdapter.isEnabled())
        {
            Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
            ArrayList list = new ArrayList();
            if(pairedDevices.size()>0){
                for(BluetoothDevice device: pairedDevices){
                    String devicename = device.getName();
                    String macAddress = device.getAddress();
                    //list.add("Name: "+devicename+"MAC Address: "+macAddress);
                    list.add(devicename+"\n"+macAddress);
                    //list.add(macAddress);
                    Log.e("device","device"+devicename);
                    Log.e("device","device"+macAddress);
                }
                lstvw = (ListView) findViewById(R.id.deviceList);
                lstvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //  String macAddress1=lstvw.getSelectedItem().getText().toString();
                        String selectedEntry = (String) aAdapter.getItem(position);
                        String info = ((TextView) view).getText().toString();
                        String address = info.substring(info.length() - 17);
                        Intent intent = new Intent(getApplicationContext(),Timer.class);
                        intent.putExtra("EXTRA_SESSION_ID",address);
                        startActivity(intent);
                        //selectedEntry.replaceFirst("unionName","");
                        //selectedEntry.replaceFirst("unionName","");
                        Log.e("union=>","union=>"+selectedEntry);
                        Log.e("nhi=>","nhi=>"+address);
                        //Log.e("union","union"+);
                    }
                });
                aAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                lstvw.setAdapter(aAdapter);
            }
        }
        else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

        }
    }
}
