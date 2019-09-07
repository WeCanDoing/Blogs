/**
 * 
 */
package com.example.gradletext.service;

import com.example.gradletext.domain.Vote;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年9月2日 下午4:17:24  
 */

public interface VoteService {

    /**
     * 根据id获取Vote
     * @param id
     * @return
     */
    Vote getVoteById(Long id);



    /**
     * 根据id删除Vote
     * @param id
     */

    void removeVote(Long id);





}