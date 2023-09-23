CREATE TABLE author (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    age INT NOT NULL
);

CREATE TABLE journal_entry (
    id SERIAL PRIMARY KEY,
    creation_date DATE NOT NULL,
    updated_date DATE NOT NULL,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    neutral_sentiment_score DOUBLE PRECISION NOT NULL,
    positive_sentiment_score DOUBLE PRECISION NOT NULL,
    negative_sentiment_score DOUBLE PRECISION NOT NULL,
    mixed_sentiment_score DOUBLE PRECISION NOT NULL,
    author_id INT NOT NULL REFERENCES author(id) ON DELETE CASCADE
);