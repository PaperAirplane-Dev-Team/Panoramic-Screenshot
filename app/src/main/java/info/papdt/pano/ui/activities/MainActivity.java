package info.papdt.pano.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import info.papdt.pano.R;
import info.papdt.pano.service.ScreenshotService;

public class MainActivity extends ToolbarActivity
{

	@Override
	protected int getLayoutResource() {
		return R.layout.main;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//startService(new Intent(this, ScreenshotService.class));
	}
}
