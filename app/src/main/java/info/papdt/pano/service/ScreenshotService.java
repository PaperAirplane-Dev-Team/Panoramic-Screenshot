package info.papdt.pano.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.papdt.pano.R;
import info.papdt.pano.ui.activities.ScreenshotActivity;
import static info.papdt.pano.BuildConfig.DEBUG;

public class ScreenshotService extends Service
{
	private static final String TAG = ScreenshotService.class.getSimpleName();
	
	
	public static final String ACTION_SCREENSHOT = "info.papdt.pano.action.TAKE_SCREENSHOT";
	public static final String EXTRA_PATHS = "paths";
	
	// TODO Make this changeable
	private static final String SCREENSHOT_DIR = "/sdcard/Pictures/Screenshots";
	
	private Intent mIntent = null;
	private Notification mNotification = null;
	private NotificationManager mNotificationManager = null;
	private ScreenshotObserver mObserver = null;
	
	private List<String> mFiles = null;
	
	private ScreenshotBinder mBinder;

	@Override
	public void onCreate() {
		super.onCreate();
		
		mBinder = new ScreenshotBinder();
	}

	@Override
	public IBinder onBind(Intent i) {
		return mBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mObserver.stopWatching();
		stopForeground(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// Create the intent
		mIntent = new Intent(ACTION_SCREENSHOT);
		mIntent.setClass(this, ScreenshotActivity.class);
		
		// Create the notification
		mNotification = new Notification.Builder(this)
							.setSmallIcon(android.R.color.transparent)
							.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap())
							.setContentTitle(getString(R.string.app_name))
							.setContentText(getString(R.string.notifi_first))
							//.setSubText(getString(R.string.notifi_tip))
							.setPriority(Notification.PRIORITY_DEFAULT)
							.build();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		mFiles = new ArrayList<>();
		
		mObserver = new ScreenshotObserver();
		mObserver.startWatching();
							
		startForeground(R.drawable.ic_launcher, mNotification);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	public class ScreenshotBinder extends Binder {
		public List<String> getFiles() {
			return mFiles;
		}
	}
	
	private class ScreenshotObserver extends FileObserver {
		public ScreenshotObserver() {
			// TODO: The path should be changable
			super(SCREENSHOT_DIR, FileObserver.CREATE);
		}

		@Override
		public void onEvent(int event, String path) {
			if (event != FileObserver.CREATE) return;
			
			if (path.endsWith(".png")) {
				String file = SCREENSHOT_DIR + "/" + path;
				mFiles.add(file);
				
				if (DEBUG) {
					Log.d(TAG, "Adding " + file);
				}
				
				//mIntent.putExtra(EXTRA_PATHS, mFiles.toArray(new String[mFiles.size()]));
				
				PendingIntent i = PendingIntent.getActivity(ScreenshotService.this, 0, mIntent, 0);
				
				mNotification.setLatestEventInfo(
					ScreenshotService.this,
					getString(R.string.app_name),
					String.format(getString(R.string.notifi_count), mFiles.size() + 1),
					i);
				
				mNotification.extras.putString(Notification.EXTRA_SUB_TEXT, getString(R.string.notifi_tip));
				
				mNotificationManager.notify(R.drawable.ic_launcher, mNotification);
			}
		}

	}
}
