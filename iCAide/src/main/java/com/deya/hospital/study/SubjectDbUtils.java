package com.deya.hospital.study;

import android.content.Context;
import android.util.Log;

import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workspace.TaskUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 * 题目数据库操作类
 *
 * @author : sunp
 * @date 2016/11/24
 */
public class SubjectDbUtils {
    public static void deleteData(Context context, KnowledgeVo.ListBean listBean) {
        try {
            DataBaseHelper
                    .getDbUtilsInstance(context)
                    .delete(listBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean saveData(Context context, KnowledgeVo.ListBean listBean) {

        listBean.setItemStr(TaskUtils.gson.toJson(listBean.getItems()));
        try {
            DataBaseHelper
                    .getDbUtilsInstance(context)
                    .save(listBean);
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }
    public static boolean updateData(Context context, KnowledgeVo.ListBean listBean) {
        if (!isAddedById(context,listBean)) {
            return false;
        }
        try {
            DataBaseHelper
                    .getDbUtilsInstance(context)
                    .update(listBean);
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static List<KnowledgeVo.ListBean> getAllDbData(Context context) {
        List<KnowledgeVo.ListBean> list = null;
        try {
            list = DataBaseHelper
                    .getDbUtilsInstance(context)
                    .findAll(
                            Selector.from(KnowledgeVo.ListBean.class).orderBy(
                                    "dbid"));
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public static List<KnowledgeVo.ListBean> getlocalDataByKey(Context mcontext, String key, String value) {
        List<KnowledgeVo.ListBean> list = null;
        try {
            list = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(KnowledgeVo.ListBean.class).where(key, "=", value));
        } catch (DbException e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    public static List<KnowledgeVo.ListBean>  getSizelocalDataByKey(Context mcontext, String[] key, String[] value) {
        List<KnowledgeVo.ListBean> list = null;
        try {
            list = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(KnowledgeVo.ListBean.class).where(key[0], "=", value[0]).and(key[1], "=", value[1]));
        } catch (DbException e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    public static boolean isAddedById(Context mcontext,KnowledgeVo.ListBean listBean){

        return getlocalDataByKey(mcontext, "id", listBean.getId()+"").size() > 0;

    }

    public static boolean isCollected(Context mcontext,KnowledgeVo.ListBean listBean){
        List<KnowledgeVo.ListBean> list = null;
        try {
            list = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(KnowledgeVo.ListBean.class).where("id", "=", listBean.getId()).and("is_colected","=",1));
        } catch (DbException e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<>();
        }
        return list.size()>0;

    }

    public static KnowledgeVo.ListBean  getDataById(Context mcontext, KnowledgeVo.ListBean listBean){
        if(null==listBean){
            return null;
        }
        List<KnowledgeVo.ListBean> list=getlocalDataByKey(mcontext, "id", listBean.getId()+"");
        if(list.size() > 0){
            return list.get(0);
        }

        return  null;


    }
}
