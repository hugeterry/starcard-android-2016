package com.xingcard.shequ.bean;

import cn.bmob.v3.BmobObject;

public class Lost extends BmobObject {

	private String title;// ����
	private String describe;// ����
	private String name;// �ǳ�

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
