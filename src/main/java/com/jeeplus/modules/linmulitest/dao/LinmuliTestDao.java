/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.linmulitest.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.linmulitest.entity.LinmuliTest;

/**
 * 测试功能DAO接口
 * @author slowdiver
 * @version 2016-04-27
 */
@MyBatisDao
public interface LinmuliTestDao extends CrudDao<LinmuliTest> {
	
}