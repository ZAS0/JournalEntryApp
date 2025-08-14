package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.DTOs.JournalEntryDto;
import com.zeeecom.journalEntry.DTOs.JournalEntryRequestDto;
import com.zeeecom.journalEntry.Services.JournalEntryServices;
import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.JournalEntry;
import com.zeeecom.journalEntry.entity.Users;
import com.zeeecom.journalEntry.Mappers.JournalEntryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
@Tag(name = "Journal Entry APIs", description = "Create, Read, Update and Delete Journals")
public class JournalEntryControllerV2 {

    private final JournalEntryServices journalEntryServices;
    private final UserServices userServices;
    private final JournalEntryMapper mapper;

    @GetMapping
    @Operation(summary = "Get all journal entries of the logged-in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of journal entries found"),
                    @ApiResponse(responseCode = "404", description = "No journal entries found")
            })
    public ResponseEntity<List<JournalEntryDto>> getAllJournalEntriesOfUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userServices.find_by_userName(userName);

        List<JournalEntryDto> journalList = user.getJournalEntries()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return journalList.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(journalList);
    }

    @PostMapping
    @Operation(summary = "Add a journal entry for the logged-in user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JournalEntryRequestDto.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Journal entry created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload")
            })
    public ResponseEntity<JournalEntryDto> addJournalForUser(@RequestBody JournalEntryRequestDto entryDto) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            var entry = mapper.toEntity(entryDto);
            journalEntryServices.SaveEntry(entry, userName);
            return new ResponseEntity<>(mapper.toDTO(entry), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myid}")
    @Operation(summary = "Get a single journal entry by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Journal entry found"),
                    @ApiResponse(responseCode = "404", description = "Journal entry not found or not owned by user")
            })
    public ResponseEntity<JournalEntryDto> findById(@PathVariable String myid) {
        ObjectId objectId = new ObjectId(myid);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userServices.find_by_userName(userName);

        boolean ownsEntry = user.getJournalEntries()
                .stream()
                .anyMatch(x -> x.getId().equals(objectId));

        if (!ownsEntry) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Optional<JournalEntry> journal = journalEntryServices.find_by_id(objectId);
        return journal.map(e -> ResponseEntity.ok(mapper.toDTO(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/id/{myid}")
    @Operation(summary = "Update a journal entry by ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JournalEntryRequestDto.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Journal entry updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Journal entry not found or not owned by user")
            })
    public ResponseEntity<JournalEntryDto> updateById(@PathVariable ObjectId myid,
                                                      @RequestBody JournalEntryRequestDto newEntryDto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userServices.find_by_userName(userName);

        boolean ownsEntry = user.getJournalEntries()
                .stream()
                .anyMatch(x -> x.getId().equals(myid));
        if (!ownsEntry) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Optional<JournalEntry> journal = journalEntryServices.find_by_id(myid);
        if (journal.isPresent()) {
            var existing = journal.get();

            if (!newEntryDto.name().isEmpty()) {
                existing.setName(newEntryDto.name());
            }
            if (!newEntryDto.content().isEmpty()) {
                existing.setContent(newEntryDto.content());
            }

            // Sentiment is always non-null due to @NonNull in DTO
            existing.setSentiment(newEntryDto.sentiment());

            journalEntryServices.SaveEntry(existing);
            return ResponseEntity.ok(mapper.toDTO(existing));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/id/{myid}")
    @Operation(summary = "Delete a journal entry by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Journal entry deleted"),
                    @ApiResponse(responseCode = "404", description = "Journal entry not found or not owned by user")
            })
    public ResponseEntity<Void> deleteById(@PathVariable("myid") ObjectId id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean removed = journalEntryServices.delete_by_id(id, userName);
        return removed
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
