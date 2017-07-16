package com.services;

import com.model.Bugs;
import com.repository.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bugService")
public class BugServiceImpl implements BugService {

    private MongoTemplate mongoTemplate;
    private BugRepository bugRepository;

    @Autowired
    public BugServiceImpl(BugRepository bugRepository, MongoTemplate mongoTemplate) {
        this.bugRepository = bugRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveBug(Bugs bug) {
        mongoTemplate.save(bug);
    }

    @Override
    public Bugs deleteBugById(String id) {
        return mongoTemplate.findAndRemove(new Query().addCriteria(Criteria.where("_id").is(id)), Bugs.class);
    }

    @Override
    public List<Bugs> findAllBugs() {
        return (List<Bugs>) bugRepository.findAll();
    }

    @Override
    public Bugs findByID(String id) {
        return mongoTemplate.findById(id, Bugs.class);
    }
}
