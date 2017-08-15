package com.deya.hospital.consumption;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.consumption.ConsumptionEditorAdapter.ConsumptionEditorInter;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.GsonUtils;
import com.deya.hospital.vo.ComunptionToalInfo;
import com.deya.hospital.vo.ProductInfo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.google.gson.JsonArray;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditorConsumptionActivity extends BaseActivity implements RequestInterface {
    private static final int SAVE_EDITOR = 0x820;
    ListView listView;
    String titleItem[] = {"手消毒液统计", "洗手液统计", "干手纸统计", "手套统计"};
    List<ComunptionToalInfo.ListBean.RecordsBean> consumpList;
    ConsumptionEditorAdapter adapter;
    LayoutInflater inflater;
    Button sumbmitBtn;
    int parentPosition;
    private RelativeLayout rlBack;
    boolean editorbal = true;
    ComunptionToalInfo.ListBean listBean;
    String product_type;
    String departmentId;
    String departmentName;
    String month;
    int index;
    String unit = "";
    List<ProductInfo.ListBean.ProductBean> productBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consumption);
        inflater = LayoutInflater.from(mcontext);
        intTopView();
        setList();
        initView();


    }

    TextView titleTv;

    private void intTopView() {
        titleTv = (TextView) this.findViewById(R.id.title);
        rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
        rlBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void setList() {
        listBean = (ComunptionToalInfo.ListBean) getIntent().getSerializableExtra("data");
        product_type = listBean.getProduct_type();
        parentPosition = Integer.parseInt(product_type) - 1;
        titleTv.setText(titleItem[parentPosition]);
        consumpList = listBean.getRecords();
        month = getIntent().getStringExtra("month");
        departmentId = getIntent().getStringExtra("departmentId");
        departmentName = getIntent().getStringExtra("departmentName");
        productBeanList = new ArrayList<>();
        getProductStandard();
        if (consumpList.size() == 0) {
            addData();
        }

    }

    public void deletItem(int position) {
        resetData(2, position);

    }

    Button addBtn;
    StandardDialog dialog;

    private void initView() {

        listView = (ListView) this.findViewById(R.id.list);
        adapter = new ConsumptionEditorAdapter(mcontext, consumpList, parentPosition, editorbal, new ConsumptionEditorInter() {

            @Override
            public void onDropStandard(final int posion1, final String unit1) {
                unit = unit1;
                index = posion1;
                dialog.show();
            }
        });
        View bottomView = inflater.inflate(R.layout.consumption_editor_bottomview, null);
        addBtn = (Button) bottomView.findViewById(R.id.addBtn);

        sumbmitBtn = (Button) bottomView.findViewById(R.id.sumbmitBtn);
        if (!editorbal) {
            sumbmitBtn.setVisibility(View.GONE);
        }


        sumbmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addData();


            }
        });
        if (editorbal) {
            listView.addFooterView(bottomView);
        }
        listView.setAdapter(adapter);

        dialog = new StandardDialog(mcontext, productBeanList, new StandardDialog.ChooseItem() {

            @Override
            public void onChoosePosition(int pos) {
                ProductInfo.ListBean.ProductBean productBean = productBeanList.get(pos);
                consumpList.get(index).setStandard(productBean.getStandard() + "");
                consumpList.get(index).setProduct_id(productBean.getProduct_id());
                consumpList.get(index).setStandardName(productBean.getStandard() + unit + "_" + productBean.getName() + "_" + productBean.getSub_type_name());
                adapter.notifyDataSetChanged();

            }
        });
    }


    public void addData() {
        ComunptionToalInfo.ListBean.RecordsBean vo = new ComunptionToalInfo.ListBean.RecordsBean();
        if (productBeanList.size() > 0) {
            ProductInfo.ListBean.ProductBean bean = productBeanList.get(0);
            if (null != bean) {
                vo.setStandard(bean.getStandard() + "");
                vo.setProduct_id(bean.getProduct_id());
                vo.setProduct_type(bean.getProduct_type());
            } else {
                vo.setStandard("");
            }
            consumpList.add(vo);
        }
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * @param type     1时为添加 2时为删除 3时为保存
     * @param position
     */
    public void resetData(int type, int position) {
        for (int i = 0; i < consumpList.size(); i++) {
            addData();
        }
        if (type == 1) {
            ComunptionToalInfo.ListBean.RecordsBean vo = new ComunptionToalInfo.ListBean.RecordsBean();
            if (parentPosition == 2) {
                vo.setStandard(200 + "");
            } else {
                vo.setStandard(500 + "");
            }
            consumpList.add(vo);
        } else if (type == 2) {
            consumpList.remove(position);
        }
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        String id = jsonObject.optString("task_id");
        TaskVo tv = Tasker.findDbTaskByKeyValue("task_id", id);
        if (tv != null) {

        } else {
            tv = new TaskVo();
            tv.setDepartment(departmentId);
            tv.setMission_time(TaskUtils.getLoacalDate());
            tv.setStatus(0);
            tv.setType("15");
            tv.setDepartmentName(departmentName);
            tv.setMobile(tools.getValue(Constants.MOBILE));
            TaskUtils.onAddTaskInDb(tv);
            Intent intent = new Intent(WorkSpaceFragment.UPDATA_ACTION);
            sendBroadcast(intent);
        }
        finish();

    }

    @Override
    public void onRequestErro(String message) {
        ToastUtil.showMessage(message);

    }

    @Override
    public void onRequestFail(int code) {
        ToastUtil.showMessage("服务器错误");

    }


    public void saveData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("department_id", departmentId);
            job.put("report_period", month);
            job.put("records", getrecords());
            job.put("product_type", product_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,
                this, SAVE_EDITOR, job,
                "consume/submitRecord");
    }

    public JsonArray getrecords() {

        return GsonUtils.ListToJsonArray(consumpList);
    }

    void getProductStandard() {
        List<ProductInfo.ListBean.ProductBean> cache = DataBaseHelper.findListByKeyValue(mcontext, ProductInfo.ListBean.ProductBean.class, "product_type", product_type);
            if (null != cache) {
            productBeanList.addAll(cache);
        }
    }
}
