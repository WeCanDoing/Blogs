/**
 * 
 */
package com.example.gradletext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.gradletext.domain.User;

/**
 * @author singjumprap
 * @Description:    TODO(接口)   
 * @date:   2019年5月16日 下午4:22:51  
 */
public interface UserRepository extends JpaRepository<User, Long>{
    /**
     * 根据用户姓名分页查询用户列表 
     * @param name
     * @param pagable
     * @return
     */
	Page<User>findByNameLike(String name,Pageable pagable);
	/**
	 * @param username
	 * @param password
	 * @return
	 */
	User findByUsernameAndPassword(String username, String password);

	/**
	 * @param username
	 * @return
	 */
	UserDetails findByUsername(String username);
	/**
	 * 根据名称列表查询
	 * @param usernames
	 * @return
	 */
	List<User> findByUsernameIn(Collection<String> usernames);


}
