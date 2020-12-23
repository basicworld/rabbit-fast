package com.rabbit.system.service;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.common.util.RSAUtils;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ISysUserServiceTest {

	@Value("${rsa.publicKey}")
	private String rsaPublicKey;

	@Value("${rsa.privateKey}")
	private String rsaPrivateKey;

	@Autowired
	ISysUserService userService;
	private SysUser user;

	@Before
	public void before() {
		SysUser item = new SysUser();
		item.setName("name");
		item.setSalt("salt");
		item.setPassword("password");
		item.setAllAccounts(new ArrayList<SysAccount>());
		userService.insertSelective(item);

		this.user = item;
	}

	@After
	public void after() {
		if (null != this.user && null != this.user.getId()) {
			userService.deleteByPrimaryKey(this.user.getId());
		}
	}

	@Test
	public void rsatest() {
		String rawPassword = "Abcd1234";
		String encodePassword = RSAUtils.encrypt(rsaPublicKey, rawPassword);

		String decodePassword = RSAUtils.decrypt(rsaPrivateKey, encodePassword);
		System.out.println("rawPassword=" + rawPassword);
		System.out.println("encodePassword=" + encodePassword);
		TestCase.assertEquals(rawPassword, decodePassword);
	}

	@Test
	public void createUser() {
		SysUserDTO userDTO = new SysUserDTO();
		userDTO.setUsername("admin");

		// 初始化密码设置
		// 进行加密
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePassword = encoder.encode("123456");
		SysUser user = userService.dto2User(userDTO);
		user.setPassword(encodePassword);
		System.out.println("encodePassword=" + encodePassword);
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.user);
		TestCase.assertNotNull(this.user.getId());

		this.user.setName("new name");
		userService.updateSelective(this.user);

		SysUser existItem = userService.selectByPrimaryKey(this.user.getId());
		TestCase.assertNotNull(existItem);
		TestCase.assertEquals("new name", existItem.getName());

	}
}
