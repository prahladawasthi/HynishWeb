package com.services;

import java.util.List;

import com.model.ServiceRequest;
import com.model.ServiceRequestStatusEnum;

public interface ServiceRequestService {

	List<ServiceRequest> findAllServiceRequest();

	List<ServiceRequestStatusEnum> findAllServiceRequestStatus();

	void saveServiceRequest(ServiceRequest serviceRequest);

	ServiceRequest findRequestByID(String id);

	ServiceRequest deleteRequestById(String id);
	
	List<ServiceRequest> findServiceRequestByUserID(String id);

}
