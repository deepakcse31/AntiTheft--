package com.example.bluetooth_fool;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ListView lstvw;
    private ArrayAdapter aAdapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.btnGet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bAdapter==null){
                    Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
                }
                else{
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
            }
        });

    }
}
