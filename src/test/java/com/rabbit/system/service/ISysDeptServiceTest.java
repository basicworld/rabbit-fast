package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.SysDept;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)

public class ISysDeptServiceTest {
	@Autowired
	ISysDeptService deptService;
	private SysDept dept;

	@Before
	public void beforeEach() {
		SysDept item = new SysDept();
		item.setParentId(new Long(0));
		item.setFullname("fullname");
		item.setName("name");
		item.setOrgcode("232");
		deptService.insertSelective(item);
		this.dept = item;
	}

	@After
	public void afterEach() {
		if (null != this.dept && null != this.dept.getId()) {
			deptService.deleteByPrimaryKey(this.dept.getId());
		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.dept);
		TestCase.assertNotNull(this.dept.getId());

		this.dept.setFullname("new fullname");
		deptService.updateSelective(this.dept);

		SysDept existItem = deptService.selectByPrimaryKey(this.dept.getId());
		TestCase.assertNotNull(existItem);
		TestCase.assertEquals("new fullname", existItem.getFullname());

	}
}
