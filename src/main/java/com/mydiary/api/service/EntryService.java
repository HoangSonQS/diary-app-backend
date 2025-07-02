package com.mydiary.api.service;

import com.mydiary.api.dto.EntryDto;
import java.util.List;

public interface EntryService {
    EntryDto createEntry(EntryDto entryDto, String username);
    List<EntryDto> getEntriesForUser(String username);
    // Chúng ta sẽ thêm các phương thức khác sau
    EntryDto updateEntry(Long entryId, EntryDto entryDto, String username);
    void deleteEntry(Long entryId, String username);
    List<EntryDto> getEntriesByTag(String tagName, String username);

    // Thêm vào file EntryService.java
    List<EntryDto> searchEntries(String username, String keyword);

    List<EntryDto> getOnThisDayEntries(String username);
}