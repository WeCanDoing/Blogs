package com.example.gradletext.service;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.example.gradletext.domain.User;
import com.example.gradletext.domain.es.EsBlog;
import com.example.gradletext.repository.es.EsBlogRepository;
import com.example.gradletext.vo.TagVO;



/**
 * EsBlog 服务.
 * 
 * @since 1.0.0 2017年4月12日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
@Service
public class EsBlogServiceImpl implements EsBlogService {
	@Autowired
	private EsBlogRepository esBlogRepository;
	//创建搜索模板
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private UserService userService;
	
	private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
	private static final String EMPTY_KEYWORD = "";
	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.EsBlogService#removeEsBlog(java.lang.String)
	 */
	@Override
	public void removeEsBlog(String id) {
		esBlogRepository.deleteById(id);;
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.EsBlogService#updateEsBlog(com.waylau.spring.boot.blog.domain.es.EsBlog)
	 */
	@Override
	public EsBlog updateEsBlog(EsBlog esBlog) {
		return esBlogRepository.save(esBlog);
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.EsBlogService#getEsBlogByBlogId(java.lang.Long)
	 */
	@Override
	public EsBlog getEsBlogByBlogId(Long blogId) {
		return esBlogRepository.findByBlogId(blogId);
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.EsBlogService#listNewestEsBlogs(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) throws SearchParseException {
		Page<EsBlog> pages = null;
		Sort sort = new Sort(Direction.DESC,"createTime"); 
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		}
 
		pages = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,keyword,keyword,keyword, pageable);
 
		return pages;
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.EsBlogService#listHotestEsBlogs(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) throws SearchParseException{
 
		Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize","createTime"); 
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		}
 
		return esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
	}

	@Override
	public Page<EsBlog> listEsBlogs(Pageable pageable) {
		return esBlogRepository.findAll(pageable);
	}
 
 
	/**
	 * 最新前5
	 * @param keyword
	 * @return
	 */
	@Override
	public List<EsBlog> listTop5NewestEsBlogs() {
		Page<EsBlog> page = this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
		return page.getContent();
	}
	
	/**
	 * 最热前5
	 * @param keyword
	 * @return
	 */
	@Override
	public List<EsBlog> listTop5HotestEsBlogs() {
		Page<EsBlog> page = this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
		return page.getContent();
	}

	@Override
	public List<TagVO> listTop30Tags() {
 
		List<TagVO> list = new ArrayList<>();
		// given
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("blog").withTypes("blog")
				.addAggregation(terms("tags").field("tags").size(30))
				.build();
		// when
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		
		StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("tags"); 
	        
        Iterator<org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = modelBucketIt.next();
 
            list.add(new TagVO(actiontypeBucket.getKey().toString(),
                    actiontypeBucket.getDocCount()));
        }
		return list;
	}
	
	@Override
	public List<User> listTop12Users() {
 
		List<String> usernamelist = new ArrayList<>();
		// given
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())   //设置查询所有，相当于不设置查询条件
				//2.1设置搜索类型，默认值就是QUERY_THEN_FETCH
				.withSearchType(SearchType.QUERY_THEN_FETCH) //指定索引的类型，只先从各分片中查询匹配的文档，再重新排序和排名，取前size个文档
		    	//2.2指定索引库和文档类型
				.withIndices("blog").withTypes("blog")//指定要查询的索引库的名称和类型，其实就是我们文档@Document中设置的indedName和type
				.addAggregation(terms("users").field("username").size(12))
		    	//2.4构建查询对象
				.build();
		// when
     	//3.通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		//获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
		StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("users"); 
		//将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了    
        Iterator<org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = modelBucketIt.next();
            String username = actiontypeBucket.getKey().toString();
            usernamelist.add(username);
        }
        List<User> list = userService.listUsersByUsernames(usernamelist);
        
		return list;
	}
}
