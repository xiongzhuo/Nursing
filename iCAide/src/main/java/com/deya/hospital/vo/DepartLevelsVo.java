package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DepartLevelsVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 023123121L;

	DepartRootVo root=new DepartRootVo();

	List<ChildsVo> childs=new ArrayList<ChildsVo>();
	private int chooseNum;
	private String question_type_id;
	private String question_type_name;
	
	
	public String getQuestion_type_id() {
		return question_type_id;
	}

	public void setQuestion_type_id(String question_type_id) {
		this.question_type_id = question_type_id;
	}

	public String getQuestion_type_name() {
		return question_type_name;
	}

	public void setQuestion_type_name(String question_type_name) {
		this.question_type_name = question_type_name;
	}

	public int getChooseNum() {
		return chooseNum;
	}

	public void setChooseNum(int chooseNum) {
		this.chooseNum = chooseNum;
	}
	public DepartRootVo getRoot() {
		return root;
	}
	public void setRoot(DepartRootVo root) {
		this.root = root;
	}
	public List<ChildsVo> getChilds() {
		return childs;
	}
	public void setChilds(List<ChildsVo> childs) {
		this.childs = childs;
	}
	public class DepartRootVo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 312312311L;
		private String uid = "";
		private String id = "";
		private String register_time = "";
		private String remark = "";
		private String name = "";
		private String state = "";
		private String parent = "";
		private String orders = "";
		private String employee = "";
		private String localoffice = "";
		private String hospital = "";
		public String getUid() {
			return uid;
		}
		public void setUid(String uid) {
			this.uid = uid;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getRegister_time() {
			return register_time;
		}
		public void setRegister_time(String register_time) {
			this.register_time = register_time;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getParent() {
			return parent;
		}
		public void setParent(String parent) {
			this.parent = parent;
		}
		public String getOrders() {
			return orders;
		}
		public void setOrders(String orders) {
			this.orders = orders;
		}
		public String getEmployee() {
			return employee;
		}
		public void setEmployee(String employee) {
			this.employee = employee;
		}
		public String getLocaloffice() {
			return localoffice;
		}
		public void setLocaloffice(String localoffice) {
			this.localoffice = localoffice;
		}
		public String getHospital() {
			return hospital;
		}
		public void setHospital(String hospital) {
			this.hospital = hospital;
		}
		

}
}