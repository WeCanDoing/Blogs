/**
 * 
 */
package com.example.gradletext.vo;

import java.io.Serializable;

import com.example.gradletext.domain.Catalog;

/**
 * @author singjumprap
 * @Description:    TODO(分类标签值对象)   
 * @date:   2019年9月3日 下午4:54:16  
 */
public class CatalogVO implements Serializable {
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private Catalog catalog;
	/**
	 * Jpa规范
	 */
	public CatalogVO() {
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Catalog getCatalog() {
		return catalog;
	}
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
    
}
