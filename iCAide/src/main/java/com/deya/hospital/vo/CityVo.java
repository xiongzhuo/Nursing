package com.deya.hospital.vo;

import java.util.List;

public class CityVo {
	public List<HospitalInfo> getHospitals() {
		return hospitals;
	}
	public void setHospitals(List<HospitalInfo> hospitals) {
		this.hospitals = hospitals;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private List<HospitalInfo> hospitals;
	private String id;
	private String pid;
	private String zipcode;
	private String name;

}
