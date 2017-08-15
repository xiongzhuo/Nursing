package com.deya.hospital.widget.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.im.sdk.dy.common.utils.LogUtil;

import java.util.List;

public abstract class TextViewGroup extends RelativeLayout implements
		OnClickListener {
	/**
	 * item 形状
	 * 
	 * @author yung
	 * @date 创建时间：2016年1月14日 上午11:15:48
	 * @version 1.0
	 */
	enum ShowType {
		/*
		 * 正方形
		 */
		square,
		/*
		 * 矩形，高度自动
		 */
		rectangle,
		/*
		 * 矩形,高度为宽的一半
		 */
		rectangle_half,
		/*
		 * 矩形,高度为宽的三分之二
		 */
		rectangle_third
	}

	/**
	 * 选择类型
	 * 
	 * @author yung
	 * @date 创建时间：2016年1月14日 上午11:16:00
	 * @version 1.0
	 */
	public enum Select {
		/**
		 * 单选
		 */
		radio,
		/**
		 * 多选
		 */
		multiple,
		/**
		 * 单选，可清除
		 */
		radio_empty
	}

	/**
	 * 自定义控件item对象
	 */
	public class TextViewGroupItem {
		int position;
		String text="";
		String department="";
		Object tag;
		boolean status;

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		public Object getTag() {
			return tag;
		}

		public void setTag(Object tag) {
			this.tag = tag;
		}
	}

	/**
	 * 新建ITEM对象
	 * @return
	 */
	public TextViewGroupItem NewTextViewGroupItem() {
		return new TextViewGroupItem();
	}

	public Context context;
	/**
	 * 字体大小
	 */
	public float viewgroup_textSize;
	/**
	 * 默认字体颜色
	 */
	public int viewgroup_textColor;
	/**
	 * 选中字体颜色
	 */
	public int viewgroup_textColor_s;
	/**
	 * 默认背景
	 */
	public int viewgroup_textBackground;
	/**
	 * 选中背景
	 */
	public int viewgroup_textBackground_s;

	/**
	 * 控件宽度
	 */
	public int layout_width;
	/**
	 * textview 水平 margin
	 */
	public int viewgroup_itemmargin;
	/**
	 * textview 垂直 margin
	 */
	public int viewgroup_linemargin;
	/**
	 * textview padding
	 */
	public int viewgroup_itempadding;
	/**
	 * 控件 选择属性
	 * 如  单选  多选  单选可以清除
	 * 参考enum Select
 	 */
	public int viewgroup_select;

	/**
	 * textview 宽
	 */
	public int item_width;
	/**
	 * textview 高
	 */
	public int item_height;
	/**
	 * 每列显示textview数量
	 * 默认为0  即不限
	 */
	public int viewgroup_columnNum;
	/**
	 * textview 形状
	 * 参考enum ShowType
	 */
	public int viewgroup_shape;
	/**
	 *  每行的textview 是否自动拉伸 以填满一行
	 *  
	 */
	public boolean viewgroup_overspread;

	private Resources res;

	public TextViewGroup(Context context) {
		super(context);
		init(context);
	}

	public TextViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.TextViewGroup);

		viewgroup_textColor = array.getColor(
				R.styleable.TextViewGroup_viewgroup_textColor, 0XFF00FF00); // 提供默认值，放置未指定
		viewgroup_textColor_s = array.getColor(
				R.styleable.TextViewGroup_viewgroup_textColor_s, 0XFFFFFF); // 提供默认值，放置未指定
		viewgroup_textSize = array.getDimension(
				R.styleable.TextViewGroup_viewgroup_textSize, 24);
		viewgroup_textSize = px2sp(context, viewgroup_textSize);

		viewgroup_itemmargin = array.getDimensionPixelSize(
				R.styleable.TextViewGroup_viewgroup_itemmargin, 0);
		viewgroup_linemargin = array.getDimensionPixelSize(
				R.styleable.TextViewGroup_viewgroup_linemargin, 0);
		viewgroup_itempadding = array.getDimensionPixelSize(
				R.styleable.TextViewGroup_viewgroup_itempadding, 0);
		viewgroup_textBackground = array.getResourceId(
				R.styleable.TextViewGroup_viewgroup_textBackground, -1);
		viewgroup_textBackground_s = array.getResourceId(
				R.styleable.TextViewGroup_viewgroup_textBackground_s, -1);
		viewgroup_select = array.getInt(
				R.styleable.TextViewGroup_viewgroup_select, 0);
		viewgroup_shape = array.getInt(
				R.styleable.TextViewGroup_viewgroup_shape, 0);
		viewgroup_overspread = array.getBoolean(
				R.styleable.TextViewGroup_viewgroup_overspread, false);
		viewgroup_columnNum = array.getInteger(
				R.styleable.TextViewGroup_viewgroup_columnNum, 0);

		array.recycle();
		init(context);
	}

	private void init(Context context) {
		this.context = context;
	}

	private static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public List<TextViewGroupItem> _dataList;

	/**
	 * 初始化数据
	 * 
	 * @param dataList
	 */
	public void setTextViews(List<TextViewGroupItem> dataList) {
		this._dataList = dataList;
		if (layout_width > 0) {
			setTextViewsTrue();
		} else {
			ViewTreeObserver viewTreeObserver = getViewTreeObserver();
			if (viewTreeObserver.isAlive()) {
				viewTreeObserver
						.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
							@Override
							public void onGlobalLayout() {

								layout_width = getWidth();
								LogUtil.d("addOnGlobalLayoutListener",
										"layout_width>>" + layout_width);
								if (layout_width > 0) {
									getViewTreeObserver()
											.removeGlobalOnLayoutListener(this);

									setTextViewsTrue();
								}
							}
						});
			}
		}
	}

	/**
	 * 设置控件显示
	 * 根据不同类型的控件计算并设置   textview 相关属性 
	 */
	public abstract void setTextViewsTrue();

	/**
	 * 获取控件宽
	 * 
	 * @param v
	 * @return
	 */
	public int getMeasuredWidth(View v) {
		return v.getMeasuredWidth();
	}

	/**
	 * 控件item点击事件
	 * 
	 * @author yung
	 * @date 创建时间：2016年1月14日 上午11:20:12
	 * @version 1.0
	 */
	class ItemClick implements OnClickListener {

		private TextViewGroupItem item;

		public ItemClick(TextViewGroupItem item) {
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (viewgroup_select == Select.radio.ordinal()) {
				for (int i = 0; i < _dataList.size(); i++) {
//					if (_dataList.get(i).getPosition() == item.getPosition()) {
//						((TextView) v).setTextColor(viewgroup_textColor_s);
//						((TextView) v)
//								.setBackgroundResource(viewgroup_textBackground_s);
//						_dataList.get(i).setStatus(true);
//					} else {
//						((TextView) getChildAt(i))
//								.setTextColor(viewgroup_textColor);
//						((TextView) getChildAt(i))
//								.setBackgroundResource(viewgroup_textBackground);
//						_dataList.get(i).setStatus(false);
//					}

				}
			} else if (viewgroup_select == Select.multiple
					.ordinal()) {
				((TextView) v)
						.setTextColor(item.isStatus() ? viewgroup_textColor
								: viewgroup_textColor_s);
				((TextView) v)
						.setBackgroundResource(item.isStatus() ? viewgroup_textBackground
								: viewgroup_textBackground_s);

				for (int i = 0; i < _dataList.size(); i++) {
					if (!AbStrUtil.isEmpty(_dataList.get(i).getDepartment())) {//选科室的
						if(_dataList.get(i).getDepartment().equals(item.getDepartment())){
						_dataList.get(i).setStatus(!item.isStatus());
						break;
						}
					}else if(_dataList.get(i).getPosition()==item.getPosition()){
						_dataList.get(i).setStatus(!item.isStatus());
						break;
					}
				}
			} else if (viewgroup_select == Select.radio_empty
					.ordinal()) {
				for (int i = 0; i < _dataList.size(); i++) {
					if (_dataList.get(i).getPosition() == item.getPosition()) {
						((TextView) v)
								.setTextColor(item.isStatus() ? viewgroup_textColor
										: viewgroup_textColor_s);
						((TextView) v)
								.setBackgroundResource(item.isStatus() ? viewgroup_textBackground
										: viewgroup_textBackground_s);

						_dataList.get(i).setStatus(!item.isStatus());
					} else {
						((TextView) getChildAt(i))
								.setTextColor(viewgroup_textColor);
						((TextView) getChildAt(i))
								.setBackgroundResource(viewgroup_textBackground);
						_dataList.get(i).setStatus(false);
					}
				}
			}

			if (null != listener) {
				listener.OnTextViewGroupClick(v, _dataList, item);
			}
		}
	}

	/**
	 * item点击时间接口
	 * 
	 * @author yung
	 * @date 创建时间：2016年1月14日 上午11:20:34
	 * @version 1.0
	 */
	public interface OnTextViewGroupItemClickListener {
		void OnTextViewGroupClick(View view, List<TextViewGroupItem> _dataList,
				TextViewGroupItem item);
	}

	/**
	 * 获取item点击事件
	 * 
	 * @return
	 */
	public OnTextViewGroupItemClickListener getOnMultipleTVItemClickListener() {
		return listener;
	}

	private OnTextViewGroupItemClickListener listener;

	/**
	 * 设置item点击事件
	 * 
	 * @return
	 */
	public void setOnTextViewGroupItemClickListener(
			OnTextViewGroupItemClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {

	}
	
	public interface OnItemclick{
		public void onItemclick(int postion);
		
	}
}
