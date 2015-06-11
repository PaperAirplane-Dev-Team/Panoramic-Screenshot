package info.papdt.pano.ui.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.support.design.widget.FloatingActionButton;

import android.support.v4.view.ViewPager;
import android.support.v13.app.FragmentStatePagerAdapter;

import info.papdt.pano.R;
import info.papdt.pano.service.ScreenshotService;
import info.papdt.pano.ui.fragments.AboutFragment;
import info.papdt.pano.ui.fragments.ImageListFragment;
import info.papdt.pano.ui.fragments.SettingsFragment;
import static info.papdt.pano.ui.util.UiUtility.*;

public class MainActivity extends ToolbarActivity
{
	
	private Fragment[] mFragments = {
		new ImageListFragment(),
		new SettingsFragment(),
		new AboutFragment(),
	};
	
	private ViewPager mPager;
	private FloatingActionButton mFAB;

	@Override
	protected int getLayoutResource() {
		return R.layout.main;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPager = $(this, R.id.pager);
		mFAB = $(this, R.id.main_fab);
		
		mFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startService(new Intent(MainActivity.this, ScreenshotService.class));
				
				finish(); // Finish after clicking
			}
		});
		
		// Setup pager
		// TODO Switch to About when no screenshots found.
		final String[] titles = getResources().getStringArray(R.array.main_tabs);
		mPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
				@Override
				public int getCount() {
					return mFragments.length;
				}

				@Override
				public Fragment getItem(int position) {
					return mFragments[position];
				}
				
				@Override
				public CharSequence getPageTitle(int position) {
					return titles[position];
				}
		});
		setupTabs(mPager);
		
	}
}
