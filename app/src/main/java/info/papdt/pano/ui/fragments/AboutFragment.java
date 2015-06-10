package info.papdt.pano.ui.fragments;

import android.view.View;
import android.widget.TextView;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import info.papdt.pano.R;
import static info.papdt.pano.ui.util.UiUtility.*;

public class AboutFragment extends BaseFragment
{

	@Override
	protected int getLayoutResource() {
		return R.layout.about;
	}

	@Override
	protected void onInflated(View v) {
		TextView text = $(v, R.id.about_text);
		
		text.setText(Html.fromHtml(getString(R.string.about)));
		text.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
}
