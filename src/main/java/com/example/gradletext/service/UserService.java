/**
 * 
 */
package com.example.gradletext.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.gradletext.domain.User;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月16日 下午4:33:10  
 */
public interface UserService {
   /**
    * 新增，编辑，保存用户
    * @param user
    * @return
    */
	User saveOrUpdateUser(User user);
	/**
	 * 注册用户
	 */
	String registerUser(User user);
	/**
	 * 删除用户
	 * @param id
	 */
	void removeUser(Long id);
	/**
	 * 根据id获取用户
	 * @param id
	 * @return
	 */
	User getUserById(Long id);
	/**
	 * 根据用户名进行分页模糊查询
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<User>listUserByNameLike(String name,Pageable pageable);
	/**
	 * 根据用户名获取用户
	 * @param id
	 * @return
	 */
	User getUserByUsernameAndPassword(String username,String password);
	/**
	 * 更具名称列表查询
	 * @param usernames
	 * @return
	 */
	List<User> listUsersByUsernames(Collection<String> usernames);
}
