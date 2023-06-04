package com.capstone.smart_white_cane.map.navigation.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.MySystem.MyApplication;
import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.CurrentLocationData;
import com.capstone.smart_white_cane.map.data.JibunAddress;
import com.capstone.smart_white_cane.map.data.RoadAddress;
import com.capstone.smart_white_cane.map.navigation.function.ReverseGeoCoding;
import com.capstone.smart_white_cane.map.navigation.tMap.data.NavigateData;
import com.skt.tmap.TMapView;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import kotlin.Pair;

public class GpsManager implements LocationListener {

    private LocationManager locationManager = null;
    private boolean isOnNavigating = false;
    private NavigateData navigateData;
    private TextView curLocationText;

    String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };

    public GpsManager(TextView curLocationText) {
        locationManager = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        Context context = MyApplication.getContext();
        Activity curActivity = (Activity) MyApplication.getContext();

        this.curLocationText = curLocationText;

        if ( ContextCompat.checkSelfPermission( MyApplication.getContext(), permissions[0] ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( curActivity, permissions, 0 );
            Toast.makeText(MyApplication.getContext(), "GPS 허용이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    3,
                    this
            );

            try{
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                Coordinate curCoordinate = new Coordinate(lon, lat);
                ReverseGeoCoding rGeoCoding = new ReverseGeoCoding(curCoordinate);
                try {
                    rGeoCoding.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setNavigate(NavigateData navigateData) {
        this.navigateData = navigateData;
        isOnNavigating = true;
    }
    public void offNavigate() {
        isOnNavigating = false;
    }

    //---------------------------------------------------------------------------------------------------------------//

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        Coordinate curCoordinate = new Coordinate(lon, lat);

        //현위치 데이터 업데이트
        ReverseGeoCoding rgc = new ReverseGeoCoding(curCoordinate);
        rgc.execute();

        //Navigate 중이면 관련 작업 처리하기
        if(isOnNavigating) {
            //navigation 작동 중
            navigateData.setCallback(new NavigateData.CallBack() {
                @Override
                public String onArrive(NavigateData navigationData) {
                    //callback

                    return null; //navigationData.getDescription();
                }
            });

            try {
                String desc = navigateData.checkLocation();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //navigation 비작동

        }

        try{
            //Log.d("MyTag", CurrentLocationData.getInstance().getRoadAddress().toString());
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d("MyTag", "CurLocationData is empty");
        }

    }

    @Override
    public void onFlushComplete(int requestCode) {
        //LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        //LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        //LocationListener.super.onProviderDisabled(provider);
    }
}