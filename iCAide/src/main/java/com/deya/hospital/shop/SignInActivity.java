package com.deya.hospital.shop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.GoodsItemAdapter;
import com.deya.hospital.adapter.ScorGvAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView2;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.Goods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends BaseActivity implements OnClickListener {
    protected static final int SIGN_SUCESS = 0x5006;
    protected static final int SIGN_FAILE = 05007;
    private static final int SUCCESS_LIST = 0x1058;
    private static final int FAIL_LIST = 0x1059;

    private ListView listView;
    GridView scoreGv;
    ScorGvAdapter scAdapter;
    TextView tomorrowNum;
    TextView continueNumTv;
    public int totalNum = 0;
    private int totalScore = 0;
    private TextView totalTv;
    Tools tools;
    private List<Goods> list = new ArrayList<Goods>();
    private GoodsItemAdapter adapter;
    boolean isVipHospital = false;
    private CommonTopView2 topView;
    SinTipsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        tools = new Tools(mcontext, Constants.AC);
        initView();
        getUserInfo();
        setAdapter();
        getData();
        dialog = new SinTipsDialog(SignInActivity.this, "+");
    }

    @Override
    protected void onResume() {
        totalTv.setText(tools.getValue(Constants.INTEGRAL));
        super.onResume();
    }

    private void setAdapter() {
        list.clear();
        adapter = new GoodsItemAdapter(mcontext, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Goods p = list.get(position - 1);
                Intent itent = new Intent(mcontext, ShopGoodsInfoActivity.class);
                itent.putExtra("data", p);
                startActivity(itent);
            }
        });
    }

    /**
     * 刷新数据
     *
     * @param msg
     */
    private void refreshData(String msg) {
        DebugUtil.debug("getgoodslist", msg);
        list.clear();
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String r = jsonObject.getString("result_id");
            if (null != r && r.equals("0")) {
                // String goods=jsonObject.getString("goods");
                JSONArray jsonArray = jsonObject.getJSONArray("goods");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Goods goods = new Goods();
                    JSONObject itemJson = jsonArray.getJSONObject(i);
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

    private void getData() {

        showprocessdialog();
        JSONObject job = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("device_type", "2");
            String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
                    .parseStrToMd5L32(job.toString()) + "goods/allGoodsList");
            json.put("token", jobStr);
            json.put("msg_content", job.toString());
            Log.i("11111111", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                SignInActivity.this, SUCCESS_LIST, FAIL_LIST, job,
                "goods/allGoodsList");
    }

    private void initView() {

        topView = (CommonTopView2) findViewById(R.id.topView);
        topView.setBackAndRightClick(this, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mcontext, ScoreRuleActivity.class);
                it.putExtra("notop", true);
                startActivityForResult(it, 0x105);
            }
        });

        ((TextView) this.findViewById(R.id.title)).setText(R.string.sign_in);
        listView = (ListView) this.findViewById(R.id.list);

        View headView = LayoutInflater.from(mcontext).inflate(
                R.layout.item_signin_list_top, null);
        totalTv = (TextView) headView.findViewById(R.id.totalTv);
        totalTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
        tomorrowNum = (TextView) headView.findViewById(R.id.tomorrowNum);
        continueNumTv = (TextView) headView.findViewById(R.id.continueNumTv);
        scoreGv = (GridView) headView.findViewById(R.id.scoreGv);
        listView.addHeaderView(headView);
        scAdapter = new ScorGvAdapter(mcontext, totalNum, isVipHospital);
        scoreGv.setAdapter(scAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;

        }
    }

    // 请求服务器验证 账号密码

    private void signRequest() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("soft_ver", "3.5.2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                SignInActivity.this, SIGN_SUCESS, SIGN_FAILE, job,
                "member/userSign");
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case SIGN_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setsignResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case SIGN_FAILE:
                        finish();
                        ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
                        break;
                    case SUCCESS_LIST:
                        dismissdialog();
                        if (null != msg && null != msg.obj) {
                            refreshData(msg.obj.toString());
                        }
                        break;
                    case FAIL_LIST:
                        dismissdialog();

                        ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
                        break;
                    case GET_SUCESS:
                        if (null != msg && null != msg.obj) {
                            Log.i("1111msg", msg.obj + "");
                            try {
                                setInfoRes(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };


    protected void setsignResult(JSONObject jsonObject) {

        Log.i("11111111", jsonObject.toString());
        if (jsonObject.optString("result_id").equals("0")) {
            if (!AbStrUtil.isEmpty(jsonObject.optString("total"))) {
                totalNum = jsonObject.optInt("total");
                continueNumTv.setText(totalNum + "");
            }
            if (!AbStrUtil.isEmpty(jsonObject.optString("integral_sum"))) {
                String integal = jsonObject.optString("integral_sum");
                totalTv.setText(integal);
                tools.putValue(Constants.INTEGRAL, integal);
            }
            if (!AbStrUtil.isEmpty(jsonObject.optString("integral"))) {
                int score2 = jsonObject.optInt("integral");
                if (null != mcontext && null != dialog && score2 > 0) {
                    if (this != null) {
                        try {
                            dialog.setScoreText("+" + score2);
                            dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (totalNum >= 7) {
//				if (isVipHospital) {
//					tomorrowNum.setText("+" + 35 * 2);
//				} else {
                tomorrowNum.setText("+" + 70);
                //		}

            } else {
//				if (isVipHospital) {
//					tomorrowNum.setText("+" + (totalNum + 1) * 10);
//				} else {
                tomorrowNum.setText("+" + (totalNum + 1) * 10);
                //		}

            }
            scAdapter.setNum(totalNum - 1);

        } else {
            ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
            finish();
        }
    }

    protected static final int GET_SUCESS = 0x3007;
    protected static final int GET_FAILE = 0x3008;

    public void getUserInfo() {
        JSONObject job = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            Log.i("1111", job.toString());
            String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
                    .parseStrToMd5L32(job.toString()) + "member/getUserInfo");
            json.put("token", jobStr);
            json.put("msg_content", job.toString());
            Log.i("11111111", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                SignInActivity.this, GET_SUCESS, GET_FAILE, job,
                "member/getUserInfo");
    }

    protected void setInfoRes(JSONObject jsonObject) {
        Log.i("111111111", jsonObject.toString());
        if (jsonObject.optString("result_id").equals("0")) {

            JSONObject job = jsonObject.optJSONObject("user");

            String isVip = job.optString("is_sign");
            if (!AbStrUtil.isEmpty(isVip) && isVip.equals("0")) {
                tools.putValue(Constants.IS_VIP_HOSPITAL, "0");
                isVipHospital = true;

            } else {
                tools.putValue(Constants.IS_VIP_HOSPITAL, "1");
                isVipHospital = false;
            }
            scAdapter.setVipState(isVipHospital);
            signRequest();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != dialog) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    ;
}
