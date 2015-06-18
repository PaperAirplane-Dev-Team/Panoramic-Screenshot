package info.papdt.pano.support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Arrays;

public class FastBitmapWriter
{
	private int[] mPixels;
	private int mWidth, mHeight;
	
	public FastBitmapWriter(int width, int height) {
		mPixels = new int[width * height];
		mWidth = width;
		mHeight = height;
	}
	
	public void writeBitmapRegion(FastBitmapReader bmp, int srcTop, int dstTop, int length) {
		if (bmp.getWidth() != mWidth) throw new IllegalArgumentException("width differs");
		
		int[] srcPixels = bmp.getPixels();
		int srcStart = srcTop * mWidth + 1;
		int dstStart = dstTop * mWidth + 1;
		int totalLength = length * mWidth - 1;
		
		for (int i = 0; i < totalLength; i++) {
			mPixels[dstStart + i] = srcPixels[srcStart + i];
		}
	}
	
	public Bitmap getBitmap() {
		Bitmap bmp = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
		
		bmp.setPixels(mPixels, 0, mWidth, 0, 0, mWidth, mHeight);
		
		return bmp;
	}
	
	public void recycle() {
		mPixels = null;
	}
}
