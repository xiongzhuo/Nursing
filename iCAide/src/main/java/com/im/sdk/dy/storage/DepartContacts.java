package com.im.sdk.dy.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DepartContacts implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 298127389121L;
	int total;
	List<FriendsVo> departmentFriends = new ArrayList<DepartContacts.FriendsVo>();
	String my_department;
	String department;
	String department_id;

	public class FriendsVo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2103712973L;
		String friend_id;

		public String getFriend_id() {
			return friend_id;
		}

		public void setFriend_id(String friend_id) {
			this.friend_id = friend_id;
		}

	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<FriendsVo> getDepartmentFriends() {
		return departmentFriends;
	}

	public void setDepartmentFriends(List<FriendsVo> departmentFriends) {
		this.departmentFriends = departmentFriends;
	}

	public String getMy_department() {
		return my_department;
	}

	public void setMy_department(String my_department) {
		this.my_department = my_department;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

}
