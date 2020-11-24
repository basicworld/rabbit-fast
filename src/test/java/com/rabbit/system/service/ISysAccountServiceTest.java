package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.SysAccount;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ISysAccountServiceTest {
	@Autowired
	ISysAccountService accountService;

	private SysAccount account;

	@Before
	public void beforeEach() {
		SysAccount account = new SysAccount();
		account.setUserId(new Long(0));
		account.setCategory(1);
		account.setOpenCode("opencode");
		accountService.insertSelective(account);

		this.account = account;
	}

	@After
	public void afterEach() {
		if (null != this.account && null != this.account.getId()) {
			accountService.deleteByPrimaryKey(this.account.getId());
		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.account.getId());
		this.account.setOpenCode("new openCode");
		accountService.updateSelective(this.account);
		SysAccount existItem = accountService.selectByPrimaryKey(this.account.getId());

		TestCase.assertNotNull(existItem);
		TestCase.assertEquals("new openCode", existItem.getOpenCode());
	}

}
