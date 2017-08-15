package com.deya.hospital.workspace.priviewbase;

import android.content.Context;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.CaseListVo2;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class CaseUtil {
    public static List<CaseListVo.CaseListBean> getLoacalCase(){
            List<CaseListVo.CaseListBean> list = null;
            try {
                list = DataBaseHelper
                        
                        .getDbUtilsInstance(MyAppliaction.getContext())
                        .findAll(
                                Selector.from(CaseListVo.CaseListBean.class).orderBy("dbid"));
            } catch (DbException e) {
                Log.e("upload", e.getMessage() + "");
                e.printStackTrace();
            }
            if (list == null) {
                list = new ArrayList<CaseListVo.CaseListBean>();
            }
            return list;
    }

    public static void onAddCaseInDb(CaseListVo.CaseListBean caseVo) {
        try {
            DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext()).save(caseVo);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void updateCaseInDb( CaseListVo.CaseListBean caseVo) {
        try {
            DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext())
                    .update(caseVo, WhereBuilder.b("dbid", "=", caseVo.getDbid()));
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void updateCaseInDb(CaseListVo2.CaseListBean caseVo) {
        try {
            DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext())
                    .update(caseVo, WhereBuilder.b("dbid", "=", caseVo.getDbid()));
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void removeLoacalCase(int id){
        try {
            DataBaseHelper
                    
                    .getDbUtilsInstance(MyAppliaction.getContext())
                    .delete(CaseListVo.CaseListBean.class, WhereBuilder.b("dbid", "=", id));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static  void syscServiceCase(Context mcontext,List<CaseListVo.CaseListBean> tasks){
        try {
            int status = 0; // 已上传
            List<CaseListVo.CaseListBean> list=getAllLocalCase(mcontext);
            if(null==list){

                list=new ArrayList<CaseListVo.CaseListBean>();
            }

            for (CaseListVo.CaseListBean task : tasks) {
                boolean needSave=true;

                for(CaseListVo.CaseListBean vo:list){

                    if(task.getCase_id()==vo.getCase_id()){
                        task.setDbid(vo.getDbid());
                        task.setUploadStatus(vo.getUploadStatus());
                        task.setJsonStr(vo.getJsonStr());
                        task.setSupStr(vo.getSupStr());
                        Log.i("task_save", "need"+task.getCase_id());
                        needSave=false;
                        break;
                    }
                }

                if(needSave){
                    DataBaseHelper.getDbUtilsInstance(mcontext)
                            .save(task);
                }else{
                    DataBaseHelper.getDbUtilsInstance(mcontext)
                            .update(task);
                }
            }

        } catch (DbException e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

    }


    public static List<CaseListVo.CaseListBean> getUploadedCase(Context mcontext) {
        List<CaseListVo.CaseListBean> list = null;
        try {
            list= DataBaseHelper
                    
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(CaseListVo.CaseListBean.class)
                                    .where("uploadStatus", "=",0)
                                    .orderBy("dbid"));
        } catch (DbException e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }
        if (list == null) {
            list = new ArrayList<CaseListVo.CaseListBean>();
        }

        return list;
    }

    public static List<CaseListVo.CaseListBean> getAllLocalCase(Context mcontext) {
        List<CaseListVo.CaseListBean> list = null;
        Log.i("info",
                "mobile: " + MyAppliaction.getConstantValue(Constants.MOBILE));
        try {
            list = DataBaseHelper
                    
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(CaseListVo.CaseListBean.class).orderBy(
                                            "uploadStatus desc, mission_time desc, dbid"));
        } catch (DbException e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<CaseListVo.CaseListBean>();
        }
        return list;
    }


    public static List<CaseListVo.CaseListBean> getlocalCaseById(Context mcontext, String  type_id) {
        List<CaseListVo.CaseListBean> list = null;
        Log.i("info",
                "mobile: " + MyAppliaction.getConstantValue(Constants.MOBILE));
        try {
            list = DataBaseHelper
                    
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(CaseListVo.CaseListBean.class).where("temp_type_id", "=", type_id).orderBy(
                                    "uploadStatus desc, mission_time desc, dbid"));
        } catch (DbException e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<CaseListVo.CaseListBean>();
        }
        return list;
    }
}
