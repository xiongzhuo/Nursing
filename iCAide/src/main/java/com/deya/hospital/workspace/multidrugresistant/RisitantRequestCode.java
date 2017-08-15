package com.deya.hospital.workspace.multidrugresistant;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class RisitantRequestCode {
    public static final int GET_CASES_SUC=0x8000;//获取病例成功
    public static final int GET_CASES_FAIL=0x8001;//获取病例列表失败

    public static final int GET_DUCHA_SUC=0x8002;//获取督察模版详情成功
    public static final int GET_DUCHA_FAIL=0x8003;//获取督察详情失败


    public static final int SEB_CASE_SUCESS=0x8004;//获取督察模版详情成功
    public static final int SEB_CASE_FAIL=0x8005;//获取督察详情失败



    //CHECK_SPECIMEN   ： 送检标本
   // DRUG_RESISTANCE  ： 耐药菌
    public static final int GRT_MUTITEXT_SUC=0x8006;//获取送检标本
    public static final int GRT_MUTITEXT_FAIL=0x8007;//获取督察详情失败

    public static final int GRT_DRUG_MUTITEXT_SUC=0x8008;//耐药多选成功
    public static final int GRT_DRUG_MUTITEXT_FAIL=0x8009;//耐药多选

    public static final int SEND_TASK_SUCESS=0x8010;//提交任务成功
    public static final int SEND_TASK_FAIL=0x8011;//提交任务失败

    public static final int SEND_FEEDBACK_SUCESS=0x8012;//提交存在问题成功
    public static final int SEND_FEEDBACK_FAIL=0x8013;//提交存在为题失败

    //删除病例
    public static final int REMOVE_CASE_SUCESS=0x8014;//提交存在问题成功
    public static final int REMOVE_CASE_FAIL=0x8015;//提交存在为题失败

    //获取督导本详情
    public static final int REMOVE_SUPDETAIL_SUCESS=0x8016;
    public static final int REMOVE_SUPDETAIL_FAIL=0x8017;




}
