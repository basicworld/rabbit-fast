package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.SysDeptUser;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)

public class ISysDeptUserServiceTest {
	@Autowired
	ISysDeptUserService deptUserService;
	private SysDeptUser deptUser;

	@Before
	public void before() {
		SysDeptUser item = new SysDeptUser();
		item.setDeptId(new Long(1));
		item.setUserId(new Long(1));
		deptUserService.insertSelective(item);
		this.deptUser = item;
	}

	@After
	public void after() {
		if (null != this.deptUser && null != this.deptUser.getId()) {
			deptUserService.deleteByPrimaryKey(this.deptUser.getId());
		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.deptUser);
		TestCase.assertNotNull(this.deptUser.getId());

		this.deptUser.setUserId(new Long(2));
		deptUserService.updateSelective(this.deptUser);

		SysDeptUser existItem = deptUserService.selectByPrimaryKey(this.deptUser.getId());
		TestCase.assertNotNull(existItem);

		TestCase.assertEquals(new Long(2), existItem.getUserId());

	}
}
