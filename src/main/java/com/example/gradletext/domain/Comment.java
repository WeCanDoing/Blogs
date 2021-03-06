/**
 * 
 */
package com.example.gradletext.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;

/**
 * @author singjumprap
 * @Description:  评论实体类
 * @date:   2019年5月28日 上午9:01:42  
 */
@Entity 
public class Comment implements Serializable{

	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; //用户唯一标识
	
	@NotEmpty(message="评论内容不能为空")
	@Size(min=2,max=500)
	@Column(nullable=false)
    private String content;

	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

	
    
    @Column(nullable=false)
    @CreationTimestamp
    private Timestamp createTime;

	/**
	 * Jpa要求
	 */
	protected Comment() {
		
	}

	/**
	 * @param content
	 * @param user
	 */
	public Comment(@NotEmpty(message = "评论内容不能为空") @Size(min = 2, max = 500) String content, User user) {
		super();
		this.content = content;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", user=" + user + ", createTime=" + createTime + "]";
	}

	
	
    
    
}
