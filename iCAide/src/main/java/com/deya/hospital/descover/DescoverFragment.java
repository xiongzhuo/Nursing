package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.DiscovewMenueVo;
import com.deya.hospital.vo.TypesVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DescoverFragment extends BaseFragment implements OnClickListener {
	private LayoutInflater inflater;
	private View view;
	protected static final int GET_TYPELIST_SUCESS = 0x40001;
	protected static final int GET_TYPELIST_FAIL = 0x40002;
	private MyHandler myHandler;
	Gson gson = new Gson();
	Tools tools;
	List<DiscovewMenueVo> list = new ArrayList<DiscovewMenueVo>();
	ListView listView;
	MyAdapter adpter;
	private DisplayImageOptions optionsSquare;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {
		this.inflater = inflater;
		if (view == null) {
			view = inflater.inflate(R.layout.descover_fragment, container,
					false);
		} else {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}
		optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defult_list_img)
				.showImageForEmptyUri(R.drawable.defult_list_img)
				.showImageOnFail(R.drawable.defult_list_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		tools = new Tools(getActivity(), Constants.AC);
		inflater = LayoutInflater.from(getActivity());
		initMyHandler();

		initView();
		return view;
	}

	LinearLayout networkView;

	public void checkNetWork() {

		if (NetWorkUtils.isConnect(getActivity())) {
			networkView.setVisibility(View.GONE);
		} else {
			networkView.setVisibility(View.VISIBLE);
		}

	}

	private void initView() {
		networkView = (LinearLayout) view.findViewById(R.id.networkView);
		listView = (ListView) view.findViewById(R.id.list);
		adpter = new MyAdapter();
		listView.setAdapter(adpter);
		if (NetWorkUtils.isConnect(getActivity())) {
			if (hasCacheData()) {
				setCacheData();
			} else {
				getTypeList();
			}
		} else {
			setCacheData();

		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position > 0) {
					startOtherActivity(position - 1);
				} else {
					Intent guideIn = new Intent(getActivity(),
							QuestionSortActivity.class);
					startActivity(guideIn);
				}
			}

		});

	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		Log.i("shitu-sp", (pxValue / fontScale + 0.5f) + "");
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		Log.i("shitu-sp", (pxValue / scale + 0.5f) + "");
		return (int) (pxValue / scale + 0.5f);
	}

	public void setCacheData() {
		String data = SharedPreferencesUtil.getString(getActivity(),
				"discoverData", "");
		if (data.length() <= 0) {
			checkNetWork();
			return;
		}
		JSONObject job;
		try {
			job = new JSONObject(data);
			setTypeListResult(job);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean hasCacheData() {
		String data = SharedPreferencesUtil.getString(getActivity(),
				"discoverData", "");
		return data.length() > 0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guidelayout:
			startOtherActivity(0);
			break;
		case R.id.teachLayout:
			startOtherActivity(1);
			break;
		case R.id.countyLayout:
			startOtherActivity(2);
			break;
		case R.id.guizhouLayout:
			startOtherActivity(3);
			break;
		case R.id.hainanLayout:
			startOtherActivity(4);
			break;

		default:
			break;
		}

	}

	public void startOtherActivity(Class<?> t, Bundle b) {
		Intent it = new Intent(getActivity(), t);
		it.putExtra("data", b);
		startActivity(it);

	}

	public void startOtherActivity(int position) {
		Intent guideIn2 = new Intent(getActivity(), GuidListActivity.class);
		if (typeList.size() > 0) {
			guideIn2.putExtra("list", (Serializable) typeList.get(position)
					.getSubTypes());
			guideIn2.putExtra("id", typeList.get(position).getId());
			guideIn2.putExtra("title", typeList.get(position).getName());
			tools.putValue(Constants.SEARCHTYPE, typeList.get(position).getId());
			startActivity(guideIn2);
		}
	}

	// 鑾峰彇鍒嗙被淇℃伅

	private void initMyHandler() {
		myHandler = new MyHandler(getActivity()) {
			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case GET_TYPELIST_SUCESS:
						dismissdialog();
						if (null != msg && null != msg.obj) {
							try {
								setTypeListResult(new JSONObject(
										msg.obj.toString()));
							} catch (JSONException e5) {
								e5.printStackTrace();
							}
						}
						break;
					case GET_TYPELIST_FAIL:
						dismissdialog();
						ToastUtils.showToast(getActivity(), "网络延迟，请稍后重试！");
						break;

					default:
						break;
					}
				}
			}
		};
	}

	List<TypesVo> typeList = new ArrayList<TypesVo>();

	public void getTypeList() {
		showprocessdialog();
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(),
				GET_TYPELIST_SUCESS, GET_TYPELIST_FAIL, job,
				"discover/discoverTypesNew");
	}

	protected void setTypeListResult(JSONObject jsonObject) {
		Log.i("1111111111111111", jsonObject.toString());
		JSONArray jarr = jsonObject.optJSONArray("types");
		if (null != jarr) {
			typeList = gson.fromJson(jarr.toString(),
					new TypeToken<List<TypesVo>>() {
					}.getType());
			adpter.notifyDataSetChanged();
		}
		networkView.setVisibility(View.GONE);
		SharedPreferencesUtil.saveString(getActivity(), "discoverData",
				jsonObject.toString());
	}

	public class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return typeList.size() + 1;
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
			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.discover_list_item,
						null);
				viewHolder.imgIcon = (ImageView) convertView
						.findViewById(R.id.imgIcon);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (position != 0) {
				Log.i("1111111111111",
						WebUrl.FILE_PDF_LOAD_URL
								+ typeList.get(position - 1).getAttachment());
				ImageLoader.getInstance().displayImage(
						WebUrl.FILE_PDF_LOAD_URL
								+ typeList.get(position - 1).getAttachment(),
						viewHolder.imgIcon, optionsSquare);
				viewHolder.title.setText(typeList.get(position - 1)
						.getType_name());
			} else {
				viewHolder.imgIcon
						.setBackgroundResource(R.drawable.answer_icon);
				viewHolder.title.setText("感控问答");
			}
			return convertView;
		}

	}
	public class ViewHolder {
		public ImageView imgIcon;
		public TextView title;

	}
}
