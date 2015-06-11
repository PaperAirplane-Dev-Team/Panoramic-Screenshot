package info.papdt.pano.ui.fragments;

import android.preference.Preference;

import info.papdt.pano.R;
import info.papdt.pano.support.Settings;
import static info.papdt.pano.ui.util.UiUtility.*;

public class SettingsFragment extends BasePreferenceFragment
{
	private Settings mSettings;
	
	private Preference mShotDir;
	private Preference mOptDir;
	
	@Override
	protected int getPreferenceResource() {
		return R.xml.settings;
	}

	@Override
	protected void onInit() {
		mSettings = Settings.getInstance(getActivity());
		
		mShotDir = $(this, Settings.SCREENSHOT_DIRECTORY);
		mOptDir = $(this, Settings.OUTPUT_DIRECTORY);
		
		mShotDir.setSummary(mSettings.getString(Settings.SCREENSHOT_DIRECTORY));
		mOptDir.setSummary(mSettings.getString(Settings.OUTPUT_DIRECTORY));
		
		register(mShotDir, mOptDir);
	}

	@Override
	public boolean onPreferenceClick(Preference p1) {
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference p1, Object p2) {
		// TODO: Implement this method
		return false;
	}
	
}
