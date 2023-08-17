package com.jiggycode.journalentry;

import com.jiggycode.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

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
}
