package info.papdt.pano.ui.activities;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;

import android.support.v4.view.ViewCompat;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import info.papdt.pano.R;
import static info.papdt.pano.ui.util.UiUtility.*;

public class PictureActivity extends ToolbarActivity
{
	public static final String EXTRA_FILE = "file";
	public static final String TRANSIT_PIC = "picture";
	
	private SubsamplingScaleImageView mImageView;

	@Override
	protected int getLayoutResource() {
		return R.layout.picture;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mImageView = $(this, R.id.picture);
		mImageView.setMaxScale(3.0f);
		mImageView.setImage(ImageSource.uri(getIntent().getStringExtra(EXTRA_FILE)));
		mImageView.setZoomEnabled(true);
		mImageView.setScaleAndCenter(1.0f, new PointF(0, 0));
		
		setAppBarAlpha(0.7f);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);
		
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideOrShowToolbar();
			}
		});
	}
}
