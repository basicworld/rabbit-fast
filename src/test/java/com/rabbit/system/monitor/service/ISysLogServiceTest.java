package com.rabbit.system.monitor.service;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.monitor.domain.SysLog;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ISysLogServiceTest {
	@Autowired
	ISysLogService logService;
	private SysLog sysLog;

	@Before
	public void beforeEach() {
		SysLog item = new SysLog();
		item.setIsSuccess(true);
		item.setUserId(new Long(1));
		item.setUserName("username");
		item.setOperTime(new Date());

		logService.insertSelective(item);
		this.sysLog = item;
	}

	@After
	public void afterEach() {
		if (null != this.sysLog && null != this.sysLog.getId()) {
			logService.deleteByPrimaryKey(this.sysLog.getId());
		}
	}

	@Test
	public void insert() {
		TestCase.assertNotNull(this.sysLog);
		TestCase.assertNotNull(this.sysLog.getId());
	}
}
