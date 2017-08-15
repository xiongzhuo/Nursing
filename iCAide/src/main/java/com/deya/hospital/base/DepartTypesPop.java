package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.DepartVos;

import java.util.ArrayList;
import java.util.List;

public class DepartTypesPop extends BaseDialog {
    List<DepartVos.DepartmentTypeListBean> typeList;
    ListView leftList, rightList;
    DepartTypeAdapter departTypeAadapter;
    Context context;
    List<DepartVos.DepartmentTypeListBean.ChildrenBean> childList;
    ChildTypeAdapter childTypeAdapter;
    TypeChooseInter inter;

    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public DepartTypesPop(Context context, List<DepartVos.DepartmentTypeListBean> typeList) {
        super(context);
        this.typeList = typeList;
        this.context = context;
    }

    public void setChooseListener(TypeChooseInter inter) {
        this.inter = inter;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_depart_types);
        initView();
    }

    private void initView() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        int wh[]= AbViewUtil.getDeviceWH(context);
        lp.height = wh[1] / 100 * 80;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        childList = new ArrayList<>();
        leftList = (ListView) findViewById(R.id.leftList);
        rightList = (ListView) findViewById(R.id.rightList);
        departTypeAadapter = new DepartTypeAdapter(context, typeList);
        leftList.setAdapter(departTypeAadapter);
        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartVos.DepartmentTypeListBean bean = typeList.get(position);
                if (typeList.get(position).getChildren().size() > 0) {
                    childList.clear();
                    childList.addAll(typeList.get(position).getChildren());
                    childTypeAdapter.notifyDataSetChanged();
                } else {
                    inter.onChoose(bean.getName(), bean.getId());
                    dismiss();
                }

            }
        });
        childTypeAdapter = new ChildTypeAdapter(context, childList);
        rightList.setAdapter(childTypeAdapter);
        rightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartVos.DepartmentTypeListBean.ChildrenBean bean = childList.get(position);
                inter.onChoose(bean.getName(), bean.getId());
                dismiss();
            }
        });
    }

    private class DepartTypeAdapter extends DYSimpleAdapter<DepartVos.DepartmentTypeListBean> {

        public DepartTypeAdapter(Context context, List<DepartVos.DepartmentTypeListBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            TextView listtext = BaseViewHolder.get(convertView, R.id.listtext);
            DepartVos.DepartmentTypeListBean bean = getItem(position);
            listtext.setText(bean.getName());

        }

        @Override
        public int getLayoutId() {
            return R.layout.fivelist_item;
        }
    }

    private class ChildTypeAdapter extends DYSimpleAdapter<DepartVos.DepartmentTypeListBean.ChildrenBean> {

        public ChildTypeAdapter(Context context, List<DepartVos.DepartmentTypeListBean.ChildrenBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            TextView listtext = BaseViewHolder.get(convertView, R.id.listtext);
            DepartVos.DepartmentTypeListBean.ChildrenBean bean = getItem(position);
            listtext.setText(bean.getName());

        }

        @Override
        public int getLayoutId() {
            return R.layout.fivelist_item;
        }
    }

    public interface TypeChooseInter {
        public void onChoose(String name, int id);
    }
}
