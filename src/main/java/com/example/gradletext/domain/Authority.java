/**
 * 
 */
package com.example.gradletext.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author singjumprap
 * @Description:    权限实体类   
 * @date:   2019年5月21日 下午4:49:19  
 */
@Entity
public class Authority  implements GrantedAuthority{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//主键自增长
	private Long id;
	
	
	@Column(nullable=false)//非空判断
	private String name;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name;
	}
	

}
