package com.jiggycode.journalentry;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class JournalEntryKey implements Serializable {
    private LocalDate creationDate;
    private Long authorId;

    public JournalEntryKey(LocalDate creationDate, Long authorId) {
        this.creationDate = creationDate;
        this.authorId = authorId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalEntryKey that = (JournalEntryKey) o;
        return Objects.equals(creationDate, that.creationDate) && Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationDate, authorId);
    }
}
