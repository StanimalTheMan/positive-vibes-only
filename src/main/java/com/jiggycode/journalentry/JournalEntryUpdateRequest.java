package com.jiggycode.journalentry;

public record JournalEntryUpdateRequest (
    String subject,
    String content
) {

}
