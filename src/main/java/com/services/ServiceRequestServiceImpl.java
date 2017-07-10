package com.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.model.ServiceRequest;
import com.model.ServiceRequestStatusEnum;
import com.repository.ServiceRequestRepository;

@Service("serviceRequestService")
public class ServiceRequestServiceImpl implements ServiceRequestService {

	private MongoTemplate mongoTemplate;
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	public ServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository, MongoTemplate mongoTemplate) {
		this.serviceRequestRepository = serviceRequestRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<ServiceRequest> findAllServiceRequest() {
		return (List<ServiceRequest>) serviceRequestRepository.findAll();
	}

	@Override
	public List<ServiceRequestStatusEnum> findAllServiceRequestStatus() {
		return Arrays.asList(ServiceRequestStatusEnum.values());
	}

	@Override
	public void saveServiceRequest(ServiceRequest serviceRequest) {
		serviceRequestRepository.save(serviceRequest);
	}

	public ServiceRequest findRequestByID(String id) {
		return mongoTemplate.findById(id, ServiceRequest.class);
	}

	public ServiceRequest deleteRequestById(String id) {
		return mongoTemplate.findAndRemove(new Query().addCriteria(Criteria.where("_id").is(id)), ServiceRequest.class);
	}

	public List<ServiceRequest> findServiceRequestByUserID(String id) {
		return mongoTemplate.find(new Query().addCriteria(Criteria.where("raisedBy").is(id)), ServiceRequest.class);
	}
}
