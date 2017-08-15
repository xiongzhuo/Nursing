package com.deya.hospital.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragmentActivity;

/**
 * ClassName:. PagerTab【自定义控件，ViewPager的指针标题】 <br/>
 */
public class PagerTab extends ViewGroup {

	private ViewPager myViewPager;
	private PageListener myPageListener = new PageListener();// 用于注册给ViewPager监听状态和滚动
	private OnPageChangeListener myDelegatePageListener;// 用于通知外界ViewPager的状态和滚动
	private BaseFragmentActivity myActivity;

	private int myDividerPadding = 12;// 分割线上下的padding
	private int myDividerWidth = 1;// 分割线的宽度
	private int myDividerColor = 0xFFFFFFFF;// 分割线颜色
	private Paint myDividerPaint;// 分割线的画笔

	private int myIndicatorHeight = 2;// 指示器的高度
	private int myIndicatorWidth;// 指示器的宽度，是动态的随着tab的宽度变化
	private int myIndicatorLeft;// 指示器的距离左边的距离
	private int myIndicatorColor = R.color.blue;// 指示器颜色222
	private Paint myIndicatorPaint; // 指示器的画笔

	private int myContentWidth;// 记录自身内容的宽度
	private int myContentHeight;// 记录自身内容的高度

	private int myTabPadding = 10;// tab左右的内边距
	private int myTabTextSize = 17; // tab文字大小
	private int myTabBackgroundResId = R.drawable.tab_textview_bg_style;// tab背景资源
	private int myTabTextColorResId = R.drawable.circle_tab_color_style; // tab文字颜色
	private int myTabCount;// tab的个数

	private int myCurrentPosition = 0;// 当前光标所处的tab，规则是以光标的最左端所在的item的position
	private float myCurrentOffsetPixels;// 光标左边距离当前光标所处的tab的左边距离
	private int mySelectedPosition = 0; // 当前被选中的tab，用于记录手指点击tab的position

	private boolean myIsBeingDragged = false;// 是否处于拖动中
	private float myLastMotionX;// 上一次手指触摸的x坐标
	private VelocityTracker myVelocityTracker;// 用于记录速度的帮助类
	private int myMinimumVelocity;// 系统默认的最小满足fling的速度
	private int myMaximumVelocity;// 系统默认最大的fling速度
	private int myTouchSlop;// 系统默认满足滑动的最小位移

	private ScrollerCompat myScroller;// 处理滚动的帮助者
	private int myLastScrollX;// 记录上一次滚动的x位置，这是用于处理overScroll，实际位置可能会受到限制

	private int myMaxScrollX = 0;// 控件最大可滚动的距离
	private int mySplitScrollX = 0;// 根据item的个数，计算出每移动一个item控件需要移动的距离

	private EdgeEffectCompat myLeftEdge;// 处理overScroll的反馈效果
	private EdgeEffectCompat myRightEdge;
	private Context context;

	public PagerTab(Context context) {
		this(context, null);
		this.context = context;
	}

	public PagerTab(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	/**
	 * Creates a new instance of PagerTab.
	 *
	 * @param context
	 *            上下文
	 * @param attrs
	 *            属性
	 * @param defStyle
	 *            样式
	 */
	public PagerTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		if (context instanceof BaseFragmentActivity) {
			myActivity = (BaseFragmentActivity) context;
		}
		init();
		initPaint();
	}

	/**
	 * init:(初始化一些常量). <br/>
	 */
	private void init() {
		// 把一个值从dip转换成px
		myIndicatorHeight = dip2px(myIndicatorHeight);
		myDividerPadding = dip2px(myDividerPadding);
		myTabPadding = dip2px(myTabPadding);
		myDividerWidth = dip2px(myDividerWidth);
		myTabTextSize = dip2px(myTabTextSize);
		// 创建一个scroller
		myScroller = ScrollerCompat.create(myActivity);
		// 获取一个系统关于View的常量配置类
		final ViewConfiguration configuration = ViewConfiguration
				.get(myActivity);
		// 获取滑动的最小距离
		myTouchSlop = configuration.getScaledTouchSlop();
		// 获取fling的最小速度
		myMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		// 获取fling的最大速度
		myMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

		myLeftEdge = new EdgeEffectCompat(myActivity);
		myRightEdge = new EdgeEffectCompat(myActivity);
	}

	/**
	 * initPaint:(初始化画笔). <br/>
	 */
	private void initPaint() {
		myIndicatorPaint = new Paint();
		myIndicatorPaint.setAntiAlias(true);
		myIndicatorPaint.setStyle(Paint.Style.FILL);
		myIndicatorPaint.setColor(myIndicatorColor);

		myDividerPaint = new Paint();
		myDividerPaint.setAntiAlias(true);
		myDividerPaint.setStrokeWidth(myDividerWidth);
		myDividerPaint.setColor(myDividerColor);
	}

	/**
	 * setViewPager:(设置ViewPager). <br/>
	 */
	public void setViewPager(ViewPager viewPager) {
		if (viewPager == null || viewPager.getAdapter() == null) {
			throw new IllegalStateException(
					"ViewPager is null or ViewPager does not have adapter instance.");
		}
		myViewPager = viewPager;
		onViewPagerChanged();
	}

	private void onViewPagerChanged() {
		myViewPager.setOnPageChangeListener(myPageListener);// 给ViewPager设置监听
		myTabCount = myViewPager.getAdapter().getCount();// 有多少个tab需要看ViewPager有多少个页面
		for (int i = 0; i < myTabCount; i++) {
			if (myViewPager.getAdapter() instanceof IconTabProvider) {
				// 如果想要使用icon作为tab，则需要adapter实现IconTabProvider接口
				addIconTab(i,
						((IconTabProvider) myViewPager.getAdapter())
								.getPageIconResId(i));
			} else {
				Log.i("circle", myViewPager.getAdapter().getPageTitle(i)
						.toString());
				addTextTab(i, myViewPager.getAdapter().getPageTitle(i)
						.toString());
			}
		}
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		if (viewTreeObserver != null) {
			// 监听第一个的全局layout事件，来设置当前的mCurrentPosition，显示对应的tab
			viewTreeObserver
					.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							getViewTreeObserver().removeGlobalOnLayoutListener(
									this);// 只需要监听一次，之后通过listener回调即可
							myCurrentPosition = myViewPager.getCurrentItem();
							if (myDelegatePageListener != null) {
								myDelegatePageListener
										.onPageSelected(myCurrentPosition);
							}
						}
					});
		}
	}

	/**
	 * setOnPageChangeListener:(设置监听，因为Tab会监听ViewPager的状态，所以不要给ViewPager设置监听了，
	 * 设置给Tab，由Tab转发). <br/>
	 */
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		myDelegatePageListener = listener;
	}

	/**
	 * addTextTab:(添加文字tab). <br/>
	 */
	private void addTextTab(final int position, String title) {
		TextView tab = new TextView(myActivity);
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();
		tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, myTabTextSize);
		tab.setTextColor(getColorStateList(myTabTextColorResId));
		tab.setBackgroundDrawable(getDrawable(myTabBackgroundResId));
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		addTab(position, tab);
	}

	/**
	 * addIconTab:(添加图片icon). <br/>
	 */
	private void addIconTab(final int position, int resId) {
		ImageButton tab = new ImageButton(myActivity);
		tab.setImageResource(resId);
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		addTab(position, tab);
	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		// 设置tab的点击事件，当tab被点击时候切换pager的页面
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				myViewPager.setCurrentItem(position);
			}
		});
		tab.setPadding(myTabPadding, 0, myTabPadding, 0);
		addView(tab, position);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取控件自身的宽高,模式
		int widthSize = MeasureSpec.getSize(widthMeasureSpec)
				- getPaddingLeft() - getPaddingRight();
		int heightSize = MeasureSpec.getSize(heightMeasureSpec)
				- getPaddingBottom() - getPaddingBottom();

		int widthMode = getMode(widthMeasureSpec);
		int totalWidth = 0;
		int highest = 0;
		int goneChildCount = 0;
		for (int i = 0; i < myTabCount; i++) {
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				goneChildCount--;
				continue;
			}
			int childWidthMeasureSpec;
			int childHeightMeasureSpec;

			LayoutParams childLayoutParams = child.getLayoutParams();
			if (childLayoutParams == null) {
				childLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
			}

			if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,
						MeasureSpec.EXACTLY);
			} else if (childLayoutParams.width == LayoutParams.WRAP_CONTENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,
						MeasureSpec.AT_MOST);
			} else {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
						childLayoutParams.width, MeasureSpec.EXACTLY);
			}

			if (childLayoutParams.height == LayoutParams.MATCH_PARENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
						heightSize, MeasureSpec.EXACTLY);
			} else if (childLayoutParams.height == LayoutParams.WRAP_CONTENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
						heightSize, MeasureSpec.AT_MOST);
			} else {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
						childLayoutParams.height, MeasureSpec.EXACTLY);
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			totalWidth += childWidth;
			highest = highest < childHeight ? childHeight : highest;
		}

		if (totalWidth <= widthSize) {
			// 如果子Tab的总宽度小于PagerTab，则采用平分模式
//			int splitWidth = (int) (widthSize
//					/ (myTabCount - goneChildCount + 0.0f) + 0.5f);
			int outWith = (int) ((widthSize - totalWidth)
					/ (myTabCount - goneChildCount + 0.0f));
			for (int i = 0; i < myTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int normalwith = child.getMeasuredWidth() + outWith;

				int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
						normalwith, MeasureSpec.EXACTLY);
				int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
						child.getMeasuredHeight(), MeasureSpec.EXACTLY);
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			}
			myMaxScrollX = 0;
			mySplitScrollX = 0;
		} else {
			// 如果所有子View大于控件的宽度
			myMaxScrollX = totalWidth - widthSize;
			mySplitScrollX = (int) (myMaxScrollX
					/ (myTabCount - goneChildCount - 1.0f) + 0.5f);
		}

		if (widthMode == MeasureSpec.EXACTLY) {
			myContentWidth = widthSize;
		} else {
			myContentWidth = totalWidth;
		}
		int heightMode = getMode(heightMeasureSpec);
		if (heightMode == MeasureSpec.EXACTLY) {
			myContentHeight = heightSize;
		} else {
			myContentHeight = highest;
		}

		int measureWidth = myContentWidth + getPaddingLeft()
				+ getPaddingRight();
		int measureHeight = myContentHeight + getPaddingTop()
				+ getPaddingBottom();
		setMeasuredDimension(measureWidth, measureHeight);
	}

	/**
	 * getMode:【根据模型返回你想要的大小值】. <br/>
	 * .@param number .@return.<br/>
	 */
	private int getMode(int number) {
		return MeasureSpec.getMode(number);
	}

	@Override
	protected void onLayout(boolean change, int lftLine, int topLine,
			int rghtLine, int bottomLine) {
		// 这里简化了，没有考虑margin的情况
		if (change) {
			int height = bottomLine - topLine;// 控件供子View显示的高度
			int left = lftLine;
			for (int i = 0; i < myTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int top = (int) ((height - child.getMeasuredHeight()) / 2.0f + 0.5f);// 如果控件比tab要高，则居中显示
				int right = left + child.getMeasuredWidth();
				child.layout(left, top, right, top + child.getMeasuredHeight());// 摆放tab
				left = right;// 因为是水平摆放的，所以为下一个准备left值
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final int height = getHeight();
		// 画指示器
		canvas.drawRect(myIndicatorLeft, height - myIndicatorHeight,
				myIndicatorLeft + myIndicatorWidth, height, myIndicatorPaint);

		// 画分割线
		for (int i = 0; i < myTabCount - 1; i++) {
			// 分割线的个数比tab的个数少一个
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				continue;
			}
			if (child != null) {
				canvas.drawLine(child.getRight(), myDividerPadding,
						child.getRight(), myContentHeight - myDividerPadding,
						myDividerPaint);
			}
		}
		// 因为overScroll效果是一个持续效果，所以需要持续画
		boolean needsInvalidate = false;
		if (!myLeftEdge.isFinished()) {
			// 如果效果没停止
			final int restoreCount = canvas.save();// 先保存当前画布
			final int heightEdge = getHeight() - getPaddingTop()
					- getPaddingBottom();
			final int widthEdge = getWidth();
			canvas.rotate(270);
			canvas.translate(-heightEdge + getPaddingTop(), 0);
			myLeftEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= myLeftEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (!myRightEdge.isFinished()) {
			final int restoreCount = canvas.save();
			final int widthEdge = getWidth();
			final int heightEdge = getHeight() - getPaddingTop()
					- getPaddingBottom();
			canvas.rotate(90);
			canvas.translate(-getPaddingTop(), -(widthEdge + myMaxScrollX));
			myRightEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= myRightEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (needsInvalidate) {
			postInvalidate();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if (myIsBeingDragged && action == MotionEvent.ACTION_MOVE) {
			// 当已经处于拖动，并且当前事件是MOVE，直接消费掉
			return true;
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			myLastMotionX = x; // 记录住当前的x坐标
			myIsBeingDragged = !myScroller.isFinished();// 如果按下的时候还在滚动，则把状态处于拖动状态
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			final float x = ev.getX();
			final int xDiff = (int) Math.abs(x - myLastMotionX);// 计算两次的差值
			if (xDiff > myTouchSlop) {
				// 如果大于最小移动的距离，则把状态改变为拖动状态
				myIsBeingDragged = true;
				myLastMotionX = x;
				ViewParent parent = getParent();// 并请求父View不要再拦截自己触摸事件，交给自己处理
				if (parent != null) {
					parent.requestDisallowInterceptTouchEvent(true);
				}
			}
			break;
		}
		case MotionEvent.ACTION_CANCEL:// 当手指离开或者触摸事件取消的时候，把拖动状态取消掉
		case MotionEvent.ACTION_UP:
			myIsBeingDragged = false;
			break;
		default:
			break;
		}
		return myIsBeingDragged;// 如果是拖动状态，则拦截事件，交给自己的onTouch处理
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (myVelocityTracker == null) {
			myVelocityTracker = VelocityTracker.obtain();
		}
		myVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			// 如果是down事件，记录住当前的x坐标
			final float x = ev.getX();
			if (!myScroller.isFinished()) {
				myScroller.abortAnimation();
			}
			myLastMotionX = x;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			final float x = ev.getX();
			final float deltaX = x - myLastMotionX;
			if (!myIsBeingDragged) {
				// 如果还没有处于拖动，则判断两次的差值是否大于最小拖动的距离
				if (Math.abs(deltaX) > myTouchSlop) {
					myIsBeingDragged = true;
				}
			}
			if (myIsBeingDragged) {
				// 如果处于拖动状态，记录住x坐标
				myLastMotionX = x;
				onMove(deltaX);
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			if (myIsBeingDragged) {
				final VelocityTracker velocityTracker = myVelocityTracker;
				// 先对速度进行一个调整，第一个参数是时间单位，1000毫秒，第二个参数是最大速度。
				velocityTracker.computeCurrentVelocity(1000, myMaximumVelocity);
				float velocity = velocityTracker.getXVelocity();// 获取水平方向上的速度
				onUp(velocity);
			}
		}
			break;
		case MotionEvent.ACTION_CANCEL:
			myIsBeingDragged = false;
			if (myVelocityTracker != null) {
				myVelocityTracker.recycle();
				myVelocityTracker = null;
			}
			break;
		default:
			break;
		}
		return true;
	}

	private void onMove(float xpoint) {
		if (myMaxScrollX <= 0) {
			if (myViewPager.isFakeDragging() || myViewPager.beginFakeDrag()) {
				myViewPager.fakeDragBy(xpoint);
			}
		} else {
			int scrollByX = -(int) (xpoint + 0.5);
			if (getScrollX() + scrollByX < 0) {
				scrollByX = 0 - getScrollX();
				myLeftEdge.onPull(Math.abs(xpoint) / getWidth());
			}
			if (getScrollX() + scrollByX > myMaxScrollX) {
				scrollByX = myMaxScrollX - getScrollX();
				myRightEdge.onPull(Math.abs(xpoint) / getWidth());
			}
			scrollBy(scrollByX, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private void onUp(float velocity) {
		if (myMaxScrollX <= 0) {
			if (myViewPager.isFakeDragging()) {
				myViewPager.endFakeDrag();
			}
		} else {
			if (Math.abs(velocity) <= myMinimumVelocity) {
				return;
			}
			myScroller.fling(getScrollX(), 0, -(int) (velocity + 0.5), 0, 0,
					myMaxScrollX, 0, 0, 270, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public void computeScroll() {
		if (myScroller.computeScrollOffset()) {
			int oldX = myLastScrollX;
			myLastScrollX = myScroller.getCurrX();
			if (myLastScrollX < 0 && oldX >= 0) {
				myLeftEdge.onAbsorb((int) myScroller.getCurrVelocity());
			} else if (myLastScrollX > myMaxScrollX && oldX <= myMaxScrollX) {
				myRightEdge.onAbsorb((int) myScroller.getCurrVelocity());
			}
			int xspace = myLastScrollX;
			if (myLastScrollX < 0) {
				xspace = 0;
			} else if (myLastScrollX > myMaxScrollX) {
				xspace = myMaxScrollX;
			}
			scrollTo(xspace, 0);
		}
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/**
	 * checkAndcalculate:(检测mIndicatorOffset的合法性，并计算出其他有关tab的属性值 ). <br/>
	 */
	private void checkAndcalculate() {
		// 如果指示器起始位置比第一个tab的起始位置还要小，纠正为第一个tab的起始位置，指示器宽度就是第一个tab的宽度
		final View firstTab = getChildAt(0);
		if (myIndicatorLeft < firstTab.getLeft()) {
			myIndicatorLeft = firstTab.getLeft();
			myIndicatorWidth = firstTab.getWidth();
		}
		// 如果指示器起始位置比最后一个tab的起始位置还要大，纠正为最后一个tab的起始位置，指示器宽度就是最后一个tab的宽度
		View lastTab = getChildAt(myTabCount - 1);
		if (myIndicatorLeft > lastTab.getLeft()) {
			myIndicatorLeft = lastTab.getLeft();
			myIndicatorWidth = lastTab.getWidth();
		}
		// 通过指示器的起始位置计算出当前处于第几个position，并且计算出已经偏移了多少，偏移量是以当前所处的tab的宽度的百分比
		for (int i = 0; i < myTabCount; i++) {
			View tab = getChildAt(i);
			if (myIndicatorLeft < tab.getLeft()) {
				myCurrentPosition = i - 1;
				View currentTab = getChildAt(myCurrentPosition);
				myCurrentOffsetPixels = (myIndicatorLeft - currentTab.getLeft())
						/ (currentTab.getWidth() + 0.0f);
				break;
			}
		}
	}

	/**
	 * scrollSelf:(滚动到指定的child). <br/>
	 */
	public void scrollSelf(int position, float offset) {
		if (position >= myTabCount) {
			return;
		}
		final View tab = getChildAt(position);
		myIndicatorLeft = (int) (tab.getLeft() + tab.getWidth() * offset + 0.5);
		int rightPosition = position + 1;
		if (offset > 0 && rightPosition < myTabCount) {
			View rightTab = getChildAt(rightPosition);
			myIndicatorWidth = (int) (tab.getWidth() * (1 - offset)
					+ rightTab.getWidth() * offset + 0.5);
		} else {
			myIndicatorWidth = tab.getWidth();
		}
		checkAndcalculate();

		int newScrollX = position * mySplitScrollX
				+ (int) (offset * mySplitScrollX + 0.5);
		if (newScrollX < 0) {
			newScrollX = 0;
		}
		if (newScrollX > myMaxScrollX) {
			newScrollX = myMaxScrollX;
		}
		// scrollTo(newScrollX, 0);//滑动
		int duration = 100;
		if (mySelectedPosition != -1) {
			duration = (Math.abs(mySelectedPosition - position)) * 100;
		}
		myScroller.startScroll(getScrollX(), 0, (newScrollX - getScrollX()), 0,
				duration);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/**
	 * selectTab:(选中指定位置的Tab). <br/>
	 */
	public void selectTab(int position) {
		for (int i = 0; i < myTabCount; i++) {
			View tab = getChildAt(i);
			if (tab != null) {
				tab.setSelected(position == i);
			}
		}
	}

	/**
	 * ClassName:. PageListener【ViewPager的OnPageChangeListener实现类，
	 * 因为我们需要在PagerTab中获取PagerView的监听，以便可以调整tab】 <br/>
	 */
	private class PageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset,
				final int positionOffsetPixels) {
			// 根据VierPager的偏移值来滚动tab
			scrollSelf(position, positionOffset);
			if (myDelegatePageListener != null) {
				// 这个是提供给外部的
				myDelegatePageListener.onPageScrolled(position, positionOffset,
						positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				mySelectedPosition = -1;
			}
			if (myDelegatePageListener != null) {
				myDelegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			System.out.println("onPageSelected:" + position);
			mySelectedPosition = position;
			selectTab(position);
			if (myDelegatePageListener != null) {
				myDelegatePageListener.onPageSelected(position);
			}
		}
	}

	/**
	 * ClassName:. IconTabProvider【如果指示器希望是图片，则继承该接口】 <br/>
	 */
	public interface IconTabProvider {
		public int getPageIconResId(int position);

		public int getPageSelectedIconResId();
	}

	/**
	 * dip2px:(dip转换px). <br/>
	 */
	private int dip2px(int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * getDrawable:(获取drawable). <br/>
	 */
	private Drawable getDrawable(int resId) {
		return context.getResources().getDrawable(resId);
	}

	/**
	 * getColorStateList:(获取颜色选择器). <br/>
	 */
	private ColorStateList getColorStateList(int resId) {
		return context.getResources().getColorStateList(resId);
	}
}
