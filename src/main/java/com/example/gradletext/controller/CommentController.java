/**
 * 
 */
package com.example.gradletext.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gradletext.domain.Blog;
import com.example.gradletext.domain.Comment;
import com.example.gradletext.domain.User;
import com.example.gradletext.service.BlogService;
import com.example.gradletext.service.CommentService;
import com.example.gradletext.util.ConstraintViolationExceptionHandler;
import com.example.gradletext.vo.Response;

/**
 * @author singjumprap
 * @Description:  评论控制器 
 * @date:   2019年5月28日 下午2:46:48  
 */
@Controller
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CommentService commentService;
	
	/**
	 * 获取评论列表
	 * @param blogId
	 * @param model
	 * @return
	 */
	@GetMapping
	public String listComments(@RequestParam(value="blogId",required=true)Long blogId,Model model){
		System.out.println("评论列表进入方法体");
		Blog blog =blogService.getBlogById(blogId);
		List<Comment>comments=blog.getComments();
		System.out.println("评论为：" + comments.toString());
		String commentOwner="";
		if(SecurityContextHolder.getContext().getAuthentication()!=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
			&& !SecurityContextHolder.getContext().getAuthentication().toString().equals("anonymousUser")	){
			User principal=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal !=null){
				commentOwner=principal.getUsername();
			}
			
		}
		
		 model.addAttribute("commentOwner", commentOwner);

	        model.addAttribute("comments", comments);

	        return "userspace/blog :: #mainContainerRepleace";
		
	}
	
	
	/**
	 * 发表评论
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	@PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
	public ResponseEntity<Response> createComment(Long blogId ,String commentContent){
		try{
		 blogService.creatComment(blogId, commentContent);
		}catch(ConstraintViolationException e){
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
			
		}catch(Exception e){
			  return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功", null));
		
	}
	

    @DeleteMapping("/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id")Long id,Long blogId ){
		boolean isOwner=false;
    	User user =commentService.getCommentById(blogId).getUser();
    	if(SecurityContextHolder.getContext().getAuthentication()!=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
    			&& !SecurityContextHolder.getContext().getAuthentication().toString().equals("anonymousUser")	){
    			User principal=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    			if(principal !=null){
    				isOwner=true;
    			}
    			
    		}
    	if(!isOwner){
    		return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
    	}
    	
    	try{
    		blogService.removeComment(blogId, id);
    		commentService.removeComment(id);
    	}catch(ConstraintViolationException e){
    	    return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));

        } catch (Exception e) {

            return ResponseEntity.ok().body(new Response(false, e.getMessage()));

        }
    	return ResponseEntity.ok().body(new Response(true, "处理成功", null));
		
    	}

    }

 