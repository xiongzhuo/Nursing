package com.im.sdk.dy.core;

/** 
 * 自定义消息类型枚举
 * @author  yung
 * @date 创建时间：2015年12月19日 下午2:05:50 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public enum  MessageType {
	/**好友处理类型*/
	FRIEND("FRIEND",0),
	/**系统消息类型*/
	SYSTEM("SYSTEM",1),
	/**群组操作消息类型*/
	GROUP("GROUP",2);
	int code;
    String name;
	
	private MessageType(String _name,int _code){
		this.code=_code;
		this.name=_name;
	}
}
