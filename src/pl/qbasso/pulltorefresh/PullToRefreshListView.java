package pl.qbasso.pulltorefresh;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PullToRefreshListView extends ListView {

	private int mState = 0;
	private float mPrevY = 0f;
	private int mCurrentMargin = 0;
	private static final int IDLE = 0;
	private static final int PULLING = 1;
	private static final int REFRESHING = 2;
	private static final float SCROLL_RESISTANCE = 1.5f;
	private static final int RELEASE_TO_REFRESH = 3;
	private View mHeaderView;
	private int headerViewHeight;
	private LinearLayout mHeaderContent;
	private PullToRefreshListner mListener;

	public interface PullToRefreshListner {
		public void onRefreshTriggered();
	}

	private OnGlobalLayoutListener mGlobalLayoutChangeListener = new OnGlobalLayoutListener() {

		@Override
		public void onGlobalLayout() {
			headerViewHeight = mHeaderView.getHeight();
			if (headerViewHeight > 0) {
				setHeaderMargin(-headerViewHeight);
			}
			mHeaderView.getViewTreeObserver()
					.removeGlobalOnLayoutListener(this);
		}
	};
	private TextView mRefreshState;

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mHeaderView = inflate(getContext(), R.layout.refresh_item, null);
		mHeaderContent = (LinearLayout) mHeaderView
				.findViewById(R.id.header_content);
		mRefreshState = (TextView) mHeaderView.findViewById(R.id.refresh_state);
		addHeaderView(mHeaderView);
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				mGlobalLayoutChangeListener);
	}

	private void setHeaderMargin(int margin) {
		mCurrentMargin = margin;
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mHeaderContent
				.getLayoutParams();
		params.setMargins(0, margin, 0, 0);
		mHeaderContent.setLayoutParams(params);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		float diff;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (getFirstVisiblePosition() == 0 && mState == IDLE) {
				mState = PULLING;
				mPrevY = ev.getY();
			} else {
				mState = IDLE;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mState == PULLING || mState == RELEASE_TO_REFRESH) {
				diff = ev.getY() - mPrevY;
				mPrevY = ev.getY();
				int newMargin = Math.max(mCurrentMargin
						+ (int) (diff / SCROLL_RESISTANCE), -headerViewHeight);
				if (newMargin != mCurrentMargin) {
					setHeaderMargin(newMargin);
					if (mState == PULLING && newMargin > headerViewHeight) {
						mState = RELEASE_TO_REFRESH;
					} else if (mState == RELEASE_TO_REFRESH
							&& newMargin < headerViewHeight) {
						mState = PULLING;
					}
					return true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (getFirstVisiblePosition() == 0) {
				if (mState == PULLING) {
					mState = IDLE;
					hideHeader(headerViewHeight, mCurrentMargin);
					return true;
				} else if (mState == RELEASE_TO_REFRESH) {
					mState = REFRESHING;
					if (mListener != null) {
						mListener.onRefreshTriggered();
					}
					hideHeader(0, mCurrentMargin);
					mRefreshState.setText("Refreshing...");
					postDelayed(new Runnable() {

						@Override
						public void run() {
							refreshDone();
						}
					}, 3000);
				}
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void hideHeader(final int headerHeight, final int margin) {
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0,
				-(headerHeight + margin));
		ta.setDuration(700);
		ta.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setHeaderMargin(-(headerHeight));
				scrollTo(0, 0);
			}
		});
		mHeaderView.startAnimation(ta);

	}

	public void setPullToRefreshListener(PullToRefreshListner mListener) {
		this.mListener = mListener;
	}

	public void refreshDone() {
		mState = IDLE;
		mRefreshState.setText(String.format(Locale.getDefault(),
				"Last refreshed: %s", (new SimpleDateFormat(
						"dd/MM/yyyy")).format(new Date(System
						.currentTimeMillis()))));
		hideHeader(headerViewHeight, mCurrentMargin);
	}

}
