package com.deya.hospital.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuItem.Type;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.form.FormEditorAdapter2.TextContral;
import com.deya.hospital.form.jude_and_score.FormJugeAndSocreEditorAdapter;
import com.deya.hospital.form.jude_and_score.FormJugeAndSocreEditorAdapter.FormJudeAndScoreSettingInter;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.supervisor.SupervisionDetailActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.google.gson.Gson;

public class FormSettingActivity extends BaseActivity implements
		OnClickListener {
	SwipeMenuListView itemList;
	private TextView title;
	int formtype;
	private FormDetailListVo data;
	private String titleStr = "";
	List<FormDetailVo> list = new ArrayList<FormDetailVo>();
	List<FormDetailListVo> gropList = new ArrayList<FormDetailListVo>();
	double totoalScore;
	private RelativeLayout rlBack;
	TaskVo tv = new TaskVo();
	public int posistion = -1;
	TextView totalscore;
	boolean isUpdated = false;
	boolean editorbal = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_form2);
		getData();
		intTopView();
		intiView();
		findView();
	}

	private void getData() {
		data = (FormDetailListVo) getIntent().getSerializableExtra("vo");
		tv = (TaskVo) getIntent().getSerializableExtra("data");
		totoalScore = data.getScore();
		formtype = getIntent().getIntExtra("type", 0);
		gropList.addAll((List<FormDetailListVo>) getIntent()
				.getSerializableExtra("list"));
		boolean isUpdate = getIntent().getBooleanExtra("updated", false);

		// 可编辑
		editorbal = tv.isUpdatedTask();
		if (isUpdate) {
			isUpdated = true;
			for (FormDetailListVo flv : gropList) {
				for (FormDetailVo fv : flv.getSub_items()) {
					fv.setScores(fv.getScore());
					if (!AbStrUtil.isEmpty(fv.getRemark())) {
						fv.setRemark(true);
						fv.setRemarkVo(gson.fromJson(fv.getRemark(),
								TaskVo.class));
						fv.setOpenState(1);
						fv.setCanOpen(true);
					} else {
						if (!editorbal) {
							fv.setCanOpen(true);
						} else {
							fv.setCanOpen(false);
						}
						fv.setRemark(false);
						fv.setOpenState(2);

					}
				}

			}
		}
		posistion = getIntent().getIntExtra("position", -1);
		list.addAll(gropList.get(posistion).getSub_items());
		tv = (TaskVo) getIntent().getSerializableExtra("data");

	}

	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(tv.getName());
		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(this);
		TextView rightView = (TextView) this.findViewById(R.id.submit);

		if (!isUpdated) {
			rightView.setVisibility(View.VISIBLE);
			rightView.setText("预览");
			rightView.setOnClickListener(this);
		} else if (editorbal) {
			rightView.setVisibility(View.VISIBLE);
			rightView.setText("保存");
			rightView.setOnClickListener(this);
		}

	}

	private TextView lastTv;
	private TextView nextTv;
	TextView pageTv;

	private void findView() {
		lastTv = (TextView) this.findViewById(R.id.lastPage);
		nextTv = (TextView) this.findViewById(R.id.nextPage);
		lastTv.setVisibility(View.INVISIBLE);
		lastTv.setOnClickListener(null);
		pageTv = (TextView) this.findViewById(R.id.pageTv);
		pageTv.setText(posistion + 1 + "");
		TextView totalPageTv = (TextView) this.findViewById(R.id.toatalPageTv);
		totalPageTv.setText("/" + gropList.size());
		setLastTv(posistion);
		nextTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (posistion < gropList.size() - 1) {
					gropList.get(posistion).setSub_items(list);
					list.clear();
					list.addAll(gropList.get(posistion + 1).getSub_items());
					posistion = posistion + 1;
					onListChanged(posistion);
				} else {
					Intent it = new Intent(mcontext, PreViewActivity.class);
					it.putExtra("data", (Serializable) gropList);
					setResult(0x171, it);
					finish();
				}
			}

		});

		lastTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gropList.get(posistion).setSub_items(list);
				list.clear();
				list.addAll(gropList.get(posistion - 1).getSub_items());
				posistion = posistion - 1;
				onListChanged(posistion);
			}
		});
	}

	public static String reMoveUnUseNumber(String s) {

		if (s.indexOf(".") > 0) {
			// 正则表达
			s = s.replaceAll("0+?$", "");// 去掉后面无用的零
			s = s.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
		}
		return s;
	}

	void onListChanged(int position) {
		totalscore.setText(gropList.get(position).getName() + "("
				+ reMoveUnUseNumber(gropList.get(position).getScore() + "")
				+ "分)");
		pageTv.setText((position + 1) + "");
		setLastTv(posistion);
		setNextTv(posistion);
		if (formtype == 1) {
			adapter.notifyDataSetChanged();
		} else if (formtype == 2) {
			adapter2.notifyDataSetChanged();
		} else {
			FormDetailListVo fv2 = gropList.get(position);
			if(fv2.getItemTotalScore()!=-1){
				scoreTv2.setText(reMoveUnUseNumber(fv2.getItemTotalScore() + "分"));
			}else{
				scoreTv2.setText(reMoveUnUseNumber(fv2.getScore() + "分"));
			}
			directoryTv.setText(fv2.getName());
			literatureTv.setText(fv2.getCheck_method());
			adapter3.notifyDataSetChanged();
		}

	}

	private void setLastTv(int position) {
		if (position <= 0) {
			lastTv.setVisibility(View.INVISIBLE);
			lastTv.setEnabled(false);
		} else {
			lastTv.setVisibility(View.VISIBLE);
			lastTv.setText("上一页");
			lastTv.setEnabled(true);
		}
	}

	public void setNextTv(int position) {
		if (position < gropList.size() - 1) {
			nextTv.setText("下一页");
		} else {
			nextTv.setText("预览");
		}
	}

	TextView typetext;
	FormJugeEditorAdapter2 adapter2;
	FormEditorAdapter2 adapter;
	FormJugeAndSocreEditorAdapter adapter3;
	private TextView literatureTv;
	private TextView titleTv;
	private TextView scoreTv2;
	private TextView directoryTv;

	private void intiView() {
		typetext = (TextView) this.findViewById(R.id.totalscore);
		LinearLayout result_lay = (LinearLayout) this
				.findViewById(R.id.resultLay);
		totalscore = (TextView) this.findViewById(R.id.totalscore);
		itemList = (SwipeMenuListView) this.findViewById(R.id.listView);
		if (formtype == 1) {
			adapter = new FormEditorAdapter2(mcontext, isUpdated, editorbal,
					list, new TextContral() {

						@Override
						public void setText(List<FormDetailVo> list) {
							double totoalScore1 = 0;
							for (FormDetailVo fv : list) {
								totoalScore1 += fv.getScores();
							}
							gropList.get(posistion).setScore(totoalScore1);
							typetext.setText(gropList.get(posistion).getName()
									+ "("
									+ reMoveUnUseNumber(totoalScore1 + "")
									+ "分)");
						}
					}, itemList);
			itemList.setAdapter(adapter);

		} else if (formtype == 2) {
			typetext.setVisibility(View.GONE);
			adapter2 = new FormJugeEditorAdapter2(mcontext, isUpdated,
					editorbal, list, itemList);
			itemList.setAdapter(adapter2);
		} else if (formtype == 4) {
			findViewById(R.id.discibeView).setVisibility(View.VISIBLE);
			findViewById(R.id.totalscore).setVisibility(View.GONE);

			directoryTv = (TextView) this.findViewById(R.id.directoryTv2);
			scoreTv2 = (TextView) this.findViewById(R.id.scoreTv2);
			titleTv = (TextView) this.findViewById(R.id.titleTv);
			literatureTv = (TextView) this.findViewById(R.id.literatureTv);
			FormDetailListVo fv2 = gropList.get(posistion);
			literatureTv.setText(fv2.getCheck_method());
			directoryTv.setText(fv2.getName());
			if(fv2.getItemTotalScore()!=-1){
				scoreTv2.setText(reMoveUnUseNumber(fv2.getItemTotalScore() + "分"));
			}else{
				scoreTv2.setText(reMoveUnUseNumber(fv2.getScore() + "分"));
			}
			adapter3 = new FormJugeAndSocreEditorAdapter(mcontext, isUpdated,
					editorbal, list, itemList, 0,
					new FormJudeAndScoreSettingInter() {

						@Override
						public void onDelResult(int postion) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onDecreaseScore(double position) {
							FormDetailListVo fdv = gropList.get(posistion);
							fdv.setSubstrScore(fdv.getSubstrScore()+1);
							if (fdv.getItemTotalScore() == -1) {
								fdv.setItemTotalScore(fdv.getScore());
							}

							if(fdv.getItemTotalScore()
									- position <= 0 ){
								fdv.setItemTotalScore(0);
								//adapter3.setEditorType(1);
							}else{
//								if(fdv.getSubstrScore()==data.getScore()){
//									adapter3.setEditorType(0);
//								}
								fdv.setItemTotalScore( fdv
										.getItemTotalScore() - position);
							//	adapter3.setEditorType(0);
							}
							scoreTv2.setText(reMoveUnUseNumber(fdv
									.getItemTotalScore() + "")
									+ "分");
						}

						@Override
						public void onAddScore(double position) {
							FormDetailListVo fdv = gropList.get(posistion);
							fdv.setSubstrScore(fdv.getSubstrScore()-1);
							if (fdv.getItemTotalScore() == -1) {
								fdv.setItemTotalScore(fdv.getScore());
							}

							if(fdv.getItemTotalScore()
									+ position>= data.getScore() ){
								fdv.setItemTotalScore(data.getScore());
								//adapter3.setEditorType(2);
							}else{
								if(fdv.getSubstrScore()>=fdv.getScore()){
									fdv.setItemTotalScore(0);
								}else{
								fdv.setItemTotalScore( fdv
										.getItemTotalScore() + position);
								}
							}
							scoreTv2.setText(reMoveUnUseNumber(fdv
									.getItemTotalScore() + "")
									+ "分");
						}
					});
			itemList.setAdapter(adapter3);
		}
		totalscore.setText(gropList.get(posistion).getName() + "("
				+ reMoveUnUseNumber(gropList.get(posistion).getScore() + "")
				+ "分)");
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
					if (!isUpdated) {
						toRemark(position);
					} else {
						toDetailRemark(position);
					}
				}

			}
		});

	}

	public int remarkPosition = -1;
	private Gson gson = new Gson();

	public void toRemark(int position) {
		this.remarkPosition = position;
		Intent it = new Intent(mcontext, SupervisionActivity.class);
		TaskVo remarkVo = list.get(position).getRemarkVo();
		// if(isUpdated&&null!=list.get(remarkPosition).getRemark()){
		// remarkVo=gson.fromJson(list.get(remarkPosition).getRemark(),
		// TaskVo.class);
		// list.get(position).setRemarkVo(remarkVo);
		// }
		if (null != remarkVo) {
		}
		if (null == remarkVo || AbStrUtil.isEmpty(remarkVo.getCheck_content())) {
			remarkVo = tv;
			remarkVo.setCheck_content(list.get(position).getDescribes());
		}
		it.putExtra("data", remarkVo);
		it.putExtra("isRemark", "1");
		startActivityForResult(it, 0x117);
	}

	public void toDetailRemark(int itemPosition) {
		this.remarkPosition = itemPosition;
		Intent it = new Intent(mcontext, SupervisionDetailActivity.class);
		TaskVo remarkVo;
		if (!AbStrUtil.isEmpty(list.get(itemPosition).getRemark())) {
			remarkVo = gson.fromJson(list.get(itemPosition).getRemark(),
					TaskVo.class);
			remarkVo.setTmp_id(list.get(itemPosition).getId());
			remarkVo.setDepartment(tv.getDepartment());
			remarkVo.setDepartmentName(tv.getDepartmentName());
			remarkVo.setType("2");

		} else {
			remarkVo = tv;
		}
		it.putExtra("data", remarkVo);
		it.putExtra("isRemark", "1");
		it.putExtra("remarkId", list.get(itemPosition).getId());
		startActivityForResult(it, 0x117);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		DebugUtil.debug("SwipeMenuAdapter", "onActivityResult");
		// TODO 此处 需要进行数据处理
		if (resultCode == 0x172 && null != data) {
			// int position = data.getIntExtra("position", -1);
			// String remark = data.getStringExtra("remark");
			// if (position >= 0) {
			// for (int i = 0; i < list.size(); i++) {
			// if (i == position) {
			// list.get(i).setRemark(remark);
			// list.get(i).setRemark(true);
			// break;
			// }
			// }
			// if (type == 1) {
			// adapter.notifyDataSetChanged();
			// } else if (type == 2) {
			// adapter2.notifyDataSetChanged();
			//
			// }
			// }

			TaskVo remarkVo = (TaskVo) data.getSerializableExtra("remarkdata");
			String remarkStr = gson.toJson(remarkVo);
			Log.i("1111111111", remarkStr);
			list.get(remarkPosition).setRemark(remarkStr);
			list.get(remarkPosition).setRemark(true);
			list.get(remarkPosition).setRemarkVo(remarkVo);
			if (formtype == 1) {
				adapter.notifyDataSetChanged();
			} else if (formtype == 2) {
				adapter2.notifyDataSetChanged();
			} else {
				adapter3.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void ListViewMenuCreate(SwipeMenu menu) {

		SwipeMenuItem kankanItem = new SwipeMenuItem(mcontext);
		// set item background
		// kankanItem
		// .setBackground(new ColorDrawable(Color.rgb(0x33, 0x66, 0xcc)));//
		// 设置背景颜色
		// kankanItem.setBackground(getResources().getColor(R.color.green));
		// set item width
		// kankanItem.setWidth(dp2px(60));// 设置宽度
		kankanItem.setWidth(dp2Px(60));
		// set item title
		kankanItem.setTitle("备注");// 设置第一个标题
		// set item title fontsize
		kankanItem.setTitleSize(18);// 设置标题文字的大小
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			// 第二版预览
			// Intent it = new Intent(mcontext,
			// FormSettingPreViewActivity.class);
			// it.putExtra("list", (Serializable) gropList);
			// startActivity(it);
			// 如果不是已上传

			if (editorbal) {
				Intent it = new Intent(mcontext, PreViewActivity.class);
				it.putExtra("list", (Serializable) gropList);
				it.putExtra("data", tv);
				startActivity(it);
				break;
			}

		case R.id.rl_back:
			Intent it = new Intent(mcontext, PreViewActivity.class);
			gropList.get(posistion).setSub_items(list);
			it.putExtra("data", (Serializable) gropList);
			setResult(0x171, it);
			finish();
			break;
		default:
			break;
		}

	}

}
