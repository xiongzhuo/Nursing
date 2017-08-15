package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.CompletlyWHOTaskActivity;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.GuideTypeVo;
import com.deya.hospital.vo.KindsVo;
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

import java.util.ArrayList;
import java.util.List;

public class GuidListActivity extends BaseActivity implements OnClickListener {
    protected static final int GET_TYPELIST_SUCESS = 0x40001;
    protected static final int GET_TYPELIST_FAIL = 0x40002;
    ListView listView;
    LayoutInflater inflater;
    private List<GuideTypeVo> guidTypelist = new ArrayList<GuideTypeVo>();
    private DisplayImageOptions optionsSquare;
    TextView searchlTv;
    String typeId;
    List<KindsVo> kindlist = new ArrayList<KindsVo>();
    private MyHandler myHandler;
    Gson gson = new Gson();
    private List<TypesVo> typeList = new ArrayList<TypesVo>();
    private GuidAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_guide_activity);

        optionsSquare = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.defult_list_img)
                .showImageForEmptyUri(R.drawable.defult_list_img)
                .showImageOnFail(R.drawable.defult_list_img)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .considerExifParams(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        initMyHandler();

        RelativeLayout rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

        listView = (ListView) this.findViewById(R.id.list);
        inflater = LayoutInflater.from(mcontext);
        typeId = getIntent().getStringExtra("id");
        adapter = new GuidAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onStartActivity(position);

            }
        });
        searchlTv = (TextView) this.findViewById(R.id.et_search);
        searchlTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mcontext, SearchActivity.class);
                startActivity(it);

            }
        });

        getTypeList();
    }


    public void onStartActivity(int position){
        if (guidTypelist.get(position).getPid().equals("13")) {
            if (tools.getValue(Constants.IS_VIP_HOSPITAL) != null && tools.getValue(Constants.IS_VIP_HOSPITAL).equals("1")) {
                Intent it2 = new Intent(mcontext, CompletlyWHOTaskActivity.class);
                it2.putExtra("type", "2");
                startActivity(it2);
            }
//					else if(guidTypelist.get(position).getSubTypesTwo().size()<=0){
//						Intent it3=new Intent(mcontext, CompletlyWHOTaskActivity.class);
//						it3.putExtra("type", "1");//没有文章类型列表的时候
//						startActivity(it3);
//					}
            else {
                Intent it = new Intent(mcontext, DoucmentListActivity.class);
                it.putExtra("types", guidTypelist.get(position).getPid());
                it.putExtra("id", guidTypelist.get(position).getId());
//						it.putExtra("kindslist",(Serializable) guidTypelist.get(position).getSubTypesTwo());
                it.putExtra("kinds", "");
                startActivity(it);
            }
        } else {
            Intent it = new Intent(mcontext, DoucmentListActivity.class);
            it.putExtra("types", guidTypelist.get(position).getPid());
            it.putExtra("id", guidTypelist.get(position).getId());
//					it.putExtra("kindslist",(Serializable) guidTypelist.get(position).getSubTypesTwo());
            it.putExtra("kinds", "");
            startActivity(it);
        }
    }
    private void initMyHandler() {
        myHandler = new MyHandler(this) {
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
                            ToastUtils.showToast(GuidListActivity.this, "网络延迟，请稍后重试！");
                            break;

                        default:
                            break;
                    }
                }
            }
        };
    }

    protected void setTypeListResult(JSONObject jsonObject) {
        JSONArray jarr = jsonObject.optJSONArray("types");
        if (null != jarr) {
            typeList = gson.fromJson(jarr.toString(),
                    new TypeToken<List<TypesVo>>() {
                    }.getType());
            guidTypelist = (ArrayList<GuideTypeVo>) typeList.get(1).getSubTypes();
            adapter.notifyDataSetChanged();
        }

//		SharedPreferencesUtil.saveString(this, "discoverData",
//				jsonObject.toString());
    }


    public void getTypeList() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler, GuidListActivity.this,
                GET_TYPELIST_SUCESS, GET_TYPELIST_FAIL, job,
                "discover/discoverTypesNew");
    }

    public class GuidAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return guidTypelist.size() > 0 ? guidTypelist.size() : 0;
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
                convertView = inflater.inflate(R.layout.discover_guide_item, null);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.titleTv);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.contentTv);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(guidTypelist.get(position).getType_name());
            viewHolder.content.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL + guidTypelist.get(position).getAttachment(), viewHolder.img, optionsSquare);
            viewHolder.content.setText(guidTypelist.get(position).getDescription());
            return convertView;

        }

    }

    public class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView content;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            default:
                break;
        }

    }
}
