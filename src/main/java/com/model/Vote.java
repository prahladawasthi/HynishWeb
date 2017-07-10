package com.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "vote")
public class Vote {
	@Id
	private String id;

	@Field
	private String votingTopic;

	@Field
	private List<String> votingOptions;
	
	@Field
	private Map<String,Integer> votingStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVotingTopic() {
		return votingTopic;
	}

	public void setVotingTopic(String votingTopic) {
		this.votingTopic = votingTopic;
	}

	public List<String> getVotingOptions() {
		return votingOptions;
	}

	public void setVotingOptions(List<String> votingOptions) {
		this.votingOptions = votingOptions;
	}

	public Map<String, Integer> getVotingStatus() {
		return votingStatus;
	}

	public void setVotingStatus(Map<String, Integer> votingStatus) {
		this.votingStatus = votingStatus;
	}

}
