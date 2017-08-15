package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.deya.acaide.R;
import com.deya.hospital.adapter.DoucmentAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.dypdf.DyPdfActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.FileDownloadThread;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.DoucmentVo;
import com.deya.hospital.vo.SearchCacheVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements OnClickListener {
	private ImageView imgClear;
	private TextView imgSearch;
	private EditText edtsearch;
	private ListView historyListView;
	HistoryAdapter hAdapter;
	HotAdapter hotAdapter;
	private LayoutInflater inflater;
	List<SearchCacheVo> historyList = new ArrayList<SearchCacheVo>();
	List<SearchCacheVo> hotList = new ArrayList<SearchCacheVo>();
	private PullToRefreshListView listView;
	private DoucmentAdapter dcAdapter;
	Gson gson = new Gson();
	private static final int GET_DUCMENTS_SUCESS = 0x40003;
	private static final int GET_DUCMENTS_FAIL = 0x40004;
	private static final int READ_SUCESS = 0x40006;
	private static final int READ_FAIL = 0x40007;
	// 分页
	private int totalnum;
	int page = 1;
	private boolean isFirst = false;
	Tools tools;
	LinearLayout cacheView;
	String key = "";
	private ListView gv;
	String hotItem[] = { "手卫生", "职业暴露", "医院消毒", "内镜清洗消毒", "一次性使用卫生用品" };
	private LinearLayout networkView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		inflater = LayoutInflater.from(mcontext);
		tools = new Tools(mcontext, Constants.AC);
		getHistoryData();
		checkNetWork();
		initView();
	}

	public void checkNetWork() {
		networkView = (LinearLayout) this.findViewById(R.id.networkView);
		if (NetWorkUtils.isConnect(mcontext)) {
			networkView.setVisibility(View.GONE);
		} else {
			networkView.setVisibility(View.VISIBLE);
		}

	}

	private void initView() {
		imgSearch = (TextView) this.findViewById(R.id.searchImg);
		edtsearch = (EditText) this.findViewById(R.id.et_search);
		imgClear = (ImageView) this.findViewById(R.id.clear);
		imgClear.setOnClickListener(this);
		gv = (ListView) this.findViewById(R.id.gv);
		cacheView = (LinearLayout) this.findViewById(R.id.searchView);
		historyListView = (ListView) this.findViewById(R.id.listView);
		hAdapter = new HistoryAdapter(historyList);
		hotAdapter = new HotAdapter();
		gv.setAdapter(hotAdapter);
		historyListView.setAdapter(hAdapter);
		RelativeLayout rl_back=(RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		imgSearch.setOnClickListener(this);

		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				edtsearch.setText(hotItem[position]);
				startSearch(edtsearch.getText().toString());
			}
		});

		TextView text = (TextView) this.findViewById(R.id.empertyView);
		listView = (PullToRefreshListView) this.findViewById(R.id.list);
		listView.setMode(Mode.PULL_UP_TO_REFRESH);
		listView.setEmptyView(text);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String pdfur = doucmentList.get(position - 1).getPdf_attach();
				if (!AbStrUtil.isEmpty(pdfur)) {
					Log.i("11111111111", pdfur);
					shareUrl = WebUrl.DOCMENTURL
							+ doucmentList.get(position - 1).getId();
					OpenPdf(pdfur, doucmentList.get(position - 1).getTopic());
				} else {
					Intent toWeb = new Intent(mcontext, WebViewDemo.class);
					toWeb.putExtra("url",
							WebUrl.DOCMENTURL
									+ doucmentList.get(position - 1).getId());
					toWeb.putExtra("title", doucmentList.get(position - 1)
							.getTopic());
					toWeb.putExtra("type", "1");
					startActivity(toWeb);
				}
				int num = Integer.parseInt(doucmentList.get(position - 1)
						.getRead_count());
				doucmentList.get(position - 1).setRead_count((num + 1) + "");
				dcAdapter.notifyDataSetChanged();
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if (totalnum >= (page)) {
					getAddDoucmentsList(key);
				}
			}
		});
		dcAdapter = new DoucmentAdapter(mcontext, doucmentList);
		listView.setAdapter(dcAdapter);
		edtsearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView view, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) imgSearch.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					String text = edtsearch.getText().toString().trim();
					startSearch(text);
					return true;
				}
				return false;
			}
		});

		historyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cacheView.setVisibility(View.GONE);
				edtsearch.setText(historyList.get(position).getKey());
				startSearch(edtsearch.getText().toString());
				closeVirtualKeyboard(SearchActivity.this);
			}
		});
	}

	public void startSearch(String text) {
		this.key = text;
		if (text.length() > 0) {
			SearchCacheVo vo = new SearchCacheVo();
			vo.setKey(text);
			boolean hasCode = false;
			for (int i = 0; i < historyList.size(); i++) {
				if (historyList.get(i).getKey().equals(text)) {
					hasCode = true;
				}

			}

			if (!hasCode && text.trim().length() > 0) {
				historyList.add(vo);
				hAdapter.notifyDataSetChanged();
				saveCache();
			}
			cacheView.setVisibility(View.GONE);
			getDoucmentsList(text);
			closeVirtualKeyboard(SearchActivity.this);
		} else {
			ToastUtils.showToast(mcontext, "亲，请输入您要搜索的信息再搜索哦!");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;
		case R.id.searchImg:
			startSearch(edtsearch.getText().toString());
			break;
		case R.id.clear:
			cacheView.setVisibility(View.VISIBLE);
			edtsearch.setText("");
			break;
		default:
			break;
		}

	}

	public void saveCache() {
		try {
			if (null != DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(SearchCacheVo.class)
					&& DataBaseHelper
							.getDbUtilsInstance(mcontext)
							.findAll(SearchCacheVo.class).size() > 0) {
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.deleteAll(SearchCacheVo.class);
			} else {
				if (historyList.size() > 0) {
					Log.i("1111111", historyList.size() + "");
					DataBaseHelper.getDbUtilsInstance(mcontext)
							.saveAll(historyList);
				}
			}

		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	//
	public class HistoryAdapter extends BaseAdapter {
		List<SearchCacheVo> list;

		public HistoryAdapter(List<SearchCacheVo> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return historyList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.list_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.listtext);
			text.setText(list.get(position).getKey());
			return convertView;
		}

	}

	//
	public class HotAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return hotItem.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.list_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.listtext);
			text.setText(hotItem[position]);
			return convertView;
		}

	}

	public void getHistoryData() {
		try {
			if (null != DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(SearchCacheVo.class)) {
				historyList = DataBaseHelper
						.getDbUtilsInstance(mcontext)
						.findAll(SearchCacheVo.class);
				Log.i("1111111111", historyList.size() + "");

			} else {
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.saveAll(historyList);
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	// 获取列表数据
	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case GET_DUCMENTS_SUCESS:
					networkView.setVisibility(View.GONE);
					if (null != msg && null != msg.obj) {
						try {

							setListResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case GET_DUCMENTS_FAIL:
					listView.onRefreshComplete();
					networkView.setVisibility(View.VISIBLE);
					ToastUtils.showToast(SearchActivity.this, "亲，您的网络不顺畅哦！");
					break;

				default:
					break;
				}
			}
		}
	};

	protected void getDoucmentsList(String key) {
		isFirst = true;
		page = 1;
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("keywords", key);
			job.put("page_index", 1);
			job.put("types", tools.getValue(Constants.SEARCHTYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,
				SearchActivity.this, GET_DUCMENTS_SUCESS, GET_DUCMENTS_FAIL,
				job,"discover/documentInfo");
	}

	protected void getAddDoucmentsList(String key) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("keywords", key);
			job.put("page_index", page);
			job.put("types", tools.getValue(Constants.SEARCHTYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,
				SearchActivity.this, GET_DUCMENTS_SUCESS, GET_DUCMENTS_FAIL,
				job,"discover/documentInfo");
	}

	List<DoucmentVo> doucmentList = new ArrayList<DoucmentVo>();

	protected void setListResult(JSONObject jsonObject) {
		Log.i("search", jsonObject.toString());
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
						listView.setMode(Mode.PULL_UP_TO_REFRESH);
					} else {
						listView.setMode(Mode.DISABLED);
					}
					refreshUi(list);
					page++;
				}

			} else {
				ToastUtils.showToast(mcontext,
						jsonObject.optString("result_msg"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void refreshUi(List<DoucmentVo> list) {
		if (isFirst) {
			doucmentList.clear();
			doucmentList.addAll(list);
			isFirst = false;
		} else {
			doucmentList.addAll(list);
		}
		dcAdapter.notifyDataSetChanged();
	}

	/**
	 * colseVirtualKeyboard:【隐藏软键盘】. <br/>
	 * .@param activity.<br/>
	 */
	public static void closeVirtualKeyboard(Activity activity) {
		View view = activity.getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT);
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public boolean isLoading = false;
	String subfile = "";
	String title = "";
	private String shareUrl = "";

	public void OpenPdf(String fileName, String title) {
		subfile = fileName;
		this.title = title;
		File pdffile = new File(getPath() + "/" + fileName + ".pdf");
		// 再判断文件是否存在
		if (pdffile.exists() && !isLoading) {
			Uri uri = Uri.parse(pdffile.toString());
			Intent intent = new Intent(mcontext, DyPdfActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("url", shareUrl);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(uri);
			Log.i("11111", uri + "执行了");
			startActivity(intent);
		} else {
			if (isLoading) {
				return;

			}

			download(fileName);
		}
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
		Log.i("11111111", file.toString());
		// 创建下载目录
		if (!file.exists()) {
			file.mkdirs();
			Log.i("11111111", file.exists() + "");
		}

		// 读取下载线程数，如果为空，则单线程下载
		int downloadTN = 1;
		// 如果下载文件名为空则获取Url尾为文件名
		String downloadUrl = WebUrl.FILE_PDF_LOAD_URL + fileName;
		String pdfName = fileName + ".pdf";
		Log.i("11111111", downloadUrl);
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
				Log.i("1111111",
						fileSize + "" + conn.getHeaderField("Content-Length"));
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
					.append(DoucmentListActivity.DOWNLOAD_FOLDER_NAME)
					.append(File.separator).toString();
		}
		return "";
	}

}
