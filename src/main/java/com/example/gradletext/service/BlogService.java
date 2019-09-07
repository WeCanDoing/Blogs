/**
 * 
 */
package com.example.gradletext.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.gradletext.domain.Blog;
import com.example.gradletext.domain.Catalog;
import com.example.gradletext.domain.User;

/**
 * @author singjumprap
 * @Description:    TODO(Blog服务类接口)   
 * @date:   2019年5月23日 下午8:56:08  
 */
public interface BlogService {
        
	/**
	 * 保存Blog
	 * @param blog
	 * @return
	 */
	Blog saveBlog(Blog blog);
	
	/**
	 * 删除Blog
	 * @param id
	 */
	void removeBlog(Long id);

	/**
	 * 更新Blog
	 * @param Blog
	 * @return
	 */
	Blog updateBlog(Blog blog);
	/**
	 *根据id获取Blog 
	 * @param id
	 * @return
	 */
    Blog getBlogById(Long id);
    
    /**
     * 更具用户名进行博客名称分页模糊查询（最新）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleVote(User user,String title ,Pageable pageable);

    /**
     * 更具用户名进行博客名称分页模糊查询（最热）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleVoteAndSort(User user,String title ,Pageable pageable);

    /**
     * 阅读量递增
     * @param id
     */
    void readingIncrease(Long id);
    
    /**
     * 发表评论
     * @param blogId
     * @param commentId
     * @return
     */
    Blog creatComment(Long blogId,String commentContent);
    
    /**
     * 删除评论
     * @param blogId
     * @param commentId
     */
    void removeComment(Long blogId,Long commentId);
    /**
     * 点赞
     * @param blogId
     * @return
     */
     Blog createVote(Long blogId);


    /**
     * 取消点赞
     * @param blogId
     * @param voteId
     */

    void removeVote(Long blogId,Long voteId);
    
	/**
	 * 根据分类进行查询
	 * @param catalog
	 * @param pageable
	 * @return
	 */
	Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable); 
}
