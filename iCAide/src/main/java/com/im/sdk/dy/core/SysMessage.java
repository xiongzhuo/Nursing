package com.im.sdk.dy.core;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

/**
 * 系统消息
 */
public class SysMessage implements Serializable {
	

	int id;
	int type;
	String title;
	String img;
	String dec;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public JSONObject toJson() {
		JSONObject j = new JSONObject();

		try {
			j.put("id", id);
			j.put("type", type);
			j.put("title", title);
			j.put("img", img);
			j.put("dec", dec);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return j;
	}

//	public SysMessage toSysMessage(JSONObject jsonObject){
//		jsonObject.getInt("");
//	}
}
