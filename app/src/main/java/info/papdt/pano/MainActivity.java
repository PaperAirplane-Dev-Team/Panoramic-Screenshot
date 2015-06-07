package info.papdt.pano;

import android.app.*;
import android.os.*;

import info.papdt.pano.processor.ScreenshotComposer;

public class MainActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				ScreenshotComposer composer = ScreenshotComposer.getInstance();
				composer.test();
			}
		}).start();
	}
}
