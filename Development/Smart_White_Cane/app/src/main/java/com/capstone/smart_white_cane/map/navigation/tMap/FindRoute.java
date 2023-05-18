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

    String jsonString = "{\"metaData\":{\"requestParameters\":{\"busCount\":2,\"expressbusCount\":0,\"subwayCount\":0,\"airplaneCount\":0,\"locale\":\"ko\",\"endY\":\"37.49238127275918\",\"endX\":\"126.98157541065862\",\"wideareaRouteCount\":0,\"subwayBusCount\":0,\"startY\":\"37.476566014033494\",\"startX\":\"126.98136044617819\",\"ferryCount\":0,\"trainCount\":0,\"reqDttm\":\"20230503160837\"},\"plan\":{\"itineraries\":[{\"fare\":{\"regular\":{\"totalFare\":1200,\"currency\":{\"symbol\":\"￦\",\"currency\":\"원\",\"currencyCode\":\"KRW\"}}},\"totalTime\":632,\"legs\":[{\"mode\":\"WALK\",\"sectionTime\":215,\"distance\":183,\"start\":{\"name\":\"출발지\",\"lon\":126.98136044617819,\"lat\":37.476566014033494},\"end\":{\"name\":\"사당역12번출구\",\"lon\":126.98183611111111,\"lat\":37.477675},\"steps\":[{\"streetName\":\"보행자도로\",\"distance\":20,\"description\":\"보행자도로 을 따라 20m 이동\",\"linestring\":\"126.98135,37.476566 126.98138,37.47675\"},{\"streetName\":\"\",\"distance\":16,\"description\":\"직진 후 16m 이동 \",\"linestring\":\"126.98138,37.47675 126.98135,37.47682 126.98142,37.476803\"},{\"streetName\":\"\",\"distance\":42,\"description\":\"횡단보도 후 42m 이동 \",\"linestring\":\"126.98142,37.476803 126.9819,37.476776\"},{\"streetName\":\"\",\"distance\":25,\"description\":\"좌회전 후 25m 이동 \",\"linestring\":\"126.9819,37.476776 126.98195,37.476803 126.981964,37.476982\"},{\"streetName\":\"보행자도로\",\"distance\":13,\"description\":\"사당역 13번출구 에서 횡단보도 후 보행자도로 을 따라 13m 이동 \",\"linestring\":\"126.981964,37.476982 126.98204,37.47709\"},{\"streetName\":\"동작대로\",\"distance\":5,\"description\":\"주식회사 올 투 에서 좌회전 후 동작대로 을 따라 5m 이동 \",\"linestring\":\"126.98204,37.47709 126.982,37.47712\"},{\"streetName\":\"보행자도로\",\"distance\":17,\"description\":\"주식회사 올 투 에서 횡단보도 후 보행자도로 을 따라 17m 이동 \",\"linestring\":\"126.982,37.47712 126.98196,37.477272\"},{\"streetName\":\"동작대로\",\"distance\":45,\"description\":\"뚜레쥬르 사당중앙점 에서 직진 후 동작대로 을 따라 45m 이동 \",\"linestring\":\"126.98196,37.477272 126.981964,37.47734 126.98198,37.47767\"}]},{\"mode\":\"BUS\",\"routeColor\":\"53B332\",\"sectionTime\":77,\"route\":\"마을:동작09\",\"distance\":538,\"start\":{\"name\":\"사당역12번출구\",\"lon\":126.98183611111111,\"lat\":37.477675},\"passStopList\":{\"stationList\":[{\"index\":0,\"stationName\":\"사당역12번출구\",\"lon\":\"126.981836\",\"lat\":\"37.477675\",\"stationID\":\"751269\"},{\"index\":1,\"stationName\":\"이수역\",\"lon\":\"126.982092\",\"lat\":\"37.482522\",\"stationID\":\"751688\"}]},\"end\":{\"name\":\"이수역\",\"lon\":126.98209166666666,\"lat\":37.48252222222222},\"type\":3,\"passShape\":{\"linestring\":\"126.981792,37.477683 126.981792,37.477692 126.981911,37.480075 126.982050,37.482506\"}},{\"mode\":\"TRANSFER\",\"sectionTime\":0,\"distance\":0,\"start\":{\"name\":\"이수역\",\"lon\":126.98209166666666,\"lat\":37.48252222222222},\"end\":{\"name\":\"이수역\",\"lon\":126.98209166666666,\"lat\":37.48252222222222},\"passShape\":{\"linestring\":\"126.982092,37.482522 126.982092,37.482522\"}},{\"mode\":\"BUS\",\"routeColor\":\"53B332\",\"sectionTime\":80,\"route\":\"지선:4212\",\"distance\":563,\"start\":{\"name\":\"이수역\",\"lon\":126.98209166666666,\"lat\":37.48252222222222},\"passStopList\":{\"stationList\":[{\"index\":0,\"stationName\":\"이수역\",\"lon\":\"126.982092\",\"lat\":\"37.482522\",\"stationID\":\"751688\"},{\"index\":1,\"stationName\":\"총신대입구역.남성시장입구앞\",\"lon\":\"126.982381\",\"lat\":\"37.487592\",\"stationID\":\"752243\"}]},\"end\":{\"name\":\"총신대입구역.남성시장입구앞\",\"lon\":126.98238055555555,\"lat\":37.48759166666667},\"type\":12,\"passShape\":{\"linestring\":\"126.982050,37.482506 126.982183,37.484872 126.982211,37.485358 126.982217,37.485506 126.982233,37.485975 126.982306,37.487181 126.982336,37.487597\"}},{\"mode\":\"TRANSFER\",\"sectionTime\":0,\"distance\":0,\"start\":{\"name\":\"총신대입구역.남성시장입구앞\",\"lon\":126.98238055555555,\"lat\":37.48759166666667},\"end\":{\"name\":\"총신대입구역.남성시장입구앞\",\"lon\":126.98238055555555,\"lat\":37.48759166666667},\"passShape\":{\"linestring\":\"126.982381,37.487592 126.982381,37.487592\"}},{\"mode\":\"BUS\",\"routeColor\":\"53B332\",\"s\n";

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
                //Log.d("JSON", response.toString());
                JSONObject jsonObject = new JSONObject(response.body().string());

                Log.d("MyTag", jsonObject.toString());

                JSONObject myObj = (JSONObject) jsonObject.get("metaData");
                //myObj = (JSONObject) myObj.get("requestParameters");

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
