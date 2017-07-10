package com.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.model.DocumentTypeEnum;
import com.model.Staff;
import com.model.StaffTypeEnum;
import com.repository.StaffRepository;

@Service("staffService")
public class StaffServiceImpl implements StaffService {

	private MongoTemplate mongoTemplate;
	private StaffRepository staffRepository;

	@Autowired
	public StaffServiceImpl(StaffRepository staffRepository, MongoTemplate mongoTemplate) {
		this.staffRepository = staffRepository;
		this.mongoTemplate = mongoTemplate;
	}

	public List<StaffTypeEnum> findStaffType() {
		return Arrays.asList(StaffTypeEnum.values());
	}

	@Override
	public boolean isStaffExist(Staff staff) {
		return ((mongoTemplate.find(
				Query.query(new Criteria().andOperator(Criteria.where("mobile").regex(staff.getMobile(), "i"),
						Criteria.where("documentType").regex(staff.getDocumentType(), "i"),
						Criteria.where("documentNumber").regex(staff.getDocumentNumber(), "i"))),
				Staff.class)).size() > 0) ? true : false;

	}

	@Override
	public void saveStaff(Staff staff) {
		staffRepository.save(staff);
	}

	@Override
	public List<DocumentTypeEnum> findDocumentType() {
		return Arrays.asList(DocumentTypeEnum.values());
	}

	@Override
	public List<Staff> findAllStaffs() {
		return (List<Staff>) staffRepository.findAll();
	}

	public Staff deleteStaffById(String id) {
		return mongoTemplate.findAndRemove(new Query().addCriteria(Criteria.where("_id").is(id)), Staff.class);
	}

	@Override
	public Staff findByID(String id) {
		return mongoTemplate.findById(id, Staff.class);
	}

}
