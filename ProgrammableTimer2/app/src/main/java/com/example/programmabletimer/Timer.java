package com.example.programmabletimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Timer extends AppCompatActivity {
    EditText start1, end1, start2, end2;
    Calendar calendar;
    DatePickerDialog picker;
    int currentHour;
    int currentMinute;
    String name1;
    String amPm;
    TimePickerDialog timePickerDialog;
    Button submit2, Resett1, Resett2, Devicetime1;
    TextView Device1;

    private static final String TAG = "bluetooth2";

    Button btnOn, btnOff;
    TextView txtArduino;
    Handler h;

    final int RECIEVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    //private static String address = "00:15:FF:F2:19:5F";
    private static String address="98:D3:32:11:74:7F";
    private String connectedDeviceAdd = "";
    private BluetoothDevice connectedDevice;

    public static final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";


    //String address1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        start1 = findViewById(R.id.start);
        end1 = findViewById(R.id.end);

        Resett1 = findViewById(R.id.reset1);
        Resett2 = findViewById(R.id.reset2);

        start1.setInputType(InputType.TYPE_NULL);
        start1.setText("00:00");
        start2 = findViewById(R.id.starttimer2);
        end2 = findViewById(R.id.endtimer2);
        submit2 = findViewById(R.id.submittimer2);
        Devicetime1 = findViewById(R.id.devicetime1);
        Device1 = findViewById(R.id.device);
        String macAddress = Settings.Secure.getString(getContentResolver(), SECURE_SETTINGS_BLUETOOTH_ADDRESS);
        Log.e("Dekh lena", "Dekh lena" + macAddress);


        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                    // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);// create string from bytes array
                        Log.e("Logcat Details","Logcat Details"+strIncom);
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                        if (endOfLineIndex > 0) {                                            // if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);                // extract string
                            sb.delete(0, sb.length());// and clear
                            Log.e("", "" + sbprint);
                            //txtArduino.setText("Data from Arduino: " + sbprint); 	        // update TextView
                           // btnOff.setEnabled(true);
                            //btnOn.setEnabled(true);
                        }
                        //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            }

            ;
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();// get Bluetooth adapter
        // address1=btAdapter.getAddress();
        //Log.e("hello->","hello->"+address1);
        checkBTState();

        Resett1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start1.setText("00:00");
                end1.setText("00:00");
            }
        });
        Resett2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start2.setText("00:00");
                end2.setText("00:00");
            }
        });
        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(Timer.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        start1.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        end1.setInputType(InputType.TYPE_NULL);
        end1.setText("00:00");
        end1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Timer.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        end1.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        start2.setInputType(InputType.TYPE_NULL);
        start2.setText("00:00");
        start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Timer.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        start2.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });
        end2.setInputType(InputType.TYPE_NULL);
        end2.setText("00:00");
        end2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Timer.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        end2.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String starttime1 = start2.getText().toString();
                Log.e("start time", "End Time" + starttime1);
                String endtime1 = end2.getText().toString();
                String starttime = start1.getText().toString();
                Log.e("start time", "End Time" + starttime);
                String endtime = end1.getText().toString();
                Log.e("start time", "End Time" + endtime);
                Log.e("start time", "End Time" + endtime1);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                try {
                    Date inTime = sdf.parse(starttime);
                    Date outTime = sdf.parse(endtime);
                    Date inTime1 = sdf.parse(starttime1);
                    Date outTime1 = sdf.parse(endtime1);
                    if (outTime1.before(inTime1)) {
                        Toast.makeText(Timer.this, "Please Enter correct Time!", Toast.LENGTH_SHORT).show();
                    } else if (outTime.before(inTime)) {
                        Toast.makeText(Timer.this, "Please Enter correct Time!", Toast.LENGTH_SHORT).show();

                    } else {
                        //String x="http://192.168.4.1/@T1_S"+starttime+",E"+endtime+"T2_S"+starttime1+",E"+endtime1+"$";
                        String x = starttime + endtime + starttime1 + endtime1;
                        x = x.replace("AM", "");
                        x = x.replace("PM", "");
                        //btnOn.setEnabled(false);
                        mConnectedThread.write(x);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
       Devicetime1.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               String x1="current";
               mConnectedThread.write(x1);
               h = new Handler() {
                   public void handleMessage(android.os.Message msg) {
                       switch (msg.what) {
                           case RECIEVE_MESSAGE:                                                    // if receive massage
                               byte[] readBuf = (byte[]) msg.obj;
                               String strIncom = new String(readBuf, 0, msg.arg1);// create string from bytes array
                              Device1.setText(strIncom);
                               Log.e("Logcat Details","Logcat Details"+strIncom);
                               sb.append(strIncom);                                                // append string
                               int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                               if (endOfLineIndex > 0) {                                            // if end-of-line,
                                   String sbprint = sb.substring(0, endOfLineIndex);                // extract string
                                   sb.delete(0, sb.length());// and clear
                                   Log.e("", "" + sbprint);
                                   //txtArduino.setText("Data from Arduino: " + sbprint); 	        // update TextView
                                   // btnOff.setEnabled(true);
                                   //btnOn.setEnabled(true);
                               }
                               //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                               break;
                       }
                   }

                   ;
               };

           }
       });

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});

                final String a = device.getAddress();
                Log.e("lala", "lala" + a);
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);

        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

    /*try {
      btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
    } catch (IOException e) {
      errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
    }*/

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                // If there are paired devices
               
                Log.d(TAG, "...Bluetooth ON...");
                //address1=btAdapter.getAddress();
                //Log.e("hello","hello"+address1);
                Log.e("nhi", "nhi" + btAdapter.getAddress());

            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }


    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();        // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }
}




