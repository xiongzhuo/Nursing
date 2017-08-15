/*
 *  
 *
 *  
 *  
 *
 *   
 *
 *  
 * 
 *  IM VIEW
 */
package com.im.sdk.dy.storage;

/**
 * @author yung•
 * @date 2015-12-13
 * @version 4.0
 */
public class MessageObservable extends IMObservable<OnMessageChange> {

	/**
	 * 分发数据库改变通知
	 * @param session
	 */
	 public void notifyChanged(String session) {
	        synchronized(mObservers) {
	            for (int i = mObservers.size() - 1; i >= 0; i--) {
	            	
	            	if(mObservers.get(i)!=null){
	                mObservers.get(i).onChanged(session);
	            	}
	            }
	        }
	    }
}
