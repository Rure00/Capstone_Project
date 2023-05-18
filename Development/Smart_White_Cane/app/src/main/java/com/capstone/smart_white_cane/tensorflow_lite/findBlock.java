package com.capstone.smart_white_cane.tensorflow_lite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

import com.MySystem.MyApplication;
import com.capstone.smart_white_cane.R;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.util.List;

public class findBlock {
    private String TAG = "findBlock";

    public void find(Bitmap bitmap) {
        Context context = MyApplication.getContext();
        //bitmap = BitmapFactory.decodeResource(context.getResources(), );
        TensorImage image = TensorImage.fromBitmap(bitmap);
        try{
            ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions.builder()
                    .setMaxResults(5)
                    .setScoreThreshold(0.5f)
                    .build();
            // String modelPath = "best-fp16.tflite"; -> Input tensor has type kTfLiteFloat32
            // String modelPath = "best-fp16-version2-half.tflite"; -> Input tensor has type kTfLiteFloat32
            String modelPath = "best-add.tflite"; //->Mobile SSD models are expected to have exactly 4 outputs, found 1

            ObjectDetector objectDetector = ObjectDetector.createFromFileAndOptions(
                    context, modelPath, options
            );

            List<Detection> results = objectDetector.detect(image);
            debugPrint(results);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void debugPrint(List<Detection> lists) {
        for (Detection detection: lists) {
            RectF rectF = detection.getBoundingBox();
            Log.d(TAG, "BoundingBox: " + rectF.left + ", " + rectF.right + ", "
                    + rectF.top + ", " + rectF.bottom + ", ");

            for(Category category: detection.getCategories()) {
                String label = category.getLabel();
                int confidence = (int) category.getScore() * 100;
                Log.d(TAG, "Label is " + label +" and confidence is " + confidence);
            }
        }
    }
}
