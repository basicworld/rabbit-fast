package com.rabbit.system.service;

import java.util.List;

import com.rabbit.common.util.valid.ValidCheckService;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.dto.SysUserDTO;

public interface ISysAccountService extends BaseService<SysAccount>, ValidCheckService<SysAccount> {
	List<SysAccount> listByUserId(Long userId);

	/**
	 * 根据删除状态、账号类型、账号 模糊查询查询
	 * 
	 * @param userDTO
	 * @return
	 */
	List<SysAccount> listByUserDTO(SysUserDTO userDTO);

	/**
	 * 根据账号类型、账号 精确查询
	 * 
	 * @param account
	 * @return
	 */
	List<SysAccount> listByCategoryAndOpenCode(SysAccount account);

	/**
	 * 根据用户ID物理删除
	 * 
	 * @param userId
	 * @return
	 */
	Integer deleteByUserId(Long userId);

	/**
	 * 根据账号类型和用户ID等值查询
	 * 
	 * @param account
	 * @return
	 */
	List<SysAccount> listByCategoryAndUserId(SysAccount account);

	/**
	 * 新增或更新前校验：用户ID、账号类型、账号 校验
	 * 
	 * @param account
	 * @return
	 */
	ValidResult validCheckBeforeInsertOrUpdate(SysAccount account);

	/**
	 * 根据用户ID和账号类型精确删除
	 * 
	 * @param account
	 * @return
	 */
	Integer deleteByUserIdAndCategory(SysAccount account);

	/**
	 * 选择性新增或更新：<br>
	 * 如果userid+category+openCode相同，则不操作<br>
	 * ，如果userid+category存在，但openCode不同，则更新openCode <br>
	 * ，如果userid+category不存在，则新增<br>
	 * 逻辑前提：每个用户、每个账号类型只能有一个账号
	 * 
	 * @param account
	 * @return
	 */
	Integer insertOrUpdateSelective(SysAccount account);
}
