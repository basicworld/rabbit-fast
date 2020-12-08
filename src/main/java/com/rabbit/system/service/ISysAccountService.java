package com.rabbit.system.service;

import java.util.List;

import com.rabbit.common.util.valid.ValidCheckService;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;

/**
 * 账号service
 * 
 * @author wlfei
 *
 */
public interface ISysAccountService extends BaseService<SysAccount>, ValidCheckService<SysAccount> {
	/**
	 * 根据用户主键获取账号列表
	 * 
	 * @param userId
	 * @return
	 */
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
	 * 为用户更新账号
	 * 
	 * @param user
	 * @return
	 */
	Integer updateByUser(SysUser user);

	/**
	 * 批量删除账号，如果用户所有账号均已删除，则逻辑删除用户
	 * 
	 * @param accountIds
	 * @return
	 */
	Integer deleteByPrimaryKey(Long[] accountIds);
}
