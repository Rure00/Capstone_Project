package com.capstone.smart_white_cane.map.navigation.function;

import android.os.AsyncTask;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.CurrentLocationData;
import com.capstone.smart_white_cane.map.data.JibunAddress;
import com.capstone.smart_white_cane.map.data.LocationData;
import com.capstone.smart_white_cane.map.data.RoadAddress;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.Pair;

public class ReverseGeoCoding extends AsyncTask<Void, Void, Pair<String, String>> {

    private String clientId = "p49yc3zz58";
    private String clientSecret = "69QaWsIH1jADH5wDVeagWbPgfJML9H12tDpxBaOk";

    private Coordinate coordinate;

    private String apiURL;

    Pair<String, String> myResult;

    public ReverseGeoCoding(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordToaddr"
                + "&coords=" + coordinate.toString()
                + "&orders=" + "addr,roadaddr"
                + "&output=" + "json";	// JSON
    }

    @Override
    protected Pair<String, String> doInBackground(Void ... params) {
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Geocoding 개요에 나와있는 요청 헤더 입력.
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            // 요청 결과 확인. 정상 호출인 경우 200
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine = null;
            StringBuffer response = new StringBuffer();

            while((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            
            JSONTokener token = new JSONTokener(response.toString());
            JSONArray resultsArray = new JSONObject(token).getJSONArray("results");
            JSONObject jibunObject = (JSONObject) resultsArray.get(0);
            String region = jibunObject.getJSONObject("region").getJSONObject("area1").get("name").toString()
                    + " " + jibunObject.getJSONObject("region").getJSONObject("area2").get("name").toString();
            JSONObject jibunData = jibunObject.getJSONObject("land");
            String jibunAddressStr = region + " "
                    + jibunObject.getJSONObject("region").getJSONObject("area3").get("name").toString() + " "
                    + jibunData.get("number1");

            String roadAddressStr= "";
            if(resultsArray.length() >= 2) {
                JSONObject roadObject = (JSONObject) resultsArray.get(1);
                JSONObject roadData = roadObject.getJSONObject("land");
                roadAddressStr = region + " "
                        + roadData.get("name") + " "
                        + roadData.get("number1") + "-" + roadData.get("number2");
            }

            myResult = new Pair<>(jibunAddressStr, roadAddressStr);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return myResult;
    }


    @Override
    protected void onPostExecute(Pair<String, String> result) {
        super.onPostExecute(result);
        /**
         1. doInBackground 메소드 후에 실행되는 메소드입니다
         2. 최종 작업이 완료된 경우 수행이 됩니다
         */



    }
}
