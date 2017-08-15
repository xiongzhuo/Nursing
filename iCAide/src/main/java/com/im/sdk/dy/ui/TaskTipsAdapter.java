package com.im.sdk.dy.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.TipsMessage;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.core.DyMessage;
import com.yuntongxun.ecsdk.ECMessage;

import java.util.List;

public abstract class TaskTipsAdapter<T> extends BaseAdapter {
    private LayoutInflater inflater;
    public T t;
    public  List<T> list;
    Context context;

    public static int sava_position = -1;
    public  int drawables[] = {R.drawable.task_type_round_blue,
            R.drawable.task_type_round_blue, R.drawable.task_type_round_green,
            R.drawable.task_type_round_blue, R.drawable.task_type_round_green,
            R.drawable.task_type_round_green, R.drawable.task_type_round_blue};
    public  String Strings[] = {"","改进列表提醒","注册提醒"};

    public TaskTipsAdapter(Context context,T t) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.t=t;
    }


    public int getSavePosition() {
        return sava_position;

    }

    public void setdata(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tips_list_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.typeTv = (TextView) convertView.findViewById(R.id.typeTv);
            viewHolder.finishtime = (TextView) convertView.findViewById(R.id.finishtime);


            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
//        ECMessage ecMessage = list.get(position);
//        DyMessage dyMessage = new DyMessage(ecMessage.getUserData());
//        TipsMessage tm = TaskUtils.gson.fromJson(dyMessage.getValue(), TipsMessage.class);
//
//        ECTextMessageBody textBody = (ECTextMessageBody) ecMessage.getBody();
//
//        viewHolder.title.setText(textBody.getMessage());
//        viewHolder.finishtime.setText(DateUtil.getDateString(ecMessage.getMsgTime(), DateUtil.SHOW_TYPE_CALL_LOG).trim());
//
//        viewHolder.typeTv.setBackgroundResource(drawables[tm.getP()]);
//        viewHolder.typeTv.setText(Strings[0]);
        setItem(position,viewHolder.title,viewHolder.typeTv,viewHolder.finishtime);
        return convertView;
    }

    public abstract void  setItem(int position,TextView title,TextView typeTv,TextView timeTv);
    public TipsMessage getTipsMessage(int position) {
        ECMessage ecMessage = (ECMessage) list.get(position);
        DyMessage dyMessage = new DyMessage(ecMessage.getUserData());
        TipsMessage tm = TaskUtils.gson.fromJson(dyMessage.getValue(), TipsMessage.class);

        return tm;
    }

    public int getTaskId(int position) {
        return getTipsMessage(position).getId();
    }
    public int getTaskP(int position) {
        return getTipsMessage(position).getP();
    }
    class ViewHolder {
        public TextView title;
        // public TextView rtRate;// 正确率
        public TextView timeTypes;
        TextView finishtime;
        TextView typeTv;
    }
}
