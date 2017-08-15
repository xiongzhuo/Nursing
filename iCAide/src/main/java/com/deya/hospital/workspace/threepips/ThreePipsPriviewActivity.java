package com.deya.hospital.workspace.threepips;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;
import com.deya.hospital.workspace.priviewbase.BaseSupvisorFragment;
import com.deya.hospital.workspace.priviewbase.RisistantPriveiwActivity;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ThreePipsPriviewActivity extends RisistantPriveiwActivity {
    @Override
    public String getTaskType() {
        return "9";
    }

    @Override
    public String getCacheForm() {
        return SharedPreferencesUtil.getString(mcontext, "comonform" + taskVo.getType(), "");
    }

    @Override
    public BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean) {
        if (getIntent().hasExtra("isInspector")) {
//            bean.setTemp1(1);
//            bean.setTemp2(1);
//            bean.setTemp3(1);
            bean.setIsInspector(1);
            bean.setTemp_type_id(2);


        }
        return ThreePipsInfoFragment.newInstance(bean);
    }

    @Override
    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
        if (businessBean.getTemp1() == 0 && businessBean.getTemp2() == 0 && businessBean.getTemp3() == 0) {
            fragmentPager.setCurrentItem(0);
            ToastUtil.showMessage("请至少选择一项三管内容");
            return;
        }
        try {
            job.put("sex", businessBean.getSex());
            job.put("apache", businessBean.getApache());
            job.put("temp_type_id", "2");
            job.put("temp1", businessBean.getTemp1());
            job.put("temp2", businessBean.getTemp2());
            job.put("temp3", businessBean.getTemp3());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setTask() {
        if(null!=chacheList){
        rv.getTemp_list().clear();
        rv.getTemp_list().addAll(chacheList);
        }
        super.setTask();
    }

    boolean isAddTemp1, isAddTemp2, isAddTemp3;
    BaseSupvisorFragment baseSupvisorFragment1, baseSupvisorFragment2, baseSupvisorFragment3;
    RisistantVo.TempListBean tempListBean1, tempListBean2, tempListBean3;
    List<RisistantVo.TempListBean> tablist;
    List<RisistantVo.TempListBean> chacheList;

    @Override
    public void initChildForm(RisistantVo rv, CaseListVo.CaseListBean businessBean) {
        CaseListVo.CaseListBean bean = businessBean;
//        if (bean.getTemp1() == 0 && bean.getTemp2() == 0 && bean.getTemp3() == 0) {
//            bean.setTemp1(1);
//            bean.setTemp2(1);
//            bean.setTemp3(1);
//
//        }
        tablist = new ArrayList<>();
        chacheList = new ArrayList<>();
        tabGv = (HorizontalListView) this.findViewById(R.id.tabGv);

        //过滤重复的
        if (null != rv.getTemp_list() && rv.getTemp_list().size() > 0) {
            for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
                if (null == rt) {
                    continue;
                }
                switch (rt.getTemp_type_id()) {
                    case 3:
                        if (!isAddTemp1) {
                            isAddTemp1 = true;
                            baseSupvisorFragment1 = BaseSupvisorFragment.newInstance(rv.getTask_info());
                            tempListBean1 = rt;
                        }
                        break;
                    case 4:
                        if (!isAddTemp2) {
                            isAddTemp2 = true;
                            tempListBean2 = rt;
                            baseSupvisorFragment2 = BaseSupvisorFragment.newInstance(rv.getTask_info());
                        }
                        break;
                    case 5:
                        if (!isAddTemp3) {
                            isAddTemp3 = true;
                            tempListBean3 = rt;
                            baseSupvisorFragment3 = BaseSupvisorFragment.newInstance(rv.getTask_info());
                        }
                        break;
                    default:
                        break;

                }
            }


            basePriviewInfoFragment = getInfoFragment(businessBean);
            listfragment.add(basePriviewInfoFragment);
            if (bean.getTemp1() == 1 && null != baseSupvisorFragment1) {
                listfragment.add(baseSupvisorFragment1);

                tablist.add(tempListBean1);
                chacheList.add(tempListBean1);
            }
            if (bean.getTemp2() == 1 && null != baseSupvisorFragment2) {
                listfragment.add(baseSupvisorFragment2);
                tablist.add(tempListBean2);
                chacheList.add(tempListBean2);
            }
            if (bean.getTemp3() == 1 && null != baseSupvisorFragment3) {
                tablist.add(tempListBean3);
                chacheList.add(tempListBean3);
                listfragment.add(baseSupvisorFragment3);
            }
        }
        RisistantVo.TempListBean tempListBean = new RisistantVo.TempListBean();
        tempListBean.setTemp_type_id(0);
        tablist.add(0, tempListBean);
        //initTab(tablist);
        myadapter.notifyDataSetChanged();
        if (bean.getTemp1() == 0 && bean.getTemp2() == 0 && bean.getTemp3() == 0) {
            fragmentPager.setCurrentItem(0);

        }

    }


    @Override
    protected String getTopTitle() {
        return "三管感染防控";
    }

    private String getTempStrs() {
        temp_type = "";
        if (caseVo.getTemp1() == 1) {
            temp_type += "3";
        }
        if (caseVo.getTemp2() == 1) {
            if (!AbStrUtil.isEmpty(temp_type)) {
                temp_type += ",4";
            } else {
                temp_type += "4";
            }
        }
        if (caseVo.getTemp3() == 1) {
            if (!AbStrUtil.isEmpty(temp_type)) {
                temp_type += ",5";
            } else {
                temp_type += "5";
            }
        }
        return temp_type;
    }

    @Override
    public void reSetTemps(int temp, int state) {
        listfragment.remove(spvisorFragment);
        switch (temp) {
            case 1:
                if (state == 1) {
                    listfragment.add(baseSupvisorFragment1);
                    tablist.add(tempListBean1);
                    chacheList.add(tempListBean1);
                } else {
                    listfragment.remove(baseSupvisorFragment1);
                    tablist.remove(tempListBean1);
                    chacheList.remove(tempListBean1);
                }
                break;
            case 2:
                if (state == 1) {
                    listfragment.add(baseSupvisorFragment2);
                    tablist.add(tempListBean2);
                    chacheList.add(tempListBean2);
                } else {
                    listfragment.remove(baseSupvisorFragment2);
                    tablist.remove(tempListBean2);
                    chacheList.remove(tempListBean2);
                }

                break;
            case 3:
                if (state == 1) {
                    listfragment.add(baseSupvisorFragment3);
                    tablist.add(tempListBean3);
                    chacheList.add(tempListBean3);
                } else {
                    listfragment.remove(baseSupvisorFragment3);
                    tablist.remove(tempListBean3);
                    chacheList.remove(tempListBean2);
                }
                break;

        }
        tabPagerAdapter.notifyDataSetChanged();
        listfragment.add(spvisorFragment);
        myadapter.notifyDataSetChanged();
    }
}
