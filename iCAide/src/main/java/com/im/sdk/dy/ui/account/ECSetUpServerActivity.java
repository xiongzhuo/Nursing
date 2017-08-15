package com.im.sdk.dy.ui.account;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.deya.acaide.R;
import com.im.sdk.dy.ui.ECSuperActivity;

public class ECSetUpServerActivity extends ECSuperActivity implements OnClickListener{

	private EditText etConnect;
	private EditText etConnectPort;
	private EditText etLVS;
	private EditText etLVSPort;
	private EditText etFile;
	private EditText etFilePort;
	private EditText etAppid;
	private EditText etAppToken;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.im_setup_server_layout;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style,
                R.color.white, null,
                getString(R.string.save),
                getString(R.string.setup_server), null, this);
		
		initViews();
	}

	 private void initViews() {

		 
		 etConnect = (EditText) findViewById(R.id.setup_connect);
		 etConnectPort = (EditText) findViewById(R.id.setup_connect_port);
		 etLVS = (EditText) findViewById(R.id.setup_lvs);
		 etLVSPort = (EditText) findViewById(R.id.setup_lvs_port);
		 etFile = (EditText) findViewById(R.id.setup_fileserver);
		 etFilePort = (EditText) findViewById(R.id.setup_fileserver_port);
		 etAppid = (EditText) findViewById(R.id.setup_appid);
		 etAppToken = (EditText) findViewById(R.id.setup_apptoken);
		 
	}

	@Override
	    public void onClick(View v) {
	        switch (v.getId()) {
	            case R.id.btn_left:
	            case R.id.btn_text_left:
	                hideSoftKeyboard();
	                finish();
	                break;
	            case R.id.text_right:
	            	
	            String connect=etConnect.getText().toString().trim();
	            String connectPort=etConnectPort.getText().toString().trim();
	            String lvs=etLVS.getText().toString().trim();
	            String lvsport=etLVSPort.getText().toString().trim();
	            String file=etFile.getText().toString().trim();
	            String fileport=etFilePort.getText().toString().trim();
	            String appkey=etAppid.getText().toString().trim();
	            String apptoken=etAppToken.getText().toString().trim();
	            
	            	finish();

	                break;
	            default:
	                break;
	        }
	    }

}
