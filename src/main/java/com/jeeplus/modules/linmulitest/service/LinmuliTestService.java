/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.linmulitest.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.linmulitest.entity.LinmuliTest;
import com.jeeplus.modules.linmulitest.dao.LinmuliTestDao;

/**
 * 测试功能Service
 * @author slowdiver
 * @version 2016-04-27
 */
@Service
@Transactional(readOnly = true)
public class LinmuliTestService extends CrudService<LinmuliTestDao, LinmuliTest> {

	public LinmuliTest get(String id) {
		return super.get(id);
	}
	
	public List<LinmuliTest> findList(LinmuliTest linmuliTest) {
		return super.findList(linmuliTest);
	}
	
	public Page<LinmuliTest> findPage(Page<LinmuliTest> page, LinmuliTest linmuliTest) {
		return super.findPage(page, linmuliTest);
	}
	
	@Transactional(readOnly = false)
	public void save(LinmuliTest linmuliTest) {
		super.save(linmuliTest);
	}
	
	@Transactional(readOnly = false)
	public void delete(LinmuliTest linmuliTest) {
		super.delete(linmuliTest);
	}
	
}