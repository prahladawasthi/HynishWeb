package com.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.model.ServiceRequest;

@Repository("serviceRequestRepository")
public interface ServiceRequestRepository extends MongoRepository<ServiceRequest, String> {

}