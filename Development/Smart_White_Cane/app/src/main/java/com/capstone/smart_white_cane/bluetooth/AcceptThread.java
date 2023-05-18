package com.capstone.smart_white_cane.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.MySystem.MyApplication;

import java.io.IOException;
import java.util.UUID;

import lombok.val;

public class AcceptThread extends Thread {
    private BluetoothServerSocket serverSocket;
    private BluetoothAdapter adapter;
    private String TAG = "ACCEPT_THREAD";
    private String SOCKET_NAME = "server";
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");


    public AcceptThread(BluetoothAdapter adapter) {
        BluetoothServerSocket tmp = null;
        this.adapter = adapter;
        Context context = MyApplication.getContext();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            tmp = adapter.listenUsingRfcommWithServiceRecord(SOCKET_NAME, MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = tmp;
    }

    public void run() {
        BluetoothSocket clientSocket = null;
        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
            }

        }
    }

    public void connectDevice(String deviceAddress) {
        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (adapter.isDiscovering()) {
            adapter.cancelDiscovery();
        } else {
            BluetoothDevice device = adapter.getRemoteDevice(deviceAddress);
            try {
                //ConnectThread thread = new ConnectThread(MY_UUID, device);

                //thread.run();
                Log.d("AcceptThread", device.getName() + "와 연결되었습니다.");
            } catch (Exception e) { // 연결에 실패할 경우 호출됨
                e.printStackTrace();
            }

        }
    }



    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }

}
