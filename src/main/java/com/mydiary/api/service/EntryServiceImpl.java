package com.mydiary.api.service;

import com.mydiary.api.dto.EntryDto;
import com.mydiary.api.dto.MoodDto;
import com.mydiary.api.entity.Entry;
import com.mydiary.api.entity.Mood;
import com.mydiary.api.entity.Tag;
import com.mydiary.api.entity.User;
import com.mydiary.api.exception.ResourceNotFoundException;
import com.mydiary.api.repository.EntryRepository;
import com.mydiary.api.repository.MoodRepository;
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
    @Autowired
    private MoodRepository moodRepository;
    @Autowired
    private AchievementService achievementService;

    @Override
    @Transactional // Đảm bảo tất cả các thao tác CSDL trong hàm này là một giao dịch
    public EntryDto createEntry(EntryDto entryDto, String username) {
        User user = findUserByUsername(username);

        LocalDate today = LocalDate.now();

        // Logic mới: Kiểm tra xem đã có bài chính trong ngày chưa
        boolean isFirstEntryOfDay = !entryRepository.findByUserAndEntryDate(user, today).stream()
                .anyMatch(Entry::getIsPrimary);

        Entry entry = new Entry();
        entry.setTitle(entryDto.getTitle());
        entry.setContent(entryDto.getContent());
        entry.setEntryDate(today);
        entry.setUser(user);
        entry.setIsPrimary(isFirstEntryOfDay);

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

        // Xử lý mood
        if (entryDto.getMoodId() != null) {
            Mood mood = moodRepository.findById(entryDto.getMoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mood not found with id: " + entryDto.getMoodId()));
            entry.setMood(mood);
        }

        Entry savedEntry = entryRepository.save(entry);

        achievementService.checkAndGrantAchievements(user);

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
        entryDto.setTitle(entry.getTitle());
        entryDto.setContent(entry.getContent());
        entryDto.setEntryDate(entry.getEntryDate());
        if (entry.getTags() != null) {
            entryDto.setTags(entry.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        }

        if (entry.getMood() != null) {
            MoodDto moodDto = new MoodDto();
            moodDto.setId(entry.getMood().getId());
            moodDto.setName(entry.getMood().getName());
            moodDto.setIconName(entry.getMood().getIconName());
            entryDto.setMood(moodDto);
        }

        return entryDto;
    }

    @Override
    public EntryDto updateEntry(Long entryId, EntryDto entryDto, String username) {
        Entry entry = findAndVerifyEntryOwnership(entryId, username);
        //Cap nhat nd
        entry.setTitle(entryDto.getTitle());
        entry.setContent(entryDto.getContent());

        entry.getTags().clear();
        if(entryDto.getTags() != null && !entryDto.getTags().isEmpty()) {
            Set<Tag> managedTags = new HashSet<>();
            for (String tagName : entryDto.getTags()) {
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

        if (entryDto.getMoodId() != null) {
            Mood mood = moodRepository.findById(entryDto.getMoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mood not found with id: " + entryDto.getMoodId()));
            entry.setMood(mood);
        } else {
            entry.setMood(null); // Cho phép bỏ mood nếu không gửi moodId
        }

        Entry updatedEntry = entryRepository.save(entry);

        return mapToDto(updatedEntry);
    }

    @Override
    public void deleteEntry(Long entryId, String username) {
        Entry entry = findAndVerifyEntryOwnership(entryId, username);

        entryRepository.delete(entry);
    }

    @Override
    public List<EntryDto> getEntriesByTag(String tagName, String username) {
        User user = findUserByUsername(username);

        List<Entry> entries = entryRepository.findAllByUserOrderByEntryDateDesc(user)
                .stream()
                .filter(entry -> entry.getTags().stream()
                        .anyMatch(tag -> tag.getName().equalsIgnoreCase(tagName.trim().toLowerCase())))
                .collect(Collectors.toList());

        return entries.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<EntryDto> searchEntries(String username, String keyword) {
        User user = findUserByUsername(username); // Dùng lại hàm helper

        List<Entry> entries = entryRepository.findByUserAndContentContainingIgnoreCaseOrderByEntryDateDesc(user, keyword);

        return entries.stream()
                .map(this::mapToDto) // Dùng lại hàm helper mapToDto
                .collect(Collectors.toList());
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private Entry findAndVerifyEntryOwnership(Long entryId, String username) {
        User user = findUserByUsername(username);

        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + entryId));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("You do not have permission to access this entry");
        }

        return entry;
    }

    @Override
    public List<EntryDto> getOnThisDayEntries(String username) {
        User user = findUserByUsername(username); // Dùng lại hàm helper
        LocalDate today = LocalDate.now();

        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        List<Entry> entries = entryRepository.findOnThisDay(user, month, day);

        return entries.stream()
                .map(this::mapToDto) // Dùng lại hàm helper mapToDto
                .collect(Collectors.toList());
    }

    // Thêm vào file EntryServiceImpl.java

    @Override
    @Transactional
    public EntryDto setPrimaryEntry(Long entryId, String username) {
        // 1. Tìm và xác thực bài viết người dùng muốn đặt làm bài chính
        Entry newPrimaryEntry = findAndVerifyEntryOwnership(entryId, username);
        User user = newPrimaryEntry.getUser();
        LocalDate entryDate = newPrimaryEntry.getEntryDate();

        // 2. Tìm bài viết chính cũ trong cùng ngày (nếu có)
        entryRepository.findByUserAndEntryDateAndIsPrimaryTrue(user, entryDate)
                .ifPresent(oldPrimaryEntry -> {
                    // Nếu tìm thấy và nó không phải là bài đang được chọn,
                    // đặt nó về trạng thái "bài phụ"
                    if (!oldPrimaryEntry.getId().equals(entryId)) {
                        oldPrimaryEntry.setIsPrimary(false);
                        entryRepository.save(oldPrimaryEntry);
                    }
                });

        // 3. Đặt bài viết được chọn làm bài chính và lưu lại
        newPrimaryEntry.setIsPrimary(true);
        Entry savedEntry = entryRepository.save(newPrimaryEntry);

        return mapToDto(savedEntry);
    }

}