/**
 * 
 */
package com.example.gradletext.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gradletext.domain.Comment;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月28日 上午11:10:17  
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
