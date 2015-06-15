package info.papdt.pano.support;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class Settings
{
	private static final String PREF = "pref";
	
	private static final Map<String, Object> DEFAULTS = new HashMap<>();
	
	public static final String SCREENSHOT_DIRECTORY = "screenshot_directory";
	public static final String OUTPUT_DIRECTORY = "output_directory";
	public static final String MATCHING_THRESHOLD = "matching_threshold";
	public static final String TOP_SHADOW_DEPTH = "top_shadow_depth";
	
	private static Settings sInstance;
	
	private SharedPreferences mPref;
	
	static {
		DEFAULTS.put(SCREENSHOT_DIRECTORY, "/sdcard/Pictures/Screenshots");
		DEFAULTS.put(OUTPUT_DIRECTORY, "/sdcard/Pictures/Panoramic");
		DEFAULTS.put(MATCHING_THRESHOLD, 8);
		DEFAULTS.put(TOP_SHADOW_DEPTH, 10);
		
	}
	
	public static final Settings getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new Settings(context);
		}
		
		return sInstance;
	}
	
	private Settings(Context context) {
		mPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
	}
	
	public <T> T getDefault(String key, Class<T> type) {
		return (T) DEFAULTS.get(key);
	}
	
	public String getString(String key) {
		return mPref.getString(key, getDefault(key, String.class));
	}
	
	public Settings putString(String key, String value) {
		mPref.edit().putString(key, value).commit();
		return this;
	}
	
	public int getInt(String key) {
		return mPref.getInt(key, getDefault(key, int.class));
	}
	
	public Settings putInt(String key, int value) {
		mPref.edit().putInt(key, value).commit();
		return this;
	}
}
