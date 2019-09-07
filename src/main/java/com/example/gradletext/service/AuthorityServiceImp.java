/**
 * 
 */
package com.example.gradletext.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gradletext.domain.Authority;
import com.example.gradletext.repository.AuthorityRepository;

/**
 * @author singjumprap
 * @Description:    AuthorityService 服务接口实现   
 * @date:   2019年5月21日 下午7:53:36  
 */
@Service
public class AuthorityServiceImp implements AuthorityService{
     @Autowired
	private AuthorityRepository authorityRepository;

	/* (non-Javadoc)
	 * @see com.example.gradletext.service.AuthorityService#getAuthorityById(java.lang.Long)
	 */
	@Override
	public Authority getAuthorityById(Long id) {
		// TODO Auto-generated method stub
		return authorityRepository.findById(id).get();
	}
}
