package com.jiggycode.journalentry;

import com.jiggycode.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name="journal_entry",
        uniqueConstraints = {
                @UniqueConstraint( // in order to store only one date e.g. 2023-08-17 per journal entry
                     name = "journal_entry_creation_date_unique",
                        columnNames = "creation_date"
                )
        }
)
public class JournalEntry {

    @Id
    @SequenceGenerator(
            name = "journal_entry_id_seq",
            sequenceName = "journal_entry_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "journal_entry_id_seq"
    )
    private Integer id;

    @Column(
            name = "creation_date",
            nullable = false
    )
    private LocalDate creationDate;

    @Column(
            nullable = false
    )
    private String subject; // main idea / title of journal entry?

    @Column(
            columnDefinition = "TEXT",
            nullable = false
    )
    private String content;

    // Naive way of storing actual sentiment scores from AWS Comprehend (Neutral, Positive, Negative, Mixed)
    @Column(
            nullable = false
    )
    private double neutralSentimentScore;

    @Column(
            nullable = false
    )
    private double positiveSentimentScore;

    @Column(
            nullable = false
    )
    private double negativeSentimentScore;

    @Column(
            nullable = false
    )
    private double mixedSentimentScore;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, getters, setters

    public JournalEntry() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getNeutralSentimentScore() {
        return neutralSentimentScore;
    }

    public void setNeutralSentimentScore(double neutralSentimentScore) {
        this.neutralSentimentScore = neutralSentimentScore;
    }

    public double getPositiveSentimentScore() {
        return positiveSentimentScore;
    }

    public void setPositiveSentimentScore(double positiveSentimentScore) {
        this.positiveSentimentScore = positiveSentimentScore;
    }

    public double getNegativeSentimentScore() {
        return negativeSentimentScore;
    }

    public void setNegativeSentimentScore(double negativeSentimentScore) {
        this.negativeSentimentScore = negativeSentimentScore;
    }

    public double getMixedSentimentScore() {
        return mixedSentimentScore;
    }

    public void setMixedSentimentScore(double mixedSentimentScore) {
        this.mixedSentimentScore = mixedSentimentScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalEntry that = (JournalEntry) o;
        return Double.compare(neutralSentimentScore, that.neutralSentimentScore) == 0 && Double.compare(positiveSentimentScore, that.positiveSentimentScore) == 0 && Double.compare(negativeSentimentScore, that.negativeSentimentScore) == 0 && Double.compare(mixedSentimentScore, that.mixedSentimentScore) == 0 && Objects.equals(id, that.id) && Objects.equals(creationDate, that.creationDate) && Objects.equals(subject, that.subject) && Objects.equals(content, that.content) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, subject, content, neutralSentimentScore, positiveSentimentScore, negativeSentimentScore, mixedSentimentScore, user);
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", neutralSentimentScore=" + neutralSentimentScore +
                ", positiveSentimentScore=" + positiveSentimentScore +
                ", negativeSentimentScore=" + negativeSentimentScore +
                ", mixedSentimentScore=" + mixedSentimentScore +
                ", user=" + user +
                '}';
    }
}
