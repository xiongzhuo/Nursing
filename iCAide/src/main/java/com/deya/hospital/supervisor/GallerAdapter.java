package com.deya.hospital.supervisor;

import com.deya.acaide.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GallerAdapter extends BaseAdapter {
	LayoutInflater infalte;
	public GallerAdapter(Context context) {
		infalte=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=infalte.inflate(R.layout.adapter_item_button, null);

		return convertView;
	}

	public class ViewHolder {

	}
}
