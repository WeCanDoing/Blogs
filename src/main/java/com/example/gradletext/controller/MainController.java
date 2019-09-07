/**
 * 
 */
package com.example.gradletext.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.gradletext.domain.Authority;
import com.example.gradletext.domain.User;
import com.example.gradletext.service.AuthorityService;
import com.example.gradletext.service.UserService;
import com.example.gradletext.vo.Response;

/**
 * @author singjumprap
 * @Description:   主页控制器  
 * @date:   2019年5月14日 下午4:23:41  
 */
@Controller
public class MainController {
  
  private static final Long ROLE_USER_AUTHORITY_ID=2l;
  @Autowired
  private UserService userService;
  @Autowired
	private AuthorityService  authorityService;
  @GetMapping("/")
  public String root(){
	  return "redirect:index";
  }
  //此处预留修改
  @GetMapping("/index")
  public String index(){
	  return "redirect:blogs";
  }
  @GetMapping("/login")
  public String login(){
	  return "login";
  }
  @GetMapping("/login-error")
  public String loginError(Model model){
	  model.addAttribute("loginError",true);
	  model.addAttribute("erroMsg","登录失败");
	  
	  return "login";
  }
	@GetMapping("/register")
	public String register() {
		return "register";
	}
  @PostMapping("/register")
  public String register(User user){
	  List<Authority> authorities=new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
		user.setAuthorities(authorities);
		user.setEncodePassword(user.getPassword());//加密密码
		String username= userService.registerUser(user);	
		return "redirect:login";	  
  }
  
}
