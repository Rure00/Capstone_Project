package com.capstone.smart_white_cane.map;

import android.util.Log;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.GeoTrans;
import com.capstone.smart_white_cane.map.data.GeoTransPoint;
import com.capstone.smart_white_cane.map.data.LocationData;

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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class navigationController implements navigationControllerInterface {

    String clientId = "NJ0Rv7lDOHM4YUIMIDAM";
    String clientSecret = "JrQTF788pj";

    @Override
    public ArrayList<LocationData> findLocation(String location)  {
        new Thread(new Callable<ArrayList<LocationData>>() {
            @Override
            public ArrayList<LocationData> call() throws Exception {
                try {
                    String text = URLEncoder.encode(location, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=" + 10;

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

                    //TODO: item 결과값 받아와 클래스 배열 만들기
                    int len = arr.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject temp = (JSONObject) arr.get(i);

                        String name = temp.get("title").toString();
                        String jibunAddress = temp.get("address").toString();
                        String roadAddress = temp.get("roadAddress").toString();
                        String category = temp.get("category").toString();
                        String description = temp.get("description").toString();
                        double mapX = Double.parseDouble(temp.get("mapx").toString());
                        double mapY = Double.parseDouble(temp.get("mapy").toString());

                        GeoTransPoint oKA = new GeoTransPoint(mapX, mapY);
                        GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
                        Double lat = oGeo.getY() * 1E6;
                        Double lng = oGeo.getX() * 1E6;
                        Coordinate coordinate = new Coordinate(lat.intValue(), lng.intValue());

                        LocationData locationData = new LocationData(
                                name, roadAddress, jibunAddress, category, description,coordinate
                        );

                        locationDataList.add(locationData);
                    }

                    Log.d("MyTag", locationDataList.get(0).getName() + ", " + locationDataList.get(0).getDescription());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            return
            }
        });

        return null;
    }

    @Override
    public Coordinate reverseGeoCoding(String coordinate) {


        return null;
    }
}
