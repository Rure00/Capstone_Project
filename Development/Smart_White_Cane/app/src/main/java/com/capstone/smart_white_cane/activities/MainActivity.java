package com.capstone.smart_white_cane.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.MySystem.MyApplication;
import com.capstone.smart_white_cane.R;
import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.navigation.NavigationController;
import com.capstone.smart_white_cane.map.navigation.tMap.data.NavigateData;

public class MainActivity extends AppCompatActivity {


    NavigationController navigationController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.setContext(this);

        navigationController = new NavigationController();

        Coordinate start = new Coordinate(126.981638570, 37.476559992);
        Coordinate end = new Coordinate(126.981654803, 37.485306540);

        NavigateData nd = navigationController.findRoute(start, end);

        
    }
}