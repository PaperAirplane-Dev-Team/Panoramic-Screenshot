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
import android.widget.Toast;

import java.util.List;

import info.papdt.pano.R;
import info.papdt.pano.processor.ScreenshotComposer;
import info.papdt.pano.service.ScreenshotService;
import static info.papdt.pano.support.Utility.*;
import static info.papdt.pano.BuildConfig.DEBUG;

public class ScreenshotActivity extends Activity
{
	private static final String TAG = ScreenshotActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: Let the user confirm
		final Intent i = new Intent(this, ScreenshotService.class);
		bindService(i, new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				List<String> files = ((ScreenshotService.ScreenshotBinder) binder).getFiles();
				
				if (files != null && files.size() > 0) {
					new ScreenshotTask().execute(files);
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
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			prog = new ProgressDialog(ScreenshotActivity.this);
			prog.setMessage(getString(R.string.plz_wait));
			prog.setCancelable(false);
			prog.show();
		}

		@Override
		protected String doInBackground(List<String>... params) {
			return ScreenshotComposer.getInstance().compose(params[0].toArray(new String[params[0].size()]), new ScreenshotComposer.ProgressListener() {
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
			
			prog.dismiss();
			
			if (result != null) {
				prog.dismiss();
				notifyMediaScanner(ScreenshotActivity.this, result);
				Toast.makeText(
					ScreenshotActivity.this, 
					String.format(getString(R.string.saved), result),
					Toast.LENGTH_LONG).show();
			} else {
				// If null?
			}
		}

	}
}
