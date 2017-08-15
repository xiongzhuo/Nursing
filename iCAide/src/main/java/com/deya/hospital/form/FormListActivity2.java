package com.deya.hospital.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.ReviewTipsDialog;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.form.xy.XiangyaPrivewActivity;
import com.deya.hospital.supervisor.ChooseDepartActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormVo;
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
 * yugq
 * 2016-6-21 14:21:17
 */
public class FormListActivity2 extends BaseActivity implements OnClickListener {

    protected static final int SEND_SUCESS = 0x7008;
    protected static final int SEND_FAILE = 0x7009;

    private static final int SAVE_SUCESS = 0x7010;
    private static final int SAVE_FAILE = 0x7011;
    FormListAdapter formAdapter;
    Gson gson = new Gson();
    TaskVo data = new TaskVo();
    List<FormVo> flist_isClose = new ArrayList<FormVo>();
    private com.deya.hospital.util.CommonTopView topView;
    private ListView formList;
    private LayoutInflater layoutInflater;
    private MyHandler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list2_1);
        initHandler();
        bindViews();
        initList();
        initCilck();
        // getFormList();
        getSaveLevels();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFormCache();

    }

    private void initHandler() {
        myHandler = new MyHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case SEND_SUCESS:
                            if (null != msg && null != msg.obj) {
                                try {
                                    setsListResult(new JSONObject(msg.obj.toString()));
                                } catch (Exception e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case SEND_FAILE:
                            String str = FormDataCache.getFormList(mcontext);
                            if (str.length() > 0) {
                                try {
                                    setsListResult(new JSONObject(str));
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
                            }
                            break;
                        case SAVE_SUCESS:
                            if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                                    ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
                                    if (jsonObject.optString("result_id").equals("0")) {
                                        FormVo fv=flist_isClose.get(formAdapter.getSavePosition());

                                        fv.setIs_save(1);
                                        DataBaseHelper.getDbUtilsInstance(mcontext).update(fv);
                                        if(jsonObject.has("id")){
                                            fv.setId(jsonObject.optString("id"));
                                        }
                                        fv.setIs_open(0);
                                        fv.setCreater(tools.getValue(Constants.USER_NAME));
                                        fv.setHospital(tools.getValue(Constants.HOSPITAL_NAME));
                                        fv.setUse_cnt("0");
                                        DataBaseHelper.getDbUtilsInstance(mcontext).save(fv);
                                        getFormCache();
                                        formAdapter.setdata(flist_isClose);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case SAVE_FAILE:
                            if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                                    ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
                                } catch (Exception e) {
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
    }

    private void bindViews() {
        topView = (com.deya.hospital.util.CommonTopView) findViewById(R.id.topView);
        formList = (ListView) findViewById(R.id.formList);
        layoutInflater = LayoutInflater.from(this);
        formAdapter = new FormListAdapter(mcontext, flist_isClose, 0,myHandler);
        formList.setAdapter(formAdapter);
    }

    private void initList() {

        formList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
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
                        it.putExtra("jastLook", "yes");
                        it.putExtra("is_save", flist_isClose.get(position).getIs_save());
                        it.putExtra("item_id", flist_isClose.get(position).getId());
                        it.putExtra("FormVo", flist_isClose.get(position));
                        startActivityForResult(it, 0x116);
                        break;
                    case 3:
                        Intent it2 = new Intent(FormListActivity2.this,
                                PreViewActivity.class);

                        Intent brodcastIntent = new Intent();
                        brodcastIntent.setAction(ChooseDepartActivity.CLOSE_DEPART_ACTIVITY);// 关闭前一个页面
                        FormListActivity2.this.sendBroadcast(brodcastIntent);
                        data.setFormType(flist_isClose.get(position).getType());
                        data.setTotalscore(flist_isClose.get(position).getScore());
                        data.setName(flist_isClose.get(position).getName());
                        data.setFormId(flist_isClose.get(position).getId());
                        it2.putExtra("time", getIntent().getStringExtra("time"));
                        it2.putExtra("itemList",
                                (Serializable) getFormItemCache(position));
                        it2.putExtra("data", data);
                        it2.putExtra("jastLook", "yes");
                        it2.putExtra("is_save", flist_isClose.get(position).getIs_save());
                        it2.putExtra("item_id", flist_isClose.get(position).getId());
                        it2.putExtra("FormVo", flist_isClose.get(position));
                        startActivityForResult(it2, 0x116);
                    default:
                        break;
                }

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFormCache();
        formAdapter.setdata(flist_isClose);
    }

    private void initCilck() {
        topView.onbackClick(this, this);
    }

    private void getFormList() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                FormListActivity2.this, SEND_SUCESS, SEND_FAILE, job,
                "grid/queryTemplateInfos");
    }


    private void getFormCache() {

        try {
            flist_isClose = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(FormVo.class).where("types", "!=", 5).and(WhereBuilder.b(
                                    "is_open",
                                    "=",
                                    1))
                                    .orderBy("dbid"));
        } catch (DbException e) {
            e.printStackTrace();
        }
        formAdapter.setdata(flist_isClose);

    }

    // private void getData() {
    // data = (TaskVo) getIntent().getSerializableExtra("data");
    // }

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


    protected void setsListResult(JSONObject jsonObject) throws DbException {
        JSONArray jarr = jsonObject.optJSONArray("grid_templates");
        if (null != jarr) {
            flist_isClose = gson.fromJson(jarr.toString(),
                    new TypeToken<List<FormVo>>() {
                    }.getType());
            formAdapter.setdata(flist_isClose);
            formAdapter.notifyDataSetChanged();
        }
    }

}