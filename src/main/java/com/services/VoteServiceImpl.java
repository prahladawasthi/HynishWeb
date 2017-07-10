package com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.model.Vote;
import com.repository.VoteRepository;

@Service("voteService")
public class VoteServiceImpl implements VoteService {

	private MongoTemplate mongoTemplate;
	private VoteRepository voteRepository;

	@Autowired
	public VoteServiceImpl(VoteRepository voteRepository, MongoTemplate mongoTemplate) {
		this.voteRepository = voteRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void saveVote(Vote vote) {
		mongoTemplate.save(vote);

	}

	@Override
	public Vote deleteVoteById(String id) {
		return mongoTemplate.findAndRemove(new Query().addCriteria(Criteria.where("_id").is(id)), Vote.class);
	}

	@Override
	public List<Vote> findAllvotes() {
		return (List<Vote>) voteRepository.findAll();
	}

	@Override
	public Vote findByID(String id) {
		return mongoTemplate.findById(id, Vote.class);
	}
}
