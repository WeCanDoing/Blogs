/**
 * 
 */
package com.example.gradletext.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gradletext.domain.Authority;

/**
 * @author singjumprap
 * @Description:  权限 
 * @date:   2019年5月21日 下午7:49:08  
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long>{

}
