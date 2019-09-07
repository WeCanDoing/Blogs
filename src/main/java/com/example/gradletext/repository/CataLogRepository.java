/**
 * 
 */
package com.example.gradletext.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gradletext.domain.Catalog;
import com.example.gradletext.domain.User;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年9月3日 下午5:05:44  
 */
public interface CataLogRepository extends JpaRepository<Catalog, Long>{

	/**
	 * 根据用户查询
	 */
	List<Catalog>findByUser(User user);
	/**
	 * 根据用户查询和分类名称查询
	 */
	List<Catalog>findByUserAndName(User user,String name);
}
