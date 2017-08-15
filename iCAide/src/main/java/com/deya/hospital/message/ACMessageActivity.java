package com.deya.hospital.message;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MessageAdapter;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.picMessageVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ACMessageActivity extends BaseActivity implements OnClickListener {
	private TextView timeTv;
	private RelativeLayout imgRl;
	private ListView list;
	private ImageView firtImg;
	private TextView aboutTv,firstcontent;
	private ListView listView;
	private MessageAdapter adapter;
	List<picMessageVo> plist=new ArrayList<picMessageVo>();
	private DisplayImageOptions optionsSquare;
	private LinearLayout networkView;
	private CommonTopView topView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagae_ac);
		//plist=SystemMessageFragment.picMeaasgelist;
		plist=MyAppliaction.picMeaasgelist;
		Log.i("1111111111adpter", plist.size()+"");
		checkNetWork();
		initView();
	}
	public void checkNetWork(){
		networkView=(LinearLayout) this.findViewById(R.id.networkView);
			if(NetWorkUtils.isConnect(mcontext)){
				networkView.setVisibility(View.GONE);
			}else{
				networkView.setVisibility(View.VISIBLE);
			}
			
		}
	private void initView() {
		optionsSquare = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.defult_img)
		.showImageForEmptyUri(R.drawable.defult_img)
		.showImageOnFail(R.drawable.defult_img)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		timeTv = (TextView) this.findViewById(R.id.timeTv);
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle("新手帮助");
		imgRl = (RelativeLayout) this.findViewById(R.id.imgRl);
		imgRl.setOnClickListener(this);
		firtImg=(ImageView) this.findViewById(R.id.firtImg);
		firstcontent=(TextView) this.findViewById(R.id.firstcontent);
		//aboutTv = (TextView) this.findViewById(R.id.aboutTv);
		listView=(ListView) this.findViewById(R.id.listView);
		adapter=new MessageAdapter(mcontext, plist);
		listView.setAdapter(adapter);
		if(plist.size()>0){
			timeTv.setText(plist.get(0).getRecommend_time());
			if (!AbStrUtil.isEmpty(plist.get(0).getTop_pic())) {
				ImageLoader.getInstance().displayImage(
						WebUrl.FILE_LOAD_URL+	plist.get(0).getTop_pic(),
						firtImg, optionsSquare);
			}
			firstcontent.setText(plist.get(0).getTopic());
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent toWeb = new Intent(mcontext, WebViewDemo.class);
				toWeb.putExtra("url",
						WebUrl.DOCMENTURL
								+ plist.get(position+1).getId());
				Log.i("1111111111", WebUrl.DOCMENTURL
								+ plist.get(position+1).getId());
				toWeb.putExtra("title",plist.get(position+1).getTopic());
				startActivity(toWeb);
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgRl:
			Intent it=new Intent(mcontext,WebViewDemo.class);
			if(null!=plist&&plist.size()>0){
			it.putExtra("title", plist.get(0).getTopic());
			it.putExtra("url",
					WebUrl.DOCMENTURL
							+ plist.get(0).getId());
			startActivity(it);
			}
			break;
		case R.id.rl_back:
			finish();
			break;
		default:
			break;
		}

	}

}
