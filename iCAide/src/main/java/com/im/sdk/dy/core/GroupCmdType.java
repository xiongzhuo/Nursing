package com.im.sdk.dy.core;


/** 
 * 群组操作枚举
 * @author  yung
 * @date 创建时间：2015年12月19日 下午2:05:50 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public enum  GroupCmdType {
	/**系统邀请加入默认群组*/
	SYSINVITEJOIN("SYSINVITEJOIN",0),
	/**邀请加入群组*/
	INVITEJOIN("INVITEJOIN",1);
	
	
	int code;
    String name;
	
	private GroupCmdType(String _name,int _code){
		this.code=_code;
		this.name=_name;
	}
	
	public int code(){
		return code;
	}
	
	public static GroupCmdType nxModel(int code){
		GroupCmdType model=INVITEJOIN;
		for (GroupCmdType m:GroupCmdType.values()) {
			if(m.code()==code){
				model=m;
				break;
			}
		}
		return model;
	}
}
