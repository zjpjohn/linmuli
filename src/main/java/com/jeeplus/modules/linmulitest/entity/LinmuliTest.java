/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.linmulitest.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 测试功能Entity
 * @author slowdiver
 * @version 2016-04-27
 */
public class LinmuliTest extends DataEntity<LinmuliTest> {
	
	private static final long serialVersionUID = 1L;
	private String testField;		// 测试字段
	
	public LinmuliTest() {
		super();
	}

	public LinmuliTest(String id){
		super(id);
	}

	@Length(min=0, max=64, message="测试字段长度必须介于 0 和 64 之间")
	@ExcelField(title="测试字段", align=2, sort=7)
	public String getTestField() {
		return testField;
	}

	public void setTestField(String testField) {
		this.testField = testField;
	}
	
}