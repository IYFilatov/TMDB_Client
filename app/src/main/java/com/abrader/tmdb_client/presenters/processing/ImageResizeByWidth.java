package com.abrader.tmdb_client.presenters.processing;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.squareup.picasso.Transformation;

public class ImageResizeByWidth implements Transformation {

    private int targetWidth;

    public ImageResizeByWidth(int targetWidth){
        this.targetWidth = targetWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        if (targetWidth < 1) {
            return source;
        }
        if ((width < 1) || (height < 1)){
            return Bitmap.createBitmap(targetWidth, 1, Bitmap.Config.RGB_565);
        }

        //int size = Math.min(source.getWidth(), source.getHeight());
        float aspectRatio = width / (float) height;
        int targetHeight = Math.round(targetWidth / aspectRatio);
        float scaleWidth = ((float) targetWidth) / width;
        float scaleHeight = ((float) targetHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap result = Bitmap.createBitmap(source, 0, 0, targetWidth, targetHeight, matrix, false);

        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override public String key() { return "square()"; }
}
