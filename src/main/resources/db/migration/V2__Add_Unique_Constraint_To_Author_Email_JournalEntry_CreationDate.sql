ALTER TABLE author
ADD CONSTRAINT author_email_unique UNIQUE (email);

-- Add a unique constraint on creation_date
ALTER TABLE journal_entry
ADD CONSTRAINT journal_entry_creation_date_unique UNIQUE (creation_date);