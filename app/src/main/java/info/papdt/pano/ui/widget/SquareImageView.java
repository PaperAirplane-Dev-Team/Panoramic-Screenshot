package info.papdt.pano.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView
{
	
	public SquareImageView(Context context) {
		this(context, null);
	}
	
	public SquareImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = getMeasuredWidth();
		
		setMeasuredDimension(width, width);
	}
	
}
