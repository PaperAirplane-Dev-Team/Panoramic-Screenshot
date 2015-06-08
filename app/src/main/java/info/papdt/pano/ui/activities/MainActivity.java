package info.papdt.pano.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import info.papdt.pano.service.ScreenshotService;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startService(new Intent(this, ScreenshotService.class));
	}
}
