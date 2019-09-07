/**
 * 
 */
package com.example.gradletext.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gradletext.domain.Vote;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年9月2日 下午4:15:10  
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
