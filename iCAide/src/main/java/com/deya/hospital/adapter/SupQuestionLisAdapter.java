package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.vo.SupervisorQestionVo;
import com.deya.hospital.workspace.TaskUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupQuestionLisAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<SupervisorQestionVo> list;
    private String stateSring[] = {"未反馈", "待确认", "已确认", "已关闭"};
    private int statusImg[] = {R.drawable.not_feedback, R.drawable.not_cfmd, R.drawable.has_cfmd, R.drawable.has_closed,};
    private int drawables[] = {R.drawable.circle_1,
            R.drawable.circle_2, R.drawable.circle_3,
            R.drawable.circle_4, R.drawable.circle_3,
            R.drawable.circle_5, R.drawable.circle_2, R.drawable.circle_1, R.drawable.circle_1, R.drawable.circle_5, R.drawable.circle_2, R.drawable.circle_3, R.drawable.circle_5, R.drawable.circle_1,};

    Map<Integer,String> map=new HashMap<>();

    public SupQuestionLisAdapter(Context context, List<SupervisorQestionVo> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        initTypes();


    }

    public void setData(List<SupervisorQestionVo> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder2 mviewHolder = null;
        if (convertView == null) {
            mviewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.sup_questionlist_item, null);
            mviewHolder.title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            mviewHolder.author = (TextView) convertView
                    .findViewById(R.id.tv_author);
            mviewHolder.stateImg = (ImageView) convertView
                    .findViewById(R.id.stateImg);
            mviewHolder.finishtime = (TextView) convertView
                    .findViewById(R.id.finishtime);
            mviewHolder.stateTv = (TextView) convertView
                    .findViewById(R.id.stateTv);
            mviewHolder.typeTv= (TextView) convertView
                    .findViewById(R.id.typeTv);
            convertView.setTag(mviewHolder);
        } else {
            mviewHolder = (ViewHolder2) convertView.getTag();
        }

        SupervisorQestionVo tv = list.get(position);

        mviewHolder.stateTv.setText(tv.getState() == 0 ? "" : stateSring[tv.getState() - 1]);
        mviewHolder.title.setText(tv.getDepartmentName()+":"+tv.getCheck_content());
        mviewHolder.stateImg.setImageResource(statusImg[tv.getState() - 1]);
        mviewHolder.author.setText(tv.getUser_name());
        mviewHolder.typeTv.setText(getTypeName(tv.getOrigin()));
        int r = tv.getOrigin() % 6;
        mviewHolder.typeTv.setBackgroundResource(drawables[r]);
        if (!AbStrUtil.isEmpty(tv.getCreate_time() + "")) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            String str = "";
            try {
                date = sdf.parse(tv.getCreate_time());

                // str= sdf.getTimeInstance().format(date);
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            mviewHolder.finishtime.setText(tv.getCreate_time());

        } else {
            mviewHolder.finishtime.setVisibility(View.GONE);
        }


        return convertView;
    }


    /**
     * 获取来源
     */
    void initTypes(){
        String str= SharedPreferencesUtil.getString(context, "get_dudao_types", "");
        ResistantMutiTextVo rmtv= TaskUtils.gson.fromJson(str,ResistantMutiTextVo.class);
        if(null!=rmtv){
            for(ResistantMutiTextVo.ResultListBean resultListBean:rmtv.getResultList()){
                map.put(resultListBean.getData_id(),resultListBean.getData_name());

            }
        }
    }
    private String getTypeName(int origin) {
        return AbStrUtil.isEmpty(map.get(origin))?"":map.get(origin);
    }

    class ViewHolder2 {
        public TextView title;
        public TextView author;
        public TextView ycRate;
        public ImageView stateImg;
        // public TextView rtRate;// 正确率
        public TextView timeTypes, stateTv;
        TextView finishtime;
        TextView typeTv;
    }

    public int dp2Px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }

}
