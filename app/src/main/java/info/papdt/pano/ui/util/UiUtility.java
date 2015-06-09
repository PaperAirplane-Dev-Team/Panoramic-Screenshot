package info.papdt.pano.ui.util;

import android.app.Activity;
import android.view.View;

public class UiUtility
{
	public static <T extends View> T $(Activity activity, int id) {
		return (T) activity.findViewById(id);
	}
	
	public static <T extends View> T $(View v, int id) {
		return (T) v.findViewById(id);
	}
}
