package com.deya.hospital.descover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.QuestionVo;

import java.util.List;

public class SortNewListAdapter extends BaseAdapter {
	private final int frontCorlor;
	private Context context;
	private LayoutInflater inflater;
	private List<QuestionVo> list;
	public int drawables[] = { R.drawable.circle_1, R.drawable.circle_2,
			R.drawable.circle_3, R.drawable.circle_4, R.drawable.circle_5,
			R.drawable.circle_6 };
	private int[] wh;

	public SortNewListAdapter(Context context, List<QuestionVo> list) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		wh = AbViewUtil.getDeviceWH(context);
		this.list = list;
		frontCorlor=context.getResources().getColor(R.color.top_color);
	}

	public void setData(List<QuestionVo> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder2 mviewHolder = null;
		if (convertView == null) {
			mviewHolder = new ViewHolder2();
			convertView = inflater.inflate(R.layout.discover_new_list, null);
			mviewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_title);
			mviewHolder.question_type = (TextView) convertView
					.findViewById(R.id.question_type);
			mviewHolder.answerNum = (TextView) convertView
					.findViewById(R.id.answerNum);
			mviewHolder.answerBtn = (TextView) convertView
					.findViewById(R.id.answerBtn);
			mviewHolder.textLay=(RelativeLayout) convertView.findViewById(R.id.textLay);
			mviewHolder.question_asker=(TextView) convertView
			.findViewById(R.id.question_asker);
			mviewHolder.question_type=(TextView) convertView
					.findViewById(R.id.question_type);
			// mviewHolder.preview.setText("阅读:"+list.get(position).getRead_count());

			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}
		
		final QuestionVo qVo=list.get(position);
		mviewHolder.answerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,AnswerQuestionActivity.class);
				intent.putExtra("id", qVo.getQ_id()+"");
				context.startActivity(intent);
			}
		});
		
		int colorIndex = qVo.getQ_type() % 6;
		mviewHolder.question_type.setBackgroundResource(drawables[colorIndex]);
		mviewHolder.answerNum.setText("("+qVo.getAnswer_count()+"个回答)");
		if(qVo.getIntegral()>0){
			String frontText="【悬赏"+qVo.getIntegral()+"橄榄】";
			mviewHolder.title.setText(frontText+qVo.getTitle());
			AbStrUtil.setPiceTextCorlor(frontCorlor,mviewHolder.title,mviewHolder.title.getText().toString(),frontText.length(),0);
		}else{
			mviewHolder.title.setText(qVo.getTitle());
		}
		mviewHolder.question_asker.setText(qVo.getUser_name());
		mviewHolder.question_type.setText(qVo.getType_name());
		setTextView(mviewHolder.title, mviewHolder.answerNum,mviewHolder.textLay);
		return convertView;
	}

	class ViewHolder2 {
		public TextView question_asker;
		public ImageView sort;
		public TextView title, question_type, answerNum;
		public TextView author;
		public TextView hospital;
		public TextView issue;
		public TextView answerBtn;
		public TextView preview;
		RelativeLayout textLay;
	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	public void setTextSpan(String text, TextView tx) {
		int bstart = 0;
		int bend = bstart + 3;
		int fstart = 0;
		int fend = 3;
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(new BackgroundColorSpan(Color.rgb(252, 127, 26)), bstart,
				bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.WHITE), fstart, fend,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.WHITE), fstart, fend,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tx.setText(style);
	}

	public void setTextView(final TextView tx1, final TextView tx2,final RelativeLayout layout) {
		
		 
		
		tx1.post(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				final int with = tx1.getMeasuredWidth() + tx2.getMeasuredWidth()
						+ dp2Px(context, 20);
				final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.FILL_PARENT);
				Layout layout2=tx1.getLayout();
				
				int lineCount=tx1.getLineCount();
				Log.i("Adapter"   ,layout2.getLineWidth(0)+"====="+ layout2.getLineWidth(lineCount-1)+"==="+tx2.getMeasuredWidth());
				if (lineCount == 1 && wh[0] < with) {
					layoutParams.height = tx1.getMeasuredHeight() + dp2Px(context, 30);
					layoutParams.width =ViewGroup.LayoutParams.FILL_PARENT;
					layout.setLayoutParams(layoutParams);
					Log.i("Adapter", tx1.getLineCount() + "------"+ tx1.getText()+"执行1");
				}else if(lineCount > 1&&layout2.getLineWidth(0)-layout2.getLineWidth(lineCount-1)<(tx2.getMeasuredWidth()-dp2Px(context, 15))){
					layoutParams.height = tx1.getMeasuredHeight() + dp2Px(context, 30);
					layoutParams.width =ViewGroup.LayoutParams.FILL_PARENT;
					Log.i("Adapter", tx1.getLineCount() + "------"+ tx1.getText()+"执行2");
					layout.setLayoutParams(layoutParams);
				}else{
					layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
					layoutParams.width =ViewGroup.LayoutParams.FILL_PARENT;
					layout.setLayoutParams(layoutParams);
					Log.i("Adapter", tx1.getLineCount() + "------"+ tx1.getText()+"执行3");
				}
				Log.i("Adapter", tx1.getLineCount() + "------"+ tx1.getText());
			}
			
		});
		
		

	}
	
	public static String ToDBC(String input) {  
		   char[] c = input.toCharArray();  
		   for (int i = 0; i< c.length; i++) {  
		       if (c[i] == 12288) {  
		         c[i] = (char) 32;  
		         continue;  
		       }if (c[i]> 65280&& c[i]< 65375)  
		          c[i] = (char) (c[i] - 65248);  
		       }  
		   return new String(c);  
		} 
}
