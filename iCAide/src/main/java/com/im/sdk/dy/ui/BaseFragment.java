package com.im.sdk.dy.ui;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.deya.hospital.util.LoadingAlertDialog;
import com.im.sdk.dy.common.AudioManagerTools;
import com.im.sdk.dy.common.utils.LogUtil;

/**
 * 自定义BaseFragment，处理上下音量键按下事件
 * Created by yung on 2015/12/18.
 */
public abstract class BaseFragment extends CCPFragment {

    /**当前CCPFragment所承载的FragmentActivity实例*/
    private FragmentActivity mActionBarActivity;
    private AudioManager mAudioManager;

    /**AudioManager.STREAM_MUSIC类型的音量最大值*/
    private int mMusicMaxVolume;
    public Activity activity;
    private LoadingAlertDialog dialog;
    

    /**
     * 设置ActionBarActivity实例
     * @param activity
     */
    public void setActionBarActivity(FragmentActivity activity) {
        this.mActionBarActivity = activity;
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = AudioManagerTools.getInstance().getAudioManager();
        mMusicMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 自定义页面方法,处理上下音量键按下事件
     * @param keyCode
     * @param event
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
                && mAudioManager != null) {
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (streamVolume >= mMusicMaxVolume) {
                LogUtil.d(LogUtil.getLogUtilsTag(BaseFragment.class),
                        "has set the max volume");
                return true;
            }
            int mean = mMusicMaxVolume / 7;
            if (mean == 0) {
                mean = 1;
            }
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume + mean, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
            return true;
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
                && mAudioManager != null) {
            int streamVolume = mAudioManager .getStreamVolume(AudioManager.STREAM_MUSIC);
            int mean = mMusicMaxVolume / 7;
            if (mean == 0) {
                mean = 1;
            }
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume - mean, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
	
	/**
	   * showprocessdialog:【显示请求数据的processdialog】. <br/>
	   * ..<br/>
	   */
	  protected void showprocessdialog() {
		  try {
			  if (null == dialog) {
			      dialog = new LoadingAlertDialog(activity);
			    }
			    dialog.show();
		} catch (Exception e) {
			
		}
	   
	  }

	  protected void  showCaclebleDialog(){
		  if (null == dialog) {
			  
		      dialog = new LoadingAlertDialog(activity);
		     
		    }
		  dialog.setCancelable(true);
		    dialog.show();
	  }
	  /**
	   * dismissdialog:【取消请求数据的processdialog】. <br/>
	   * ..<br/>
	   */
	  protected void dismissdialog() {
		  try {
			  if (dialog != null && dialog.isShowing()) {
			      dialog.dismiss();
			    }
		} catch (Exception e) {
		}
		 
	   
	  }
}

