package com.deya.hospital.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class TaskLisAdapter extends BaseTaskAadpter<TaskVo> {
    private int drawables[] = {R.drawable.circle_1,
            R.drawable.circle_2, R.drawable.circle_3,
            R.drawable.circle_4, R.drawable.circle_3,
            R.drawable.circle_5, R.drawable.circle_2, R.drawable.circle_1, R.drawable.circle_1, R.drawable.circle_5, R.drawable.circle_2, R.drawable.circle_3, R.drawable.circle_5, R.drawable.circle_1,};
    private String stateSring[] = {"已上传", "待上传", "未完成", "未完成", "上传失败","预设"};
    private int statusImg[] = {R.drawable.finish_img, R.drawable.pripair_upload, R.drawable.unfinish, R.drawable.unfinish, R.drawable.upload_fail, R.drawable.after_status};
    public boolean shwoName = false;


    public TaskLisAdapter(Context context, List<TaskVo> list) {
        super(context, list);
        this.context = context;
        this.list = list;
        tools = new Tools(context, Constants.AC);
        String job = tools.getValue(Constants.JOB);
        String isAdmin = tools.getValue(Constants.IS_ADMIN);
        if (AbStrUtil.isEmpty(isAdmin)) {
            isAdmin = "";
        }
        if (isAdmin.equals("1")) {
            shwoName = true;
        }
        if (job != null && job.equals("1")) {
            shwoName = true;
        }
    }

    @Override
    public void setItem(int position, TaskAdapterViewHolder mviewHolder) {
        if (position < list.size()) {
            final TaskVo tv = list.get(position);
            if (AbStrUtil.isEmpty(tv.getType())) {
                tv.setType("4");
            }
            int tasktype = Integer.parseInt(tv.getType());
            int r = tasktype % 6;
            mviewHolder.typeTv.setBackgroundResource(drawables[r]);
            if (!AbStrUtil.isEmpty(tv.getType()) && tv.getType().equals("1")) {
                mviewHolder.typeTv.setText("手卫生");
                if (null != tv.getYc_rate()) {
                    mviewHolder.title.setText(tv.getDepartmentName() + "：依从率"
                            + tv.getYc_rate() + "% " + "正确率"
                            + tv.getValid_rate() + "%");
                } else if (null == tv.getTotalNum()
                        || tv.getTotalNum().equals("0")) {
                    mviewHolder.title.setText(tv.getDepartmentName() + "：依从率"
                            + "0%  " + "正确率" + "0%");
                }
//                switch (type) {
//                    case 0:
//                        if (null != tv.getDepartment()) {
//                            mviewHolder.timeTypes.setText("不限时机");
//                        } else {
//                            mviewHolder.timeTypes.setText("");
//                        }
//                        break;
//                    case 2:
//                        if (null != tv.getDepartment()) {
//                            mviewHolder.timeTypes.setText("15个时机");
//                        } else {
//                            mviewHolder.timeTypes.setText("");
//                        }
//                        break;
//                    case 1:
//                        if (null != tv.getDepartment()) {
//                            mviewHolder.timeTypes.setText("10个时机");
//                        } else {
//                            mviewHolder.timeTypes.setText("");
//                        }
//                        break;
//                    case 3:
//                        if (null != tv.getDepartment()) {
//                            mviewHolder.timeTypes.setText("20个时机");
//                        } else {
//                            mviewHolder.timeTypes.setText("");
//                        }
//                        break;
//                    case 4:
//                        if (null != tv.getDepartment()) {
//                            mviewHolder.timeTypes.setText("40个时机");
//                        } else {
//                            mviewHolder.timeTypes.setText("");
//                        }
//                        break;
//                    default:
//                        if (type >= 10 && null != tv.getDepartment()) {
//                            mviewHolder.timeTypes.setText(type - 10 + "个时机");
//                        } else {
//                            mviewHolder.timeTypes.setText("");
//                        }
//                        break;
//                }
                if (!AbStrUtil.isEmpty(tv.getFiveTasks())) {
                    try {
                        List<planListDb> list = TaskUtils.gson.fromJson(tv.getFiveTasks().replace("\\", ""), new TypeToken<List<planListDb>>() {
                        }.getType());

                        int num = 0;
                        for (com.deya.hospital.vo.dbdata.planListDb planListDb : list) {
                            num += planListDb.getSubTasks().size();
                        }
                        mviewHolder.timeTypes.setText(num + "个时机");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mviewHolder.timeTypes.setText("");
                    }
                } else {
                    mviewHolder.timeTypes.setText("");
                }
            } else if (tasktype == 2) {
                mviewHolder.typeTv.setText("督查反馈");
                mviewHolder.timeTypes.setText("");
                if (null != tv.getIs_main_remark() && tv.getIs_main_remark().equals("1")) {
                    mviewHolder.title.setText(tv.getDepartmentName() + "："
                            + tv.getMain_remark_name());
                } else {
                    mviewHolder.title.setText(tv.getDepartmentName() + "："
                            + tv.getCheck_content().trim());
                }
            } else if (tasktype == 3 || tasktype == 6) {
                mviewHolder.timeTypes.setText("");
                mviewHolder.typeTv.setText("临床考核");
                // mviewHolder.timeTypes.setText(tv.getDepartmentName()+"的"+tv.getName());
                if (null != tv.getGrid_type() && (tv.getGrid_type().equals("1") || tv.getGrid_type().equals("4")||tv.getGrid_type().equals("3"))) {
                    mviewHolder.title
                            .setText(tv.getDepartmentName()
                                    + "："
                                    + AbStrUtil.getNotNullStr(tv.getName())
                                    + "("
                                    + AbStrUtil.reMoveUnUseNumber(tv.getScore() + "")
                                    + "分)");
                } else {
                    mviewHolder.title.setText(tv.getDepartmentName() + "："
                            +  AbStrUtil.getNotNullStr(tv.getName()));
                }
            } else if (tasktype == 5) {
                mviewHolder.timeTypes.setText("");
                mviewHolder.typeTv.setText("外科手消毒");
                // mviewHolder.timeTypes.setText(tv.getDepartmentName()+"的"+tv.getName());
                if (null != tv.getGrid_type() && tv.getGrid_type().equals("1")) {
                    if (null == tv.getScore()) {
                        tv.setScore("0");
                    }
                    mviewHolder.title
                            .setText(tv.getDepartmentName()
                                    + "："
                                    + AbStrUtil.getNotNullStr(tv.getName())
                                    + "("
                                    + AbStrUtil.reMoveUnUseNumber(tv.getScore())
                                    + "分)");
                } else {
                    mviewHolder.title.setText(tv.getDepartmentName() + ":"
                            + AbStrUtil.getNotNullStr(tv.getName()));
                }
            } else if (tasktype == 4 || tasktype == 15) {
                mviewHolder.typeTv.setText("消耗量");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName() + "：手卫生消毒消耗量");
            } else if (tasktype == 7) {
                mviewHolder.typeTv.setText("督查反馈");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName() + ":"
                        + tv.getCheck_content().trim());

            } else if (tasktype == 8) {
                mviewHolder.typeTv.setText("多耐");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            } else if (tasktype == 9) {
                mviewHolder.typeTv.setText("三管");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            } else if (tasktype == 10) {
                mviewHolder.typeTv.setText("环境清洁");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            } else if (tasktype == 12) {
                mviewHolder.typeTv.setText("手术部位感染");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            } else if (tasktype == 11) {
                mviewHolder.typeTv.setText("安全注射");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            } else if (tasktype == 13) {
                mviewHolder.typeTv.setText("医废管理");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            } else if (tasktype == 14) {
                mviewHolder.typeTv.setText("其他");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            }  else if (tasktype == 15) {
                mviewHolder.typeTv.setText("消毒消耗量");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            }else if (tasktype == 16) {
                mviewHolder.typeTv.setText("临床考核");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName());
            }else if (tasktype == 17) {
                mviewHolder.typeTv.setText("临床质控");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName()+":"+tv.getTemp_list().getTitle());
            }else if (tasktype == 21) {
                mviewHolder.typeTv.setText("手卫生操作考核");
                mviewHolder.timeTypes.setText("");
                mviewHolder.title.setText(tv.getDepartmentName()+":"+tv.getTemp_list().getTitle());
            }
            mviewHolder.stateTv.setText(stateSring[tv.getStatus()]);

            mviewHolder.stateImg
                    .setBackgroundResource(statusImg[tv.getStatus()]);


            if (!AbStrUtil.isEmpty(tv.getMission_time() + "")) {

                String name="";
                if(!AbStrUtil.isEmpty(tv.getUname())){
                    name=tv.getUname();
                }else if(!AbStrUtil.isEmpty(tv.getUser_name())){
                    name=tv.getUser_name();
                }else if(!AbStrUtil.isEmpty(tv.getName())){
                    name=tv.getName();
                }else if(!AbStrUtil.isEmpty(tv.getMemberName())){
                    name=tv.getMemberName();
                }
                    mviewHolder.finishtime.setText(name + "  " + tv.getMission_time());
                    // mviewHolder.finishtime.setText(tv.getUname()+"  "+tv.getMission_time());

            } else {
                mviewHolder.finishtime.setVisibility(View.GONE);
            }
        }
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
