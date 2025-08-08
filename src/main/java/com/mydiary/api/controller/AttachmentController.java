package com.mydiary.api.controller;

import com.mydiary.api.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/entries/{entryId}/attachments")
public class AttachmentController {

    @Autowired
    private EntryService entryService;

    @PostMapping
    public ResponseEntity<String> uploadAttachment(@PathVariable Long entryId,
                                                   @RequestParam("file") MultipartFile file,
                                                   @RequestParam(value = "caption", required = false) String caption,
                                                   Authentication authentication) {
        try {
            String username = authentication.getName();
            entryService.addAttachmentToEntry(entryId, file, caption, username);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        }
    }
}