package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "department_db4")
public class DepartmentVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 00000023211L;
	@Id(column = "_id")
	private int _id;
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	private String office;
	private String register_time;
	private String remark;
	private String department;
	private String name;
	private String id;
	private String orders;
	private String employee;
	private String localoffice;
	private String hospital;
	private String parent;
	private String levels="";
	boolean isSelected;
	private int chooseNum;
	
	public int getChooseNum() {
		return chooseNum;
	}

	public void setChooseNum(int chooseNum) {
		this.chooseNum = chooseNum;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	List<ChildsVo> childs=new ArrayList<ChildsVo>();
	
	public List<ChildsVo> getChilds() {
		return childs;
	}

	public void setChilds(List<ChildsVo> childs) {
		this.childs = childs;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	//被选中
//	private boolean isSelected;
//	public boolean isSelected() {
//		return isSelected;
//	}
//
//	public void setSelected(boolean isSelected) {
//		this.isSelected = isSelected;
//	}
	
	
	
}
