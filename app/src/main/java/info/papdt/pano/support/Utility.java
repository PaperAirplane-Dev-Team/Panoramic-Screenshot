package info.papdt.pano.support;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static info.papdt.pano.BuildConfig.DEBUG;

public class Utility
{
	private static final String TAG = Utility.class.getSimpleName();
	
	public static <T> int arrayContainsEx(List<T> list, List<T> sub, float threshold) {
		int length = sub.size();
		
		int index = -1;
		for (int i = 0; i < list.size() - length; i++) {
			if (arrayCompareEx(list, sub, i, 0, length, threshold)) {
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	public static <T> boolean arrayCompareEx(List<T> a, List<T> b, int aStart, int bStart, int length, float threshold) {
		int unmatches = 0;
		for (int i = 0; i < length; i++) {
			T valueA = a.get(aStart + i);
			T valueB = b.get(bStart + i);
			
			if (valueA == null || !valueA.equals(valueB)) {
				unmatches++;
			}
		}
		
		/*if (DEBUG) {
			Log.d(TAG, "ummatches: " + unmatches + " of " + length);
		}*/
		
		return unmatches <= length * threshold;
	}
	
	public static void notifyMediaScanner(Context context, String path) {
		Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		i.setData(Uri.fromFile(new File(path)));
		context.sendBroadcast(i);
	}
	
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		
		return result;
	}
	
	public static int dp2pxY(Context context, int dip) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int px = Math.round(dip * (displayMetrics.ydpi / 160.0f));       
		return px;
	}
}
