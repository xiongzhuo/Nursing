package com.deya.hospital.form.xy;

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
import com.deya.hospital.form.PreViewActivity;
import com.deya.hospital.form.xy.XiangyaFormSettingAdapter.TextContral;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.Subrules;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XiangYaFormSettingActivity extends BaseActivity implements
		OnClickListener {
	TextView directoryTv;
	TextView scoreTv2;
	TextView methodTv, titleTv, literatureTv;
	TextView seeDetailTv, DetailTv;
	SwipeMenuListView itemList;
	private TextView title;
	int formtype;
	private FormDetailListVo data;
	private String titleStr = "";
	List<FormDetailListVo> gropList = new ArrayList<FormDetailListVo>();
	List<FormDetailVo> pageList = new ArrayList<FormDetailVo>();
	List<Subrules> ruleList = new ArrayList<Subrules>();
	double totoalScore;
	public int posistion = -1;
	private RelativeLayout rlBack;
	TaskVo tv = new TaskVo();
	private int parentPosition = -1;
	TextView totalscore;
	boolean isUpdated = false;
	boolean editorbal = false;
	boolean isShowDeTail;
	LinearLayout closeLay;
	private int itemPosition=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_form_xangya);
		getData();
		intTopView();
		initView();
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
		isUpdated = isUpdate;
		editorbal = isUpdate;
		itemPosition = getIntent().getIntExtra("position", 0);
		parentPosition = getIntent().getIntExtra("parentposition", 0);

		if (!isUpdate) {

			for (int i = 0; i < gropList.size(); i++) {
				FormDetailListVo fdlv = new FormDetailListVo();
				fdlv = gropList.get(i);
				for (int j = 0; j < fdlv.getSub_items().size(); j++) {

					Log.i("page", i + "-----" + j);
					FormDetailVo fdv = new FormDetailVo();
					fdv = fdlv.getSub_items().get(j);
					Log.i("page", i + "-----" + j + "====" + fdv.getDescribes());
					if (fdv.getChooseItem() == -1) {
						fdv.setChooseItem(0);
						fdv.getSub_rule().get(0).setChoosed(true);
					} else {
						fdv.getSub_rule().get(fdv.getChooseItem()).setChoosed(true);
					}

					pageList.add(fdv);
				}
			}
		} else {
			for (int i = 0; i < gropList.size(); i++) {
				FormDetailListVo fdlv = new FormDetailListVo();
				fdlv = gropList.get(i);
				for (int j = 0; j < fdlv.getSub_items().size(); j++) {
					Log.i("page", i + "-----" + j);
					FormDetailVo fdv = new FormDetailVo();
					fdv = fdlv.getSub_items().get(j);
					Log.i("page", i + "-----" + j + "====" + fdv.getDescribes());
					for (int k = 0; k < fdv.getSub_rule().size(); k++) {
						if (fdv.getSub_rule().get(k).getIs_choosed() == 1) {
							if (!AbStrUtil.isEmpty(fdv.getRemark())) {
								fdv.getSub_rule().get(k).setRemarked(true);
								fdv.getSub_rule().get(k).setOpenState(1);
							}
							fdv.setChooseItem(k);
							fdv.getSub_rule().get(k).setChoosed(true);
						} else {
							fdv.getSub_rule().get(k).setCanOPen(false);
						}

					}

					if (fdv.getChooseItem() == -1) {
						fdv.setChooseItem(0);
						fdv.getSub_rule().get(0).setChoosed(true);
					} else {
						fdv.getSub_rule().get(fdv.getChooseItem()).setChoosed(true);
					}
					pageList.add(fdv);
				}
			}
		}
		posistion = 0;
		a:for (int i = 0; i < gropList.size(); i++) {
			FormDetailListVo fdlv;
			fdlv = gropList.get(i);
			for (int j = 0; j < fdlv.getSub_items().size(); j++) {
				if (i == parentPosition && j == itemPosition) {
					break a;
				}
				posistion++;
			}
		}
		ruleList.addAll(pageList.get(posistion)
				.getSub_rule());
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
		} else {
			rightView.setVisibility(View.GONE);
		}

	}

	TextView typetext;
	XiangyaFormSettingAdapter adapter;

	private void initView() {
		directoryTv = (TextView) this.findViewById(R.id.directoryTv);
		scoreTv2 = (TextView) this.findViewById(R.id.scoreTv2);
		titleTv = (TextView) this.findViewById(R.id.titleTv);
		seeDetailTv = (TextView) this.findViewById(R.id.seeDetailTv);
		DetailTv = (TextView) this.findViewById(R.id.DetailTv);
		seeDetailTv.setOnClickListener(this);
		DetailTv.setText(pageList.get(posistion).getExplains());
		literatureTv = (TextView) this.findViewById(R.id.literatureTv);
		closeLay = (LinearLayout) this.findViewById(R.id.closeLay);

		closeLay.setOnClickListener(this);
		typetext = (TextView) this.findViewById(R.id.totalscore);

		directoryTv.setText(pageList.get(posistion)
				.getDescribes());
		literatureTv.setText(pageList.get(posistion)
				.getMethod());
		FormDetailVo fv2 = pageList.get(posistion);
		scoreTv2.setText(reMoveUnUseNumber(fv2.getSub_rule().get(fv2.getChooseItem()).getScore()
				+ "")
				+ "分");
		LinearLayout result_lay = (LinearLayout) this
				.findViewById(R.id.resultLay);
		itemList = (SwipeMenuListView) this.findViewById(R.id.listView);
		adapter = new XiangyaFormSettingAdapter(mcontext, isUpdated, editorbal,
				ruleList, new TextContral() {

			@Override
			public void setText(List<FormDetailVo> list) {
				double totoalScore1 = 0;
				for (FormDetailVo fv : list) {
					totoalScore1 += fv.getScores();
				}
				gropList.get(parentPosition).setScore(totoalScore1);
				// typetext.setText(gropList.get(posistion).getName()+"("+reMoveUnUseNumber(totoalScore1+"")+"分)");
			}

			@Override
			public void setPageChooseItem(int position, boolean choosed) {
				pageList.get(posistion).setChooseItem(position);
				setOpenState(position, choosed);
			}
		}, itemList);
		itemList.setAdapter(adapter);

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

	private String getRules() {
		String str = "";
		for (int i = 0; i < ruleList.size(); i++) {
			str += (i + 1) + "、" + ruleList.get(i).getName() + "\n";
		}
		return str;
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
		totalPageTv.setText("/" + pageList.size());
		setLastTv(posistion);
		nextTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (posistion < pageList.size() - 1) {
					pageList.get(posistion).setSub_rule(ruleList);
					ruleList.clear();
					ruleList.addAll(pageList.get(posistion + 1).getSub_rule());
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
				pageList.get(posistion).setSub_rule(ruleList);
				ruleList.clear();
				ruleList.addAll(pageList.get(posistion - 1).getSub_rule());
				posistion = posistion - 1;
				onListChanged(posistion);
			}
		});
	}

	void onListChanged(int position) {
		directoryTv.setText(pageList.get(position).getTitle());
		FormDetailVo fv2 = pageList.get(position);
		scoreTv2.setText(reMoveUnUseNumber(fv2.getSub_rule().get(fv2.getChooseItem()).getScore()
				+ "")
				+ "分");
		literatureTv.setText(pageList.get(position).getMethod());
		pageTv.setText((position + 1) + "");
		DetailTv.setText(pageList.get(position).getExplains());

		directoryTv.setText(pageList.get(position).getDescribes());
		setLastTv(posistion);
		setNextTv(posistion);
		adapter.notifyDataSetChanged();
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
		if (position < pageList.size() - 1) {
			nextTv.setText("下一页");
		} else {
			nextTv.setText("预览");
		}
	}

	public int remarkPosition = -1;
	private Gson gson = new Gson();

	public void toRemark(int position) {
		this.remarkPosition = posistion;
		Intent it = new Intent(mcontext, SupervisionActivity.class);
		TaskVo remarkVo = pageList.get(remarkPosition).getRemarkVo();
		if (null != remarkVo) {
		}
		if (null == remarkVo || AbStrUtil.isEmpty(remarkVo.getCheck_content())) {
			remarkVo = tv;
			remarkVo.setCheck_content(ruleList.get(position).getName());
		}
		it.putExtra("data", remarkVo);
		it.putExtra("isRemark", "1");
		startActivityForResult(it, 0x117);
	}

	public void toDetailRemark(int itemPosition) {
		// this.remarkPosition = itemPosition;
		// Intent it = new Intent(mcontext, SupervisionDetailActivity.class);
		// TaskVo remarkVo;
		// if (!AbStrUtil.isEmpty(list.get(itemPosition).getRemark())) {
		// remarkVo = gson.fromJson(list.get(itemPosition).getRemark(),
		// TaskVo.class);
		// remarkVo.setTmp_id(list.get(itemPosition).getId());
		// remarkVo.setDepartment(tv.getDepartment());
		// remarkVo.setDepartmentName(tv.getDepartmentName());
		// remarkVo.setType("2");
		//
		// } else {
		// remarkVo = tv;
		// }
		// it.putExtra("data", remarkVo);
		// it.putExtra("isRemark", "1");
		// it.putExtra("remarkId", list.get(itemPosition).getId());
		// startActivityForResult(it, 0x117);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		DebugUtil.debug("SwipeMenuAdapter", "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		// TODO 此处 需要进行数据处理
		if (resultCode == 0x172 && null != data) {

			TaskVo remarkVo = (TaskVo) data.getSerializableExtra("remarkdata");
			String remarkStr = gson.toJson(remarkVo);
			Log.i("1111111111", remarkStr);
			pageList.get(remarkPosition).setRemark(remarkStr);
			pageList.get(remarkPosition).setRemark(true);
			pageList.get(remarkPosition).setRemarkVo(remarkVo);
			pageList.get(remarkPosition).getSub_rule()
					.get(pageList.get(remarkPosition).getChooseItem())
					.setRemarked(true);
			adapter.notifyDataSetChanged();

		}

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

				if (editorbal) {
					Intent it = new Intent(mcontext, PreViewActivity.class);
					it.putExtra("list", (Serializable) gropList);
					it.putExtra("data", tv);
					startActivity(it);
					break;
				}

			case R.id.rl_back:
				Intent it = new Intent(mcontext, PreViewActivity.class);
				// gropList.get(parentPosition).setSub_items(list);//保存数据
				it.putExtra("data", (Serializable) gropList);
				setResult(0x171, it);
				finish();
				break;
			case R.id.seeDetailTv:
				isShowDeTail = true;
				showDetail();
				break;
			case R.id.closeLay:
				isShowDeTail = false;
				showDetail();
				break;
			default:
				break;
		}

	}

	public void showDetail() {
		if (isShowDeTail) {
			DetailTv.setVisibility(View.VISIBLE);
			seeDetailTv.setVisibility(View.GONE);
			closeLay.setVisibility(View.VISIBLE);
		} else {
			DetailTv.setVisibility(View.GONE);
			seeDetailTv.setVisibility(View.VISIBLE);
			closeLay.setVisibility(View.GONE);
		}
	}

	public void setOpenState(int position, boolean choosed) {

		if (!isUpdated) {
			for (int i = 0; i < ruleList.size(); i++) {
				if (position == i) {
					ruleList.get(i).setChoosed(choosed);
					if (choosed) {
						pageList.get(posistion).setChooseItem(position);
						pageList.get(posistion).setScores(ruleList.get(i).getScore());
						scoreTv2.setText(reMoveUnUseNumber(ruleList.get(i).getScore()
								+ "")
								+ "分");
					} else {
						pageList.get(posistion).setChooseItem(-1);
					}
				} else {
					if (choosed) {
						ruleList.get(i).setChoosed(false);
					}
				}
				adapter.notifyDataSetChanged();

			}
		}

	}

	public static String reMoveUnUseNumber(String s) {

		if (s.indexOf(".") > 0) {
			// 正则表达
			s = s.replaceAll("0+?$", "");// 去掉后面无用的零
			s = s.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
		}
		return s;
	}
}
