package com.zeeecom.journalEntry.Repository;

import com.zeeecom.journalEntry.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Users> getUserForSentimentAnalysis(){
        Query query=new Query();
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").exists(true));
        List<Users> users = mongoTemplate.find(query, Users.class);
        return users;
    }

}
