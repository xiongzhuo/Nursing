package com.deya.hospital.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.ReviewTipsDialog;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.form.xy.XiangyaPrivewActivity;
import com.deya.hospital.supervisor.ChooseDepartActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.GsonUtils;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.KeySectorsFrom.KeySectorsFormActivity;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作间-医院感染管理临床质控-选择考核表格
 */
public class FormListActivity extends BaseActivity implements OnClickListener {
    protected static final int SEND_SUCESS = 0x7008;
    protected static final int SEND_FAILE = 0x7009;
    private static final int REQ_SUC =0x7010 ;
    RequestInterface requestInterface;
    FormListAdapter formAdapter;
    Gson gson = new Gson();
    TaskVo data = new TaskVo();
    List<FormVo> flist_isClose = new ArrayList<>();
    private com.deya.hospital.util.CommonTopView topView;
    private LinearLayout ll_common;
    private TextView tv_common;
    private ListView formList;
    private TextView tv_empty;
    private Button radio_ques;

    private LayoutInflater layoutInflater;
    private RisistantVo rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list2);
        if (getIntent().hasExtra("data")) {
            data = (TaskVo) getIntent().getSerializableExtra("data");
        }
        bindViews();
        initList();
        initCilck();
//        getFormList();
//        getFormCache();
        getQualityTempList();
        getSaveLevels();
    }

    private void bindViews() {
        topView = (com.deya.hospital.util.CommonTopView) findViewById(R.id.topView);
        ll_common = (LinearLayout) findViewById(R.id.ll_common);
        tv_common = (TextView) findViewById(R.id.tv_common);
        formList = (ListView) findViewById(R.id.formList);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        radio_ques = (Button) findViewById(R.id.radio_ques);
        layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.head_hos, null);
        formList.addHeaderView(view);
        formAdapter = new FormListAdapter(mcontext, flist_isClose, 0, myHandler);
        formList.setAdapter(formAdapter);
        requestInterface=new RequestInterface() {
            @Override
            public void onRequestSucesss(int code, JSONObject jsonObject) {
                dismissdialog();
                Log.i("FormListActivity",jsonObject.toString());

            }

            @Override
            public void onRequestErro(String message) {
                dismissdialog();

            }

            @Override
            public void onRequestFail(int code) {
                dismissdialog();

            }
        };
    }


    public void getQualityTempList(){
        try {
            JSONObject job   =GsonUtils.creatJsonObj("").put("isPubilc","0").put("authent",tools.getValue(Constants.AUTHENT));
            MainBizImpl.getInstance().onComomReq(requestInterface,this,REQ_SUC,job,"quality/tempList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initList() {

        formList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                position = position - 1;
                if(position<0){
                    return;
                }
                int types = flist_isClose.get(position).getTypes();
                switch (types) {
                    case 6:
                        Intent it = new Intent(mcontext,
                                XiangyaPrivewActivity.class);
                        data.setFormType(3);
                        data.setTotalscore(flist_isClose.get(position).getScore());
                        data.setName(flist_isClose.get(position).getName());
                        data.setFormId(flist_isClose.get(position).getId());
                        it.putExtra("data", data);
                        it.putExtra("itemList",
                                (Serializable) getFormItemCache(position));
                        it.putExtra("time", getIntent().getStringExtra("time"));
                        startActivityForResult(it, 0x116);
                        break;
                    case 3:
                        Intent it2 = new Intent(FormListActivity.this,
                                PreViewActivity.class);
                        Intent brodcastIntent = new Intent();
                        brodcastIntent
                                .setAction(ChooseDepartActivity.CLOSE_DEPART_ACTIVITY);// 关闭前一个页面
                        FormListActivity.this.sendBroadcast(brodcastIntent);
                        data.setFormType(flist_isClose.get(position).getType());
                        data.setTotalscore(flist_isClose.get(position).getScore());
                        data.setName(flist_isClose.get(position).getName());
                        data.setFormId(flist_isClose.get(position).getId());
                        it2.putExtra("time", getIntent().getStringExtra("time"));
                        it2.putExtra("itemList",
                                (Serializable) getFormItemCache(position));
                        it2.putExtra("data", data);
                        startActivityForResult(it2, 0x116);
                        break;
                    case 15:
                        RisistantVo.TempListBean bean = flist_isClose.get(position).getBean();
                        if (null == bean) {
                            return;
                        }
                        RisistantVo risistantVo = new RisistantVo();
                        risistantVo.getTemp_list().add(bean);
                        Intent it3 = new Intent(mcontext, KeySectorsFormActivity.class);
                        it3.putExtra("formdata", TaskUtils.gson.toJson(risistantVo));
                        it3.putExtra("title", bean.getTitle());
                        it3.putExtra("data", data);
                        startActivity(it3);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    private void initCilck() {
        topView.onbackClick(this, this);
        ll_common.setOnClickListener(this);
        radio_ques.setOnClickListener(this);
    }

    private void getFormCache() {
        if (null != flist_isClose) {
            flist_isClose.clear();
        }
        try {
            List<FormVo> chache2 = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(FormVo.class).where("types", "!=", 5).and(WhereBuilder.b(
                                    "is_open",
                                    "=",
                                    0))
                                    .orderBy("dbid"));
            flist_isClose.addAll(chache2);
            String str = SharedPreferencesUtil.getString(mcontext, "comonform" + 16, "");
            if (AbStrUtil.isEmpty(str)) {
                return;
            }
            rv = TaskUtils.gson.fromJson(str, RisistantVo.class);

            for (RisistantVo.TempListBean temp : rv.getTemp_list()) {
                FormVo formvo = new FormVo();
                formvo.setName(temp.getTitle());
                formvo.setCreate_time(temp.getUpdate_time());
                formvo.setId(temp.getId() + "");
                formvo.setTypes(15);
                formvo.setHospital(AbStrUtil.getNotNullStr(temp.getHospital()));
                formvo.setBean(temp);
                formvo.setUse_cnt(temp.getUse_cnt() + "");
                formvo.setUname(AbStrUtil.getNotNullStr(temp.getUname()));
                formvo.setCreater(AbStrUtil.getNotNullStr(temp.getUname()));
                flist_isClose.add(formvo);
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (flist_isClose != null && flist_isClose.size() > 0) {
            formList.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
        } else {
            formList.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
        }
        formAdapter.setdata(flist_isClose);

    }

    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();

    private void getSaveLevels() {
        String str = SharedPreferencesUtil.getString(mcontext, "depart_levels",
                "");
        String childsStr = SharedPreferencesUtil.getString(mcontext,
                "departmentlist", "");
        List<ChildsVo> list2 = gson.fromJson(childsStr,
                new TypeToken<List<ChildsVo>>() {
                }.getType());
        List<ChildsVo> otherList = new ArrayList<ChildsVo>();
        if (!AbStrUtil.isEmpty(str)) {
            for (ChildsVo cv : list2) {
                if (cv.getParent().equals("0") || cv.getParent().equals("1")) {
                    otherList.add(cv);
                }
            }
            List<DepartLevelsVo> list = gson.fromJson(str,
                    new TypeToken<List<DepartLevelsVo>>() {
                    }.getType());
            departlist.addAll(list);
            for (DepartLevelsVo dlv : departlist) {
                if (dlv.getRoot().getId().equals("0")) {
                    if (dlv.getChilds().size() == 0) {
                        dlv.getChilds().addAll(otherList);
                        break;
                    }
                }
            }
            // TODO Auto-generated catch block
        } else {

            DepartLevelsVo dlv = new DepartLevelsVo();
            dlv.getRoot().setId("0");
            dlv.getRoot().setName("全部");
            if (null != list2) {
                dlv.getChilds().addAll(list2);
            }
            departlist.add(dlv);

        }
    }

    public List<FormDetailListVo> getFormItemCache(int position) {
        String str = flist_isClose.get(position).getCacheItems();
        List<FormDetailListVo> itemList = gson.fromJson(str,
                new TypeToken<List<FormDetailListVo>>() {
                }.getType());
        return itemList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.radio_ques:
                Intent it = new Intent(mcontext, CreatReviewFormActivity.class);
                startActivityForResult(it, 0x111);
                break;
            case R.id.ll_common:
                Intent it2 = new Intent(mcontext, FormListActivity2.class);
                it2.putExtra("time", getIntent().getStringExtra("time"));
                startActivity(it2);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x111) {
            ReviewTipsDialog dialog = new ReviewTipsDialog(mcontext);
            dialog.show();
        } else if (resultCode == 0x116) {
            finish();
        }
    }

    private void getFormList() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                FormListActivity.this, SEND_SUCESS, SEND_FAILE, job,
                "grid/queryTemplateInfos");
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case SEND_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setsListResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case SEND_FAILE:
                        String str = FormDataCache.getFormList(mcontext);
                        if (str.length() > 0) {
                            try {
                                setsListResult(new JSONObject(str));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
                        }
                        break;

                    default:
                        break;
                }
            }
        }

    };

    protected void setsListResult(JSONObject jsonObject) {
        JSONArray jarr = jsonObject.optJSONArray("grid_templates");
        if (null != jarr) {
            flist_isClose = gson.fromJson(jarr.toString(),
                    new TypeToken<List<FormVo>>() {
                    }.getType());
            formAdapter.setdata(flist_isClose);
        }
        FormDataCache.saveFormList(mcontext, jsonObject.toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFormCache();
        formAdapter.notifyDataSetChanged();
    }
}
