package com.deya.hospital.util;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.TipsVo;

/**
 * 
 * @description 软文的viewflipper
 * @version 1.0
 * @author 孙鹏
 * @date 2015年9月9日 下午7:38:13
 */
public class SoftarticleViewFlipper extends CustomViewFlipper {
  private Context context;
  LayoutInflater inflater;

  public SoftarticleViewFlipper(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    inflater=LayoutInflater.from(context);
  }

  public void setData(final List<TipsVo> tipsList) {
    removeAllViews();
    for (int postition = 0; postition < tipsList.size(); postition++) {
      final int pos = postition;
      View convertView = inflater.inflate(R.layout.layout_softarticle_itemmodel, null);
      TextView tx_article_title = (TextView) convertView.findViewById(R.id.tx_article_title);
      tx_article_title.setText(tipsList.get(pos).getContent());
//      convertView.setOnClickListener(new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          if (mlist.get(pos).getLink().trim().length() > 0) {
//            Intent intent = new Intent(context, HasTitleWebViewActivity.class);
//            intent.putExtra("url", mlist.get(pos).getLink().toString());
//            intent.putExtra("type", " ");
//            // Intent intent_message = new Intent(context,
//            // MessageCenterActivity.class);
//            Activity activity = (Activity) context;
//            // activity.startActivity(intent_message);
//            activity.startActivity(intent);
//          }
//        }
//      });
      addView(convertView);
    }
    setFlipInterval(3000);
    if (tipsList.size() > 1) {
      startFlipping();
    } else {
      stopFlipping();
    }
  }
}
