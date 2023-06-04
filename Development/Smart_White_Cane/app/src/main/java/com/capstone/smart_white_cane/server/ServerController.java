package com.capstone.smart_white_cane.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.MySystem.MyApplication;
import com.capstone.smart_white_cane.R;
import com.capstone.smart_white_cane.map.navigation.tMap.data.RouteData;
import com.capstone.smart_white_cane.server.dto.DetectionData;
import com.capstone.smart_white_cane.server.dto.DetectionResponse;
import com.capstone.smart_white_cane.server.dto.RouteDto;
import com.capstone.smart_white_cane.server.dto.testMessage;
import com.capstone.smart_white_cane.server.esp.ImageThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerController {
    private final String TAG = "serverController";
    private Context context;
    private RetrofitBuilder retrofitBuilder;
    private boolean isOnDetecting = false;

    public ServerController() {
        retrofitBuilder = new RetrofitBuilder();
        context = MyApplication.getContext();
    }

    public String testRequest() {
        String result= null;

        Call<testMessage> call = retrofitBuilder.api.getTestResponse();
        call.enqueue(new Callback<testMessage>() {
            @Override
            public void onResponse(Call<testMessage> call, Response<testMessage> response) {
                if(response.isSuccessful()) {
                    //result = response.body().toString();

                    Log.d(TAG, response.body().toString());
                } else {
                    Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "RequestTest: body is missing");
                }
            }

            @Override
            public void onFailure(Call<testMessage> call, Throwable t) {

                Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "RequestTest: onFailure/ " + t);
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

    public void startDetect(ImageView captureView, ImageView detectView) {
        if(isOnDetecting) {
            Toast.makeText(context, "이미 작동 중입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageThread imageThread = new ImageThread(captureView, detectView);
        isOnDetecting = true;
        Handler handler = new Handler(imageThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "Handle");
                switch (msg.what){
                    case 0:
                        Log.e(TAG, "디바이스를 찾을 수 없습니다.");
                        Toast.makeText(context, "디바이스를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        /*
                        Bitmap bitmap = imageThread.getResult();
                        Log.d(TAG, "Detect");
                        if(bitmap != null) {
                            captureView.setImageBitmap(bitmap);
                            requestDetection(bitmap, this, detectView);
                        } else {
                            Log.e(TAG, "Image is null");
                        }
                         */
                        Log.d(TAG, "msg = 2 called");
                        Bitmap bitmap = imageThread.getResult();
                        captureView.setImageBitmap(bitmap);
                        requestDetection(bitmap, this, detectView);
                        break;
                    case 2:
                        Log.d(TAG, "msg = 3 called");
                        if(isOnDetecting) {
                            imageThread.start();
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
    public void stopDetect() {
        if(!isOnDetecting) {
            Toast.makeText(context, "꺼져있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void requestDetection(Bitmap picture, Handler handler, ImageView detectView) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Call<DetectionResponse> call = retrofitBuilder.api.detect(encoded);

        call.enqueue(new Callback<DetectionResponse>() {
            @Override
            public void onResponse(Call<DetectionResponse> call, Response<DetectionResponse> response) {
                if(response.isSuccessful()) {
                    DetectionResponse dR = response.body();
                    ArrayList<DetectionData> detectionData = dR.getResults();

                    Bitmap copied = picture.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(copied);
                    for(DetectionData data: detectionData) {
                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.FILL_AND_STROKE );
                        paint.setColor(Color.parseColor("#e1a43f"));
                        paint.setTextSize(30);
                        paint.setTextAlign(Paint.Align.LEFT);

                        Paint boxPaint = new Paint();
                        boxPaint.setStyle(Paint.Style.STROKE );
                        boxPaint.setStrokeWidth(20);
                        boxPaint.setColor(Color.parseColor("#e1a43f"));

                        canvas.drawText("confidence: " + data.confidence,data.x1 , data.y1-15 , paint);
                        canvas.drawRect(data.x1, data.y1,
                                data.x3, data.y3, boxPaint);

                        Log.d(TAG, "(" + data.x1 + "," + data.y1 +"), (" + data.x3 + "," + data.y3 + ")");
                    }

                    detectView.setImageBitmap(copied);

                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);

                } else {
                    Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response was inSuccessful");
                }
            }

            @Override
            public void onFailure(Call<DetectionResponse> call, Throwable t) {
                Toast.makeText(context, "다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Communication is fail");
            }
        });

    }

}

