package com.deya.hospital.descover;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.QuestionDetailImageListAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.AnswerVo;
import com.deya.hospital.vo.QuestionVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.widget.popu.TipsDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuetionDetailActivity extends BaseActivity implements
        View.OnClickListener {
    protected static final int GET_SUCESS = 0x70003;
    protected static final int GET_FAILE = 0x70004;
    private static final int ZAN_SUCCESS = 0x70001;
    private static final int ADOP_SUCCESS = 0x70005;
    private static final int ZAN_FAIL = 0x70001;
    private static final int ADD_SUCESS = 0x700010;
    private static final int ADD_FAILE = 0x700011;
    private Button btn_answer;
    private Button btn_share;
    PullToRefreshListView answerListView;
    AnswerAdapter adapter;
    List<AnswerVo> list = new ArrayList<AnswerVo>();
    private RelativeLayout rlBack;
    QuestionVo qVo;
    String questionId = "";
    TextView que_title;
    //    TextView que_time;
    GridView imgGv;
    String q_id;
    List<Attachments> attList = new ArrayList<Attachments>();
    QuestionDetailImageListAdapter gvAdapter;
    LinearLayout empertyView;
    private String integral="";
    private String state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questiondetail);
        qVo = (QuestionVo) getIntent().getSerializableExtra("data");
        if (qVo != null && !AbStrUtil.isEmpty(qVo.getQ_id()+"")) {
            questionId = qVo.getQ_id() + "";
        }

        if (getIntent().hasExtra("q_id") && !AbStrUtil.isEmpty(getIntent().getStringExtra("q_id"))) {
            questionId = getIntent().getStringExtra("q_id");
        }
        intTopView();
        intitView();
    }

    TextView answerContenTv;
    View headline;

    private void intitView() {
        answerListView = (PullToRefreshListView) this
                .findViewById(R.id.listView);

        adapter = new AnswerAdapter(mcontext, list, new AnswerAdapter.AnswerInter() {

            @Override
            public void onClickZan(int position) {
                // 采纳
                zanPosition = position;
                doZan(list.get(position).getA_id());
            }

            @Override
            public void onClickAdopt(int position) {
                // 点赞
                if (adapter.isMine) {
                    zanPosition = position;
                    adapter.showAdopt = false;
                    doAdopt(list.get(position).getA_id());
                }

            }
        });
        answerListView.setAdapter(adapter);

        View headView = LayoutInflater.from(mcontext).inflate(
                R.layout.layout_question_detail_headview, null);
        headline = headView.findViewById(R.id.headline);

        imgGv = (GridView) headView.findViewById(R.id.imgGv);
        gvAdapter = new QuestionDetailImageListAdapter(mcontext, attList,
                new QuestionDetailImageListAdapter.ImageAdapterInter() {

                    @Override
                    public void ondeletImage(int position) {

                    }
                });
        imgGv.setAdapter(gvAdapter);
        que_title = (TextView) headView.findViewById(R.id.que_title);
        empertyView = (LinearLayout) headView.findViewById(R.id.empertyView);
        answerContenTv = (TextView) headView.findViewById(R.id.answerContenTv);
        answerListView.getRefreshableView().addHeaderView(headView);
        btn_answer = (Button) findViewById(R.id.btn_answer);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_answer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mcontext, AnswerQuestionActivity.class);
                it.putExtra("id", questionId + "");
                startActivity(it);
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void showShare() {
        SHARE_MEDIA[] shareMedia = {SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN_CIRCLE};

        showShareDialog(sharetitle,
                shareContent,
                WebUrl.FILE_PDF_LOAD_URL + "/gkgzj-question/answer_detail.html?id="
                        + questionId);
    }

    public void getAddScore(String id){
        tools=new Tools(mcontext, Constants.AC);
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("aid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler,QuetionDetailActivity.this, ADD_SUCESS,
                ADD_FAILE, job,"goods/actionGetIntegral");
    }

    protected void setAddRes(JSONObject jsonObject) {
        if(jsonObject.optString("result_id").equals("0")){
            int score=jsonObject.optInt("integral");
            String str=tools.getValue(Constants.INTEGRAL);
            if(null!=str){
                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)+score+"");
            }else{
                tools.putValue(Constants.INTEGRAL,score+"");
            }
            if(score>0){
                showTipsDialog(score+"");
            }

        }
    }

    Dialog scoreAdddialog;
    public void showTipsDialog(String score){
        if(scoreAdddialog!=null){
            scoreAdddialog.cancel();
            scoreAdddialog=null;
        }
        scoreAdddialog = new TipsDialog(mcontext, score + "");
        if (this != null && !isFinishing()) {
            scoreAdddialog.show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        list.clear();
        sendDetailReq();
    }

    private void intTopView() {
        titleTv = (TextView) this.findViewById(R.id.title);

        rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
        rlBack.setOnClickListener(this);
        TextView answerBtn = (TextView) this.findViewById(R.id.submit);// 回答
        answerBtn.setOnClickListener(this);
        answerBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();

                break;
            case R.id.submit:
                break;
            default:
                break;
        }

    }

    public SpannableStringBuilder getSpanString(Context context, String src1, String src2, int style1, int style2) {
        SpannableStringBuilder spanStr = null;
        String src = src1 + src2;
        int length1 = src1.length();
        int lengthAll = src.length();
        if (context != null) {
            spanStr = new SpannableStringBuilder(src);
            if (0 != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style1), 0, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (lengthAll != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style2), length1, lengthAll, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spanStr != null?spanStr:null;
    }


    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case GET_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setDetailListRes(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_FAILE:
                        ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
                        break;
                    case ZAN_SUCCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setZantReq(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ADOP_SUCCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setAdopReq(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ADD_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setAddRes(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ADD_FAILE:
                        // ToastUtils.showToast(getActivity(), "");
                        break;
                    default:
                        break;
                }
            }
        }

    };

    protected void setAdopReq(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            list.get(zanPosition).setIs_adopt(1);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        }

    }

    private int zanPosition = 0;

    protected void setZantReq(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {

            list.get(zanPosition).setIs_like(jsonObject.optInt("is_like"));
            list.get(zanPosition)
                    .setLike_count(jsonObject.optInt("like_count"));
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        }

    }

    /**
     * @deprecated 详情数据获取接口
     */
    private void sendDetailReq() {
        JSONObject job = new JSONObject();
        try {

            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("q_id", questionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler,
                QuetionDetailActivity.this, GET_SUCESS, GET_FAILE, job,
                "questions/questionDetail");

    }

    Gson gson = new Gson();
    private TextView titleTv;
    private String sharetitle;
    private String shareContent;

    private void setDetailListRes(JSONObject jsonObject) {
        list.clear();

        JSONObject job = jsonObject.optJSONObject("info");
        if (job.has("title")) {
//            titleTv.setText(job.optString("title"));
            sharetitle = job.optString("title");
        }
        if (job.has("create_time")) {
            que_title.setText(getSpanString(this,sharetitle,"(" + job.optString("update_time")+")",R.style.text_balck_16sp,R.style.text_gray_12sp));
        }
        if (job.has("my_type")) {
            if ("我的提问".equals(job.optString("my_type"))) {
                adapter.isMine = true;
            }
        }
        if (job.has("q_id")) {
            q_id = job.optString("q_id");
        }
        if (!AbStrUtil.isEmpty(job.optString("integral"))) {
            integral = job.optString("integral");
        }
        if (!AbStrUtil.isEmpty(job.optString("state"))) {
            state = job.optString("state");
        }
        if (job.has("content")) {
            answerContenTv.setText(job.optString("content"));
            shareContent = job.optString("content");
            if (job.optString("content").length() <= 0) {
                headline.setVisibility(View.GONE);
            }
        }
        if (job.has("q_attachment")) {
            JSONArray attJarr = job.optJSONArray("q_attachment");
            List<Attachments> attList2 = gson.fromJson(attJarr.toString(),
                    new TypeToken<List<Attachments>>() {
                    }.getType());
            attList.clear();
            attList.addAll(attList2);
            gvAdapter.setImageList(attList);
            if (attList.size() > 0) {
                headline.setVisibility(View.VISIBLE);
            }else{
                imgGv.setVisibility(View.GONE);
            }
        }

        int  is_have_adopt = 0;
        if (job.has("answerList")) {
            JSONArray jarr = job.optJSONArray("answerList");
            List<AnswerVo> list2 = null;
            try {
                list2 = gson.fromJson(jarr.toString(),
                        new TypeToken<List<AnswerVo>>() {
                        }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (list2 != null && list2.size() > 0) {
                is_have_adopt = list2.get(0).getIs_adopt();
            }
            list.addAll(list2);
            titleTv.setText(list2.size() + "个回答");
            adapter.notifyDataSetChanged();
        } else {
            titleTv.setText( "0个回答");
        }



        if (state.equals("0") && !integral.equals("0") && is_have_adopt != 1){
            ToastUtils.showToast(mcontext,"此问题是悬赏问答，您的回答如果被采纳，您将获得"+integral+"橄榄悬赏");
        }
        if (list.size() > 0) {
            empertyView.setVisibility(View.GONE);
        } else {
            empertyView.setVisibility(View.VISIBLE);
        }
    }

    // 点赞
    protected void doZan(String aId) {
        JSONObject job2 = new JSONObject();
        try {
            job2.put("authent", tools.getValue(Constants.AUTHENT));
            // job2.put("article_id", infoList.get(position).getId());
            job2.put("a_id", aId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler,
                QuetionDetailActivity.this, ZAN_SUCCESS, ZAN_FAIL, job2,
                "questions/clickLike");
    }

    // 点赞
    protected void doAdopt(String aId) {
        JSONObject job2 = new JSONObject();
        try {
            job2.put("authent", tools.getValue(Constants.AUTHENT));
            // job2.put("article_id", infoList.get(position).getId());
            job2.put("q_id", q_id);
            job2.put("a_id", aId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler,
                QuetionDetailActivity.this, ADOP_SUCCESS, ZAN_FAIL, job2,
                "questions/adoptAnswer");

    }

}
