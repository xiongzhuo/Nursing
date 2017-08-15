package com.deya.hospital.consumption;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.ComunptionToalInfo;
import com.deya.hospital.vo.ProductInfo;

import java.util.List;

public class ConsumptionEditorAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<ComunptionToalInfo.ListBean.RecordsBean> list;
    int parentPosion;
    ConsumptionEditorInter inter;
    String stadardunit = "";
    String titleItem[] = {"手消毒液", "普通洗手液", "抗菌洗手液", "干手纸"};
    boolean showEditorView = true;

    public ConsumptionEditorAdapter(Context context,
                                    List<ComunptionToalInfo.ListBean.RecordsBean> list, int parentPosion, boolean showEditorView, ConsumptionEditorInter inter) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.parentPosion = parentPosion;
        this.inter = inter;
        this.showEditorView = showEditorView;
        if (parentPosion == 2) {
            stadardunit = "抽/盒";
        }else if(parentPosion==3){
            stadardunit = "双/盒";
        }else {
            stadardunit = "ml/瓶";
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setList(List<ComunptionToalInfo.ListBean.RecordsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//		final ViewHolder viewHolder;
//		if (null == convertView) {
//			viewHolder = new ViewHolder();
        convertView = inflater.inflate(
                R.layout.list_item_editorconsumption, null);
        TextView tv_goods = (TextView) convertView
                .findViewById(R.id.tv_goods);
        TextView unitsTv = (TextView) convertView
                .findViewById(R.id.unitsTv);
        TextView monthPicupTv = (TextView) convertView
                .findViewById(R.id.monthPicupTv);
        TextView monthStoreTv = (TextView) convertView
                .findViewById(R.id.monthStoreTv);
        EditText pickupNum = (EditText) convertView
                .findViewById(R.id.pickupNum);
        EditText monthStoreNum = (EditText) convertView
                .findViewById(R.id.monthStoreNum);
        TextView standardEdt = (TextView) convertView
                .findViewById(R.id.standardEdt);
        Button deletBtn = (Button) convertView
                .findViewById(R.id.deletBtn);
        ImageView selectTv = (ImageView) convertView.findViewById(R.id.selectTv);
        //convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}

        if (parentPosion == 3) {
            monthPicupTv.setText("本月领取(单位：盒)");
            monthStoreTv.setText("本月库存(单位：盒)");
            monthStoreNum.setHint("点击填写  例:50");
        } else if (parentPosion == 2) {
            monthPicupTv.setText("本月领取(单位：盒)");
            monthStoreTv.setText("本月库存(单位：盒)");
            monthStoreNum.setHint("点击填写  例:50");
        } else {
            monthPicupTv.setText("本月领取(单位：瓶)");
            monthStoreTv.setText("本月库存(单位：瓶)");
            monthStoreNum.setHint("点击填写  例:50");

        }
        if (!showEditorView) {
            pickupNum.setEnabled(false);
            monthStoreNum.setEnabled(false);
            standardEdt.setEnabled(false);
            deletBtn.setVisibility(View.GONE);
            selectTv.setVisibility(View.GONE);
        } else {
            pickupNum.setEnabled(true);
            monthStoreNum.setEnabled(true);
            standardEdt.setEnabled(true);
            deletBtn.setVisibility(View.VISIBLE);
            selectTv.setVisibility(View.VISIBLE);

        }

        tv_goods.setText("规格");
        pickupNum.setText(list.get(position).getPull_num() + "");
        monthStoreNum.setText(list.get(position).getStocks_num() + "");
        ProductInfo.ListBean.ProductBean productBean = finProduct(list.get(position).getProduct_id());
        if(null!=productBean){
        standardEdt
                .setText(productBean.getStandard() + stadardunit + "_" + productBean.getName() + "_" + productBean.getBrand());
        }else{
            standardEdt
                    .setText("");
        }
        standardEdt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                inter.onDropStandard(position, stadardunit);
            }
        });
        pickupNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (position < list.size()) {
                    list.get(position).setPull_num(
                            s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        monthStoreNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (position < list.size()) {
                    list.get(position).setStocks_num(
                            s.toString());
                    Log.i("11111111num", position + "----------" + s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public class ViewHolder {
    /*	TextView tv_goods;// 物品名称
        ImageView selectTv;
		EditText standardEdt;// 物品的规格大小
		TextView unitsTv;// 规格的单位
		Button deletBtn;// 删除按钮
		TextView monthPicupTv;//本月领取量+单位
		EditText pickupNum;
		TextView monthStoreTv;// 本月领取库存量
		EditText monthStoreNum;*/
    }

    public interface ConsumptionEditorInter {
        void onDropStandard(int posion, String unit);

    }

    private ProductInfo.ListBean.ProductBean finProduct(int id) {

        return DataBaseHelper.findFirstByKeyValue(context, ProductInfo.ListBean.ProductBean.class, "product_id", id + "");

    }
}
