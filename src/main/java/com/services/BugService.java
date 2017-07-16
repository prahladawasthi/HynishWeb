package com.services;

import com.model.Bugs;

import java.util.List;

public interface BugService {

     void saveBug(Bugs bug);

     Bugs deleteBugById(String id);

     List<Bugs> findAllBugs();

     Bugs findByID(String id);
}
