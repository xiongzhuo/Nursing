package com.im.sdk.dy.core;
/** 
 * 系统消息枚举
 * @author  yung
 * @date 创建时间：2015年12月26日 上午10:47:12 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public enum SysMessgeType {
	/**系统消息*/
	SYSMESSAGE("SYSMESSAGE",0),
	/**感控助手*/
	SYSASS("SYSASS",1);
	int code;
    String name;
	
	private SysMessgeType(String _name,int _code){
		this.code=_code;
		this.name=_name;
	}
}
