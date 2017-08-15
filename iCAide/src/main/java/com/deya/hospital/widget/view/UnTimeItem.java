package com.deya.hospital.widget.view;

import com.deya.acaide.R;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyTextWatcher;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UnTimeItem extends LinearLayout implements OnClickListener  {
	private View view;
	private Context mcontext;
	RelativeLayout lay_left;
	LinearLayout lay_button_left;
	TextView text_name, text_counts;// , text_sl;
	EditText edt_name;
	DySpinner sp_sl;
	private Resources res;
	
	String[] letters=new String[]{
			"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
	};
	
	public UnTimeItem(Context context) {
		super(context);
		init(context);
	}

	public UnTimeItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
	}

	private void init(Context context) {
		this.mcontext = context;
		view = LayoutInflater.from(context)
				.inflate(R.layout.view_untime_item, null);
		text_name=(TextView)view.findViewById(R.id.text_name);
		text_counts=(TextView)view.findViewById(R.id.text_counts);
		
		lay_left=(RelativeLayout)view.findViewById(R.id.lay_left);
		lay_button_left=(LinearLayout)view.findViewById(R.id.lay_button_left);
		
		edt_name=(EditText)view.findViewById(R.id.edt_name);
		
		sp_sl=(DySpinner)view.findViewById(R.id.sp_sl);
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		res=getResources();
//		float size=res.getDimension(R.dimen.untime_item_textsize);
//		
//	    int textsize=	AbViewUtil.sp2px(context, size);
//		sp_sl.setTextSize(textsize);
//		text_name.setTextSize(textsize);
//		text_counts.setTextSize(textsize);
//		edt_name.setTextSize(textsize);
		
		this.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}
	MyTextWatcher textWatcher;
	public void initData( int position ,String name,String num,String edName,String spText,BaseAdapter adapter){
		text_name.setText(name);
		text_counts.setText("("
				+ num+ ")");
		sp_sl.SetText(spText);
		sp_sl.setAdapter(position,adapter);
		DebugUtil.debug("untimeletters", "position>>"+position+"  let>>"+letters[position]+" name>"+name);
		
		String reg = "[A-Z]";
		if(null!=edName&&!edName.matches(reg)){
			edt_name.setText(edName);
		}
		
		edt_name.setHint(res.getString(R.string.untime_hit_name));
		if(!name.matches(reg)){
			edt_name.setText(name);
		}
				
		
		
			 textWatcher=new MyTextWatcher();
			 if(edt_name.getText().length()>0){
				 edt_name.setSelection(edt_name.getText().length());//set cursor to the end  
			 }
			edt_name.addTextChangedListener(textWatcher);
			textWatcher.setData(position);
			textWatcher.setOnChange(new MyTextWatcher.TextChangeLinstener() {
				
				@Override
				public void setText(String text, int index) {
					// TODO Auto-generated method stub
					if(text.length()>0){
						DebugUtil.debug("untimeitem", "position>>"+index+"  name>>"+text);
						text_name.setText(text);
						listener.OnEditChange(index, text);
					}
				}
			}); 
			
			edt_name.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						
					}
				}
			});
			
			
			sp_sl.setOnSpinnerClik(new DySpinner.DySpinnerListener() {
				
				@Override
				public String OnItemClick(int position, int index,int index2) {
					// TODO Auto-generated method stub
					return listener.OnSpinnerChange(position, index);
				}
			});
	}
	
	private UnTimeItemListener listener;
	
	public void setOnItemListener(UnTimeItemListener listener){
		this.listener=listener;
	}
	
	public interface UnTimeItemListener{
		public void OnEditChange(int position,String text);
		public String OnSpinnerChange(int position,int index);
	}

	@Override
	public void onClick(View v) {
		
	}

	public void clear() {
		// TODO Auto-generated method stub
		edt_name.setText("");
		if(null!=textWatcher)
		textWatcher.setData(0);
	}
}
