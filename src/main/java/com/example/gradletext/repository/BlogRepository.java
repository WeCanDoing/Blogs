/**
 * 
 */
package com.example.gradletext.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gradletext.domain.Blog;
import com.example.gradletext.domain.Catalog;
import com.example.gradletext.domain.User;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月23日 下午8:47:25  
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {
   /**
    * 根据用户名，博客标题分页查询博客列表
    * @param user
    * @param title
    * @param pageable
    * @return
    */
	Page<Blog>findByUserOrderByReadingSizeDesc(User user,Pageable pageable);
	
	/**
	 * 根据用户名分页查询用户列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog>findByUserOrderByCreateTimeDesc(User user,Pageable pageable);
    
	/**
	 * 根据用户名分页查询博客列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
