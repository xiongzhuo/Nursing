package com.deya.hospital.consumption;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.vo.ProductInfo;

import java.util.List;

public class StandardDialog extends BaseDialog {
    ListView listView;
    TextView titleTv;
    Context context;
    String unit = "ml";
    ChooseItem chooseInter;
    List<ProductInfo.ListBean.ProductBean> list;
    String product_type;
    GvAdapter adapter;

    public StandardDialog(Context context, List<ProductInfo.ListBean.ProductBean> list, ChooseItem chooseInter) {
        super(context);
        this.context = context;
        this.chooseInter = chooseInter;
        this.list = list;
        if(list.size()>0){
            switch (list.get(0).getProduct_type()){
                case "3":
                    unit= "抽/盒";
                    break;
                case "4":
                    unit= "双/盒";
                    break;
                default:
                    unit = "ml/瓶";
                    break;


            }
        }
    }

    public void setTitleTv(String title) {
        titleTv.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_standard);
        listView = (ListView) this.findViewById(R.id.dialog_list);
        adapter = new GvAdapter(context,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                chooseInter.onChoosePosition(position);
                dismiss();
            }
        });
    }

    public interface ChooseItem {
        public void onChoosePosition(int position);

    }

    int chooseIndex = -1;

    public class GvAdapter extends DYSimpleAdapter<ProductInfo.ListBean.ProductBean> {


        public GvAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            ProductInfo.ListBean.ProductBean productBean = list.get(position);
            TextView titleTv = BaseViewHolder.get(convertView, R.id.listtext);
            titleTv.setText(productBean.getStandard() + unit + "_" + productBean.getName() + "_" + productBean.getSub_type_name());
        }

        @Override
        public int getLayoutId() {
            return  R.layout.list_item;
        }

    }

    public int dp2Px(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }
}
