package com.model;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "staff")
public class Staff {

	@Id
	private String id;

	@Field
	@Size(min = 3, max = 30)
	private String firstName;

	@Field
	@Size(min = 3, max = 30)
	private String lastName;

	@Field
	@Size(min = 10, max = 10)
	private String mobile;

	@Field
	private String staffType;

	@Field
	private String dutyLocation;

	@Field
	private String documentType;

	@Field
	private String documentNumber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	public String getDutyLocation() {
		return dutyLocation;
	}

	public void setDutyLocation(String dutyLocation) {
		this.dutyLocation = dutyLocation;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

}
