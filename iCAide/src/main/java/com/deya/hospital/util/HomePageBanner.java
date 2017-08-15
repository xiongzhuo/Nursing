package com.deya.hospital.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.AdvEntity;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HomePageBanner extends BaseIndicatorBanner<AdvEntity.ListBean, HomePageBanner> {
    private DisplayImageOptions squareoptions;

    public HomePageBanner(Context context) {
        this(context, null, 0);
        initOptions();
    }

    public HomePageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initOptions();
    }

    public HomePageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initOptions();
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
        try {
            if (list.size() > 0 && list.size() >= position) {
                final AdvEntity.ListBean item = list.get(position);
                tv.setText(item.getTiltle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateItemView(int position) {
        String url = "";

        View inflate = View.inflate(context, R.layout.com_adapter_simple_imge, null);
        ImageView iv = (ImageView) inflate.findViewById(R.id.iv);
        int itemWidth = dm.widthPixels;
        int itemHeight = (int) (itemWidth / 16 * 9);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));


//    if (list.size() > 0 && list.size() >= position && AbStrUtil.isEmpty(list.get(position).getHref_url())) {
//      AdvEntity.ListBean item = list.get(position);
//      iv.setImageResource(item.getDrawable());
//    } else if(list.size() > 0 && list.size() >= position && !AbStrUtil.isEmpty(list.get(position).getHref_url())){
//
//    }
        if (list.size() > 0 && list.size() >= position && !AbStrUtil.isEmpty(list.get(position).getName())) {
            String pUrl = WebUrl.PUBLIC_SERVICE_URL + "/comm/download/" + list.get(position).getName();
            Log.e("banner", pUrl);
            ImageLoader.getInstance().displayImage(pUrl, iv, squareoptions);
        } else {
            AdvEntity.ListBean item = list.get(position);
            iv.setImageResource(item.getDrawable());
        }


        return inflate;
    }

    /**
     * initOptions:【初始化显示图片的options】. <br/>
     * ..<br/>
     */
    private void initOptions() {
        squareoptions =
                new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_img)
                        .showImageForEmptyUri(R.drawable.defult_img)
                        .showImageOnFail(R.drawable.defult_img).resetViewBeforeLoading(true)
                        .cacheOnDisk(true).considerExifParams(true).cacheInMemory(true)
                        .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
    }
}
