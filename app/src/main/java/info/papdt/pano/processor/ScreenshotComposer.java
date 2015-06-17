package info.papdt.pano.processor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

import static info.papdt.pano.BuildConfig.DEBUG;
import static info.papdt.pano.support.Utility.*;
import static info.papdt.pano.support.BitmapUtility.*;

/*
 * To compose a list of screenshots into one
 * But preserving the areas where they are no difference
 *
 */
public class ScreenshotComposer
{
	private static final String TAG = ScreenshotComposer.class.getSimpleName();
	//private static final String SEPERATOR = ",";
	
	static class Region {
		int startLine;
		int endLine;
	}
	
	public static interface ProgressListener {
		void onAnalyzingImage(int i, int j, int total);
		void onComposingImage();
	}
	
	private static ScreenshotComposer sInstance;
	
	private String mOutDir = "/sdcard/Pictures/Panoramic";
	private float mThreshold = 0.08f;
	private int mStatusBarHeight = 40; // px
	private int mShadowHeight = 10; // px
	
	public static final ScreenshotComposer getInstance() {
		if (sInstance == null) {
			sInstance = new ScreenshotComposer();
		}
		
		return sInstance;
	}
	
	private ScreenshotComposer() {
		
	}
	
	public void setOutputDir(String opt) {
		mOutDir = opt;
		
		File out = new File(mOutDir);
		if (!out.exists()) {
			out.mkdirs();
			out.mkdir();
		}
	}
	
	public void setThreshold(float threshold) {
		if (threshold > 1 || threshold < 0)
			throw new IllegalArgumentException("illegal threshold");
		
		mThreshold = threshold;
	}
	
	public void setStatusBarHeight(int height) {
		mStatusBarHeight = height;
	}
	
	public void setShadowHeight(int height) {
		mShadowHeight = height;
		
		if (DEBUG) {
			Log.d(TAG, "shadow height " + mShadowHeight);
		}
	}
	
	public String compose(String[] images, ProgressListener listener) {
		File[] files = new File[images.length];
		
		for (int i = 0; i < images.length; i++) {
			files[i] = new File(images[i]);
			
			if (DEBUG) {
				Log.d(TAG, "Adding " + images[i]);
			}
			
			if (!files[i].exists()) {
				
				if (DEBUG) {
					Log.d(TAG, images[i] + " not exist");
				}
				
				return null;
			}
		}
		
		return compose(files, listener);
	}
	
	public String compose(File[] images, ProgressListener listener) {
		Region[] regions = new Region[images.length];
		
		Bitmap currentBmp = null, nextBmp = null;
		int fullHeight = 0, fullWidth = 0;
		
		for (int i = 0; i < images.length - 1; i++) {
			long thisStartTime = System.currentTimeMillis();
			
			if (listener != null) {
				listener.onAnalyzingImage(i + 1, i + 2, images.length);
			}
			
			long decodeStartTime = System.currentTimeMillis();
			
			// Intented to use thresholding but failed. Help needed.
			if (currentBmp == null) {	
				currentBmp = BitmapFactory.decodeFile(images[i].getAbsolutePath());
				fullWidth = currentBmp.getWidth();
			}
			
			nextBmp = BitmapFactory.decodeFile(images[i + 1].getAbsolutePath());
			
			if (DEBUG) {
				Log.d(TAG, "decode time " + (System.currentTimeMillis() - decodeStartTime));
			}
			
			if (currentBmp.getHeight() != nextBmp.getHeight()) {
				
				if (DEBUG) {
					Log.d(TAG, "Height different");
				}
				
				return null;
			}
			
			// First, find the max different region of the two bitmaps
			
			long headStartTime = System.currentTimeMillis();
			
			// Go through from the first line
			int start = -1;
			for (int j = mStatusBarHeight + 1; j < currentBmp.getHeight(); j++) {
				if (compareLines(currentBmp, nextBmp, j)) {
					start = j;
					break;
				}
			}
			
			if (start == -1) {
				
				if (DEBUG) {
					Log.d(TAG, "start line not found");
				}
				
				return null;
			}
			
			// Go through from the last line
			int end = -1;
			for (int j = currentBmp.getHeight() - 1; j > start; j--) {
				if (compareLines(currentBmp, nextBmp, j)) {
					end = j;
					break;
				}
			}
			
			if (end == -1) {
				
				if (DEBUG) {
					Log.d(TAG, "End line not found");
				}
				
				return null;
			}
			
			if (DEBUG) {
				Log.d(TAG, "head match time " + (System.currentTimeMillis() - headStartTime));
			}
			
			if (i == 0) {
				Region region = new Region();
				region.startLine = start;
				region.endLine = end;
				regions[0] = region;
				fullHeight += currentBmp.getHeight();
			}
			
			// Second, find out the max common region
			
			// Generate a hash string of the bitmaps
			long regionStartTime = System.currentTimeMillis();
			Long[] hashCurrent = buildHashOfRegion(currentBmp, start + mShadowHeight, end);
			Long[] hashNext = buildHashOfRegion(nextBmp, start + mShadowHeight, end);
			
			if (DEBUG) {
				Log.d(TAG, "region build time " + (System.currentTimeMillis() - regionStartTime));
			}
			
			/*if (DEBUG) {
				Log.d(TAG, "hashCurrent = " + hashCurrent);
				Log.d(TAG, "hashNext = " + hashNext);
			}*/
			
			int length = end - start - mShadowHeight - 1;
			
			/*String matchSub = null;*/
			List<Long> matchSub = null;
			int matchLength = arrayHeadTailMatch(hashNext, hashCurrent, length, mThreshold);
			
			/*for (int j = length; j > 0; j--) {
				//List<Long> hashSubNext = buildHashOfSubregion(hashNext, j);
				//List<Long> hashSubCur = buildHashOfSubregionFromBottom(hashCurrent, j);
				
				//int result = arrayContainsEx(hashCurrent, hashSub, mThreshold);
				
			//	if (arrayCompareEx(hashNext, hashCurrent, 0, hashCurrent.size() - j - 1, j, mThreshold)) {
					//matchSub = hashSubNext;
			//		matchLength = j;
			//		break;
			//	}
				
				/*String hashSub = buildHashStringOfSubregion(hashNext, j);
				
				if (hashCurrent.contains(hashSub)) {
					matchSub = hashSub;
					matchLength = j;
					break;
				}*/
			//}
			
			/*if (matchSub == null) {
				return null;
			}*/
			
			//int index = arrayContainsEx(hashNext, matchSub, 0.0f);
			
			start = start/* + index*/ + matchLength + mShadowHeight + 1;
			
			if (DEBUG) {
				Log.d(TAG, "Different region between " + i + " and " + (i + 1));
				Log.d(TAG, start + "-" + end);
			}
			
			Region region = new Region();
			region.startLine = start;
			region.endLine = end;
			regions[i + 1] = region;
			fullHeight += end - start;
			
			if (DEBUG) {
				Log.d(TAG, i + " total time " + (System.currentTimeMillis() - thisStartTime));
			}
			
			currentBmp.recycle();
			currentBmp = nextBmp;
			nextBmp = null;
		}
		
		
		currentBmp.recycle();
		currentBmp = null;
		
		/*currentBmp = BitmapFactory.decodeFile(images[0].getAbsolutePath());
		nextBmp = BitmapFactory.decodeFile(images[1].getAbsolutePath());
		
		if (DEBUG) {
			Log.d(TAG, "hash1: " + getHashOfLine(currentBmp, 150));
			Log.d(TAG, "hash2: " + getHashOfLine(nextBmp, 150));
		}*/
		
		if (DEBUG) {
			Log.d(TAG, "fullHeight = " + fullHeight);
		}
		
		if (listener != null) {
			listener.onComposingImage();
		}
		
		Bitmap out = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(out);
		int totalHeight = 0;
		for (int i = 0; i < images.length; i++) {
			currentBmp = BitmapFactory.decodeFile(images[i].getAbsolutePath());
			
			Region region = regions[i];
			//int height = currentBmp.getHeight();
			
			Rect src = new Rect();
			Rect dst = new Rect();
			src.left = 0;
			src.right = fullWidth;
			dst.left = 0;
			dst.right = fullWidth;
			if (i == 0) {
				src.top = 0;
				src.bottom = region.endLine;
				dst.top = 0;
				dst.bottom = src.bottom;
				
				canvas.drawBitmap(currentBmp, src, dst, null);
				
				src.top = region.endLine;
				src.bottom = currentBmp.getHeight();
				dst.bottom = fullHeight;
				dst.top = dst.bottom - (src.bottom - src.top);
				
				canvas.drawBitmap(currentBmp, src, dst, null);
				
				totalHeight += region.endLine;
				
			} else {
				src.top = region.startLine;
				src.bottom = region.endLine;
				dst.top = totalHeight;
				dst.bottom = dst.top + (src.bottom - src.top);
				
				canvas.drawBitmap(currentBmp, src, dst, null);
				
				totalHeight += (src.bottom - src.top);
			}
			
			currentBmp.recycle();
			currentBmp = null;
		}
		
		// Write
		File outFile = new File(mOutDir + "/Panoramic-" + System.currentTimeMillis() + ".png");
		if (outFile.exists()) {
			outFile.delete();
		}
		
		try {
			outFile.createNewFile();
		} catch (IOException e) {
			
		}
		
		FileOutputStream opt = null;
		try {
			opt = new FileOutputStream(outFile);
		} catch (IOException e) {
			
		}
		
		if (opt != null) {
			out.compress(Bitmap.CompressFormat.PNG, 100, opt);
		}
		
		try {
			opt.close();
		} catch (IOException e) {
			
		}
		
		return outFile.getAbsolutePath();
	}
	
	// True = different
	private boolean compareLines(Bitmap bmp1, Bitmap bmp2, int line) {
		int diff = 0;
		for (int i = 0; i < bmp1.getWidth(); i++) {
			if (bmp1.getPixel(i, line) != bmp2.getPixel(i, line)) {
				diff++;
			}
		}
		
		return diff > bmp1.getWidth() / 10 * mThreshold;
	}
	
	private long getHashOfLine(Bitmap bmp, int line) {
		//CRC32 hash = new CRC32();
		
		long hash = 0;
		
		for (int i = 0; i < bmp.getWidth(); i++) {
			hash += bmp.getPixel(i, line);
		}
		
		return hash;
	}
	
	private Long[] buildHashOfRegion(Bitmap bmp, final int start, final int end) {
		//List<Long> list = new ArrayList<Long>();
		Long[] array = new Long[end - start];
		
		/*for (int i = start; i < end; i++) {
			array[i - start] = getHashOfLine(bmp, i);
		}*/
		
		return new MultiThreadTask<Bitmap, Long>(bmp, array) {
			@Override
			protected void doExecute(Bitmap arg, int taskStart, int taskLength) {
				for (int i = 0; i <= taskLength; i++) {
					long hash = getHashOfLine(arg, start + taskStart + i);
					
					setResult(taskStart + i, hash);
				}
			}
		}.execute(Runtime.getRuntime().availableProcessors() * 2); // 2 * CPU cores
		
		//return array;
	}
	
}
