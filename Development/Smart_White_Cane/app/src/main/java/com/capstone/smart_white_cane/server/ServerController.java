package com.capstone.smart_white_cane.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.MySystem.MyApplication;
import com.capstone.smart_white_cane.map.navigation.tMap.data.RouteData;
import com.capstone.smart_white_cane.server.dto.DetectionResult;
import com.capstone.smart_white_cane.server.dto.RouteDto;
import com.capstone.smart_white_cane.server.esp.ImageThread;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerController {
    private final String TAG = "serverController";
    private Context context;
    private RetrofitBuilder retrofitBuilder;

    public ServerController() {
        retrofitBuilder = new RetrofitBuilder();
        context = MyApplication.getContext();
    }

    public String testRequest() {
        String result= null;

        Call<String> call = retrofitBuilder.api.getTestResponse();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    //result = response.body().toString();

                    Log.d(TAG, response.body().toString());
                } else {
                    Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
            }
        });

        return result;
    }

    public void getScore(RouteData routeData) {
        RouteDto routeDto = null;
        try{
            routeDto = new RouteDto(routeData);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        Call<Integer> call = retrofitBuilder.api.getScoreResponse(routeDto);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()) {
                    String result = response.body().toString();

                    Log.d(TAG, result);
                } else {
                    Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void detect(ImageView imageView) {
        ImageThread imageThread = new ImageThread();
        Looper looper = imageThread.getLooper();

        Handler handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        Bitmap bitmap = imageThread.getResult();

                        if(bitmap != null) {
                            //requestDetection(bitmap);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Log.e(TAG, "Image is null");
                        }
                        break;
                    default:

                        break;
                }

            }
        };

        imageThread.setHandler(handler);
        imageThread.start();
    }
    private void requestDetection(Bitmap picture) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Call<String> call = retrofitBuilder.api.detect(encoded);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    String result = response.body().toString();

                    DetectionResult detectionResult
                            = new DetectionResult(response.body());

                    Log.d(TAG, result);
                } else {
                    Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

