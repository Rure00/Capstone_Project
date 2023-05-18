package com.capstone.smart_white_cane.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.MySystem.MyApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.S)
public class BluetoothController {

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter adapter;
    private Context context;
    private BroadcastReceiver br;

    private BluetoothSocket btSocket;
    Set<BluetoothDevice> mPairedDevices;

    private ConnectThread connectThread;
    private boolean isConnected = false;

    int REQUEST_ALL_PERMISSION = 2;
    final static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    String[] permissions = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    public BluetoothController() throws Exception {
        context = MyApplication.getContext();

        bluetoothManager = context.getSystemService(BluetoothManager.class);
        adapter = bluetoothManager.getAdapter();


        if (!hasPermissions()) {
            Log.e("BluetoothController", "권한 허가가 필요한 서비스입니다.");
            return;
        }
        if (adapter == null) {
            Log.e("BluetoothController", "블루투스를 지원하지 않는 장비입니다.");
        }

        br = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.EXTRA_DEVICE);
        context.registerReceiver(br, filter);
    }

    private Boolean hasPermissions() {
        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_ALL_PERMISSION);
        
        for (String permission: permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e("BluetoothController", "Permission is denied: " + permission);
                return false;
            }
        }
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------------

    public void setActivate(Boolean doActivate) {
        if (doActivate) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                Log.e("BluetoothController", "BLUETOOTH 권한 거부됨.");
                return;
            }
            adapter.enable();

            while (!adapter.isEnabled()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mPairedDevices = adapter.getBondedDevices();

            for(BluetoothDevice device: mPairedDevices) {
                if(device.getName().contains("HC-06")) {
                    BluetoothDevice connectDevice = adapter.getRemoteDevice(device.getAddress());
                    Log.d("BluetoothController", "Found");
                    try{
                        btSocket = createBluetoothSocket(connectDevice);
                        btSocket.connect();
                        Log.d("BluetoothController", "Connection is success");
                        isConnected = true;
                    } catch (Exception e) {
                        Log.e("BluetoothController", "Connecting is failed: " + e);
                    }

                    if(isConnected) {
                        connectThread = new ConnectThread(btSocket);
                        connectThread.start();
                    }

                    return;
                } else {
                    Toast.makeText(context, "하드웨어가 발견되지 않았습니다. 미리 등록해주세요.", Toast.LENGTH_SHORT).show();
                    Log.e("BluetoothController", "Not Found");
                    isConnected = false;
                }
            }
        } else {
            adapter.disable();
            isConnected = false;
        }

    }
    @SuppressLint("MissingPermission")
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, MY_UUID);
        } catch (Exception e) {
            Log.e("BluetoothController", "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public void sendMessage(String msg) {
        if(!isConnected) {
            Toast.makeText(context, "블루투스 연결을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        msg = msg + "\n";
        connectThread.write(msg);
    }
    public String getMessage() {

        return null;
    }
}


