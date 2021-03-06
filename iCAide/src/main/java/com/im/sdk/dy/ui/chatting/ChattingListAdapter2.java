package com.im.sdk.dy.ui.chatting;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deya.acaide.R;
import com.im.sdk.dy.common.utils.DateUtil;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.MediaPlayTools;
import com.im.sdk.dy.storage.ConversationSqlManager;
import com.im.sdk.dy.storage.IMessageSqlManager;
import com.im.sdk.dy.ui.CCPListAdapter;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.model.BaseChattingRow;
import com.im.sdk.dy.ui.chatting.model.ChattingRowType;
import com.im.sdk.dy.ui.chatting.model.DescriptionRxRow;
import com.im.sdk.dy.ui.chatting.model.DescriptionTxRow;
import com.im.sdk.dy.ui.chatting.model.FileRxRow;
import com.im.sdk.dy.ui.chatting.model.FileTxRow;
import com.im.sdk.dy.ui.chatting.model.IChattingRow;
import com.im.sdk.dy.ui.chatting.model.ImageRxRow;
import com.im.sdk.dy.ui.chatting.model.ImageTxRow;
import com.im.sdk.dy.ui.chatting.model.VoiceRxRow;
import com.im.sdk.dy.ui.chatting.model.VoiceTxRow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuntongxun.ecsdk.ECMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * com.im.sdk.dy.ui.chatting in ECDemo_Android
 * Created by Jorstin on 2015/4/8.
 */
public class ChattingListAdapter2 extends CCPListAdapter<ECMessage> {

    protected View.OnClickListener mOnClickListener;
    /**当前语音播放的Item*/
    public int mVoicePosition = -1;
    /**需要显示时间的Item position*/
    private ArrayList<String> mShowTimePosition;
    /**初始化所有类型的聊天Item 集合*/
    private HashMap<Integer, IChattingRow> mRowItems ;
    /**时间显示控件的垂直Padding*/
    private int mVerticalPadding;
    /**时间显示控件的横向Padding*/
    private int mHorizontalPadding;
    /**消息联系人名称显示颜色*/
    private ColorStateList[] mChatNameColor;
    private String mUsername;
    private long mThread = -1;
    private int mMsgCount = 18;
    private int mTotalCount = mMsgCount;
    private int contactsType;

    public ChattingListAdapter2(Context ctx, ECMessage ecMessage , String user) {
        this(ctx , ecMessage , user , -1,0);
    }
    /**
     * 构造方法
     * @param ctx
     * @param ecMessage
     */
    public ChattingListAdapter2(Context ctx, ECMessage ecMessage , String user , long thread,int contactsType) {
        super(ctx, ecMessage);
        mUsername = user;
        mThread = thread;
        mRowItems = new HashMap<Integer, IChattingRow>();
        mShowTimePosition = new ArrayList<String>();
        this.contactsType=contactsType;
        initRowItems();

        // 初始化聊天消息点击事件回调
        mOnClickListener = new ChattingListClickListener((ChattingActivity)mContext , null);
        mVerticalPadding = mContext.getResources().getDimensionPixelSize(R.dimen.SmallestPadding);
        mHorizontalPadding = mContext.getResources().getDimensionPixelSize(R.dimen.LittlePadding);
        mChatNameColor = new ColorStateList[]{
                mContext.getResources().getColorStateList(R.color.white),
                mContext.getResources().getColorStateList(R.color.chatroom_user_displayname_color)};
    }

    public long setUsername(String username) {
        this.mUsername = username;
        mThread = ConversationSqlManager.querySessionIdForBySessionId(mUsername);//通过sessionId找会话id
        return mThread;
    }

    public long getThread() {
        return mThread;
    }

    public int increaseCount() {
        if(isLimitCount()) {
            return 0;
        }
        mMsgCount += 18;
        if(mMsgCount <= mTotalCount) {
            return 18;
        }
        return mTotalCount % 18;
    }

    public boolean isLimitCount() {
        return mTotalCount < mMsgCount;
    }
    @Override
    protected void notifyChange() {
        this.mTotalCount = IMessageSqlManager.qureyIMCountForSession(mThread);
        // 初始化一个空的数据列表
        setCursor(IMessageSqlManager.queryIMessageCursor(mThread , mMsgCount));
        super.notifyDataSetChanged();
    }

    @Override
    protected void initCursor() {
        // 初始化一个空的数据列表
        if(mThread > 0) {
            notifyChange();
            return ;
        }
        setCursor(IMessageSqlManager.getNullCursor());
    }

    @Override
    protected ECMessage getItem(ECMessage ecMessage, Cursor cursor) {
        return IMessageSqlManager.packageMessage(cursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ECMessage item = getItem(position);
        Log.i("11111111",item.getBody()+"");

        if(item == null) {
            return null;
        }
        boolean showTimer = false;
        if(position == 0) {
            showTimer = true;
        }
        if(position != 0) {
            ECMessage previousItem = getItem(position - 1);
            if(mShowTimePosition.contains(item.getMsgId())
                    || (item.getMsgTime() - previousItem.getMsgTime() >= 180000L)) {
                showTimer = true;

            }
        }
        
        //更改为已读状态
        IMessageSqlManager.updateMsgReadStatus(item.getMsgId(), true);

        int messageType = ChattingsRowUtils.getChattingMessageType(item.getType());
        BaseChattingRow chattingRow = getBaseChattingRow(messageType, item.getDirection() == ECMessage.Direction.SEND);
        View chatView = chattingRow.buildChatView(LayoutInflater.from(mContext), convertView);
        BaseHolder baseHolder = (BaseHolder) chatView.getTag();

        if(showTimer) {
            baseHolder.getChattingTime().setVisibility(View.VISIBLE);
            baseHolder.getChattingTime().setBackgroundResource(R.drawable.chat_tips_bg);
            baseHolder.getChattingTime().setText(DateUtil.getDateString(item.getMsgTime(), DateUtil.SHOW_TYPE_CALL_LOG).trim());
            baseHolder.getChattingTime().setTextColor(mChatNameColor[0]);
            baseHolder.getChattingTime().setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
        } else {
            baseHolder.getChattingTime().setVisibility(View.GONE);
            baseHolder.getChattingTime().setShadowLayer(0.0F, 0.0F, 0.0F, 0);
            baseHolder.getChattingTime().setBackgroundResource(0);
        }
        
        chattingRow.buildChattingBaseData(mContext, baseHolder, item, position);

        if(baseHolder.getChattingUser() != null && baseHolder.getChattingUser().getVisibility() == View.VISIBLE) {
            baseHolder.getChattingUser().setTextColor(mChatNameColor[1]);
            baseHolder.getChattingUser().setShadowLayer(0.0F, 0.0F, 0.0F, 0);
        }





        return chatView;
    }

    /**
     * 消息类型数
     */
    @Override
    public int getViewTypeCount() {
        return ChattingRowType.values().length;
    }

    /**
     * 返回消息的类型ID
     */
    @Override
    public int getItemViewType(int position) {
        ECMessage message = getItem(position);
        return getBaseChattingRow(ChattingsRowUtils.getChattingMessageType(message.getType()),message.getDirection() == ECMessage.Direction.SEND).getChatViewType();
    }

    public void checkTimeShower() {
        if(getCount() > 0) {
            ECMessage item = getItem(0);
            if(item != null) {
                mShowTimePosition.add(item.getMsgId());
            }
        }
    }

    /**
     * 初始化不同的聊天Item View
     */
    void initRowItems() {
        mRowItems.put(Integer.valueOf(1), new ImageRxRow(1));
        mRowItems.put(Integer.valueOf(2), new ImageTxRow(2));
        mRowItems.put(Integer.valueOf(3), new FileRxRow(3));
        mRowItems.put(Integer.valueOf(4), new FileTxRow(4));
        mRowItems.put(Integer.valueOf(5), new VoiceRxRow(5));
        mRowItems.put(Integer.valueOf(6), new VoiceTxRow(6));
        mRowItems.put(Integer.valueOf(7), new DescriptionRxRow(7,contactsType));
        mRowItems.put(Integer.valueOf(8), new DescriptionTxRow(8));
      //  mRowItems.put(Integer.valueOf(9), new ChattingSystemRow(9));
        mRowItems.put(Integer.valueOf(10), new DescriptionTxRow(8));
    }

    /**
     * 根据消息类型返回相对应的消息Item
     * @param rowType
     * @param isSend
     * @return
     */
    public BaseChattingRow getBaseChattingRow(int rowType , boolean isSend) {
        StringBuilder builder = new StringBuilder("C").append(rowType);

        if(isSend) {
            builder.append("T");
        } else {
            builder.append("R");
        }

        LogUtil.d("ChattingListAdapter", "builder.toString() = " + builder.toString());
        ChattingRowType fromValue = ChattingRowType.fromValue(builder.toString());
        LogUtil.d("ChattingListAdapter", "fromValue = " + fromValue);
        IChattingRow iChattingRow = mRowItems.get(fromValue.getId().intValue());
        return (BaseChattingRow) iChattingRow;
    }


    /**
     * 当前语音播放的位置
     * @param position
     */
    public void setVoicePosition(int position) {
        mVoicePosition = position;
    }

    /**
     * @return the mOnClickListener
     */
    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    /**
     *
     */
    public void onPause() {
        mVoicePosition = -1;
        MediaPlayTools.getInstance().stop();
        IMessageSqlManager.unregisterMsgObserver(this);
    }

    public void onResume() {
    	IMessageSqlManager.registerMsgObserver(this);
        super.notifyDataSetChanged();
    }

    /**
     *
     */
    public void onDestory() {
        ImageLoader.getInstance().clearMemoryCache();
        if(mShowTimePosition != null) {
            mShowTimePosition.clear();
            mShowTimePosition = null;
        }
        if(mRowItems != null) {
            mRowItems.clear();
            mRowItems = null;
        }
        mOnClickListener = null;
    }
}
