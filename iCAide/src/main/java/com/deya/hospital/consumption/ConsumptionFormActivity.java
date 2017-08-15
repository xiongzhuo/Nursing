package com.deya.hospital.consumption;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.account.UserInfoEditorActivity;
import com.deya.hospital.base.BaseDepartChooseActivity;
import com.deya.hospital.base.DepartChooseActivity;
import com.deya.hospital.base.EditorTextDialog;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.view.YearMonthDialog;
import com.deya.hospital.vo.ComunptionToalInfo;
import com.deya.hospital.vo.ConsumptionFormItemVo;
import com.deya.hospital.vo.ConsumptionFormVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.ProductInfo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.widget.popu.PopuUnTimeReport.OnPopuClick;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.Gson;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConsumptionFormActivity extends BaseDepartChooseActivity implements
        OnClickListener, RequestInterface, DatePickerDialog.OnDateSetListener {
    private static final int GET_PRODUCT_INFO = 0x811;
    private static final int SEN_EMAIL = 0x812;
    private static final int SAVE_EDITOR =0x813 ;

    TextView departTv;
    ListView listView;
    private String ItemStr[] = {"手消毒液统计", "洗手液统计", "干手纸统计", "手套统计"};
    private String unitStr[] = {"ml", "ml", "抽", "双"};
    private int imgSource[] = {R.drawable.wash_bloding,
            R.drawable.hand_washing, R.drawable.tissue, R.drawable.hand_washing1,
    };
    LayoutInflater layoutInflate;
    int chooseIndex = -1;
    ConsumptionFormAdapter adapter;
    Gson gson = new Gson();
    private RelativeLayout rlBack;
    boolean isUpdate = false;
    String firstStr = "";// 初始数据串
    Button shareBtn, emailBtn;
    LinearLayout bottomView;
    Button btn;
    private LinearLayout sumbmitlay;
    String departId;
    TextView monthTv;
    String month;
    List<ComunptionToalInfo.ListBean> list;
    YearMonthDialog yearMonthDialog;
    TaskVo data;
    String task_id = "";
    TextView monthBedNum;
    EditorTextDialog edtorTextDialog;
    CommonTopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
        layoutInflate = LayoutInflater.from(mcontext);
        list = new ArrayList<>();
        intTopView();
        initView();
    }

    @Override
    protected void onChooseDepartList(String name, String id) {

        departTv.setText(name);
        departId = id;
        getConsumptionInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConsumptionInfo();
    }

    private void intTopView() {
        TextView titleTv = (TextView) this.findViewById(R.id.title);
        titleTv.setText("手卫生消毒消耗量");
        rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);

    }
    public void saveBedNum() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("department_id", departId);
            job.put("report_period", month);
            job.put("bed_no",monthBedNum.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,
                this, SAVE_EDITOR, job,
                "consume/submitRecord");
    }

    private void getConsumptionInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("department_id", departId);
            job.put("report_period", month);
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,
                ConsumptionFormActivity.this, GET_COUMINFO, job,
                "consume/findRecord");
    }

    private void getConsumptionProduct() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,
                ConsumptionFormActivity.this, GET_PRODUCT_INFO, job,
                "consume/getProduct");
    }

    private void initView() {
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        Time curTime = new Time();
        curTime.setToNow();
        int year = curTime.year;
        int month2 = curTime.month + 1;
        month = year + "-" + month2;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        monthTv = (TextView) this.findViewById(R.id.monthTv);
        Date date2= null;
        try {
            date2 = sdf.parse(month);
            month=sdf.format(date2).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        departTv = (TextView) this.findViewById(R.id.mdepartTv);
        departTv.setOnClickListener(this);

        monthTv.setOnClickListener(this);
        monthTv.setText(month);
        listView = (ListView) this.findViewById(R.id.list);
        adapter = new ConsumptionFormAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (AbStrUtil.isEmpty(departId)) {
                    ToastUtil.showMessage("请先选择科室");
                    return;
                }
                Intent it = new Intent(mcontext,
                        EditorConsumptionActivity.class);
                if (position >= list.size()) {
                    ToastUtil.showMessage("请去后台添加该产品");
                    return;
                }
                it.putExtra("data", list.get(position));
                it.putExtra("month", month);
                it.putExtra("departmentId", departId);
                it.putExtra("departmentName", departTv.getText().toString());
                startActivity(it);
            }
        });
        btn = (Button) this.findViewById(R.id.sumbmitBtn);
        bottomView = (LinearLayout) this.findViewById(R.id.bottomView);
        sumbmitlay = (LinearLayout) this.findViewById(R.id.sumbmitlay);

        monthBedNum=this.findView(R.id.monthBedNum);
        monthBedNum.setOnClickListener(this);
        // btn.setEnabled(false);

        shareBtn = (Button) this.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
        emailBtn = (Button) this.findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(this);
        final Calendar cal = Calendar.getInstance();
        yearMonthDialog = new YearMonthDialog(mcontext, new YearMonthDialog.OnDatePopuClick() {
            @Override
            public void enter(String text) {

                month = text;
                monthTv.setText(month);
                getConsumptionInfo();
            }
        });
        edtorTextDialog=new EditorTextDialog(mcontext,"本月住院床日数","请输入本月住院床日数");
        edtorTextDialog.setOnTextSumbmitLis(new EditorTextDialog.OnTextSumbmit() {
            @Override
            public void onTextSumbmit(String txt) {
                monthBedNum.setText(txt);
                saveBedNum();
            }
        });
        setdata();
        getConsumptionProduct();

    }

    private void setdata() {
        if (TaskUtils.isPartTimeJob(mcontext)) {//
            departTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
            bottomView.setVisibility(View.GONE);
            departId = tools.getValue(Constants.DEFULT_DEPARTID);
        } else if (getIntent().hasExtra("data")) {
            data = (TaskVo) getIntent().getSerializableExtra("data");
            departTv.setText(data.getDepartmentName());
            departId = data.getDepartment();
        }
    }

    private List<ConsumptionFormVo> getFormList() {
        List<ConsumptionFormVo> list = new ArrayList<ConsumptionFormVo>();
        for (int i = 0; i < ItemStr.length; i++) {
            ConsumptionFormVo cfv = new ConsumptionFormVo();
            cfv.setTitle(ItemStr[i]);
            cfv.setUnit(unitStr[i]);
            cfv.setType((i + 1) + "");
            List<ConsumptionFormItemVo> cfvList = new ArrayList<ConsumptionFormItemVo>();
            cfv.setItems(cfvList);
            list.add(cfv);
        }
        return list;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent redata) {
        super.onActivityResult(requestCode, resultCode, redata);
        if(resultCode == DepartChooseActivity.CHOOSE_SUC){
            DepartVos.DepartmentListBean bean= (DepartVos.DepartmentListBean) redata.getSerializableExtra("departData");
            departTv.setText(bean.getName());
            departId=bean.getId()+"";
            departTv.setText(bean.getName());
            getConsumptionInfo();
        }
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        Log.i("111111111", jsonObject.toString());
        switch (code) {
            case GET_COUMINFO:
                list.clear();
                ComunptionToalInfo comunptionToalInfo = TaskUtils.gson.fromJson(jsonObject.toString(), ComunptionToalInfo.class);
                list.addAll(comunptionToalInfo.getList());
                task_id = comunptionToalInfo.getTask_id();
                monthBedNum.setText(comunptionToalInfo.getBed_no()+"");
                if (AbStrUtil.isEmpty(task_id) && !task_id.equals("0")) {
                    bottomView.setVisibility(View.GONE);
                } else {
                    bottomView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                break;
            case GET_PRODUCT_INFO:
                ProductInfo productInfo = TaskUtils.gson.fromJson(jsonObject.toString(), ProductInfo.class);
                try {
                    DataBaseHelper.getDbUtilsInstance(mcontext)
                            .deleteAll(ProductInfo.ListBean.ProductBean.class);
                    for (ProductInfo.ListBean listBean : productInfo.getList()) {
                        DataBaseHelper.getDbUtilsInstance(mcontext)
                                .saveAll(listBean.getProduct());
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }


                break;
            case SEN_EMAIL:
                tools.putValue(Constants.SEND_EMAILS, emailTx);
                ToastUtil.showMessage(jsonObject.optString("result_msg"));
                break;
            case SAVE_EDITOR:
                getConsumptionInfo();
                break;
        }

    }

    @Override
    public void onRequestErro(String message) {


    }

    @Override
    public void onRequestFail(int code) {
        switch (code) {
            case SEN_EMAIL:
                ToastUtil.showMessage("邮件发送失败");
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("11111111", year + "-" + month);
        month = year + "-" + (monthOfYear + 1);

        getConsumptionInfo();

    }

    public class ConsumptionFormAdapter extends BaseAdapter {
        public int colors[] = {R.color.line1_corlor, R.color.line4_corlor,
                R.color.line5_corlor, R.color.line2_corlor,
                R.color.line6_corlor};

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 4;
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
            ViewHolder viewHolder;

            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = layoutInflate.inflate(
                        R.layout.consumptionform_list_item, null);
                viewHolder.pickupTv = (TextView) convertView
                        .findViewById(R.id.pickupNum);
                viewHolder.storeTv = (TextView) convertView
                        .findViewById(R.id.storeNum);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.title);
                viewHolder.imgIcon = (ImageView) convertView
                        .findViewById(R.id.imgIcon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            int type = 0;

            viewHolder.title.setText(ItemStr[position]);
            viewHolder.title.setTextColor(getResources().getColor(
                    colors[position]));
            if (position < list.size()) {
                ComunptionToalInfo.ListBean listBean = list.get(position);
                String str=AbStrUtil.isEmpty(list.get(position).getDay_consume_num()+"")?"0":list.get(position).getDay_consume_num();
                viewHolder.pickupTv.setText("本月消耗量:"
                        + list.get(position).getMonth_consume_num()
                        + unitStr[position]);
                viewHolder.storeTv.setText("日均消耗量:"
                        +str
                        + unitStr[position]+"/床日数");
            } else {
                viewHolder.pickupTv.setText("本月消耗量:"
                        + 0
                        + unitStr[position]);
                viewHolder.storeTv.setText("日均消耗量:"
                        + 0
                        + unitStr[position]+"/床日数");
            }
            viewHolder.imgIcon.setImageResource(imgSource[position]);
            return convertView;
        }

    }

    public class ViewHolder {
        TextView title;
        TextView pickupTv;
        TextView storeTv;
        ImageView imgIcon;

    }

    public void judeIsStrChanged(String str) {
        // if(!firstStr.equals(str)){
        // btn.setEnabled(true);
        // }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.shareBtn:
                showShare();
                break;
            case R.id.emailBtn:
                sendEmails();
                break;
            case R.id.mdepartTv:

                if ( TaskUtils.isPartTimeJob(mcontext)) {//
                    if(departId.equals("")){
                        Intent intent=new Intent(mcontext, UserInfoEditorActivity.class);
                        startActivity(intent);
                        ToastUtil.showMessage("请添加默认科室");
                    }
                    return;
                }
                Intent intent=new Intent(mcontext, DepartChooseActivity.class);
                startActivityForResult(intent,DepartChooseActivity.CHOOSE_SUC);
            //    departDialog.show();

                break;
            case R.id.monthTv:
                yearMonthDialog.show();
                break;
            case R.id.monthBedNum:
                edtorTextDialog.show();
                edtorTextDialog.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            default:
                break;
        }
    }

    private void showShare() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", task_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showShareDialog("手卫生消耗量统计",
                departTv.getText() + "(" + month + ")",
                WebUrl.LEFT_URL + "/consume/share?u="
                        + AbStrUtil.getBase64(jsonObject.toString()));
    }

    PopuUnTimeReport dialog;
    String emailTx = "";

    public void sendEmails() {
        emailTx = tools.getValue(Constants.SEND_EMAILS);
        emailTx = (!AbStrUtil.isEmpty(emailTx)) ? emailTx : tools
                .getValue(Constants.EMAIL);
        dialog = new PopuUnTimeReport(mcontext, _activity, false, listView,
                emailTx, new OnPopuClick() {
            @Override
            public void enter(String text) {
                emailTx = text;
                sendEail(emailTx);
            }

            @Override
            public void cancel() {

            }
        });

    }

    protected static final int SEND_SUCESS = 0x6001;

    protected static final int GET_COUMINFO = 0x6002;

    private void sendEail(String text) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("email", text);
            // if(job.equals("1")){
            // job.put("inter_type", types[selectPosintion]);
            // }else{
            // job.put("inter_type", types2[selectPosintion]);
            job.put("inter_type", "21");
            job.put("task_id", task_id);
            // }
            // Log.i("1111", job.toString());

            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,
                ConsumptionFormActivity.this, SEN_EMAIL, job,
                "mail/emailSend");
    }


    protected void setSendResult(JSONObject jsonObject) {
        // if(jsonObject.optString("resul_id").equals("0")){
        //
        // }
        Log.i("1111111111", jsonObject.toString());
        tools.putValue(Constants.SEND_EMAILS, emailTx);
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
    }

}
