package com.deya.hospital.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.deya.hospital.adapter.GoodsItemAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.Goods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 奖品列表
 * @author  yung
 * @date 创建时间：2016年1月15日 下午2:59:55
 * @version 1.0
 */
public class ShopGoodsListActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout rl_back;
    private ImageView img_back;

    private ListView listView;
    private TextView backText,submit;

    private GoodsItemAdapter adapter;
    private List<Goods> list=new ArrayList<Goods>();
    private Tools tools;
    private LinearLayout networkView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        tools = new Tools(mcontext, Constants.AC);
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
        rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        backText = (TextView) this.findViewById(R.id.backText);
        backText.setOnClickListener(this);
        submit = (TextView) this.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        submit.setVisibility(View.VISIBLE);

        submit.setText(res.getString(R.string.credit_my));
        ((TextView)this.findViewById(R.id.title)).setText(res.getString(R.string.credit_title));


        listView = (ListView) this.findViewById(R.id.list);
        setAdapter();
        getData();
    }

    private static final int SUCCESS = 0x1055;
    private static final int FAIL = 0x1056;
    private void getData(){

        showprocessdialog();
        JSONObject job = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("device_type", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,ShopGoodsListActivity.this, SUCCESS, FAIL,
                job,"goods/allGoodsList");
    }

    /**
     *
     */
    private void setAdapter(){
        list.clear();
        adapter = new GoodsItemAdapter(mcontext,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Goods p=list.get(position);
                Intent itent=new Intent(mcontext, ShopGoodsInfoActivity.class);
                itent.putExtra("data", p);
                startActivity(itent);
            }
        });
    }
    /**
     * 刷新数据
     * @param msg
     */
    private void refreshData(String msg){
        DebugUtil.debug("getgoodslist", msg);
        list.clear();
        try {
            JSONObject jsonObject=new JSONObject(msg);
            String r=jsonObject.getString("result_id");
            if(null!=r&&r.equals("0")){
                //String goods=jsonObject.getString("goods");
                JSONArray jsonArray=jsonObject.getJSONArray("goods");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Goods goods=new Goods();
                    JSONObject itemJson =jsonArray.getJSONObject(i);
                    goods.setDescription(itemJson.getString("description"));
                    goods.setId(itemJson.getInt("id"));
                    goods.setIntegral(itemJson.getInt("integral"));
                    goods.setName(itemJson.getString("name"));
                    goods.setPicture(itemJson.getString("picture"));
                    goods.setIs_sign(itemJson.optInt("is_sign"));
                    list.add(goods);
                }
            }
        } catch (JSONException e5) {
            e5.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case SUCCESS:
                        dismissdialog();
                        if (null != msg && null != msg.obj) {
                            refreshData(msg.obj.toString());
                        }
                        break;
                    case FAIL:
                        dismissdialog();
                        networkView.setVisibility(View.VISIBLE);
                        ToastUtils.showToast(mcontext,
                                "亲，您的网络不顺畅哦！");
                        break;

                    default:
                        break;
                }
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
            case R.id.rl_back:
            case R.id.backText:
                finish();
                break;
            case R.id.submit:
                Intent it = new Intent(ShopGoodsListActivity.this, PrizListActivity.class);
                startActivity(it);
                break;
            default:
                break;
        }

    }
}
