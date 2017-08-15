package com.deya.hospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.deya.acaide.R;
import com.deya.hospital.util.AbViewUtil;

import java.util.List;

/**
 * Created by yxm on 2017.02.26.
 */

public class RcyCompletionAdapter extends RecyclerView.Adapter {
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private Context context;
    private int width;
    private int defItem = -1;

    //设置事件回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public RcyCompletionAdapter(Context context, List<String> mDatas, int width) {
        this.mDatas = mDatas;
        this.context = context;
        this.width = width;
        this.mInflater = LayoutInflater.from(context);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private RadioButton rbState;

        public MyViewHolder(View itemView, int width,Context context) {
            super(itemView);
            rbState = (RadioButton) itemView.findViewById(R.id.rb_state);
            int pWidth = (int) (width / 5)-AbViewUtil.dip2px(context,11);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(pWidth, pWidth);
            params.setMargins(AbViewUtil.dip2px(context,8),AbViewUtil.dip2px(context,8),AbViewUtil.dip2px(context,8),AbViewUtil.dip2px(context,8));
            rbState.setLayoutParams(params);
        }
    }

    public void setDefSelect(int position) {
        this.defItem = position;
//        notifyDataSetChanged();  
        }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(mInflater.inflate(R.layout.rcy_completion_item, parent, false), width,context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.rbState.setText(mDatas.get(position));
        //單選按鈕選中
        if(defItem != -1){
            if (defItem == position){
                myViewHolder.rbState.setChecked(true);
            }else {
                myViewHolder.rbState.setChecked(false);
            }
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            myViewHolder.rbState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = myViewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(myViewHolder.rbState, pos);
                }
            });
        }
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
