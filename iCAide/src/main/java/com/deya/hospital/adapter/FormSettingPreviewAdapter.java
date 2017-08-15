package com.deya.hospital.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.form.FormSettingPreViewActivity;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;

import java.util.List;

public class FormSettingPreviewAdapter extends BaseExpandableListAdapter {
	public int colors[] = { R.color.line1_corlor, R.color.line2_corlor,
			R.color.line3_corlor, R.color.line4_corlor, R.color.line5_corlor,
			R.color.line6_corlor };
	public int drawables[] = { R.drawable.circle_1,  R.drawable.circle_2,
			R.drawable.circle_3,R.drawable.circle_4, R.drawable.circle_5,
			R.drawable.circle_6 };
	
	private Context context;
	private LayoutInflater inflater;
	List<FormDetailListVo> list;
	FormSettingPreViewActivity activity;

	public FormSettingPreviewAdapter(Context context,
			List<FormDetailListVo> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		activity = (FormSettingPreViewActivity) context;

	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).getSub_items().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder viewholder;
		if (null == convertView) {
			viewholder = new GroupViewHolder();
			convertView = inflater.inflate(R.layout.adapter_preview_headview,
					null);
			viewholder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewholder);
		} else {
			viewholder = (GroupViewHolder) convertView.getTag();
		}
		int colorIndex = groupPosition % 6;
		viewholder.title.setBackgroundResource(colors[colorIndex]);
		viewholder.title.setText(list.get(groupPosition).getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder viewholder;
		if (null == convertView) {
			viewholder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.adapter_preview_childview,
					null);
			viewholder.content = (TextView) convertView
					.findViewById(R.id.title);
			viewholder.numImg = (TextView) convertView
					.findViewById(R.id.numimg);
			viewholder.line=convertView.findViewById(R.id.line);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ChildViewHolder) convertView.getTag();
		}
		int colorIndex = groupPosition % 6;
		
		FormDetailVo fv = list.get(groupPosition).getSub_items()
				.get(childPosition);
		viewholder.content.setText(fv.getDescribes());
		viewholder.numImg.setText(childPosition+1+"");
		viewholder.numImg.setBackgroundResource(drawables[colorIndex]);
		viewholder.line.setBackgroundResource(colors[colorIndex]);
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public class GroupViewHolder {
		TextView title;

	}

	public class ChildViewHolder {
		TextView content;
		TextView numImg;
		View line;

	}

	public Bitmap setDrawabel(int color) {
		// 初始化画布
		Bitmap bitmap = Bitmap.createBitmap(50,50, Bitmap.Config.ARGB_8888);// 配置
		Canvas canvas = new Canvas(bitmap); // 画布 52
		Paint paint = new Paint();
		paint.setColor(color);

		// 画一个圆形
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(10);
		canvas.drawCircle(30, 30, 50, paint);
		return bitmap;
	}
}
