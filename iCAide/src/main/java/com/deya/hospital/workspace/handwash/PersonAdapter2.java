package com.deya.hospital.workspace.handwash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

public class PersonAdapter2 extends BaseAdapter {
    // List<PicListVo> list;
    Context mcontext;
    private LayoutInflater inflater;
    LayoutParams para;
    List<planListDb> list;
    HandWashTasksActivity activity;
    DisplayImageOptions optionsSquare;
    float x1 = 0;
    float x2 = 0;
    int chooseIndex = 0;
    String text = "";
    int item_width = 0;

    /**
     * Creates a new instance of MyImageListAdapter.
     *
     * @param jobList
     */
    public PersonAdapter2(Context context, List<planListDb> list, List<JobListVo> jobList) {
        inflater = LayoutInflater.from(context);
        mcontext = context;

        activity = (HandWashTasksActivity) context;
        int[] wh = AbViewUtil.getDeviceWH(mcontext);
        item_width = (wh[0] - dp2Px(mcontext, 80)) / 4;
        para = new LayoutParams(item_width, item_width);

        this.list = list;
    }


    public int item_x() {
        return item_width / 2;
    }

    public PersonAdapter2(Context context) {

    }


    public void setList(List<planListDb> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //	return list == null ? 0 : list.size();
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO 自动生成的方法存根
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO 自动生成的方法存根
        return 0;
    }

    public void setJobText(int position, String text) {
        chooseIndex = position;
        this.text = text;
        notifyDataSetChanged();
    }

    public void setJobText(int position) {
        chooseIndex = position;
        notifyDataSetChanged();
    }

    public void setCheck(int position) {
        this.chooseIndex = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder2 mviewHolder;
        if (convertView == null) {
            mviewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.adapter_handtask_person, null);
            mviewHolder.button = (TextView) convertView
                    .findViewById(R.id.button);
            mviewHolder.layout = (LinearLayout) convertView
                    .findViewById(R.id.layout);
            mviewHolder.lay_item= (LinearLayout) convertView.findViewById(R.id.lay_item);
            mviewHolder.tv_job = (TextView) convertView.findViewById(R.id.tv_job);
            convertView.setTag(mviewHolder);
        } else {
            mviewHolder = (ViewHolder2) convertView.getTag();
        }
        if (AbStrUtil.isEmpty(list.get(position).getPpostName())) {
            mviewHolder.tv_job.setText("岗位");
        } else {
            mviewHolder.tv_job.setText(list.get(position).getPpostName());
        }


        mviewHolder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //activity.showPopWindow(mcontext, mviewHolder.joblayout);
                activity.showJobDialog(position, list.get(position).getPname(), list.get(position).getWork_type(), list.get(position).getPpost());


            }
        });
        if (list.get(position).isSelect_status()) {
            mviewHolder.layout.setBackgroundResource(R.drawable.roundbg_btn);
            mviewHolder.tv_job.setTextColor(mcontext.getResources().getColor(R.color.top_color));

        } else {
            mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);

            mviewHolder.tv_job.setTextColor(mcontext.getResources().getColor(R.color.edt_broad));

        }
        mviewHolder.layout.setLayoutParams(para);
        mviewHolder.lay_item.setTag(R.id.person_item_position, position);


        mviewHolder.button.setText(list.get(position).getPname());

        int num = list.get(position).getSubTasks().size();


//		if (position == selection) {
//			mviewHolder.layout.setBackgroundResource(R.drawable.roundbg_btn);
//		} else {
//			mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);
//		}
        return convertView;
    }

    int selection = -1;

    public void serBgColor(int selection) {
        this.selection = selection;
        notifyDataSetChanged();
    }

    class ViewHolder2 {

        TextView button;
        TextView num;
        LinearLayout layout, lay_item;
        TextView tv_job;
    }

    public int dp2Px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }

    public PersonAdapter2() {
        // TODO Auto-generated constructor stub
    }

}
