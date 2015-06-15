package info.papdt.pano.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.preference.Preference;

import com.nononsenseapps.filepicker.FilePickerActivity;

import info.papdt.pano.R;
import info.papdt.pano.support.Settings;
import info.papdt.pano.ui.preference.DiscreteSeekBarPreference;
import static info.papdt.pano.ui.util.UiUtility.*;

public class SettingsFragment extends BasePreferenceFragment
{
	private static final int REQUEST_SHOT = 233;
	private static final int REQUEST_OPT = 234;
	
	private Settings mSettings;
	
	protected Preference mShotDir;
	protected Preference mOptDir;
	protected DiscreteSeekBarPreference mMatchingThreshold;
	protected DiscreteSeekBarPreference mTopShadow;
	
	@Override
	protected int getPreferenceResource() {
		return R.xml.settings;
	}

	@Override
	protected void onInit() {
		mSettings = Settings.getInstance(getActivity());
		
		mShotDir = $(this, Settings.SCREENSHOT_DIRECTORY);
		mOptDir = $(this, Settings.OUTPUT_DIRECTORY);
		mMatchingThreshold = $(this, Settings.MATCHING_THRESHOLD);
		mTopShadow = $(this, Settings.TOP_SHADOW_DEPTH);
		
		reload();
		
		register(mShotDir, mOptDir, mMatchingThreshold, mTopShadow);
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		if (pref == mShotDir) {
			startChooser(REQUEST_SHOT);
			return true;
		} else if (pref == mOptDir) {
			startChooser(REQUEST_OPT);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mMatchingThreshold) {
			mSettings.putInt(
				Settings.MATCHING_THRESHOLD,
				(int) newValue);
			return true;
		} else if (preference == mTopShadow) {
			mSettings.putInt(
				Settings.TOP_SHADOW_DEPTH,
				(int) newValue);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK && (requestCode == REQUEST_SHOT || requestCode == REQUEST_OPT)) {
			mSettings.putString(
				requestCode == REQUEST_SHOT ? Settings.SCREENSHOT_DIRECTORY : Settings.OUTPUT_DIRECTORY,
				data.getData().toString().substring(7)); // Remove "file://"
			
			reload();
		}
	}
	
	private void reload() {
		mShotDir.setSummary(mSettings.getString(Settings.SCREENSHOT_DIRECTORY));
		mOptDir.setSummary(mSettings.getString(Settings.OUTPUT_DIRECTORY));
		mMatchingThreshold.setValue(mSettings.getInt(Settings.MATCHING_THRESHOLD));
		mTopShadow.setValue(mSettings.getInt(Settings.TOP_SHADOW_DEPTH));
	}
	
	private void startChooser(int code) {
		Intent i = new Intent(getActivity(), FilePickerActivity.class);
		i.setAction(Intent.ACTION_GET_CONTENT);
		i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
		i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
		i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
		startActivityForResult(i, code);
	}
	
}
