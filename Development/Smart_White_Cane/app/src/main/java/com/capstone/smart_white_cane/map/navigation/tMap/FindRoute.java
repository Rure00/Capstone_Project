package com.capstone.smart_white_cane.map.navigation.tMap;

import android.util.Log;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.GeoTrans;
import com.capstone.smart_white_cane.map.data.GeoTransPoint;
import com.capstone.smart_white_cane.map.data.JibunAddress;
import com.capstone.smart_white_cane.map.data.LocationData;
import com.capstone.smart_white_cane.map.data.RoadAddress;
import com.capstone.smart_white_cane.map.navigation.function.SearchLocation;
import com.capstone.smart_white_cane.map.navigation.tMap.data.NavigateData;
import com.capstone.smart_white_cane.map.navigation.tMap.data.RouteData;

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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindRoute {
    private Coordinate start;
    private Coordinate end;
    private String searchWord;
    private ExecutorService pool = null;

    public FindRoute(Coordinate start, Coordinate end) {
        this.pool = Executors.newFixedThreadPool(3);
        this.start = start;
        this.end = end;
    }

    public RouteData search() throws Exception {
        Callable<RouteData> callable = new FindRoute.GetJson();
        Future<RouteData> future = pool.submit(callable);

        return future.get();
    }

    class GetJson implements Callable {
        @Override
        public RouteData call() throws Exception {

            RouteData routeData = null;
            String responseMsg;

            try {
                String startX = Double.toString(start.getLongitude());
                String startY = Double.toString(start.getLatitude());
                String endX = Double.toString(end.getLongitude());
                String endY = Double.toString(end.getLatitude());

                final OkHttpClient client = new OkHttpClient();

                String nr = "\"";

                MediaType mediaType = MediaType.parse("JSON");
                RequestBody body = RequestBody.create(mediaType,
                        "{\"startX\":\"" + startX
                                + "\",\"startY\":\"" + startY
                                + "\",\"endX\":\"" + endX
                                + "\",\"endY\":\"" + endY
                                + "\",\"lang\":0,\"format\":\"json\",\"count\":3}");


                Request request = new Request.Builder()
                        .url("https://apis.openapi.sk.com/transit/routes")
                        .post(body)
                        .addHeader("appkey", "zTFP0QHR2E7PLxPIUD6fN9KsNYS4GoSY3ZjrjyVh")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .build();

                //TODO [동기 처리 (execute 사용)]
                Response response = client.newCall(request).execute();

                JSONTokener token = new JSONTokener(response.toString());
                Log.d("JSON", response.toString());
                JSONObject jsonObject = new JSONObject(response.body().string());

                Log.d("MyTag", jsonObject.toString());

                JSONObject myObj = (JSONObject) jsonObject.get("metaData");
                myObj = (JSONObject) myObj.get("requestParameters");

                JSONObject planJson = myObj.getJSONObject("plan");
                JSONArray routes = planJson.getJSONArray("itineraries");

                responseMsg = jsonObject.toString();

                int bestRoute = 0;
                int score = 0;
                for(int i =0; i<3; i++) {
                    /* Find Best Route
                    int curScore = serverController.getScore(routes[i]);
                    if(curScore > score) {
                        score = curScore;
                        bestRoute = i;
                    }
                     */
                }

                routeData = new RouteData(routes.getJSONObject(bestRoute));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return routeData;
        }
    }
}
