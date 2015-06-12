package info.papdt.pano.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import info.papdt.pano.R;
import info.papdt.pano.ui.activities.PictureActivity;
import static info.papdt.pano.ui.util.UiUtility.*;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
{
	
	private LayoutInflater mInflater;
	private List<File> mFiles;
	private Context mContext;
	
	public ImageAdapter(Context context, List<File> files) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFiles = files;
		mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.image_card, parent, false));
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);
		
		holder.file = null;
	}

	@Override
	public void onBindViewHolder(ViewHolder h, int position) {
		File f = mFiles.get(position);
		h.file = f;
		
		Picasso.with(mContext)
				.load(f)
				.fit()
				.centerCrop()
				.into(h.image);
	}

	@Override
	public int getItemCount() {
		return mFiles.size();
	}
	
	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		
		ImageView image;
		File file = null;
		
		public ViewHolder(View v) {
			super(v);
			
			image = $(v, R.id.image_view);
			
			image.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			
			if (file == null) return;
			
			if (v == image) {
				Intent i = new Intent(mContext, PictureActivity.class);
				i.putExtra(PictureActivity.EXTRA_FILE, file.getAbsolutePath());
				
				if (mContext instanceof Activity) {
					ActivityOptionsCompat o = ActivityOptionsCompat.makeSceneTransitionAnimation(
						(Activity) mContext, image, PictureActivity.TRANSIT_PIC);
					ActivityCompat.startActivity((Activity) mContext, i, o.toBundle());
				} else {
					mContext.startActivity(i);
				}
			}
		}

	}
}
