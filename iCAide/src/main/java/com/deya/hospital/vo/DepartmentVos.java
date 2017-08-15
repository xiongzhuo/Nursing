package com.deya.hospital.vo;


@SuppressWarnings("serial")
public class DepartmentVos extends DepartmentVo {
	
	public DepartmentVos(){}
	
	public DepartmentVos(DepartmentVo departmentVo){
		toS(departmentVo);
	}
	
	public void toS(DepartmentVo departmentVo){
		this.setDepartment(departmentVo.getDepartment());
		this.setEmployee(departmentVo.getEmployee());
		this.setHospital(departmentVo.getHospital());
		this.setLocaloffice(departmentVo.getLocaloffice());
		this.setId(departmentVo.getId());
		this.setName(departmentVo.getName());
		this.setOffice(departmentVo.getOffice());
		this.setOrders(departmentVo.getOrders());
		this.setRegister_time(departmentVo.getRegister_time());
		this.setRemark(departmentVo.getRemark());
		this.setParent(departmentVo.getParent());
		this.setChilds(departmentVo.getChilds());
		this.setChooseNum(departmentVo.getChooseNum());
		this.setSelected(departmentVo.isSelected());
	}
}
