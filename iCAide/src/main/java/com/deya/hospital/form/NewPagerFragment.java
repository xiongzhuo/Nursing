package com.deya.hospital.form;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuItem.Type;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.deya.acaide.R;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.form.FormEditorAdapter2.TextContral;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.google.gson.Gson;

public class NewPagerFragment extends Fragment {
	private static final String TAG = "TestFragment";
	private String hello;// = "hello android";
	private String defaultHello = "default value";

	static NewPagerFragment newInstance(List<FormDetailVo> list,FormDetailListVo data,int type) {
		NewPagerFragment newFragment = new NewPagerFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", (Serializable) list);
		bundle.putString("title", data.getName());
		bundle.putDouble("score", data.getScore());
		bundle.putInt("type", type);
		bundle.putSerializable("vo", data);
		newFragment.setArguments(bundle);

		// bundle还可以在每个标签里传送数据

		return newFragment;

	}
	TextView title;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "TestFragment-----onCreateView");
		final Bundle args = getArguments();
		View view = inflater.inflate(R.layout.layout_form2, container, false);
		
		final TextView	typetext = (TextView) view.findViewById(R.id.scoreTv);
		LinearLayout result_lay = (LinearLayout) view
				.findViewById(R.id.resultLay);
		title = (TextView) view.findViewById(R.id.title);
		TextView type = (TextView) view.findViewById(R.id.type);
		final FormDetailListVo data=(FormDetailListVo) args.getSerializable("vo");
		final List<FormDetailVo> list = (List<FormDetailVo>) args.getSerializable("data");
		SwipeMenuListView itemList = (SwipeMenuListView) view.findViewById(R.id.listView);
		if (args.getInt("type") == 1) {
			result_lay.setVisibility(View.GONE);
			FormEditorAdapter2	adapter = new FormEditorAdapter2(getActivity(), false,true,list, new TextContral() {

				@Override
				public void setText(List<FormDetailVo> list) {
					double totoalScore1 = 0;
					for (FormDetailVo fv : list) {
						totoalScore1 += fv.getScores();
					}
					data.setScore(totoalScore1);
					title.setText("总得分："
							+ AbStrUtil.reMoveUnUseNumber(totoalScore1 + "")
							+ "分");
				}
			}, itemList);
			itemList.setAdapter(adapter);
//			typetext.setText("总得分："
//					+ AbStrUtil.reMoveUnUseNumber(totoalScore + "") + "分");
		} else if (args.getInt("type") == 1) {
			typetext.setVisibility(View.GONE);
			FormJugeEditorAdapter	adapter2 = new FormJugeEditorAdapter(getActivity(), list);
			itemList.setAdapter(adapter2);
			typetext.setVisibility(View.GONE);
		}

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				ListViewMenuCreate(menu);
			}
		};
		// set creator

		itemList.setMenuCreator(creator);// listview要添加menu
		itemList.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				if (menu.getMenuItem(index).getType() == Type.REMARK) {
					toRemark(position,list,"");
				}

			}
		});
	
		return view;

	}
	
	public int remarkPosition=-1;
	private Gson gson=new Gson();
	public void toRemark(int position,List<FormDetailVo> list,String title) {
		this.remarkPosition=position;
		Intent it = new Intent(getActivity(), SupervisionActivity.class);
		TaskVo remarkVo = list.get(position).getRemarkVo();
		if (null != remarkVo) {
			Log.i("111111111", remarkVo.getAttachments().size() + "");
		}
		if (null == remarkVo || AbStrUtil.isEmpty(remarkVo.getCheck_content())) {
			remarkVo.setCheck_content(title + "\n" + (position + 1) + "、"
					+ list.get(position).getDescribes());
		}

		
		it.putExtra("data", remarkVo);
		it.putExtra("isRemark", "1");
		startActivityForResult(it, 0x117);
	}
	private void ListViewMenuCreate(SwipeMenu menu) {

		SwipeMenuItem kankanItem = new SwipeMenuItem(getActivity());
		// set item background
//		kankanItem
//				.setBackground(new ColorDrawable(Color.rgb(0x33, 0x66, 0xcc)));// 设置背景颜色
//		kankanItem.setBackground(getResources().getColor(R.color.green));
		// set item width
		// kankanItem.setWidth(dp2px(60));// 设置宽度
		kankanItem.setWidth(dp2Px(60));
		// set item title
		kankanItem.setTitle("备注");// 设置第一个标题
		// set item title fontsize
		kankanItem.setTitleSize(22);// 设置标题文字的大小
		// set item title font color
		kankanItem.setTitleColor(Color.WHITE);// 设置标题颜色
		kankanItem.setType(Type.REMARK);
		
		// add to menu
		menu.addMenuItem(kankanItem);// 添加标题到menu类中
	}
	public int dp2Px(int dp) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
}
