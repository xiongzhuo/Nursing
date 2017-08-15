package com.baoyz.swipemenulistview;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.baoyz.swipemenulistview.SwipeMenuLayout.OnMenuMoveChange;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuView.OnSwipeItemClickListener;
import com.deya.hospital.form.FormEditorAdapter2;
import com.deya.hospital.form.FormJugeEditorAdapter2;
import com.deya.hospital.form.jude_and_score.FormJugeAndSocreEditorAdapter;
import com.deya.hospital.form.xy.XiangyaFormSettingAdapter;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.Subrules;

/**
 * 
 * @author baoyz
 * @date 2014-8-24
 * 
 */
public class SwipeMenuAdapter implements WrapperListAdapter,
		OnSwipeItemClickListener {

	ArrayList<Integer> list;
	private ListAdapter mAdapter;
	private Context mContext;
	private OnMenuItemClickListener onMenuItemClickListener;
	
	public SwipeMenuAdapter(Context context, ListAdapter adapter) {
		mAdapter = adapter;
		mContext = context;
	}
	
	public void setList(ArrayList<Integer> list){
		this.list=list;
	}
	
	public void setListItem(int position,int status){
		for (int i = 0; i < list.size(); i++) {
			if(i==position){
				list.set(i,status);
				break;
			}
		}
	}
	

	@Override
	public int getCount() {
		return mAdapter.getCount();
	}

	@Override
	public Object getItem(int position) {
		return mAdapter.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return mAdapter.getItemId(position);
	}

	private FormEditorAdapter2 adapter;
	private FormJugeEditorAdapter2 adapter2;
	SwipeMenuLayout layout = null;
	private XiangyaFormSettingAdapter adapter3;
	private FormJugeAndSocreEditorAdapter adapter4;
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			View contentView = mAdapter.getView(position, convertView, parent);
			SwipeMenu menu = new SwipeMenu(mContext);
			menu.setViewType(mAdapter.getItemViewType(position));
			createMenu(menu);
			SwipeMenuView menuView = new SwipeMenuView(menu,
					(SwipeMenuListView) parent);
			menuView.setOnSwipeItemClickListener(this);
			SwipeMenuListView listView = (SwipeMenuListView) parent;
			layout = new SwipeMenuLayout(contentView, menuView,
					listView.getCloseInterpolator(),
					listView.getOpenInterpolator());
			layout.setPosition(position);
			
			DebugUtil.debug("SwipeMenuAdapter", "new "+position);
		} else {
			layout = (SwipeMenuLayout) convertView;
			layout.setPosition(position);
			View view = mAdapter.getView(position, layout.getContentView(),
					parent);
			DebugUtil.debug("SwipeMenuAdapter", "old "+position);
		}
		
		FormDetailVo item = null;
		String title=null;
		if(mAdapter instanceof FormEditorAdapter2){
			adapter=(FormEditorAdapter2)mAdapter;
			 item= (FormDetailVo)adapter.getItem(position);
			 title=TextUtils.isEmpty(item.getRemark())?"填写\n备注":"已经\n备注";
			SwipeMenuView v=layout.getMenuView();
//			v.setBackground(new ColorDrawable(Color.rgb(40, 226, 157)));
			v.setBackground(TextUtils.isEmpty(item.getRemark())?new ColorDrawable(Color.rgb(252, 127, 26)):new ColorDrawable(Color.rgb(40, 226, 157)));
			v.setTitle(0, title);
			DebugUtil.debug("SwipeMenuAdapter", "new "+position);
			//layout.getMenuView().getMenu().getMenuItem(0).setTitle("abc");
			
			if(item.getOpenState()==1){
				layout.smoothOpenMenu();
			}else if(item.getOpenState()==2){
				layout.smoothCloseMenu();
				//layout.setCanOpen(false);
			}else{
				layout.smoothCloseMenu();
				//layout.setCanOpen(false);
			}
			layout.setCanOpen(item.isCanOpen());
			layout.setOnMenuMoveChange(new OnMenuMoveChange() {
				
				@Override
				public void open() {
					// TODO Auto-generated method stub
					adapter.setStatus(position, 1);
				}
				
				@Override
				public void close() {
					// TODO Auto-generated method stub
					adapter.setStatus(position, 2);
				}
			});
		}else if(mAdapter instanceof FormJugeEditorAdapter2){
			adapter2=(FormJugeEditorAdapter2)mAdapter;
			 item= (FormDetailVo)adapter2.getItem(position);
			 title=TextUtils.isEmpty(item.getRemark())?"填写备注":"已经备注";
			SwipeMenuView v=layout.getMenuView();
			v.setBackground(TextUtils.isEmpty(item.getRemark())?new ColorDrawable(Color.rgb(252, 127, 26)):new ColorDrawable(Color.rgb(40, 226, 157)));
			v.setTitle(0, title);
			DebugUtil.debug("SwipeMenuAdapter", "new "+position);
			//layout.getMenuView().getMenu().getMenuItem(0).setTitle("abc");
			layout.setCanOpen(item.isCanOpen());
			if(item.getOpenState()==1){
				layout.smoothOpenMenu();
			}else if(item.getOpenState()==2){
				layout.smoothCloseMenu();
			}else{
				layout.smoothCloseMenu();
			}
			layout.setOnMenuMoveChange(new OnMenuMoveChange() {
				
				@Override
				public void open() {
					// TODO Auto-generated method stub
					adapter2.setStatus(position, 1);
				}
				
				@Override
				public void close() {
					// TODO Auto-generated method stub
					adapter2.setStatus(position, 2);
				}
			});
		}else if(mAdapter instanceof XiangyaFormSettingAdapter){
			adapter3=(XiangyaFormSettingAdapter)mAdapter;
			Subrules item2= (Subrules)adapter3.getItem(position);
			 title=!item2.isRemarked()?"填写备注":"已经备注";
			SwipeMenuView v=layout.getMenuView();
			v.setBackground(!item2.isRemarked()?new ColorDrawable(Color.rgb(252, 127, 26)):new ColorDrawable(Color.rgb(40, 226, 157)));
			v.setTitle(0, title);
			DebugUtil.debug("SwipeMenuAdapter", "new "+position);
			//layout.getMenuView().getMenu().getMenuItem(0).setTitle("abc");
			layout.setCanOpen(item2.isCanOPen());
			if(item2.getOpenState()==1){
				layout.smoothOpenMenu();
			}else if(item2.getOpenState()==2){
				layout.smoothCloseMenu();
			}else{
				layout.smoothCloseMenu();
			}
			layout.setOnMenuMoveChange(new OnMenuMoveChange() {
				
				@Override
				public void open() {
					// TODO Auto-generated method stub
					adapter3.setStatus(position, 1);
				}
				
				@Override
				public void close() {
					// TODO Auto-generated method stub
					adapter3.setStatus(position, 2);
				}
			});
		}else if(mAdapter instanceof FormJugeAndSocreEditorAdapter){
			adapter4=(FormJugeAndSocreEditorAdapter)mAdapter;
			 item= (FormDetailVo)adapter4.getItem(position);
			 title=TextUtils.isEmpty(item.getRemark())?"填写备注":"已经备注";
			SwipeMenuView v=layout.getMenuView();
			v.setBackground(TextUtils.isEmpty(item.getRemark())?new ColorDrawable(Color.rgb(252, 127, 26)):new ColorDrawable(Color.rgb(40, 226, 157)));
			v.setTitle(0, title);
			DebugUtil.debug("SwipeMenuAdapter", "new "+position);
			//layout.getMenuView().getMenu().getMenuItem(0).setTitle("abc");
			layout.setCanOpen(item.isCanOpen());
			if(item.getOpenState()==1){
				layout.smoothOpenMenu();
			}else if(item.getOpenState()==2){
				layout.smoothCloseMenu();
			}else{
				layout.smoothCloseMenu();
			}
			layout.setOnMenuMoveChange(new OnMenuMoveChange() {
				
				@Override
				public void open() {
					// TODO Auto-generated method stub
					adapter4.setStatus(position, 1);
				}
				
				@Override
				public void close() {
					// TODO Auto-generated method stub
					adapter4.setStatus(position, 2);
				}
			});
		}
		
		return layout;
	}

	public void createMenu(SwipeMenu menu) {
		// Test Code
		SwipeMenuItem item = new SwipeMenuItem(mContext);
		item.setTitle("Item 1");
		item.setBackground(new ColorDrawable(Color.GRAY));
		item.setWidth(300);
		menu.addMenuItem(item);

//		item = new SwipeMenuItem(mContext);
//		item.setTitle("Item 2");
//		item.setBackground(new ColorDrawable(Color.RED));
//		item.setWidth(300);
//		menu.addMenuItem(item);
	}

	@Override
	public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
		if (onMenuItemClickListener != null) {
			onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
					index);
		}
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.onMenuItemClickListener = onMenuItemClickListener;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mAdapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mAdapter.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return mAdapter.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		return mAdapter.isEnabled(position);
	}

	@Override
	public boolean hasStableIds() {
		return mAdapter.hasStableIds();
	}

	@Override
	public int getItemViewType(int position) {
		return mAdapter.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return mAdapter.getViewTypeCount();
	}

	@Override
	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}

	@Override
	public ListAdapter getWrappedAdapter() {
		return mAdapter;
	}

}
