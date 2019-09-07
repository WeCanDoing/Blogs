/**
 * 
 */
package com.example.gradletext.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.gradletext.domain.Blog;
import com.example.gradletext.domain.Catalog;
import com.example.gradletext.domain.Comment;
import com.example.gradletext.domain.User;
import com.example.gradletext.domain.Vote;
import com.example.gradletext.domain.es.EsBlog;
import com.example.gradletext.repository.BlogRepository;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月23日 下午9:05:04  
 */
@Service
public class BlogServiceImp implements BlogService{

	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private EsBlogService esBlogService;

	
	@Transactional
	@Override
	public Blog saveBlog(Blog blog) {
		boolean isNew = (blog.getId() == null);
		EsBlog esBlog = null;
		
		Blog returnBlog = blogRepository.save(blog);
		
		if (isNew) {
			esBlog = new EsBlog(returnBlog);
		} else {
			esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
			esBlog.update(returnBlog);
		}
		
		esBlogService.updateEsBlog(esBlog);
		return returnBlog;
	}

	
	@Transactional
	@Override
	public void removeBlog(Long id) {
		blogRepository.deleteById(id);
		EsBlog esblog = esBlogService.getEsBlogByBlogId(id);
		esBlogService.removeEsBlog(esblog.getId());
	}
    
	@Transactional
	@Override
	public Blog updateBlog(Blog blog) {
		return blogRepository.save(blog);
	}

	
	@Transactional
	@Override
	public Blog getBlogById(Long id) {
		// TODO Auto-generated method stub
		return  blogRepository.findById(id).get();
	}


	@Override
	public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
		// 模糊查询
	
		title ="%"+title+"%";
	  
			Page<Blog> blogs=blogRepository.findByUserOrderByCreateTimeDesc( user, pageable);
		 System.out.println(blogs);                                 
		return blogs;
	}

	@Override
	public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
		// TODO Auto-generated method stub
		// 模糊查询
		title ="%"+title+"%";
		Page<Blog> blogs=blogRepository.findByUserOrderByReadingSizeDesc(user, pageable);
		return blogs;
	}

	/* (non-Javadoc)
	 * @see com.example.gradletext.service.BlogService#readingIncrease(java.lang.Long)
	 */
	@Override
	public void readingIncrease(Long id) {
		// TODO Auto-generated method stub
		Blog blog=blogRepository.findById(id).get();
		blog.setReadingSize(blog.getReadingSize()+1);
		this.saveBlog(blog);
	}

	
	@Override
	public Blog creatComment(Long blogId, String commentContent) {
	Blog originalBlog=blogRepository.findById(blogId).get();
	User user =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();//上下文中获取用户
	Comment comment =new Comment(commentContent, user);
	originalBlog.addComment(comment);
	return this.saveBlog(originalBlog);
	}

    /**
     *移除评论
     */
	@Override
	public void removeComment(Long blogId, Long commentId) {
		Blog originalBlog=blogRepository.findById(blogId).get();
      originalBlog.removeComment(commentId);
      this.saveBlog(originalBlog);
	}
	
    @Override
    public Blog createVote(Long blogId) {
        //找到博客
        Blog originalBlog = blogRepository.findById(blogId).get();
        //获取认证用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        //判断点赞是否已经存在
        boolean isExist = originalBlog.addVote(vote);
        if (isExist) {
            throw new IllegalArgumentException("该用户已经点过赞了！");
        }
        return this.saveBlog(originalBlog);



    }



    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog originalBlog = blogRepository.findById(blogId).get();;
        originalBlog.removeVote(voteId);
        this.saveBlog(originalBlog);

  }

	
	@Override
	public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
		Page<Blog>blogs=blogRepository.findByCatalog(catalog, pageable);
		return blogs;
	}


}
