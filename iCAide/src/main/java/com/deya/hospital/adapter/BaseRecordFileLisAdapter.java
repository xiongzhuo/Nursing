package com.deya.hospital.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.FileActions;
import com.deya.hospital.util.Player;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.dbdata.Attachments;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class BaseRecordFileLisAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Attachments> list;
    private DisplayImageOptions optionsSquare;
    JSONArray jarr;
    Context mContext;
    FileActions listener;


    public BaseRecordFileLisAdapter(Context context, List<Attachments> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        mContext = context;


    }

    public void setData(List<Attachments> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder2 mviewHolder;
        if (convertView == null) {
            mviewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.filelist_item, null);
            mviewHolder.title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            mviewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            mviewHolder.size = (TextView) convertView.findViewById(R.id.size);
            mviewHolder.recorderlayout = (LinearLayout) convertView.findViewById(R.id.recorderlayout);
            mviewHolder.recordimg = (ImageView) convertView.findViewById(R.id.recordimg);
            mviewHolder.delet = (ImageView) convertView.findViewById(R.id.delet);
            convertView.setTag(mviewHolder);
        } else {
            mviewHolder = (ViewHolder2) convertView.getTag();
        }

        Attachments attachments = list.get(position);
        mviewHolder.recordimg.setBackgroundResource(R.drawable.play);
        final AnimationDrawable drawable = (AnimationDrawable) mviewHolder.recordimg
                .getBackground();
        drawable.selectDrawable(2);

        if (attachments.getFile_type().equals("2")) {
            mviewHolder.recorderlayout.setVisibility(View.VISIBLE);
            mviewHolder.img.setVisibility(View.GONE);
            Log.i("111111加载成功", list.get(position).getFile_name() + "");

            String timeStr = list.get(position).getTime();
            if (timeStr.length() < 3) {
                timeStr = timeStr + "000";
            }
            String time3 = timeStr.substring(0, 3);
            mviewHolder.title.setText(time3 + "''");
            String url = "";
            if (attachments.getState().equals("2")) {
                url = WebUrl.FILE_LOAD_URL + attachments.getFile_name();
            } else {
                url = "file://" + list.get(position).getFile_name();
            }

            //  File file=new File("file://" + list.get(position).getFile_name());
            final String finalUrl = url;
            final Player player = new Player(drawable);
            mviewHolder.recorderlayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (player.isPlaying()) {
                        player.pause();
                    } else {
                        player.playUrl(finalUrl);
                    }

                }
            });

        }

        if (!AbStrUtil.isEmpty(list.get(position).getFile_type())) {
            int type = Integer.parseInt(list.get(position).getFile_type());
            final File file = new File(list.get(position).getFile_name());
            Log.i("11111file.exists()", file.exists() + "");
            if (file.exists()) {
                String size = FormetFileSize(file.length());
                mviewHolder.size.setText(size);
            }

            mviewHolder.delet.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onDeletFile(position);

                }
            });
            convertView.setOnClickListener(null);
        } else {
            mviewHolder.title.setText("");
        }


        return convertView;
    }

    public void setOnDeletListener(FileActions listener){
        this.listener=listener;

    }
    class ViewHolder2 {
        public TextView title;
        public ImageView img, recordimg, delet;

        public TextView size;
        public LinearLayout recorderlayout;
    }

    public int dp2Px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }

    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }



}
