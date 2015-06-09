package info.papdt.pano.ui.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import info.papdt.pano.R;
import static info.papdt.pano.ui.util.UiUtility.*;

public abstract class ToolbarActivity extends AppCompatActivity
{
	abstract protected int getLayoutResource();
	
	protected Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResource());
		
		mToolbar = $(this, R.id.toolbar);
		
		if (mToolbar == null) {
			throw new IllegalStateException("no toolbar");
		}
		
		setSupportActionBar(mToolbar);
		
		if (Build.VERSION.SDK_INT >= 21) {
			mToolbar.setElevation(10.6f);
		}
		
	}
}
