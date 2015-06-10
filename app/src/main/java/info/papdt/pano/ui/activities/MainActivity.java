package info.papdt.pano.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.support.design.widget.FloatingActionButton;

import info.papdt.pano.R;
import info.papdt.pano.service.ScreenshotService;
import info.papdt.pano.ui.fragments.ImageListFragment;
import static info.papdt.pano.ui.util.UiUtility.*;

public class MainActivity extends ToolbarActivity
{
	
	private FloatingActionButton mFAB;

	@Override
	protected int getLayoutResource() {
		return R.layout.main;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mFAB = $(this, R.id.main_fab);
		
		mFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startService(new Intent(MainActivity.this, ScreenshotService.class));
				
				finish(); // Finish after clicking
			}
		});
		
		getFragmentManager().beginTransaction().replace(R.id.container, new ImageListFragment()).commit();
	}
}
