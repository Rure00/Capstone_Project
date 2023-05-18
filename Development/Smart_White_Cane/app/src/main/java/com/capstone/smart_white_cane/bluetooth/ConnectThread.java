package com.capstone.smart_white_cane.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.MySystem.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.UUID;

import lombok.val;

public class ConnectThread extends Thread {
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothSocket socket;
    private Handler mBluetoothHandler;

    private int BT_MESSAGE_READ = 2;


    @SuppressLint("HandlerLeak")
    public ConnectThread(BluetoothSocket socket) {

        this.socket = socket;

        try{
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*mBluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == BT_MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        };*/
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = inputStream.available();
                if (bytes != 0) {
                    SystemClock.sleep(100);
                    bytes = inputStream.available();
                    bytes = inputStream.read(buffer, 0, bytes);
                    String msg = "";
                    for(byte myByte: buffer) {
                        if(myByte == 0)
                            break;
                        msg = msg + new String(new byte[] { myByte });
                    }

                    Log.d("ConnectThread", msg);
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    public void write(String input) {
        byte[] bytes = input.getBytes();
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
        }
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

}
