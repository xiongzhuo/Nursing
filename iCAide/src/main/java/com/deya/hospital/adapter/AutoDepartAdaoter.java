package com.deya.hospital.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.DepartmentVo;

import java.util.ArrayList;
import java.util.List;

public class AutoDepartAdaoter extends BaseAdapter implements Filterable {
	private ArrayFilter mFilter;
	private List<DepartmentVo> mList;
	private Context context;
	private ArrayList<DepartmentVo> mUnfilteredData;
	
	public AutoDepartAdaoter(List<DepartmentVo> mList, Context context) {
		this.mList = mList;
		this.context = context;
	}

	@Override
	public int getCount() {
		
		return mList==null ? 0:mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if(convertView==null){
			convertView = View.inflate(context, R.layout.list_item, null);
			
			holder = new ViewHolder();
			holder.listtext = (TextView) convertView.findViewById(R.id.listtext);
			
			
			convertView.setTag(holder);
		}else{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		;
		holder.listtext.setText(mList.get(position).getName());
//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//             Log.i("111111", pc.getName()+pc.getId());
//				
//			}
//		});
		
		return convertView;
	}
	
	static class ViewHolder{
		public TextView listtext;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<DepartmentVo>(mList);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<DepartmentVo> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<DepartmentVo> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<DepartmentVo> newValues = new ArrayList<DepartmentVo>(count);

                for (int i = 0; i < count; i++) {
                	DepartmentVo pc = unfilteredValues.get(i);
                    if (pc != null) {
                        
                    	if(pc.getName()!=null && pc.getName().startsWith(prefixString)){
                    		
                    		newValues.add(pc);
                    	}else if(pc.getName()!=null && pc.getName().startsWith(prefixString)){
                    		newValues.add(pc);
                    	}
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
		}
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			 //noinspection unchecked
            mList = (List<DepartmentVo>) results.values;//
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
		}
		
	}
}