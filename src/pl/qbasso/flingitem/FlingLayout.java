package pl.qbasso.flingitem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class FlingLayout extends RelativeLayout {
	
	private boolean mAlpha = false;

	public FlingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean onSetAlpha(int alpha) {
		// TODO Auto-generated method stub
		return super.onSetAlpha(alpha);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mAlpha) {
			canvas.saveLayerAlpha(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), 125, Canvas.ALL_SAVE_FLAG);
		}
		super.onDraw(canvas);
		if (mAlpha) {
			canvas.restore();
		}
	}
	
	

}
