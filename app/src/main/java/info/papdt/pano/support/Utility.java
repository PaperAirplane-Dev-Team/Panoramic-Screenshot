package info.papdt.pano.support;

import android.util.Log;

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
}
