package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zeeecom.journalEntry.Services.JournalEntryServices;
import com.zeeecom.journalEntry.entity.JournalEntry;

import java.util.*;
import java.util.stream.Collectors;

//Integrated this with MongoDB


@RestController
@RequestMapping("/journal")
@Tag(name = "Journal Entry APIs",description = "Read,Update and Delete Journals")
public class JournalEntryControllerV2 {

    //Dependency Injection -- not a good practice instead use Constructor Injection
    @Autowired
    public JournalEntryServices journalEntryServices;

    @Autowired
    private UserServices userServices;

    @GetMapping()
    @Operation(summary = "Get All Journal Entries Of User")
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        Users user = userServices.find_by_userName(userName);
        List<JournalEntry> journal = user.getJournalEntries();
        if (journal != null && !journal.isEmpty()) {
            return new ResponseEntity<>(journal, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    @Operation(summary = "Add Journal Entries for a User")
    public ResponseEntity<JournalEntry> add_journal_for_User(@RequestBody JournalEntry myentry) {
        try {
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String userName=authentication.getName();
            journalEntryServices.SaveEntry(myentry, userName);
            return new ResponseEntity<>(myentry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myid}")
    @Operation(summary = "Get Journal Entries Of a User by Journal's Id via Path Variable")
    public ResponseEntity<JournalEntry> find_by_id(@PathVariable String myid) {
        ObjectId objectId=new ObjectId(myid);
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();

        //To ensure that one user access its own journal entries we do the following
        //first we find the user then,
        // we check if the id that is given is same as the id that is present in the journal of that user
        Users users=userServices.find_by_userName(userName);
        List<JournalEntry> collect=users.getJournalEntries().stream().filter(x->x.getId().equals(objectId)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journal = journalEntryServices.find_by_id(objectId);
            if (journal.isPresent()) {
                return new ResponseEntity<>(journal.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //This is the RequestParameter One
    @GetMapping("/id")
    @Operation(summary = "Get Journal Entry Of User by Journal's Id via Request Body")
    public ResponseEntity<JournalEntry> find_via_id(@RequestParam(required = false, defaultValue = "1") ObjectId id) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();

        //To ensure that one user access its own journal entries we do the following
        //first we find the user then,
        // we check if the id that is given is same as the id that is present in the journal of that user
        Users users=userServices.find_by_userName(userName);
        List<JournalEntry> collect=users.getJournalEntries().stream().filter(x->x.getId().equals(id)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journal = journalEntryServices.find_by_id(id);
            if (journal.isPresent()) {
                return new ResponseEntity<>(journal.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myid}")
    @Operation(summary = "Delete Journal Entry of a User by Journal's ID")
    public ResponseEntity<?> delete_by_id(@PathVariable("myid") ObjectId Userid) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        boolean removed=journalEntryServices.delete_by_id(Userid, userName);
        if (removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/id/{myid}")
    @Operation(summary = "Update Journal Entry of a User by Journal's ID")
    public ResponseEntity<?> update_by_id(
            @PathVariable ObjectId myid,
            @RequestBody JournalEntry newentry)
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();

        //To ensure that one user access its own journal entries we do the following
        //first we find the user then,
        // we check if the id that is given is same as the id that is present in the journal of that user
        Users users=userServices.find_by_userName(userName);
        List<JournalEntry> collect=users.getJournalEntries().stream().filter(x->x.getId().equals(myid)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journal = journalEntryServices.find_by_id(myid);
            if (journal.isPresent()) {

                //This check if the journalEntry is null or not
                JournalEntry old = journalEntryServices.find_by_id(myid).orElse(null);
                if (old != null) {
                    old.setName(newentry.getName() != null && !newentry.getName().equals("") ? newentry.getName() : old.getName());
                    old.setContent(newentry.getContent() != null && !newentry.getContent().equals("") ? newentry.getContent() : old.getContent());
                    journalEntryServices.SaveEntry(old);
                    return new ResponseEntity<>(old, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
