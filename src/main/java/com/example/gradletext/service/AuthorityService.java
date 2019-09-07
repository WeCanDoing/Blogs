/**
 * 
 */
package com.example.gradletext.service;

import com.example.gradletext.domain.Authority;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月21日 下午7:51:54  
 */
public interface AuthorityService {
	/**
	 * 根据ID查询权限
	 * @param id
	 * @return
	 */
 Authority getAuthorityById(Long id);
}
