/**
 * Project Name:Vicinity3.1.<br/>
 * File Name:MainBiz.java.<br/>
 * Package Name:com.trisun.vicinity.init.biz.<br/>
 * Date:2015年4月22日上午9:44:46.<br/>
 * Copyright (c) 2015, 广东云上城网络科技有限公司.
 *
 */

package com.deya.hospital.biz;

import com.deya.hospital.util.MyHandler;

/**
 * .ClassName: MainBiz(用一句话描述这个类的作用是做什么) <br/>
 * date: 2015年4月22日 上午9:44:46 <br/>
 * 
 * @author Administrator
 */
public interface MainBiz {
  // 请求更新app地址
  public void getLogin(MyHandler handler, int successMessage, int failMessage, String param);
  //请求验证码
  public void getIdentifyCode(MyHandler handler, int successMessage, int failMessage, String param);
  //请求验证码
  public void getRegister(MyHandler handler, int successMessage, int failMessage, String param);
  //获取杂志期刊列表
  public void getMagzineSortList(MyHandler handler, int successMessage, int failMessage, String param);
  //获取杂志期刊列表
  public void getArticalSearchList(MyHandler handler, int successMessage, int failMessage, String param);
  //获取收藏列表
  public void getArticalCelection(MyHandler handler, int successMessage, int failMessage, String param);
  //文章列表接口
  public void getArticalsList(MyHandler handler, int successMessage, int failMessage, String param);
  //相关文章接口
  public void getRletiveList(MyHandler handler, int successMessage, int failMessage, String param);
  //收藏接口
  public void celectArtical(MyHandler handler, int successMessage, int failMessage, String param);
  //我的浏览记录
  public void getMyPriviewArtical(MyHandler handler, int successMessage, int failMessage, String param);
  //阅读下载操作接口
  public void downAndReadAction(MyHandler handler, int successMessage, int failMessage, String param);
//请求版本更新接口
  public void CheckUpdate(MyHandler handler, int successMessage, int failMessage, String param);
//医院列表接口
  public void getHospitalList(MyHandler handler, int successMessage, int failMessage, String param);
//获取积分物品
public void getGoodsList(MyHandler handler, int successMessage, int failMessage, String param);
//获取我的积分物品
public void getPrizeList(MyHandler handler, int successMessage, int failMessage, String param);
//兑换
public void convertGoods(MyHandler handler, int successMessage, int failMessage, String param);

public void getDepartmentList(MyHandler handler, int successMessage, int failMessage,
		String param);
void sendEditor(MyHandler handler, int successMessage, int failMessage,
		String param);
void getTaskList(MyHandler handler, int successMessage, int failMessage,
		String param);
void sendTaskList(MyHandler handler, int successMessage, int failMessage,
		String param);
void getMessageList(MyHandler myHandler, int mssageSucess, int mssageFaile,
		String string);
void getJobList(MyHandler handler, int successMessage, int failMessage,
		String param);
void getAddDepartMentReq(MyHandler myHandler, int mssageSucess,
		int mssageFaile, String string);
void getGuideTypeList(MyHandler handler, int successMessage, int failMessage,
		String param);
void getDoucmentList(MyHandler handler, int successMessage, int failMessage,
		String param);
void getReadDoucment(MyHandler handler, int successMessage, int failMessage,
		String param);
void getRegistIdentifyCode(MyHandler handler, int successMessage,
		int failMessage, String param);
void deletTasks(MyHandler handler, int successMessage, int failMessage,
		String param);
void sendemail(MyHandler handler, int successMessage, int failMessage,
		String param);
void sendSupervisor(MyHandler handler, int successMessage, int failMessage,
		String param);
void sendChangeSupervisor(MyHandler handler, int successMessage,
		int failMessage, String param);
void getSignInfo(MyHandler handler, int successMessage, int failMessage,
		String param);
void getRuleListInfo(MyHandler handler, int successMessage, int failMessage,
		String param);
void getUserInfo(MyHandler handler, int successMessage, int failMessage,
		String param);
void getAddScore(MyHandler handler, int successMessage, int failMessage,
		String param);
}
