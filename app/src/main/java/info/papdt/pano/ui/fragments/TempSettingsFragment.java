package info.papdt.pano.ui.fragments;

import android.preference.Preference;

import java.util.HashMap;
import java.util.Map;

import info.papdt.pano.support.Settings;

public class TempSettingsFragment extends SettingsFragment
{

	@Override
	protected void onInit() {
		super.onInit();
		
		mOptDir.setEnabled(false);
		mShotDir.setEnabled(false);
	}

	@Override
	public boolean onPreferenceChange(Preference p1, Object p2) {
		return false;
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		return false;
	}
	
	public Map<String, Object> getSettings() {
		Map<String, Object> map = new HashMap<>();
		
		map.put(Settings.SCREENSHOT_DIRECTORY, mShotDir.getSummary());
		map.put(Settings.OUTPUT_DIRECTORY, mOptDir.getSummary());
		map.put(Settings.MATCHING_THRESHOLD, mMatchingThreshold.getValue());
		map.put(Settings.TOP_SHADOW_DEPTH, mTopShadow.getValue());
		
		return map;
	}
	
}
