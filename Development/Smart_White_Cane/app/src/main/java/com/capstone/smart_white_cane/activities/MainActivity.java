package com.capstone.smart_white_cane.activities;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
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
import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.LocationData;
import com.capstone.smart_white_cane.map.navigation.NavigationController;
import com.capstone.smart_white_cane.map.navigation.tMap.data.NavigateData;
import com.capstone.smart_white_cane.server.ServerController;
import com.capstone.smart_white_cane.server.dto.RouteDto;
import com.google.gson.Gson;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavigationController navigationController = null;
    //BluetoothController bluetoothController = null;
    ServerController serverController = null;

    private LocationData startLocation;
    private LocationData endLocation;

    private EditText startText;
    private EditText endText;
    private Button startWordBtn;
    private Button endWordBtn;




    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.setContext(this);
        TextView curPositionText = findViewById(R.id.curPositionText);

        navigationController = new NavigationController(curPositionText);
        serverController = new ServerController();

        //Bluetooth
        /*
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
         */

        startWordBtn = findViewById(R.id.startBtn);
        startWordBtn.setOnClickListener(v -> {
            startText = findViewById(R.id.startText);
            if(startLocation == null) {
                String word = startText.getText().toString();
                if(word.equals("")) {
                    Toast.makeText(this, "출발지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, SearchLocationActivity.class);
                    intent.putExtra("searchWord", word);
                    startActivityForResult(intent, 0);
                }
            } else {
                startText.setEnabled(true);
                startText.setText("");
                startLocation = null;
                startWordBtn.setText("검색");
            }

        });

        endWordBtn = findViewById(R.id.endBtn);
        endWordBtn.setOnClickListener(v -> {
            endText = findViewById(R.id.endText);
            if(endLocation == null) {
                String word = endText.getText().toString();
                if(word.equals("")) {
                    Toast.makeText(this, "목적지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, SearchLocationActivity.class);
                    intent.putExtra("searchWord", word);
                    startActivityForResult(intent, 1);
                }
            } else {
                endText.setEnabled(true);
                endText.setText("");
                endLocation = null;
                endWordBtn.setText("검색");
            }
        });

        ImageView captureView = findViewById(R.id.captureView);
        ImageView detectView = findViewById(R.id.detectView);
        Button startScanBtn = findViewById(R.id.scanStartBtn);
        startScanBtn.setOnClickListener(v -> {
            serverController.startDetect(captureView, detectView);
        });
        Button stopScanBtn = findViewById(R.id.scanStopBtn);
        stopScanBtn.setOnClickListener(v -> {
            serverController.stopDetect();
        });

        Button navigateBtn = findViewById(R.id.findRouteBtn);
        navigateBtn.setOnClickListener(v-> {
            if(startLocation == null || endLocation == null) {
                Toast.makeText(this, "출발지 또는 목적지가 지정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Coordinate start = startLocation.getCoordinate();
            Coordinate end = endLocation.getCoordinate();

            NavigateData navigateData = navigationController.findRoute(start, end);

            String description = "";
            for(String des: navigateData.getDescribes()) {
                description = new StringBuilder(description).append(des).append("\n").toString();
            }

            TextView navigateTextView = findViewById(R.id.navigateText);
            navigateTextView.setText(description);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    String data1 =  data.getStringExtra("result");
                    startLocation = new Gson().fromJson(data1, LocationData.class);
                    startText.setText(startLocation.getName());

                    startText.setEnabled(false);
                    startWordBtn.setText("취소");
                }
                break;
            case 1:
                if(resultCode == RESULT_OK) {
                    String data1 = data.getStringExtra("result");
                    endLocation = new Gson().fromJson(data1, LocationData.class);
                    endText.setText(endLocation.getName());

                    endText.setEnabled(false);
                    endWordBtn.setText("취소");
                }
                break;
            default:
                break;
        }
    }

}