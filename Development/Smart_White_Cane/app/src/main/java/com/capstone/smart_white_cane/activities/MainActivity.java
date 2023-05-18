package com.capstone.smart_white_cane.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MySystem.MyApplication;
import com.capstone.smart_white_cane.R;
import com.capstone.smart_white_cane.bluetooth.BluetoothController;
import com.capstone.smart_white_cane.map.navigation.NavigationController;
import com.capstone.smart_white_cane.server.ServerController;
import com.capstone.smart_white_cane.server.dto.RouteDto;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;
import com.skt.tmap.TMapView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavigationController navigationController = null;
    BluetoothController bluetoothController = null;
    ServerController serverController = null;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.setContext(this);

        //navigationController = new NavigationController();
        serverController = new ServerController();

        try{
            bluetoothController = new BluetoothController();
        } catch (Exception e) {
            e.printStackTrace();
        }


        CheckBox connectionCheck = findViewById(R.id.connectionCheck);
        connectionCheck.setOnClickListener(v -> {
            if(connectionCheck.isChecked()) {
                //켤때
                bluetoothController.setActivate(true);
            } else {
                //끌떄
                bluetoothController.setActivate(false);
            }
        });

        Button requestBtn = findViewById(R.id.requestBtn);
        ImageView imageView = findViewById(R.id.captureView);
        requestBtn.setOnClickListener(v -> {
            serverController.detect(imageView);
        });




    }
}