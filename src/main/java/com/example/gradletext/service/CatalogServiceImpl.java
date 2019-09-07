
package com.example.gradletext.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gradletext.domain.Catalog;
import com.example.gradletext.domain.User;
import com.example.gradletext.repository.CataLogRepository;



/**
 * Catalog 服务.
 * 
 */
@Service
public class CatalogServiceImpl implements CatalogService{

	@Autowired
	private CataLogRepository catalogRepository;

	@Override
	public Catalog saveCatalog(Catalog catalog) {
		// 判断重复
		List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
		if(list !=null && list.size() > 0) {
			throw new IllegalArgumentException("该分类已经存在了");
		}
		return catalogRepository.save(catalog);
	}


	@Override
	public void removeCatalog(Long id) {
		catalogRepository.deleteById(id);
		
	}


	@Override
	public Catalog getCatalogById(Long id) {
		
		return catalogRepository.findById(id).get();
	}


	@Override
	public List<Catalog> listCatalogs(User user) {
		return catalogRepository.findByUser(user);
	}


}
