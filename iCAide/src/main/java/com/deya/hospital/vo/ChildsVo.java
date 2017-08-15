package com.deya.hospital.vo;


import java.io.Serializable;

public class ChildsVo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 12312445441L;
		String name = "";
		String id = "";
		String parent = "";
		boolean isChoosed;

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public boolean isChoosed() {
			return isChoosed;
		}

		public void setChoosed(boolean isChoosed) {
			this.isChoosed = isChoosed;
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
	}