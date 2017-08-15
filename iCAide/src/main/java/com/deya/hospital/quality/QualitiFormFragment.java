package com.deya.hospital.quality;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.GsonUtils;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.ZformVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/6
 */
public class QualitiFormFragment extends BaseTableFragment implements RequestInterface {
    private static final int ADD_HOS_FORM = 0x002;
    FormAdapter formAdapter;
    private static final int GET_DEPART_FORM = 0x001;
    List<RisistantVo.TaskInfoBean.TempListBean> formList;
    ListView listView;
    int type;//区分是本院还是平台模板  0本院 1平台


    public static QualitiFormFragment newInstance(int type, String departmentId) {
        QualitiFormFragment newFragment = new QualitiFormFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("departmentId", departmentId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDepartFormList();
    }

    @Override
    public void initView() {
        listView = findView(R.id.listView);
        formList = new ArrayList<>();
        formAdapter = new FormAdapter(getActivity(), formList);
        listView.setAdapter(formAdapter);
        Bundle args = getArguments();
        type = args.getInt("type", 0);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Intent intent=new Intent(getActivity(),QualityPriviewActivity.class);
//                intent.putExtra("tempId",formList.get(position).getTemp_type_id());
//                intent.putExtra("data", formList.get(position));
//                startActivity(intent);
                LinearLayout ll_opne = (LinearLayout) view.findViewById(R.id.ll_opne);
                ll_opne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFormToHospital(formList.get(position).getId() + "");
                    }
                });
                TaskVo taskVo = new TaskVo();
                RisistantVo.TaskInfoBean taskInfoBean = new RisistantVo.TaskInfoBean();
                RisistantVo.TaskInfoBean.TempListBean bean = formList.get(position);
                bean.setTempId(bean.getId());
                taskInfoBean.setTemp_list(bean);
                if(bean.getItem_list().size()<=0){
                    ToastUtil.showMessage("该模板没有数据!");
                    return;
                }
                taskInfoBean.setTempId(bean.getTempId());
                taskInfoBean.setIs_save(bean.getIs_save());
                taskVo.setTaskListBean(TaskUtils.gson.toJson(taskInfoBean));
                Intent it = new Intent(getActivity(), QualityPriviewActivity.class);
                it.putExtra("data", taskVo);
                it.putExtra("title", bean.getTitle());
                it.putExtra("isPriview",type==1?true:false);
                it.putExtra("task_type",getActivity().getIntent().getIntExtra("task_type",17));
                startActivity(it);
            }
        });
        getDepartFormList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dy_listview_lay;
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        dismissdialog();
        switch (code) {
            case GET_DEPART_FORM:
                formList.clear();
                ZformVo vo = TaskUtils.gson.fromJson(jsonObject.toString(), ZformVo.class);
                formList.addAll(vo.getList());
                formAdapter.notifyDataSetChanged();
                break;
            case ADD_HOS_FORM:

                getDepartFormList();
                break;
        }

    }

    @Override
    public void onRequestErro(String message) {
        dismissdialog();
        ToastUtil.showMessage(message);

    }

    @Override
    public void onRequestFail(int code) {
        dismissdialog();
        ToastUtil.showMessage("服务器数据异常!");

    }

    public void getDepartFormList() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("isPublic", type);
            job.put("task_type", getActivity().getIntent().getIntExtra("task_type",17));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, getActivity(),
                GET_DEPART_FORM, job, "quality/tempList");
    }

    /**
     * 添加模板
     */

    public void addFormToHospital(String id) {
        showprocessdialog();
        try {
            JSONObject job = GsonUtils.creatJsonObj("").put("authent", tools
                    .getValue(Constants.AUTHENT)).put("tempId", id).put("task_type", getActivity().getIntent().getIntExtra("task_type",17));;
            MainBizImpl.getInstance().onComomReq(this, getActivity(),
                    ADD_HOS_FORM, job, "quality/saveToHospital");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class FormAdapter extends DYSimpleAdapter<RisistantVo.TaskInfoBean.TempListBean> {

        public FormAdapter(Context context, List<RisistantVo.TaskInfoBean.TempListBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(final int position, View convertView) {
            TextView titleTv = BaseViewHolder.get(convertView, R.id.tv_title);
            TextView readNumTv = BaseViewHolder.get(convertView, R.id.tv_readnum);
            TextView publishTimeTv = BaseViewHolder.get(convertView, R.id.tv_time);
            ImageView img = BaseViewHolder.get(convertView, R.id.img);
            TextView author = BaseViewHolder.get(convertView, R.id.author);
            Button btn_save = BaseViewHolder.get(convertView, R.id.btn_save);
            LinearLayout ll_opne = BaseViewHolder.get(convertView, R.id.ll_opne);
            RisistantVo.TaskInfoBean.TempListBean vo = getItem(position);
            titleTv.setText(vo.getTitle());
            readNumTv.setText(vo.getUse_cnt() + "");
            publishTimeTv.setText("创建时间:"+vo.getCreate_time() + "");
            author.setText(vo.getUname() + "(" + vo.getHospital() + ")");
            if (type == 1) {
                ll_opne.setVisibility(View.VISIBLE);
            } else {
                ll_opne.setVisibility(View.GONE);
            }
            if (list.get(position).getIs_save() == 0) {
                //未保存

               btn_save.setPressed(false);
                btn_save.setEnabled(true);
                btn_save.setText("保存到本医院");
            } else {
                btn_save.setPressed(true);
              btn_save.setEnabled(false);
                btn_save.setText("已保存");
            }
            img.setVisibility(View.GONE);
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//				if (tools.getValue(Constants.JOB).equals("1") || tools.getValue(Constants.JOB).equals("2")) {
                    addFormToHospital(list.get(position).getId()+"");
//				} else {
//					ToastUtils.showToast(context,"您不能做此操作哟！请联系院感科专职人员操作吧！");
//				}
                }
            });
        }

        @Override
        public int getLayoutId() {
            return R.layout.form_list_item;
        }
    }
}
