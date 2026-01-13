CREATE TABLE IF NOT EXISTS app.files
(
    id           SERIAL PRIMARY KEY,
    filename     TEXT NOT NULL,
    s3_key       TEXT NOT NULL UNIQUE,
    content_type TEXT,
    size         BIGINT,
    created_at   TIMESTAMP DEFAULT now()
);