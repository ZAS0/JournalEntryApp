package com.zeeecom.journalEntry.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.zeeecom.journalEntry.entity.Users;

public interface UserRepo extends MongoRepository<Users,ObjectId> {
    
    //This is to help update user ,a helper function
    Users findByuserName(String username);

    void deleteByUserName(String name);
}
