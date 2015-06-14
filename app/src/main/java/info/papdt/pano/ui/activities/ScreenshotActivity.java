package info.papdt.pano.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import android.support.design.widget.FloatingActionButton;

import java.util.List;
import java.util.Map;

import info.papdt.pano.R;
import info.papdt.pano.processor.ScreenshotComposer;
import info.papdt.pano.service.ScreenshotService;
import info.papdt.pano.support.Settings;
import info.papdt.pano.ui.fragments.TempSettingsFragment;
import static info.papdt.pano.support.Utility.*;
import static info.papdt.pano.ui.util.UiUtility.*;
import static info.papdt.pano.BuildConfig.DEBUG;

public class ScreenshotActivity extends ToolbarActivity
{
	private static final String TAG = ScreenshotActivity.class.getSimpleName();

	private Settings mSettings;
	private TempSettingsFragment mFragment;
	private List<String> mFiles;
	private FloatingActionButton mFAB;
	
	@Override
	protected int getLayoutResource() {
		return R.layout.screenshot;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mFAB = $(this, R.id.shot_fab);
		mFAB.setVisibility(View.GONE);
		mFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new ScreenshotTask().execute(mFiles);
			}
		});
		
		mSettings = Settings.getInstance(this);
		
		mFragment = new TempSettingsFragment();
		
		// TODO: Let the user confirm
		// TODO: Temporary settings
		// TODO: Finish after composing and go back to main.
		final Intent i = new Intent(this, ScreenshotService.class);
		bindService(i, new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				mFiles = ((ScreenshotService.ScreenshotBinder) binder).getFiles();
				
				if (mFiles != null && mFiles.size() > 0) {
					getFragmentManager().beginTransaction().replace(R.id.frame, mFragment).commit();
					mFAB.setVisibility(View.VISIBLE);
				} else {
					// If null?
				}
				
				stopService(i);
			}
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				
			}
		}, 0);
	}
	
	private class ScreenshotTask extends AsyncTask<List<String>, String, String> {
		ProgressDialog prog;
		Map<String, Object> settings;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			prog = new ProgressDialog(ScreenshotActivity.this);
			prog.setMessage(getString(R.string.plz_wait));
			prog.setCancelable(false);
			prog.show();
			
			settings = mFragment.getSettings();
		}

		@Override
		protected String doInBackground(List<String>... params) {
			ScreenshotComposer composer = ScreenshotComposer.getInstance();
			composer.setOutputDir((String) settings.get(Settings.OUTPUT_DIRECTORY));
			return composer.compose(params[0].toArray(new String[params[0].size()]), new ScreenshotComposer.ProgressListener() {
				@Override
				public void onAnalyzingImage(int i, int j, int total) {
					publishProgress(String.format(getString(R.string.analyzing_image), i, j, total));
				}
				
				@Override
				public void onComposingImage() {
					publishProgress(getString(R.string.composing_image));
				}
			});
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			prog.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result != null) {
				notifyMediaScanner(ScreenshotActivity.this, result);
				Toast.makeText(
					ScreenshotActivity.this, 
					String.format(getString(R.string.saved), result),
					Toast.LENGTH_LONG).show();
				
				mFAB.postDelayed(new Runnable() {
					@Override
					public void run() {
						prog.dismiss();
						finish();
					}
				}, 1000);
			} else {
				prog.dismiss();
				finish();
				// If null?
			}
		}

	}
}
