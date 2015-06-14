package info.papdt.pano.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import info.papdt.pano.R;
import static info.papdt.pano.ui.util.UiUtility.*;

public class DiscreteSeekBarPreference extends Preference implements DiscreteSeekBar.OnProgressChangeListener
{

	private int mMin = 0, mMax = 1, mValue = 0, mTmp = Integer.MIN_VALUE;
	private String mFormat;
	private DiscreteSeekBar mSeekbar;

	public DiscreteSeekBarPreference(Context context) {
		this(context, null);
	}

	public DiscreteSeekBarPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DiscreteSeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiscreteSeekBarPreference);
			mMax = a.getInt(R.styleable.DiscreteSeekBarPreference_dsbp_max, 1);
			mMin = a.getInt(R.styleable.DiscreteSeekBarPreference_dsbp_min, 0);
			mFormat = a.getString(R.styleable.DiscreteSeekBarPreference_dsbp_format);
			mValue = mMin;
			a.recycle();

			if (mFormat == null || mFormat.trim().equals("")) {
				mFormat = "%d";
			}
		}
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(R.layout.pref_discrete, parent, false);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		mSeekbar = $(view, R.id.dsbp_seek);
		mSeekbar.setMin(mMin);
		mSeekbar.setMax(mMax);
		mSeekbar.setIndicatorFormatter(mFormat);
		mSeekbar.setProgress(mValue);
		mSeekbar.setOnProgressChangeListener(this);
	}

	@Override
	public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

	}

	@Override
	public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
		if (fromUser) {
			mTmp = value;
		}
	}

	@Override
	public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
		if (mTmp >= mMin && mTmp <= mMax) {
			OnPreferenceChangeListener listener = getOnPreferenceChangeListener();
			if (listener != null) {
				listener.onPreferenceChange(this, mTmp);
			}

			mValue = mTmp;
			mTmp = Integer.MIN_VALUE;
		}
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		setValue(restorePersistedValue ? getPersistedInt(mValue) : (Integer) defaultValue);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInt(index, mMin);
	}

	public void setValue(int value) {
		if (value >= mMin && value <= mMax) {
			mValue = value;

			if (mSeekbar != null)
				mSeekbar.setProgress(value);
		}
	}

	public int getValue() {
		return mValue;
	}
}

