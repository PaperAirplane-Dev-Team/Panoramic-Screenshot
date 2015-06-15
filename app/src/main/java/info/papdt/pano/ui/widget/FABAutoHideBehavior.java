package info.papdt.pano.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;

import info.papdt.pano.R;
import static info.papdt.pano.ui.util.UiUtility.*;

/*
 * A hacky Behavior class for FloatingActionButton to collapse
 * With the AppBarLayout
 * Only works with a collapsing toolbar (AppBarLayout)
 *
 */
public class FABAutoHideBehavior extends CoordinatorLayout.Behavior
{
	public FABAutoHideBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(final CoordinatorLayout parent, final View child, MotionEvent ev) {
		if (!(child instanceof FloatingActionButton)) throw new IllegalArgumentException("Cannot work for non-FAB views");
		
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
		View anchor = $(parent, lp.getAnchorId());
		
		if (anchor == null) {
			throw new IllegalStateException("must be anchored");
		}
		
		Object tag = anchor.getTag(R.id.appbar);
		
		if (tag == null) tag = false;
		
		if (!Boolean.parseBoolean(tag.toString())) {
			final View appbar = $(parent, R.id.appbar);
			final View toolbar = $(parent, R.id.toolbar);
			
			anchor.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
				@Override
				public void onDraw() {
					int childHeight = child.getMeasuredHeight() + (parent.getMeasuredHeight() - child.getBottom());
					int toolbarHeight = toolbar.getMeasuredHeight();
					float translationY = appbar.getTranslationY();
					child.setTranslationY(- translationY / (float) toolbarHeight * childHeight);
				}
			});
			
			anchor.setTag(R.id.appbar, true);
		}
		
		return super.onInterceptTouchEvent(parent, child, ev);
	}
	
}
