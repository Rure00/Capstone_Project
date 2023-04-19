package com.capstone.smart_white_cane.map.navigation.function;

import android.util.Log;


import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.GeoTrans;
import com.capstone.smart_white_cane.map.data.GeoTransPoint;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchLocation {
    private String clientId = "NJ0Rv7lDOHM4YUIMIDAM"; //애플리케이션 클라이언트 아이디
    private String clientSecret = "JrQTF788pj"; //애플리케이션 클라이언트 시크릿

    private String searchWord;
    private ExecutorService pool = null;

    public SearchLocation(String searchWord) {
        this.pool = Executors.newFixedThreadPool(3);
        this.searchWord = searchWord;
    }

    public ArrayList<LocationData> search() throws Exception {
        Callable<ArrayList<LocationData>> callable = new GetJson();
        Future<ArrayList<LocationData>> future = pool.submit(callable);

        return future.get();
    }

    class GetJson implements Callable {
        @Override
        public ArrayList<LocationData> call() throws Exception {

            ArrayList<LocationData> locationDataList = new ArrayList<>();

            try {
                String text = URLEncoder.encode(searchWord, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text + "&display=" + 3;

                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    Log.d("MyTag", "Error" + "(" + con.getResponseCode() + "): " + con.getResponseMessage());
                }

                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                    response.append("\n");
                }
                br.close();

                JSONTokener token = new JSONTokener(response.toString());
                JSONObject object = new JSONObject(token);
                JSONArray arr = object.getJSONArray("items");

                int len = arr.length();
                for (int i = 0; i < len; i++) {
                    JSONObject temp = (JSONObject) arr.get(i);

                    String name = temp.get("title").toString();
                    String jibunAddressStr = temp.get("address").toString();
                    String roadAddressStr = temp.get("roadAddress").toString();
                    double mapX = Double.parseDouble(temp.get("mapx").toString());
                    double mapY = Double.parseDouble(temp.get("mapy").toString());

                    GeoTransPoint oKA = new GeoTransPoint(mapX, mapY);
                    GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
                    Double lat = oGeo.getY(); //* 1E6;
                    Double lng = oGeo.getX(); //* 1E6;
                    Coordinate coordinate = new Coordinate(lng, lat);

                    JibunAddress jibunAddress = JibunAddress.toJibun(jibunAddressStr);
                    RoadAddress roadAddress = RoadAddress.toRoad(roadAddressStr);

                    LocationData locationData = new LocationData(
                            name, roadAddress, jibunAddress, coordinate
                    );

                    locationDataList.add(locationData);
                }

            } catch (Exception e) {
                Log.d("MyTag", "Error is detected: " + e);
            }

            return locationDataList;
        }
    }
}
