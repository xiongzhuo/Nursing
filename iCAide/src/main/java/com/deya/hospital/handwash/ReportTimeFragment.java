package com.deya.hospital.handwash;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.base.DeletDialog2;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.GsonUtils;
import com.deya.hospital.util.Player;
import com.deya.hospital.util.ShowNetWorkImageActivity;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.HandTimesVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/17
 */
public class ReportTimeFragment extends BaseTableFragment implements RequestInterface,RadioGroup.OnCheckedChangeListener,PullToRefreshBase.OnRefreshListener2{
    private static final int GET_DATA_DETAIL = 0x9011;
    private static final int DELET_SUC = 0x9012;
    PullToRefreshListView listView;
    List<HandTimesVo.TimesListBean> timesList;
    HandTimesAdapter handTimesAdapter;
    HandTimesVo handTimesVo;
    private int  pageIndex=1;
    RadioGroup radioGroup;
    int  type;
    List<Attachments> attachmentsList;
    HandTimesFileAdpter fileAdpter;
    LinearLayout empertyView;

    RadioButton rb0,rb1;
    private int pageCount;
    private String departmentId="";
  DeletDialog2 deletDialog2;
    private String time="";


    public static ReportTimeFragment newInstance(int type,String departmentId,String time){
        ReportTimeFragment newFragment = new ReportTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("departmentId", departmentId);
        bundle.putSerializable("time", time);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    @Override
    public void initView() {
        timesList=new ArrayList<>();
        attachmentsList=new ArrayList<>();
        listView=findById(R.id.listView);
        rb0=findById(R.id.rb0);
        rb1=findById(R.id.rb1);
        radioGroup=findById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        handTimesAdapter=new HandTimesAdapter(getActivity(),timesList);
        fileAdpter=new HandTimesFileAdpter(getActivity(),attachmentsList);
        Bundle args = getArguments();
        type= args.getInt("type",0);
        departmentId=args.getString("departmentId");
        time=args.getString("time");
        listView.setAdapter(handTimesAdapter);
        empertyView=findById(R.id.empertyView);
        listView.setOnRefreshListener(this);
        deletDialog2=new DeletDialog2(getActivity(), "确认删除", new DeletDialog2.DeletInter() {
            @Override
            public void onDelet(int position) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.rb0) {
                    if(isThisMonth(timesList.get(position).getMission_time())){
                    if (tools.getValue(Constants.USER_ID).equals(timesList.get(position).getUid() + ""))
                    {
                        deletTimer(timesList.get(position).getId());

                    }else{
                        ToastUtil.showMessage("此时机不是您做的，您没有权限删除！");
                    }
                    }else{
                        ToastUtil.showMessage("只能删除当月数据！");
                    }
                }
                deletDialog2.dismiss();
            }
        });
        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.rb0){
                    deletDialog2.show();
                    deletDialog2.setDeletPosition(position-1);
                }
                return false;
            }
        });
        showprocessdialog();
        getdetailData(type,departmentId);
    }
    private void deletTimer(int id) {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, getActivity(),
                DELET_SUC, job, "task/deleteTimer");
    }

    //判断选择的日期是否是本月
    public static boolean isThisMonth(String time)
    {
        return isThisTime(time,"yyyy-MM");
    }
    private static boolean isThisTime(String time,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if(param.equals(now)){
            return true;
        }
        return false;
    }
    private void getdetailData(int type, String departmentId) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("checkType",type+"");
            if(!AbStrUtil.isEmpty(departmentId)){
                job.put("departmentId", departmentId + "");
            }
            job.put("pageIndex", pageIndex);
            job.put("pageCount", 10);
            job.put("time",time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, getActivity(),
                GET_DATA_DETAIL, job, "task/timesAttachmentReport");
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_times_fragment;
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        dismissdialog();
        switch (code){
            case GET_DATA_DETAIL:
                dismissdialog();
                Log.i("GET_DATA_DETAIL",jsonObject.toString());
                listView.onRefreshComplete();

                handTimesVo= GsonUtils.JsonToObject(jsonObject.toString(),HandTimesVo.class);
                pageCount=handTimesVo.getTotalcnt();
                if(pageCount<=0){
                    TextView tv= (TextView) empertyView.findViewById(R.id.tv);
                    tv.setText("亲，没有查询到数据哟！");
                }else{
                    empertyView.setVisibility(View.GONE);
                }

                if (pageCount <= pageIndex * 10) {
                    listView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                } else {
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                }
                timesList.addAll(handTimesVo.getTimesList());
                if(null!=handTimesVo.getAttachmentList()){
                    attachmentsList.addAll(handTimesVo.getAttachmentList());
                    fileAdpter.notifyDataSetChanged();
                }
                rb0.setText("时机("+handTimesVo.getTotalcnt()+")");
                rb1.setText("附件("+attachmentsList.size()+")");
                handTimesAdapter.notifyDataSetChanged();
                listView.setEmptyView(empertyView);
                pageIndex++;
                break;
            case DELET_SUC:
                listView.setRefreshing();
                break;

        }

    }

    @Override
    public void onRequestErro(String message) {
        ToastUtil.showMessage(message);

    }

    @Override
    public void onRequestFail(int code) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb0:
                if (pageCount < (pageIndex - 1) * 10) {
                    listView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                } else {
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                }

                listView.setAdapter(handTimesAdapter);
                break;
            case R.id.rb1:
                listView.setMode(PullToRefreshBase.Mode.DISABLED);
                listView.setAdapter(fileAdpter);
                break;
        }

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        pageCount = 0;
        timesList.clear();
        handTimesAdapter.notifyDataSetChanged();
        getdetailData(type,departmentId);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        getdetailData(type,departmentId);
    }


    public class HandTimesAdapter extends DYSimpleAdapter<HandTimesVo.TimesListBean>{

        public HandTimesAdapter(Context context, List<HandTimesVo.TimesListBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            TextView person = findView(convertView,R.id.person);
            TextView unrules = findView(convertView,R.id.times);
            TextView method = findView(convertView,R.id.method);
            TextView result = findView(convertView,R.id.result);
            TextView indicationTv = findView(convertView,R.id.indicationTv);
            TextView  timeTv= findView(convertView,R.id.timeTv);
            HandTimesVo.TimesListBean timesBean=list.get(position);
            person.setText(timesBean.getPostname()+"  "+timesBean.getPname());
            unrules.setText(timesBean.getUnrules());
            unrules.setVisibility(timesBean.getUnrules().length()>0?View.VISIBLE:View.GONE);
            timeTv.setText(timesBean.getCheck_type()==30?timesBean.getMission_time():timesBean.getUsername()+"  "+timesBean.getMission_time());
            indicationTv.setText("("+timesBean.getCol_type_name()+")"+timesBean.getResults());
            result.setBackgroundResource(timesBean.getIs_result().equals("是")?R.drawable.finishs_wrong:R.drawable.finishs_right);
        }

        @Override
        public int getLayoutId() {
            return R.layout.finish_list_item;
        }
    }

   class HandTimesFileAdpter extends DYSimpleAdapter<Attachments>{

       public HandTimesFileAdpter(Context context, List<Attachments> list) {
           super(context, list);
       }

       @Override
       protected void setView(final int position, View convertView) {
           TextView title = (TextView) convertView
                   .findViewById(R.id.tv_title);
           ImageView img = (ImageView) convertView.findViewById(R.id.img);
           TextView size = (TextView) convertView.findViewById(R.id.size);
           LinearLayout recorderlayout = (LinearLayout) convertView
                   .findViewById(R.id.recorderlayout);
           ImageView recordimg = (ImageView) convertView
                   .findViewById(R.id.recordimg);
           ImageView delet = (ImageView) convertView
                   .findViewById(R.id.delet);

           delet.setVisibility(View.GONE);
           Attachments attachments = list.get(position);
           recordimg.setBackgroundResource(R.drawable.play);
           final AnimationDrawable drawable = (AnimationDrawable) recordimg
                   .getBackground();
           drawable.selectDrawable(2);

           switch (attachments.getFile_type()){
               case "1":
                           String  imgurl="";
                               imgurl= WebUrl.FILE_LOAD_URL + attachments.getFile_name();

                           final String[] strings = {imgurl };
                           ImageLoader.getInstance().displayImage(imgurl
                                   ,
                                   img, AbViewUtil.getOptions(R.drawable.defult_img));

                           recorderlayout.setVisibility(View.GONE);
                           img.setVisibility(View.VISIBLE);
                       convertView.setOnClickListener(new View.OnClickListener() {

                               @Override
                               public void onClick(View v) {

                                   Intent it = new Intent(context,
                                           ShowNetWorkImageActivity.class);

                                   it.putExtra("urls", strings);
                                   it.putExtra("nowImage", 0);
                                   context.startActivity(it);

                               }
                           });
                   break;
               case "2":
                   recorderlayout.setVisibility(View.VISIBLE);
                   img.setVisibility(View.GONE);
                   Log.i("111111加载成功", list.get(position).getFile_name() + "");

                   String timeStr = list.get(position).getTime();
                   if(null!=timeStr){
                   if (null!=timeStr&&timeStr.length() < 3) {
                       timeStr = timeStr + "000";
                   }
                   String time3 = timeStr.substring(0, 3);
                   title.setText(time3 + "''");
                   }
                   String url = WebUrl.FILE_LOAD_URL + attachments.getFile_name();

                   //  File file=new File("file://" + list.get(position).getFile_name());
                   final String finalUrl = url;
                   final Player player = new Player(drawable);
                   recorderlayout.setOnClickListener(new View.OnClickListener() {

                       @Override
                       public void onClick(View v) {

                           if (player.isPlaying()) {

                               player.pause();
                           } else {
                               player.playUrl(finalUrl);
                           }

                       }
                   });
                   convertView.setOnClickListener(null);
                   break;
               default:
                   break;
           }
           if (!AbStrUtil.isEmpty(list.get(position).getFile_type())) {
               int type = Integer.parseInt(list.get(position).getFile_type());
               final File file = new File(list.get(position).getFile_name());
               Log.i("11111file.exists()", file.exists() + "");
               if (file.exists()) {
                   String size1 = FormetFileSize(file.length());
                   size.setText(size1);
               }

               delet.setOnClickListener(new View.OnClickListener() {

                   @Override
                   public void onClick(View v) {
                      attachmentsList.remove(position);
                       notifyDataSetChanged();

                   }
               });
           } else {
               title.setText("");
           }

       }

       @Override
       public int getLayoutId() {
           return R.layout.filelist_item;
       }
   }

    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
