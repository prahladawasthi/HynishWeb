package com.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.model.Vote;

@Repository("voteRepository")
public interface VoteRepository extends MongoRepository<Vote, String> {

}
