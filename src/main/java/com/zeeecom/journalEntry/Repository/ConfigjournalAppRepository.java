package com.zeeecom.journalEntry.Repository;

import com.zeeecom.journalEntry.entity.ConfigJournalAppEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigjournalAppRepository extends MongoRepository<ConfigJournalAppEntity,ObjectId>{

}
