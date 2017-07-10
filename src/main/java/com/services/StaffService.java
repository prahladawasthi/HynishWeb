package com.services;

import java.util.List;

import com.model.DocumentTypeEnum;
import com.model.Staff;
import com.model.StaffTypeEnum;

public interface StaffService {

	public List<StaffTypeEnum> findStaffType();

	public boolean isStaffExist(Staff staff);

	public void saveStaff(Staff staff);
	
	public List<DocumentTypeEnum> findDocumentType();

	public List<Staff> findAllStaffs();

	public Staff deleteStaffById(String id);

	public Staff findByID(String id);
}
