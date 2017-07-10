package com.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.model.Staff;

@Repository("staffRepository")
public interface StaffRepository extends MongoRepository<Staff, String> {

}
