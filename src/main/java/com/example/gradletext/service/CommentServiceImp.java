/**
 * 
 */
package com.example.gradletext.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gradletext.domain.Comment;
import com.example.gradletext.repository.CommentRepository;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月28日 上午11:14:31  
 */
@Service
public class CommentServiceImp implements CommentService {

	@Autowired
	private CommentRepository commentRepostiory;
	@Override
	public Comment getCommentById(Long id) {
		
		return  commentRepostiory.findById(id).get();
	}

	
	@Override
	public void removeComment(Long id) {
		
       commentRepostiory.deleteById(id);
	}

}
