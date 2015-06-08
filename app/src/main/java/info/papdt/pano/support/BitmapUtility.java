package info.papdt.pano.support;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class BitmapUtility
{
	/*public static Bitmap bitmapThresholding(Bitmap originalBmp, int threshold) {
		Bitmap bmp = toGrayscale(originalBmp);
		Bitmap out = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
		
		for (int x = 0; x < bmp.getWidth(); x++) {
			for (int y = 0; y < bmp.getWidth(); y++) {
				int color = Color.red(bmp.getPixel(x, y));
				
				if (color < threshold) {
					out.setPixel(x, y, Color.rgb(0, 0, 0));
				} else {
					out.setPixel(x, y, Color.rgb(255, 255, 255));
				}
			}
		}
		
		return out;
		
	}
	
	public static int calculateThresholdValue(Bitmap originalBmp) {
		Bitmap bmp = toGrayscale(originalBmp);
		
		int T = 128;
		int Ts = 127;
		
		while (T != Ts) {
			
			T = Ts;
			
			int T1 = 0;
			int T2 = 0;
			
			int G1 = 0;
			int G2 = 0;
			int G1_count = 0;
			int G2_count = 0;
		
			for (int x = 0; x < bmp.getWidth(); x++) {
				for (int y = 0; y < bmp.getWidth(); y++) {
					int color = Color.red(bmp.getPixel(x, y));
					
					if (color < T) {
						G1 += color;
						G1_count++;
					} else {
						G2 += color;
						G2_count++;
					}
				}
			}
			
			T1 = G1 / G1_count;
			T2 = G2 / G2_count;
		
			Ts = (T1 + T2) / 2;
		}
		
		return T;
	}*/
	
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {        
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();    

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}
}
