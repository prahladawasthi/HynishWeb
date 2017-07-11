package com.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "serviceRequest")
public class ServiceRequest {

	@Id
	private String id;

	@Field
	private String title;
	@Field
	private String description;

	@Field
	private List<String> userCommentList;
	
	@Field
	private String userComment;
		
	@Field
	private List<String> maintenanceCommentList;
	
	@Field
	private String maintenanceComment;

	@Field
	private String tower;

	@Field
	private String flatNo;

	@Field
	private String raisedBy;

	@Field
	private String assignedTo;
	
	@Field
	private String assignedStaff;

	@Field
	private String currentStatus;

	@Field
	private String raisedDate;

	@Field
	private String targetDate;

	@Field
	private String resolvedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(String raisedDate) {
		this.raisedDate = raisedDate;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public String getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public String getTower() {
		return tower;
	}

	public void setTower(String tower) {
		this.tower = tower;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	

	public String getAssignedStaff() {
		return assignedStaff;
	}

	public void setAssignedStaff(String assignedStaff) {
		this.assignedStaff = assignedStaff;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getMaintenanceCommentList() {
		return maintenanceCommentList;
	}

	public void setMaintenanceCommentList(List<String> maintenanceCommentList) {
		this.maintenanceCommentList = maintenanceCommentList;
	}

	public String getMaintenanceComment() {
		return maintenanceComment;
	}

	public void setMaintenanceComment(String maintenanceComment) {
		this.maintenanceComment = maintenanceComment;
	}

	public List<String> getUserCommentList() {
		return userCommentList;
	}

	public void setUserCommentList(List<String> userCommentList) {
		this.userCommentList = userCommentList;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	

	
}
