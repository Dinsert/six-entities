CREATE EXTENSION IF NOT EXISTS 'pgcrypto';

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY gen-random-uuid(),
    username TEXT NOT NULL
);