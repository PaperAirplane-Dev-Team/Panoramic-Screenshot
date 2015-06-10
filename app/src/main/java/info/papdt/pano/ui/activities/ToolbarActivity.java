package info.papdt.pano.ui.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import info.papdt.pano.R;
import static info.papdt.pano.ui.util.UiUtility.*;

public abstract class ToolbarActivity extends AppCompatActivity
{
	abstract protected int getLayoutResource();
	
	protected AppBarLayout mAppBar;
	protected Toolbar mToolbar;
	protected TabLayout mTabLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResource());
		
		mAppBar = $(this, R.id.appbar);
		mToolbar = $(this, R.id.toolbar);
		
		if (mToolbar == null || mAppBar == null) {
			throw new IllegalStateException("no toolbar");
		}
		
		setSupportActionBar(mToolbar);
		
		if (Build.VERSION.SDK_INT >= 21) {
			mAppBar.setElevation(10.6f);
		}
		
	}
	
	protected void setupTabs(ViewPager pager) {
		mTabLayout = $(this, R.id.tab);
		
		if (mTabLayout == null) {
			throw new IllegalStateException("no tabs");
		}
		
		mTabLayout.setupWithViewPager(pager);
		mTabLayout.setVisibility(View.VISIBLE);
	}
}
