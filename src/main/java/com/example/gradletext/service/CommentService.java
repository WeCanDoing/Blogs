/**
 * 
 */
package com.example.gradletext.service;

import com.example.gradletext.domain.Comment;

/**
 * @author singjumprap
 * @Description:    CommentService 接口  
 * @date:   2019年5月28日 上午11:11:18  
 */
public interface CommentService {
	
	/**
	 * 根据id获取Comment
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);
	
	/**
	 * 删除评论
	 * @param id
	 */
    void  removeComment(Long id);
}
