package com.im.sdk.dy.ui.group;

import java.util.ArrayList;

import com.im.sdk.dy.storage.GroupSqlManager;


/**
 * 联系人逻辑处理
 * Created by yung on 2015/12/18.
 */
public class GroupLogic {

	public static ArrayList<DemoGroup> groupList=new ArrayList<DemoGroup>();
	
    private static GroupLogic sInstance;
    public static GroupLogic getInstance() {
        if (sInstance == null) {
            sInstance = new GroupLogic();
        }
        return sInstance;
    }
    
    public static ArrayList<DemoGroup> getGroups(){
    	groupList= GroupSqlManager.getGroups();
    	return groupList;
    }
    
    public static ArrayList<DemoGroup> getSearchGroups(){
    	if(null!=groupList||groupList.size()==0){
    		groupList= GroupSqlManager.getGroups();
    	}
    	return groupList;
    }
   
}
