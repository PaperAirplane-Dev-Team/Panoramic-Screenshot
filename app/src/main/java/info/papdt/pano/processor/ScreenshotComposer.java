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
	
	// Remember we are in testing stage.
	private static String[] TEST_SCREENSHOT_LIST = {
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-21-41-46.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-21-41-55.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-21-42-04.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-21-42-10.png",
		/*"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-19-42-01.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-19-42-04.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-19-42-07.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-19-42-12.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-19-42-17.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-19-42-22.png",
		"/sdcard/Pictures/Screenshots/Screenshot_2015-06-07-19-42-30.png",*/
	};
	
	private static ScreenshotComposer sInstance;
	
	// Should be customizable
	private String mOutDir = "/sdcard/Pictures/Panoramic";
	
	public static final ScreenshotComposer getInstance() {
		if (sInstance == null) {
			sInstance = new ScreenshotComposer();
		}
		
		return sInstance;
	}
	
	private ScreenshotComposer() {
		
		File out = new File(mOutDir);
		if (!out.exists()) {
			out.mkdirs();
			out.mkdir();
		}
	}
	
	public Bitmap compose(String[] images) {
		File[] files = new File[images.length];
		
		for (int i = 0; i < images.length; i++) {
			files[i] = new File(images[i]);
			
			if (!files[i].exists()) {
				throw new RuntimeException("NOT EXIST?");
			}
		}
		
		return compose(files);
	}
	
	public Bitmap compose(File[] images) {
		Region[] regions = new Region[images.length];
		
		Bitmap currentBmp = null, nextBmp = null;
		int fullHeight = 0, fullWidth = 0;
		
		for (int i = 0; i < images.length - 1; i++) {
			if (currentBmp == null) {
				currentBmp = BitmapFactory.decodeFile(images[i].getAbsolutePath());
				fullWidth = currentBmp.getWidth();
			}
			
			nextBmp = BitmapFactory.decodeFile(images[i + 1].getAbsolutePath());
			
			if (currentBmp.getHeight() != nextBmp.getHeight()) {
				
				if (DEBUG) {
					Log.d(TAG, "Height different");
				}
				
				return null;
			}
			
			// First, find the max different region of the two bitmaps
			
			// Go through from the first line
			int start = -1;
			// TODO Should start from where just below statusbar
			for (int j = 40; j < currentBmp.getHeight(); j++) {
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
			
			if (i == 0) {
				Region region = new Region();
				region.startLine = start;
				region.endLine = end;
				regions[0] = region;
				fullHeight += currentBmp.getHeight();
			}
			
			// Second, find out the max common region
			
			// Generate a hash string of the bitmaps
			List<Long> hashCurrent = buildHashOfRegion(currentBmp, start + 40, end);
			List<Long> hashNext = buildHashOfRegion(nextBmp, start + 40, end);
			
			/*if (DEBUG) {
				Log.d(TAG, "hashCurrent = " + hashCurrent);
				Log.d(TAG, "hashNext = " + hashNext);
			}*/
			
			int length = end - start - 40;
			
			/*String matchSub = null;*/
			List<Long> matchSub = null;
			int matchLength = -1;
			
			for (int j = length; j > 0; j--) {
				List<Long> hashSub = buildHashOfSubregion(hashNext, j);
				
				int result = arrayContainsEx(hashCurrent, hashSub, 0.08f);
				
				if (result != -1) {
					matchSub = hashSub;
					matchLength = j;
					break;
				}
				
				/*String hashSub = buildHashStringOfSubregion(hashNext, j);
				
				if (hashCurrent.contains(hashSub)) {
					matchSub = hashSub;
					matchLength = j;
					break;
				}*/
			}
			
			/*if (matchSub == null) {
				return null;
			}*/
			
			int index = arrayContainsEx(hashNext, matchSub, 0.0f);
			
			start = start + index + matchLength + 40;
			
			if (DEBUG) {
				Log.d(TAG, "Different region between " + i + " and " + (i + 1));
				Log.d(TAG, start + "-" + end);
			}
			
			Region region = new Region();
			region.startLine = start;
			region.endLine = end;
			regions[i + 1] = region;
			fullHeight += end - start;
			
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
		
		return out;
	}
	
	public Bitmap test() {
		
		if (!DEBUG) throw new IllegalAccessError();
		
		return compose(TEST_SCREENSHOT_LIST);
	}
	
	// True = different
	private boolean compareLines(Bitmap bmp1, Bitmap bmp2, int line) {
		int diff = 0;
		for (int i = 0; i < bmp1.getWidth(); i++) {
			if (bmp1.getPixel(i, line) != bmp2.getPixel(i, line)) {
				diff++;
			}
		}
		
		return diff > 4;
	}
	
	private long getHashOfLine(Bitmap bmp, int line) {
		CRC32 hash = new CRC32();
		
		for (int i = 0; i < bmp.getWidth(); i++) {
			hash.update(bmp.getPixel(i, line));
		}
		
		return hash.getValue();
	}
	
	private List<Long> buildHashOfRegion(Bitmap bmp, int start, int end) {
		List<Long> list = new ArrayList<Long>();
		
		for (int i = start; i < end; i++) {
			list.add(getHashOfLine(bmp, i));
		}
		
		return list;
	}
	
	private List<Long> buildHashOfSubregion(List<Long> hash, int length) {
		List<Long> list = new ArrayList<Long>();
		
		for (int i = 0; i < length; i++) {
			list.add(hash.get(i));
		}
		
		return list;
	}
	
}
