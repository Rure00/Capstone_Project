package com.capstone.smart_white_cane.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.smart_white_cane.R;
import com.capstone.smart_white_cane.map.data.LocationData;
import com.capstone.smart_white_cane.map.navigation.NavigationController;
import com.capstone.smart_white_cane.map.navigation.function.SearchLocation;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchLocationActivity extends Activity {
    private final String TAG = "SearchLocationActivity";
    private String searchWord;
    private LocationData locationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new PaintDrawable(Color.WHITE));
        setContentView(R.layout.activity_search_location);

        Intent intent = getIntent();
        try{
            searchWord = intent.getStringExtra("searchWord");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        findViewById(R.id.cancelBtn).setOnClickListener(v-> {
            Intent nIntent = new Intent();
            setResult( RESULT_CANCELED, nIntent );
            finish();
        });


        SearchThread searchThread = new SearchThread(searchWord);
        Handler searchHandler = new Handler(searchThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(SearchLocationActivity.this, "찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        ArrayList<LocationData> results = searchThread.getResult();
                        setResults(results);
                        break;
                    default:

                        break;
                }

            }
        };

        searchThread.setHandler(searchHandler);
        searchThread.start();
    }

    void setResults(ArrayList<LocationData> results) {
        int len = results.size();
        switch (len) {
            case 3:
                LocationData data2 = results.get(2);
                TextView text2 = findViewById(R.id.thirdText);
                text2.setText(data2.getName());
            case 2:
                LocationData data1 = results.get(1);
                TextView text1 = findViewById(R.id.secondText);
                text1.setText(data1.getName());
            case 1:
                LocationData data0 = results.get(0);
                TextView text0 = findViewById(R.id.firstText);
                text0.setText(data0.getName());
                break;
            default:
                Toast.makeText(this, "결과값이 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }

        findViewById(R.id.firstBtn).setOnClickListener(v-> {
            if(results.size() < 1){
                return;
            }

            Intent intent = new Intent();
            LocationData data0 = results.get(0);
            String intentData = new Gson().toJson(data0);
            intent.putExtra( "result", intentData );
            setResult( RESULT_OK, intent );
            finish();
        });
        findViewById(R.id.secondBtn).setOnClickListener(v-> {
            if(results.size() < 2){
                return;
            }
            Intent intent = new Intent();
            LocationData data1 = results.get(1);
            String intentData = new Gson().toJson(data1);
            intent.putExtra( "result", intentData );
            setResult( RESULT_OK, intent );
            finish();
        });
        findViewById(R.id.thirdBtn).setOnClickListener(v-> {
            if(results.size() < 3){
                return;
            }
            Intent intent = new Intent();
            LocationData data2 = results.get(2);
            String intentData = new Gson().toJson(data2);
            intent.putExtra( "result", intentData );
            setResult( RESULT_OK, intent );
            finish();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_OUTSIDE ) {
            return false;
        }
        return true;
    }

}

class SearchThread extends Thread {
    private String searchWord;
    private ArrayList<LocationData> results;
    private Handler handler;

    public SearchThread(String searchWord) {
        this.searchWord = searchWord;
    }

    @Override
    public void run() {
        SearchLocation searchLocation = new SearchLocation(searchWord);
        try {
            results = searchLocation.search();
        } catch (Exception e) {
            Log.e("SearchLocationActivity", e.toString());
        }

        int len = results.size();
        Message msg = new Message();
        msg.what = len > 0 ? 1: 0;

        Log.d("SearchLocationActivity", len + "개의 검색결과");

        handler.sendMessage(msg);
    }

    public void setHandler(Handler handler)  {this.handler = handler; }
    public ArrayList<LocationData> getResult() { return results; }
    public Looper getLooper() { return Looper.myLooper(); }
}