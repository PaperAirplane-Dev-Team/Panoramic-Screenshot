package info.papdt.pano.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import info.papdt.pano.R;
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
	public void onBindViewHolder(ViewHolder h, int position) {
		File f = mFiles.get(position);
		
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
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		ImageView image;
		
		public ViewHolder(View v) {
			super(v);
			
			image = $(v, R.id.image_view);
		}
	}
}
