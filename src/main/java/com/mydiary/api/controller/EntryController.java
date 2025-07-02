package com.mydiary.api.controller;

import com.mydiary.api.dto.EntryDto;
import com.mydiary.api.service.EntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

    @Autowired
    private EntryService entryService;

    // API tạo bài viết mới (bảo vệ)
    @PostMapping
    public ResponseEntity<EntryDto> createEntry(@Valid @RequestBody EntryDto entryDto, Authentication authentication) {
        // Lấy username của người dùng đã đăng nhập từ đối tượng Authentication
        String username = authentication.getName();
        EntryDto createdEntry = entryService.createEntry(entryDto, username);
        return new ResponseEntity<>(createdEntry, HttpStatus.CREATED);
    }

    // API lấy tất cả bài viết của người dùng hiện tại (bảo vệ)
    @GetMapping
    public ResponseEntity<List<EntryDto>> getAllEntries(Authentication authentication) {
        String username = authentication.getName();
        List<EntryDto> entries = entryService.getEntriesForUser(username);
        return ResponseEntity.ok(entries);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EntryDto> updateEntry(@PathVariable(name = "id") Long entryId,
                                                @Valid @RequestBody EntryDto entryDto,
                                                Authentication authentication) {
        String username = authentication.getName();
        EntryDto updatedEntry = entryService.updateEntry(entryId, entryDto, username);
        return ResponseEntity.ok(updatedEntry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable(name = "id") Long entryId,
                                                Authentication authentication) {
        String username = authentication.getName();
        entryService.deleteEntry(entryId, username);
        return ResponseEntity.ok("Entry deleted successfully!");
    }

    @GetMapping("/tag/{tagName}")
    public ResponseEntity<List<EntryDto>> getEntriesByTag(@PathVariable String tagName, Authentication authentication) {
        String username = authentication.getName();
        List<EntryDto> entries = entryService.getEntriesByTag(tagName, username);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EntryDto>> searchEntries(@RequestParam("keyword") String keyword,
                                                        Authentication authentication) {
        String username = authentication.getName();
        List<EntryDto> entries = entryService.searchEntries(username, keyword);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/on-this-day")
    public ResponseEntity<List<EntryDto>> getOnThisDayEntries(Authentication authentication) {
        String username = authentication.getName();
        List<EntryDto> entries = entryService.getOnThisDayEntries(username);
        return ResponseEntity.ok(entries);
    }
}