package com.capstone.smart_white_cane.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.MySystem.MyApplication;

@SuppressLint("MissingPermission")
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final PendingResult pendingResult = goAsync();

        switch (intent.getAction()) {
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("MyBroadcastReceiver", device.getName() + " is detected");
        }

        /*
        Task asyncTask = new Task(pendingResult, intent);
        try{
            BluetoothDevice foundDevice = asyncTask.execute().get();
            Log.d("MyBroadcastReceiver", foundDevice.getName() + " is found and running");
        } catch (Exception e) {
            Log.d("MyBroadcastReceiver", "onReceive: " + e.toString());
        }
        */

    }

    private static class Task extends AsyncTask<BluetoothDevice, Integer, BluetoothDevice> {

        private final PendingResult pendingResult;
        private final Intent intent;

        private Task(PendingResult pendingResult, Intent intent) {
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected BluetoothDevice doInBackground(BluetoothDevice... bluetoothDevices) {
            BluetoothDevice device = null;

            switch (intent.getAction()) {
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    return device;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(BluetoothDevice values) {

        }

    }


}


