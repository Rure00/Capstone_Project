package com.capstone.smart_white_cane.server.esp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.MySystem.MyApplication;
import com.capstone.smart_white_cane.R;
import com.capstone.smart_white_cane.server.ServerController;

import java.net.URL;
import java.util.HashMap;

public class ImageThread extends Thread {
    private final String urlStr = "http://192.168.154.75/capture";
    private static HashMap<String, Bitmap> bitmapHash = new HashMap<String, Bitmap>();
    private Handler handler;
    private Bitmap result;
    private ImageView capture;
    private ImageView detect;
    private Looper looper;
    private Context context;


    public ImageThread(ImageView capture, ImageView detect) {
        this.capture = capture;
        this.detect = detect;
        looper = Looper.myLooper();
        context = MyApplication.getContext();
    }

    @Override
    public void run() {
        Bitmap bitmap = null;

        Looper.prepare();
        looper = Looper.myLooper();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.my_block_image2);


        try {
            if (bitmapHash.containsKey(urlStr)) {
                Bitmap oldbitmap = bitmapHash.remove(urlStr);
                if(oldbitmap != null) {
                    oldbitmap.recycle();
                    oldbitmap = null;
                }
            }
            URL url = new URL(urlStr);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            bitmapHash.put(urlStr,bitmap);

        } catch (Exception e) {
            Log.d("ImageThread", e.toString());
        }



        if(bitmap == null) {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
            return;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);
        result = bitmap;

        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }

    public Bitmap getResult() { return result; }
    public void setHandler(Handler handler) { this.handler = handler; }
    public Looper getLooper() { return looper; }
}

