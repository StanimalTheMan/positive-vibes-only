package com.jiggycode.journalentry;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry,Integer> {
    List<JournalEntry> findByAuthorId(Integer authorId);

    @Transactional
    void deleteByAuthorId(Integer authorId);
}
