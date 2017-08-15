package com.deya.hospital.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.LoadingAlertDialog;
import com.deya.hospital.util.Tools;


/**
 * ClassName:. BaseFragment【Fragment基类】 <br/>
 */
public class BaseFragment extends Fragment {
  protected View rootView;
  Context mContext;
  PartTimeStaffDialog tipsDialog;
  public Tools tools;
  private DialogToast dialogToast;

  /**
   * TODO 简单描述该方法的实现功能（可选）.
   * 
   * @see android.support.v4.app.Fragment#onAttach(Activity)
   */
  @Override
  public void onAttach(Activity activity) {
    // TODO Auto-generated method stub
    super.onAttach(activity);
    mContext=getActivity();
    tools=new Tools(mContext, Constants.AC);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  /**
   * 公共弹出框吐司
   * @param txt1
   * @param txt2
   */
  public void showDialogToast(String  txt1,String txt2){
    if(null==dialogToast){
      dialogToast=new DialogToast(getActivity());
    }
    dialogToast.showTips(txt1,txt2);
  }
  @Override
	public void onDestroy() {
	  if (dialog != null && dialog.isShowing()) {
	      dialog.dismiss();;
	    }

    if(null!=dialogToast){
      dialogToast.dismiss();
    }
    if(null!=tipsDialog&&tipsDialog.isShowing()){
      tipsDialog.dismiss();
    }
		super.onDestroy();
	}
  private LoadingAlertDialog dialog;

  /**
   * showprocessdialog:【显示请求数据的processdialog】. <br/>
   * ..<br/>
   */
  protected void showprocessdialog() {
    if (null == dialog) {
      dialog = new LoadingAlertDialog(getActivity());
    }
    dialog.show();
  }

  /**
   * dismissdialog:【取消请求数据的processdialog】. <br/>
   * ..<br/>
   */
  protected void dismissdialog() {
    if (dialog != null && dialog.isShowing()) {
      dialog.cancel();
    }
  }
  
  protected View findViewById(int id) {
      if(null != rootView) {
          return rootView.findViewById(id);
      }
      return null;
  }

  public void onStartActivity(Class<?> t){
    Intent it=new Intent(getActivity(), t);
    startActivity(it);
  }
  public    <T extends View> T findView(int id){
    return (T) findViewById(id);
  }
  public void StartActivity(Class<?> t){
    Intent it=new Intent(getActivity(), t);
    startActivity(it);
  }
}
