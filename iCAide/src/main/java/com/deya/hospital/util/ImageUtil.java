package com.deya.hospital.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageUtil {
	public static Bitmap retroflexion(Context ctx,int resId,int degree){
		Resources res = ctx.getResources();
		Bitmap  img = BitmapFactory.decodeResource(res,resId);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);       
        int width = img.getWidth();
        int height = img.getHeight();
        return Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
	}
}
