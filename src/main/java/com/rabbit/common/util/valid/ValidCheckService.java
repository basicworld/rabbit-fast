package com.rabbit.common.util.valid;

/**
 * bean校验接口类
 * 
 * @author wlfei
 *
 * @param <T>
 */
public interface ValidCheckService<T> {
	/**
	 * 新增前的校验
	 * 
	 * @param t
	 * @return
	 */
	ValidResult validCheckBeforeInsert(T t);

	/**
	 * 更新前的校验
	 * 
	 * @param t
	 * @return
	 */
	ValidResult validCheckBeforeUpdate(T t);

	/**
	 * 删除前的校验
	 * 
	 * @param id
	 * @return
	 */
	ValidResult validCheckBeforeDelete(Long id);
}
