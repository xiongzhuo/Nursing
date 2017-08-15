package com.deya.hospital.vo;

import java.util.List;

public class ProvinceVo {
	public List<CityVo> getCityes() {
		return cityes;
	}
	public void setCityes(List<CityVo> cityes) {
		this.cityes = cityes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private  List<CityVo> cityes;
	private String id;
	private String name;
}
