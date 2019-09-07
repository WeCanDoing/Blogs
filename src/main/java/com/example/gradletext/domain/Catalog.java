/**
 * 
 */
package com.example.gradletext.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author singjumprap
 * @Description:    TODO(分类管理)   
 * @date:   2019年9月3日 下午4:18:53  
 */
@Entity // 实体
public class Catalog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//自增长策略
	private Long id;//用户唯一标识
	
	@NotEmpty(message="名称不能为空")
	@Size(min=2,max=30)
	@Column(nullable=false)
	private String name;
	
	@OneToOne(cascade=CascadeType.DETACH ,fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	protected Catalog(){
		//jpa规范
	}

	/**
	 * @param name
	 * @param user
	 */
	public Catalog(@NotEmpty(message = "名称不能为空") @Size(min = 2, max = 30) String name, User user) {
		super();
		this.name = name;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
