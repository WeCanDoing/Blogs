/**
 * 
 */
package com.example.gradletext.service;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.gradletext.domain.User;
import com.example.gradletext.repository.UserRepository;

/**
 * @author singjumprap
 * @Description:    用户服务类实现   
 */
@Service
public class UserServiceImpl implements UserService ,UserDetailsService {
   @Autowired
	private UserRepository userRepository;
	/* (non-Javadoc)
	 * @see com.example.gradletext.service.UserService#saveOrUpdateUser(com.example.gradletext.domain.User)
	 */
    @Transactional
	@Override
	public User saveOrUpdateUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	/* (non-Javadoc)
	 * @see com.example.gradletext.service.UserService#registerUser(com.example.gradletext.domain.User)
	 */
    @Transactional   
	@Override
	public String registerUser(User user) {
		// TODO Auto-generated method stub
    	 String username=user.getUsername();
    	 UserDetails userDate=userRepository.findByUsername(username);
    	 if(userDate!=null){
    		 
          return "重复了的用户名";
    		 
    	 }else{
    		 userRepository.save(user);
		return "注册成功";
    	 }
	}

	/* (non-Javadoc)
	 * @see com.example.gradletext.service.UserService#removeUser(java.lang.Long)
	 */
    @Transactional
	@Override
	public void removeUser(Long id) {
		// TODO Auto-generated method stub
      userRepository.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see com.example.gradletext.service.UserService#getUserById(java.lang.Long)
	 */
	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id).get();
	}

	/* (non-Javadoc)
	 * @see com.example.gradletext.service.UserService#listUserByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<User> listUserByNameLike(String name, Pageable pageable) {
		// TODO Auto-generated method stub
		name= "%"+name+"%";
		Page<User>users=userRepository.findByNameLike(name, pageable);
		return users;
	}

	/* (non-Javadoc)
	 * @see com.example.gradletext.service.UserService#getUserByUsername(java.lang.String)
	 */
	@Override
	public User getUserByUsernameAndPassword(String username,String password) {
		// TODO Auto-generated method stub
	  User user=userRepository.findByUsernameAndPassword(username,password);
		return user;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails us= userRepository.findByUsername(username);
		return us;
	}

	
	@Override
	public List<User> listUsersByUsernames(Collection<String> usernames) {
		return userRepository.findByUsernameIn(usernames);
	}
}
