package com.zeeecom.journalEntry.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mongodb.lang.NonNull;

import lombok.Data;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id 
    private ObjectId id;

    @NonNull //To make username not null i.e it says username can't be null
    @Indexed(unique = true) //To make username Unique
    private String userName;
    
    @NonNull
    private String password;

    private String email;
    private boolean sentimentAnalysis;

    @DBRef
    private List<JournalEntry> journalEntries=new ArrayList<>();
    private List<String> roles;
}
