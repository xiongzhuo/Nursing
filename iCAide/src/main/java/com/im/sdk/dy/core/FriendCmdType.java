package com.im.sdk.dy.core;


/** 
 * 好友操作枚举
 * @author  yung
 * @date 创建时间：2015年12月19日 下午2:05:50 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public enum  FriendCmdType {
	/**请求好友*/
	REQUEST("REQUEST",0),
	/**接受好友请求*/
	ACCEPT("ACCEPT",1),
	/**拒绝好友请求*/
	REFUSE("REFUSE",2),
	/**删除好友*/
	DEL("DEL",3);
	
	int code;
    String name;
	
	private FriendCmdType(String _name,int _code){
		this.code=_code;
		this.name=_name;
	}
	
	public int code(){
		return code;
	}
	
	public static FriendCmdType nxModel(int code){
		FriendCmdType model=REQUEST;
		for (FriendCmdType m:FriendCmdType.values()) {
			if(m.code()==code){
				model=m;
				break;
			}
		}
		return model;
	}
}
