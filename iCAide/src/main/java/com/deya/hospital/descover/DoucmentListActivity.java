package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.DoucmentAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.dypdf.DyPdfActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.FileDownloadThread;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.DoucmentVo;
import com.deya.hospital.vo.GuideTypeVo;
import com.deya.hospital.workcircle.knowledge.DoucmentUitl;
import com.deya.hospital.workcircle.knowledge.WebDocmentDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DoucmentListActivity extends BaseActivity implements OnClickListener {
	private static final int GET_DUCMENTS_SUCESS = 0x40003;
	private static final int GET_DUCMENTS_FAIL = 0x40004;
	private static final int READ_SUCESS = 0x40006;
	private static final int READ_FAIL = 0x40007;
	GridView gv;
	String strItem[] = {"全部", "感控文献", "感控指南", "感控科普", "行业动态", "政策指南"};
	int gvIndex = 0;
	PullToRefreshListView listView;
	// 分页
	private int totalnum;
	int page = 1;

	Tools tools;
	List<GuideTypeVo> guidTypelist = new ArrayList<GuideTypeVo>();
	public static final String DOWNLOAD_FOLDER_NAME = "Deya/pdf";
	Gson gson = new Gson();
	String typesId = "";
	String subType = "";
	private boolean isFirst = false;
	private DoucmentAdapter dcAdapter;
	TextView imgSearch;
	TextView empertyView;
	public static String html = "";
	String shareUrl = "";
	String kinds = "";
	LinearLayout networkView;
	private String pdfId="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_activity);
		tools = new Tools(mcontext, Constants.AC);
		subType = getIntent().getStringExtra("id");
		kinds = getIntent().getStringExtra("kinds");
		networkView = (LinearLayout) this.findViewById(R.id.networkView);
		initView();
		checkNetWork();
	}

	public void checkNetWork() {

		if (NetWorkUtils.isConnect(mcontext)) {
			networkView.setVisibility(View.GONE);
		} else {
			networkView.setVisibility(View.VISIBLE);
		}

	}

	private void initView() {
		typesId = getIntent().getStringExtra("types");
		RelativeLayout rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		imgSearch = (TextView) this.findViewById(R.id.et_search);
		imgSearch.setOnClickListener(this);
		empertyView = (TextView) this.findViewById(R.id.empertyView);

		listView = (PullToRefreshListView) this.findViewById(R.id.list);
		gv = (GridView) this.findViewById(R.id.gv);
		initGridview();
		dcAdapter = new DoucmentAdapter(mcontext, doucmentList);
		listView.setAdapter(dcAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String pdfur = doucmentList.get(position - 1).getPdf_attach();
				shareUrl =  DoucmentUitl.getShareUrl( doucmentList.get(position - 1).getId());
				if (!AbStrUtil.isEmpty(pdfur)) {

					OpenPdf(pdfur, doucmentList.get(position - 1).getTopic());
					pdfId=doucmentList.get(position - 1).getId();
				} else {
					getReadResult(doucmentList.get(position - 1).getId());
					Intent toWeb = new Intent(mcontext, WebDocmentDetail.class);
					toWeb.putExtra("url", WebUrl.DOCMENTURL+doucmentList.get(position - 1).getId());
					toWeb.putExtra("title", doucmentList.get(position - 1).getTopic());
					toWeb.putExtra("type", "1");
					toWeb.putExtra("article_src", "2");
					toWeb.putExtra("shareUrl", shareUrl);
					toWeb.putExtra("article_id", doucmentList.get(position - 1).getId());
					startActivity(toWeb);
				}
				int num = Integer.parseInt(doucmentList.get(position - 1)
						.getRead_count());
				doucmentList.get(position - 1).setRead_count((num + 1) + "");
				dcAdapter.notifyDataSetChanged();
			}
		});
		getDoucmentsList(subType);
	}

	public boolean isLoading = false;
	String subfile = "";
	String title = "";
	File pdffile;

	public void OpenPdf(String fileName, String title) {
		subfile = fileName;
		this.title = title;
		pdffile = new File(getPath() + "/" + fileName + ".pdf");

		if (isLoading) {
			return;

		}

		download(fileName);
	}


	public boolean checkIsExists(int lenth) {

		return pdffile.length() == lenth;
	}

	// 下载部分 ---------

	/**
	 * 下载准备工作，获取SD卡路径、开启线程
	 */
	private void download(String fileName) {
		showprocessdialog();
		isLoading = true;
		// 获取SD卡目录
		String dowloadDir = getPath();
		File file = new File(dowloadDir);
		// 创建下载目录
		if (!file.exists()) {
			file.mkdirs();
		}

		// 读取下载线程数，如果为空，则单线程下载
		int downloadTN = 1;
		// 如果下载文件名为空则获取Url尾为文件名
		String downloadUrl = WebUrl.FILE_PDF_LOAD_URL + fileName;
		String pdfName = fileName + ".pdf";
		// 开始下载前把下载按钮设置为不可用
		// 进度条设为0
		// downloadProgressBar.setProgress(0);
		// 启动文件下载线程
		new downloadTask(downloadUrl, Integer.valueOf(downloadTN), dowloadDir
				+ pdfName).start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
			int progress = (Double
					.valueOf((downloadedSize * 1.0 / fileSize * 100)))
					.intValue();
			if (progress == 100) {
				isLoading = false;
				dismissdialog();
				OpenPdf(subfile, title);
			} else {
				// ToastUtils.showToast(mcontext, "当前进度:" + progress + "%");
			}
			// downloadProgressBar.setProgress(progress);
		}

	};
	private int fileSize = 0;
	private int downloadedSize = 0;

	public class downloadTask extends Thread {
		private int blockSize, downloadSizeMore;
		private int threadNum = 5;
		String urlStr, threadNo, fileName;

		public downloadTask(String urlStr, int threadNum, String fileName) {
			this.urlStr = urlStr;
			this.threadNum = threadNum;
			this.fileName = fileName;
		}

		@Override
		public void run() {
			FileDownloadThread[] fds = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				conn.getHeaderField("Content-Length");
				// 获取下载文件的总大小
				// fileSize = conn.getContentLength();
				fileSize = conn.getContentLength();

				if (checkIsExists(fileSize)) {
					dismissdialog();
					isLoading = false;
					Uri uri = Uri.parse(pdffile.toString());
					Intent intent = new Intent(mcontext, DyPdfActivity.class);
					intent.putExtra("title", title);
					intent.putExtra("url", shareUrl);
					intent.setAction(Intent.ACTION_VIEW);
					intent.putExtra("article_id", pdfId);
					intent.putExtra("article_src", "2");
					intent.setData(uri);
					startActivity(intent);
				} else {
					// 计算每个线程要下载的数据量
					blockSize = fileSize / threadNum;
					// 解决整除后百分比计算误差
					downloadSizeMore = (fileSize % threadNum);
					File file = new File(fileName);
					for (int i = 0; i < threadNum; i++) {
						// 启动线程，分别下载自己需要下载的部分
						FileDownloadThread fdt = new FileDownloadThread(url, file,
								i * blockSize, (i + 1) * blockSize - 1);
						fdt.setName("Thread" + i);
						fdt.start();
						fds[i] = fdt;
					}
					boolean finished = false;
					while (!finished) {
						// 先把整除的余数搞定
						downloadedSize = downloadSizeMore;
						finished = true;
						for (int i = 0; i < fds.length; i++) {
							downloadedSize += fds[i].getDownloadSize();
							if (!fds[i].isFinished()) {
								finished = false;
							}
						}
						// 通知handler去更新视图组件
						handler.sendEmptyMessage(0);
						// 休息1秒后再读取下载进度
						sleep(1000);
					}
				}
			} catch (Exception e) {

			}

		}
	}


	private String getPath() {
		// TODO Auto-generated method stub
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			return new StringBuilder(Environment.getExternalStorageDirectory()
					.getAbsolutePath()).append(File.separator)
					.append(DOWNLOAD_FOLDER_NAME).append(File.separator)
					.toString();
		}
		return "";
	}

	public void setEmperty() {
		if (doucmentList.size() > 0) {
			listView.setVisibility(View.VISIBLE);
			empertyView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.GONE);
			empertyView.setVisibility(View.VISIBLE);
		}
	}

	public void initGridview() {
		int size = guidTypelist.size();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int allWidth = (int) (100 * size * density);
		int itemWidth = (int) (100 * density);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				allWidth, LinearLayout.LayoutParams.FILL_PARENT);
		gv.setLayoutParams(params);
		gv.setColumnWidth(itemWidth);
		// gv.setHorizontalSpacing(1);
		gv.setStretchMode(GridView.NO_STRETCH);
		gv.setNumColumns(size);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

			}
		});
//		if (guidTypelist.size() > 0) {
//			getDoucmentsList(guidTypelist.get(0).getId());
//		} else {
//			listView.setVisibility(View.GONE);
//			empertyView.setVisibility(View.VISIBLE);
//		}
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
					case GET_DUCMENTS_SUCESS:
						dismissdialog();
						if (null != msg && null != msg.obj) {
							try {
								listView.onRefreshComplete();
								setListResult(new JSONObject(msg.obj.toString()));
							} catch (JSONException e5) {
								e5.printStackTrace();
							}
						}
						break;
					case GET_DUCMENTS_FAIL:
						dismissdialog();
						listView.onRefreshComplete();
						ToastUtils.showToast(DoucmentListActivity.this, "亲，您的网络不顺畅哦！");
						break;

					default:
						break;
				}
			}
		}
	};

	protected void getDoucmentsList(String id) {
		showprocessdialog();
		if (null != dcAdapter) {
			doucmentList.clear();
			dcAdapter.notifyDataSetChanged();
		}
		isFirst = true;
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			page = 1;
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("types", typesId);
			job.put("subtypes", id);
			job.put("kind", kinds);
			job.put("page_index", page);
			String jobStr = AbStrUtil
					.parseStrToMd5L32(AbStrUtil.parseStrToMd5L32(job.toString())
							+ "discover/documentInfo");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, DoucmentListActivity.this,
				GET_DUCMENTS_SUCESS, GET_DUCMENTS_FAIL, job, "discover/documentInfo");
	}

	protected void getAddDoucmentsList(String id) {
		showprocessdialog();
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("types", typesId);
			job.put("subtypes", id);
			job.put("kind", kinds);
			job.put("page_index", page);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, DoucmentListActivity.this,
				GET_DUCMENTS_SUCESS, GET_DUCMENTS_FAIL, job, "discover/documentInfo");
	}

	List<DoucmentVo> doucmentList = new ArrayList<DoucmentVo>();

	protected void setListResult(JSONObject jsonObject) {

		try {
			listView.onRefreshComplete();
			if ("0".equals(jsonObject.optString("result_id"))) {
				JSONArray jarr = jsonObject.optJSONArray("docList");
				if (null != jarr) {
					List<DoucmentVo> list = gson.fromJson(jarr.toString(),
							new TypeToken<List<DoucmentVo>>() {
							}.getType());
					String num = jsonObject.optString("totalPages");
					totalnum = Integer.parseInt(num);
					if (totalnum > page) {
						listView.setMode(Mode.BOTH);
					} else {
						listView.setMode(Mode.PULL_DOWN_TO_REFRESH);
					}
					listView.setOnRefreshListener(new OnRefreshListener2() {

						@Override
						public void onPullDownToRefresh(PullToRefreshBase refreshView) {
							getDoucmentsList(subType);
						}

						@Override
						public void onPullUpToRefresh(PullToRefreshBase refreshView) {
							if (totalnum > page) {
								page++;
								getAddDoucmentsList(subType);
							} else {
								listView.setMode(Mode.PULL_DOWN_TO_REFRESH);
							}
						}
					});
					refreshUi(list);
				}

			} else {
				listView.onRefreshComplete();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void refreshUi(List<DoucmentVo> list) {
		if (isFirst) {
			doucmentList.addAll(list);
			isFirst = false;
		} else {
			doucmentList.addAll(list);
		}
		dcAdapter.notifyDataSetChanged();
		setEmperty();
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rl_back:
				finish();
				break;
			case R.id.et_search:
				Intent it = new Intent(DoucmentListActivity.this, SearchActivity.class);
				it.putExtra("types", typesId);
				startActivityForResult(it, 0x105);
				break;
			default:
				break;
		}

	}

	// 阅读文献时，增加阅读次数
	protected void getReadResult(String id) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("doc_id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, DoucmentListActivity.this, READ_SUCESS,
				READ_FAIL, job, "discover/readDocumentInfo");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
