package com.services;

import java.util.List;

import com.model.Vote;

public interface VoteService {

	public void saveVote(Vote vote);

	public Vote deleteVoteById(String id);

	public List<Vote> findAllvotes();

	public Vote findByID(String id);

}