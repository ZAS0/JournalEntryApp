package com.zeeecom.journalEntry.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.zeeecom.journalEntry.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.zeeecom.journalEntry.Repository.JournalEntryRepo;
import com.zeeecom.journalEntry.entity.JournalEntry;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JournalEntryServices {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserServices userServices;

    @Transactional
    public void SaveEntry(JournalEntry journal, String userName) {
        try{
            Users user = userServices.find_by_userName(userName);
            journal.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(journal);
            user.getJournalEntries().add(saved);
            userServices.SaveUser(user);
        }
        catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An Error has occurred while saving entries.",e);
        }
    }

    public void SaveEntry(JournalEntry journal) {
        journalEntryRepo.save(journal);
    }

    public List<JournalEntry> getALL() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> find_by_id(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean delete_by_id(ObjectId id, String userName) {
        boolean removed=false;
        try {
            Users user = userServices.find_by_userName(userName);

            //This to get if the user was removed or not,true if removed and false if not removed
            removed=user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed){
                userServices.SaveUser(user);
                journalEntryRepo.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An Error Occurred while deleting the user,",e);
        }
        return  removed;
    }

    //This is if we want the controller to call the service to implement update
    //public ResponseEntity<?> update_by_id(ObjectId id, JournalEntry newentry) {
    //    JournalEntry old = find_by_id(id).orElse(null);
    //    if (old != null) {
    //        old.setName(
    //                newentry.getName() != null && !newentry.getName().equals("") ? newentry.getName() : old.getName());
    //        old.setContent(newentry.getContent() != null && !newentry.getContent().equals("") ? newentry.getContent()
    //                : old.getContent());
    //        SaveEntry(old, userName);
    //        return new ResponseEntity<>(old, HttpStatus.OK);
    //    }
    //    SaveEntry(old, userName);
    //    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //}



}
