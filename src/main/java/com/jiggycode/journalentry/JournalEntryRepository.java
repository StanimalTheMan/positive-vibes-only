package com.jiggycode.journalentry;

import com.jiggycode.author.Author;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry,Integer> {
    List<JournalEntry> findByAuthorId(Integer authorId);

    @Transactional
    void deleteByAuthorId(Integer authorId);

    boolean existsByCreationDateAndAuthor(LocalDate creationDate, Author author);
}
