package com.capstone.smart_white_cane.server.esp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.net.URL;
import java.util.HashMap;

public class ImageThread extends Thread {

    private final String urlStr = "http://192.168.221.75/capture";
    private static HashMap<String, Bitmap> bitmapHash = new HashMap<String, Bitmap>();
    private Handler handler;
    private Bitmap result;

    @Override
    public void run() {
        Bitmap bitmap = null;
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
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(0);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        Log.d("ImageThread", "width: " + width + ", height: " + height);

        result = bitmap;

        Message msg = new Message();
        msg.what = 1;

        handler.sendMessage(msg);
    }

    public Bitmap getResult() { return result; }
    public void setHandler(Handler handler) { this.handler = handler; }
    public Looper getLooper() { return Looper.myLooper(); }

}

