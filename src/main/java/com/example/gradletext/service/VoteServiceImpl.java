/**
 * 
 */
package com.example.gradletext.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gradletext.domain.Vote;
import com.example.gradletext.repository.VoteRepository;

/**
 * @author singjumprap
 * @Description:    点赞服务实现接口
 * @date:   2019年9月2日 下午4:19:05  
 */
@Service

public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findById(id).get();
    }



    @Override
    public void removeVote(Long id) {
        voteRepository.deleteById(id);;

    }
}