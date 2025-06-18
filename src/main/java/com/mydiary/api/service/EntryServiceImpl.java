package com.mydiary.api.service;

import com.mydiary.api.dto.EntryDto;
import com.mydiary.api.entity.Entry;
import com.mydiary.api.entity.Tag;
import com.mydiary.api.entity.User;
import com.mydiary.api.repository.EntryRepository;
import com.mydiary.api.repository.TagRepository;
import com.mydiary.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EntryServiceImpl implements EntryService {

    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    @Transactional // Đảm bảo tất cả các thao tác CSDL trong hàm này là một giao dịch
    public EntryDto createEntry(EntryDto entryDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Entry entry = new Entry();
        entry.setContent(entryDto.getContent());
        entry.setEntryDate(LocalDate.now());
        entry.setUser(user);

        // Xử lý tags
        if (entryDto.getTags() != null && !entryDto.getTags().isEmpty()) {
            Set<Tag> managedTags = new HashSet<>();
            for (String tagName : entryDto.getTags()) {
                // Tìm tag trong CSDL, nếu không có thì tạo mới
                Tag tag = tagRepository.findByName(tagName.trim().toLowerCase())
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(tagName.trim().toLowerCase());
                            return tagRepository.save(newTag);
                        });
                managedTags.add(tag);
            }
            entry.setTags(managedTags);
        }

        Entry savedEntry = entryRepository.save(entry);
        return mapToDto(savedEntry);
    }

    @Override
    public List<EntryDto> getEntriesForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<Entry> entries = entryRepository.findAllByUserOrderByEntryDateDesc(user);
        return entries.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Hàm tiện ích để chuyển đổi từ Entity sang DTO
    private EntryDto mapToDto(Entry entry) {
        EntryDto entryDto = new EntryDto();
        entryDto.setId(entry.getId());
        entryDto.setContent(entry.getContent());
        entryDto.setEntryDate(entry.getEntryDate());
        if (entry.getTags() != null) {
            entryDto.setTags(entry.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        }
        return entryDto;
    }
}