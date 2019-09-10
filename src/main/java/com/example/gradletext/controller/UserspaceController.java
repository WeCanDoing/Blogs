/**
 * 
 */
package com.example.gradletext.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.gradletext.domain.Blog;
import com.example.gradletext.domain.Catalog;
import com.example.gradletext.domain.User;
import com.example.gradletext.domain.Vote;
import com.example.gradletext.service.AuthorityService;
import com.example.gradletext.service.BlogService;
import com.example.gradletext.service.CatalogService;
import com.example.gradletext.service.UserService;
import com.example.gradletext.util.ConstraintViolationExceptionHandler;
import com.example.gradletext.vo.Response;


/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月22日 下午8:55:08  
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthorityService  authorityService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CatalogService catalogService;
	
    @Value("${file.server.url}")
    private String fileServerUrl;
    /**
     * 返回个人主页
     * @param username
     * @param model
     * @return
     */
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return "redirect:" + username + "/blogs";
	}
    /**
	 * 返回个人设置
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView profile(@PathVariable("username")String username,Model model){
		User user=(User) userDetailsService.loadUserByUsername(username);
	   model.addAttribute("user",user);
	   model.addAttribute("fileServerUrl",fileServerUrl);
  	return new ModelAndView("userspace/profile","userModel",model);
		
	}
	/**
	 * 保存个人设置
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
   public String saveProfile(@PathVariable("username")String username,User user){
	  User originalUser=userService.getUserById(user.getId());
	originalUser.setEmail(user.getEmail());
	originalUser.setName(user.getName());
    //判断密码是否做了变更
	String rawPassword =originalUser.getPassword();
	PasswordEncoder encoder=new BCryptPasswordEncoder();
	String encodePasswd=encoder.encode(user.getPassword());//进行加密，过程不可逆
    boolean isMath=encoder.matches(rawPassword, encodePasswd);
	if(!isMath){
		originalUser.setEncodePassword(user.getPassword());
		
	}
	userService.saveOrUpdateUser(originalUser);
    return "redirect:u/"+username+"/profile";
		
	}
	/**
	 * 获取编辑头像
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username")String username,Model model){
		User user=(User) userDetailsService.loadUserByUsername(username);
		   model.addAttribute("user",user);
	  	return new ModelAndView("userspace/avatar","userModel",model);

	}
	/**
	 * 保存头像
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
   public ResponseEntity<Response> saveAvatar(@PathVariable("username")String username,@RequestBody User user){
	 String avatarUrl =user.getAvatar();
	User originalUser=userService.getUserById(user.getId());
	originalUser.setAvatar(avatarUrl);
	userService.saveOrUpdateUser(originalUser);
    return  ResponseEntity.ok().body(new Response(true, "处理成功",avatarUrl));
		
	}
	/**
	 * 博客列表
	 * @param username
	 * @param order      
	 * @param category   //类别
	 * @param keyword   //关键字
	 * @param async
	 * @param pageIndex
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="catalog",required=false ) Long catalogId,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model) {
		
		User  user = (User)userDetailsService.loadUserByUsername(username);
 		
		Page<Blog> page = null;
		
		if (catalogId != null && catalogId > 0) { // 分类查询
			Catalog catalog = catalogService.getCatalogById(catalogId);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByCatalog(catalog, pageable);
			order = "";
		} else if (order.equals("hot")) { // 最热查询
			Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize"); 
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
		} else if (order.equals("new")) { // 最新查询
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByTitleVote(user, keyword, pageable);
		}	
		List<Blog> list = page.getContent();	// 当前所在页面数据列表
		
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("catalogId", catalogId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		return (async==true?"userspace/u :: #mainContainerRepleace":"userspace/u");
	}
	
	
	/**
	 * 获取博客展示页面
	 * @param username
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public String getBlogById(@PathVariable("username") String username,@PathVariable("id") Long id, Model model){
		User principal = null;
		
		Blog blog= blogService.getBlogById(id);
		//每次读取，简单的认为阅读量增加一次
		blogService.readingIncrease(id);
		//判断用户是否是博客持有者
		boolean isBlogOwner=false;
		if(SecurityContextHolder.getContext().getAuthentication()!=null&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
			    principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		     	if(principal !=null && username.equals(principal.getUsername())){
			     	isBlogOwner=true;
			}
		
		}
		  //判断操作用户的点赞情况
        List<Vote> votes = blog.getVotes();
        Vote currentVote = null; //当前用户的点赞情况
        if (principal != null) {
            for (Vote vote : votes) {
                if (vote.getUser().getUsername().equals(principal.getUsername()))
                {
                    currentVote = vote;
                    break;
                }
            }
        }
		
		

        model.addAttribute("currentVote", currentVote);
		model.addAttribute("isBlogOwner", isBlogOwner);
		model.addAttribute("blogModel",blogService.getBlogById(id));
		
		return "userspace/blog";
	}
	/**
	 * 获取新增博客的界面
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		
		model.addAttribute("blog", new Blog(null, null, null));
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("fileServerUrl", fileServerUrl);
		return new ModelAndView("userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * 获取编辑博客的界面
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit/{id}")
	public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		// 获取用户分类列表
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		
		model.addAttribute("blog", blogService.getBlogById(id));
		model.addAttribute("catalogs", catalogs);
		return new ModelAndView("userspace/blogedit", "blogModel", model);
	}
	
	
	/**
	 * 保存博客
	 * @param username
	 * @param blog
	 * @return
	 */
	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
		// 对 Catalog 进行空处理
		if (blog.getCatalog().getId() == null) {
			return ResponseEntity.ok().body(new Response(false,"未选择分类"));
		}
		try {

			// 判断是修改还是新增
			
			if (blog.getId()!=null) {
				Blog orignalBlog = blogService.getBlogById(blog.getId());
				orignalBlog.setTitle(blog.getTitle());
				orignalBlog.setContent(blog.getContent());
				orignalBlog.setSummary(blog.getSummary());
				orignalBlog.setCatalog(blog.getCatalog());
				orignalBlog.setTitle(blog.getTitle());
				blogService.saveBlog(orignalBlog);
	        } else {
	    		User user = (User)userDetailsService.loadUserByUsername(username);
	    		blog.setUser(user);
				blogService.saveBlog(blog);
	        }
			
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}
	
	/**
	 * 删除博客
	 * @param id
	 * @param model
	 * @return
	 */
	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id") Long id) {
		
		try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}

}
