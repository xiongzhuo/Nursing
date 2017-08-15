package com.im.sdk.dy.ui.chatting.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.contact.ContactDetailActivity;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.yuntongxun.ecsdk.ECMessage;

import java.util.HashMap;


/**
 * <p>Title: BaseChattingRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 * @author Jorstin Chan
 * @date 2015-4-17
 * @version 1.0
 */
public abstract class BaseChattingRow implements IChattingRow {

    public static final String TAG = LogUtil.getLogUtilsTag(BaseChattingRow.class);
    private HashMap<String, ECContacts> hashMap = new HashMap<String, ECContacts>();
    int mRowType;
    DisplayImageOptions optionsSquare_men;
    DisplayImageOptions optionsSquare_women;
    
    public BaseChattingRow(int  type) {
        mRowType = type;
        
        optionsSquare_men = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_men_defalut)
		.showImageForEmptyUri(R.drawable.im_men_defalut)
		.showImageOnFail(R.drawable.im_men_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new SimpleBitmapDisplayer()).build();
//.displayer(new FadeInBitmapDisplayer(300)).build();
        
        optionsSquare_women = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_women_defalut)
		.showImageForEmptyUri(R.drawable.im_women_defalut)
		.showImageOnFail(R.drawable.im_women_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new SimpleBitmapDisplayer()).build();
//.displayer(new FadeInBitmapDisplayer(300)).build();
    }

    /**
     * 处理消息的发送状态设置
     * @param position 消息的列表所在位置
     * @param holder 消息ViewHolder
     * @param l
     */
    protected static void getMsgStateResId(int position , BaseHolder holder , ECMessage msg , View.OnClickListener l){
        if(msg != null && msg.getDirection() == ECMessage.Direction.SEND) {
            ECMessage.MessageStatus msgStatus = msg.getMsgStatus();
            if(msgStatus == ECMessage.MessageStatus.FAILED) {
                holder.getUploadState().setImageResource(R.drawable.msg_state_failed_resend);
                holder.getUploadState().setVisibility(View.VISIBLE);
                if(holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }
            } else  if (msgStatus == ECMessage.MessageStatus.SUCCESS || msgStatus == ECMessage.MessageStatus.RECEIVE) {
                holder.getUploadState().setImageResource(0);
                holder.getUploadState().setVisibility(View.GONE);
                if(holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }

            } else  if (msgStatus == ECMessage.MessageStatus.SENDING) {
                holder.getUploadState().setImageResource(0);
                holder.getUploadState().setVisibility(View.GONE);
                if(holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.VISIBLE);
                }

            } else {
                if(holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }
                LogUtil.d(TAG, "getMsgStateResId: not found this state");
            }

            ViewHolderTag holderTag = ViewHolderTag.createTag(msg, ViewHolderTag.TagType.TAG_RESEND_MSG , position);
            holder.getUploadState().setTag(holderTag);
            holder.getUploadState().setOnClickListener(l);
        }
    }

    /**
     *
     * @param contextMenu
     * @param targetView
     * @param detail
     * @return
     */
    public abstract boolean onCreateRowContextMenu(ContextMenu contextMenu , View targetView , ECMessage detail);


    /**
     *
     * @param baseHolder
     * @param displayName
     */
    public static void setDisplayName(BaseHolder baseHolder , String displayName) {
        if(baseHolder == null || baseHolder.getChattingUser() == null) {
            return ;
        }

        if(TextUtils.isEmpty(displayName)) {
            baseHolder.getChattingUser().setVisibility(View.GONE);
            return ;
        }
        baseHolder.getChattingUser().setText(displayName);
        baseHolder.getChattingUser().setVisibility(View.VISIBLE);
    }

    protected abstract void buildChattingData(Context context , BaseHolder baseHolder , ECMessage detail , int position);

    @Override
    public void buildChattingBaseData(Context context, BaseHolder baseHolder, ECMessage detail, int position) {

        // 处理其他使用逻辑
        buildChattingData(context, baseHolder, detail, position);
        setContactPhoto(baseHolder , detail);
        if(((ChattingActivity) context).isPeerChat() && detail.getDirection() == ECMessage.Direction.RECEIVE) {
            ECContacts contact = ContactSqlManager.getContact(detail.getForm());
            if(contact != null) {
                if(TextUtils.isEmpty(contact.getNickname())) {
                    contact.setNickname(contact.getContactid());
                }
                setDisplayName(baseHolder, contact.getNickname());
            } else {
                setDisplayName(baseHolder, detail.getForm());
            }
        }
        setContactPhotoClickListener(context ,baseHolder , detail);
    }

    private void setContactPhotoClickListener(final Context context ,BaseHolder baseHolder, final ECMessage detail) {
        if(baseHolder.getChattingAvatar() != null && detail != null) {

            baseHolder.getBaseView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            baseHolder.getChattingAvatar().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ECContacts contact = ContactSqlManager.getContact(detail.getForm());
                    if(contact == null || contact.getId() == -1) {
                        return ;
                    }
                    if(contact.getType()==999){
                    	return ;
                    }
                    if(contact.getContactid().equals(CCPAppManager.getClientUser().getUserId())){
                    	return;
                    }
                    
                    Intent intent = new Intent(context, ContactDetailActivity.class);
                    intent.putExtra(ContactDetailActivity.RAW_ID, contact.getId());
                    intent.putExtra("contact_id", contact.getContactid());
                    context.startActivity(intent);
                }
            });

            /**@某人*/
//            baseHolder.getChattingAvatar().setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if(context instanceof  ChattingActivity) {
//                        final ChattingActivity activity = (ChattingActivity) context;
//                        if(activity.isPeerChat() &&  !activity.mChattingFragment.mAtsomeone) {
//                            activity.mChattingFragment.mAtsomeone = true;
//                            // 群组
//                            ECContacts contact = ContactSqlManager.getContact(detail.getForm());
//                            
//                            if(contact != null) {
//                                if(TextUtils.isEmpty(contact.getNickname() )) {
//                                    contact.setNickname(contact.getContactid());
//                                }
//                                activity.mChattingFragment.getChattingFooter().setLastText(activity.mChattingFragment.getChattingFooter().getLastText() + "@" + contact.getNickname() + (char)(8197));
//                                activity.mChattingFragment.getChattingFooter().setMode(1);
//                                v.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        activity.mChattingFragment.mAtsomeone = false;
//                                    }
//                                },2000L);
//                            }
//                        }
//                    }
//
//                    return true;
//                }
//            });
        }
    }


    /**
     * 添加用户头像
     * @param baseHolder
     * @param detail
     */
    private void setContactPhoto(BaseHolder baseHolder, ECMessage detail) {
        if(baseHolder.getChattingAvatar() != null) {
            try {
                if (TextUtils.isEmpty(detail.getForm())) {
                    return;
                }
                ECContacts contacts=null;
                if (hashMap.containsKey(detail.getForm())) {
                	contacts = hashMap.get(detail.getForm());
                } else {
                	contacts = ContactSqlManager.getContact(detail.getForm());
                }
                
                if(contacts.getType()==999){
                    baseHolder.getChattingAvatar().setImageResource(R.drawable.xiaoxi_touxiang);
                    return;
                }else if(contacts.getType()==1000){
                    baseHolder.getChattingAvatar().setImageResource(R.drawable.im_task_tips);
                }else{
                    if(contacts.getContactid().equals(CCPAppManager.getClientUser().getUserId())){
                		 if (!AbStrUtil.isEmpty(CCPAppManager.getClientUser().getAvatar())) {
 		        			ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL+CCPAppManager.getClientUser().getAvatar(),baseHolder.getChattingAvatar(), CCPAppManager.getClientUser().getSex()==1?optionsSquare_women:optionsSquare_men);
 		        			
 		        	    } else {
 		        	    	ImageLoader.getInstance().displayImage("",
 		        	    			baseHolder.getChattingAvatar(),CCPAppManager.getClientUser().getSex()==1?optionsSquare_women:optionsSquare_men);
 		        	    }
                     }else{
		                if (!AbStrUtil.isEmpty(contacts.getAvatar())) {
		        			ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL+contacts.getAvatar(),baseHolder.getChattingAvatar(), contacts.getSex()==1?optionsSquare_women:optionsSquare_men);
		        			
		        	    } else {
		        	    	ImageLoader.getInstance().displayImage("",
		        	    			baseHolder.getChattingAvatar(),  contacts.getSex()==1?optionsSquare_women:optionsSquare_men);
		        	    }
                     }
                }
//                baseHolder.getChattingAvatar().setImageBitmap(
//                        ContactLogic.getPhoto(userUin));
                
            } catch (Exception e) {
            }
        }
    }
    
    public OnLongClickListener onLongClickListener=new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			return false;
		}
	};

}
