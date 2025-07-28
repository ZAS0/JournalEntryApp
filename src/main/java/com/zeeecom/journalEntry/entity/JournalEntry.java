package com.zeeecom.journalEntry.entity;

import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import java.time.LocalDateTime;


/**
 * JournalEntry
 */
//@Getter // Getter through Lombok
//@Setter // Setter through Lombok
@Document(collection = "journals")
@Data //This is Through Lombok,which generate getter,setter,constructor at run time.
@NoArgsConstructor
public class JournalEntry {
    
    @Id
    private ObjectId id;
    
    @NonNull
    private String name;
    private String content;
    private LocalDateTime date;

}