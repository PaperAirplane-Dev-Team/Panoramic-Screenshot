package info.papdt.pano.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public abstract class BasePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
	protected abstract int getPreferenceResource();
	protected abstract void onInit();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(getPreferenceResource());
		
		onInit();
	}

	protected void register(Preference... preferences) {
		for (Preference p : preferences) {
			p.setOnPreferenceChangeListener(this);
			p.setOnPreferenceClickListener(this);
		}
	}
}
