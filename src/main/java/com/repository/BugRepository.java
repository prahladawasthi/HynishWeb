package com.repository;

import com.model.Bugs;
import com.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("bugRepository")
public interface BugRepository extends MongoRepository<Bugs, String> {

}
