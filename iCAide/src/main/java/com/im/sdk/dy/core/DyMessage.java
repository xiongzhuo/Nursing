package com.im.sdk.dy.core;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * 第三方自定义附加类
 * @author 段传荣
 * @Description:
 *
 */
public class DyMessage  implements Serializable{

	/**自定义消息内容前缀*/
	public static String IM_MYDATA="DY=";
	
	public DyMessage(){
		
	}
	public DyMessage(MessageType messageType,String value){
		this.messageType = messageType.ordinal();
		this.value=value;
	}
	private String value;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private int messageType;
	

	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public String toJson(){
		return "{\"t\":"+messageType+",\"v\":"+value+"}";
	}
	
	public String toMessgeUserData(){
		return IM_MYDATA+"{\"t\":"+messageType+",\"v\":"+value+"}";
	}
	
	public DyMessage(String msg){
		if (null != msg && msg.length() > 0
				&& msg.contains(DyMessage.IM_MYDATA)) {
			int start = msg.indexOf(DyMessage.IM_MYDATA);
			String value = msg.substring(start
					+ DyMessage.IM_MYDATA.length());
			if (null != value && !value.equals("")) {
				
					try {
						JSONObject j = new JSONObject(value);
						this.messageType=j.getInt("t");
						this.value=j.getString("v");
						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		}
	}

	
//	public DyMessage toMessage(String msg){
//		Gson gson = new Gson();
//		Type type = new TypeToken<HashMap<String,Object>>() {
//		}.getType();
//
//		HashMap<String,Object> map = gson.fromJson(
//				msg, type);
//		try {
//			this.messageType=Integer.parseInt(map.get("t").toString());
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		this.value=map.get("v").toString();
//		return this;
//	}
	
	
}
