package com.mydiary.api.service;

import com.mydiary.api.dto.EntryDto;
import java.util.List;

public interface EntryService {
    EntryDto createEntry(EntryDto entryDto, String username);
    List<EntryDto> getEntriesForUser(String username);
    // Chúng ta sẽ thêm các phương thức khác sau
}