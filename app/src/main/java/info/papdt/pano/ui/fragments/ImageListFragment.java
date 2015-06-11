package info.papdt.pano.ui.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.papdt.pano.R;
import info.papdt.pano.support.Settings;
import info.papdt.pano.ui.adapter.ImageAdapter;
import static info.papdt.pano.ui.util.UiUtility.*;

public class ImageListFragment extends BaseFragment
{
	private RecyclerView mRecycler;
	private List<File> mFiles = new ArrayList<>();
	private ImageAdapter mAdapter;
	private GridLayoutManager mManager;
	private String mOptDir;
	
	@Override
	protected int getLayoutResource() {
		return R.layout.image_list;
	}

	@Override
	protected void onInflated(View v) {
		mRecycler = $(v, R.id.list);
		
		if (getActivity() != null) {
			init();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (mRecycler != null) {
			init();
		}
	}
	
	private void init() {
		mAdapter = new ImageAdapter(getActivity(), mFiles);
		mManager = new GridLayoutManager(getActivity(), 2);
		mRecycler.setLayoutManager(mManager);
		mRecycler.setAdapter(mAdapter);
		
		Settings settings = Settings.getInstance(getActivity());
		mOptDir = settings.getString(Settings.OUTPUT_DIRECTORY);
		
		new RefreshTask().execute();
	}
	
	private class RefreshTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			List<File> list = new ArrayList<>();
			
			// TODO Output path should be changeable
			File[] files = new File(mOptDir).listFiles();
			
			if (files == null) files = new File[0];
			
			for (File f : files) {
				if (f.isFile() && f.getAbsolutePath().endsWith(".png")) {
					list.add(f);
				}
			}
			
			Collections.sort(list, new Comparator<File>() {
					@Override
					public int compare(File p1, File p2) {
						return p1.lastModified() < p2.lastModified() ? 0 : -1;
					}
			});
			
			mFiles.clear();
			mFiles.addAll(list);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			mAdapter.notifyDataSetChanged();
		}
		
	}
	
}
