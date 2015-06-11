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
	
	private static Settings sInstance;
	
	private SharedPreferences mPref;
	
	static {
		DEFAULTS.put(SCREENSHOT_DIRECTORY, "/sdcard/Pictures/Screenshots");
		DEFAULTS.put(OUTPUT_DIRECTORY, "/sdcard/Pictures/Panoramic");
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
}
