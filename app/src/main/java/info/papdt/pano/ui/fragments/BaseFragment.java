package info.papdt.pano.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment
{
	protected abstract int getLayoutResource();
	protected abstract void onInflated(View v);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(getLayoutResource(), container, false);
		onInflated(v);
		return v;
	}
}
