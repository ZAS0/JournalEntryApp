package com.zeeecom.journalEntry.Repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.zeeecom.journalEntry.entity.JournalEntry;

public interface JournalEntryRepo extends MongoRepository<JournalEntry,ObjectId>{


}
