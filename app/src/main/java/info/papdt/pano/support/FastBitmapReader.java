package info.papdt.pano.support;

import android.graphics.Bitmap;

/*
 * This class is a faster reader of Bitmap
 * Because the method Bitmap.getPixel() is extremely slow.
 * This is an alternative.
 *
 */
public class FastBitmapReader
{
	private int[] mPixels;
	private int mWidth, mHeight;
	
	public FastBitmapReader(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		mPixels = new int[width * height];
		bmp.getPixels(mPixels, 0, width, 0, 0, width, height);
		bmp.recycle(); // Destroy the original bitmap for memory
		mWidth = width;
		mHeight = height;
	}
	
	public int getPixel(int x, int y) {
		return mPixels[x + y * mWidth];
	}
	
	public int[] getPixels() {
		return mPixels;
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}
	
	public void recycle() {
		mPixels = null;
	}
}
