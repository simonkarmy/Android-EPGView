package com.sgerges.epgview.animations;

import android.graphics.Rect;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

import com.sgerges.epgview.core.EPGView;
import com.sgerges.epgview.core.FreeFlowItem;
import com.sgerges.epgview.core.LayoutChangeSet;


public class NoAnimationLayoutAnimator implements FreeFlowLayoutAnimator {

	private LayoutChangeSet changes;
	
	@Override
	public LayoutChangeSet getChangeSet() {
		return changes;
	}

	@Override
	public void cancel() {
		
	}

	@Override
	public void animateChanges(LayoutChangeSet changes,
                               EPGView callback) {
		this.changes = changes;
		for(Pair<FreeFlowItem, Rect> item : changes.getMoved()){
			Rect r = item.first.frame;
			View v = item.first.view;
			int wms = MeasureSpec.makeMeasureSpec(r.right-r.left, MeasureSpec.EXACTLY);
			int hms = MeasureSpec.makeMeasureSpec(r.bottom-r.top, MeasureSpec.EXACTLY);
			v.measure(wms,hms );
			v.layout(r.left, r.top, r.right, r.bottom);	
		}
		callback.onLayoutChangeAnimationsCompleted(this);
		
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public void onContainerTouchDown(MotionEvent event) {
		cancel();
	}

}
