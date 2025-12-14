CREATE EXTENSION IF NOT EXISTS 'pgcrypto';

CREATE TABLE IF NOT EXISTS app.users (
    id UUID PRIMARY KEY gen-random-uuid(),
    username TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS app.coupons (
    id UUID PRIMARY KEY gen-random-uuid(),
    discount INT NOT NULL
);

CREATE TABLE IF NOT EXISTS app.user_coupons (
    user_id UUID NOT NULL,
    coupon_id UUID NOT NULL,
    PRIMARY KEY (user_id, coupon_id),
    FOREIGN KEY (user_id) REFERENCES app.users(id),
    FOREIGN KEY (coupon_id) REFERENCES app.coupons(id)
);

CREATE TABLE IF NOT EXISTS app.profiles (
    id UUID PRIMARY KEY gen-random-uuid(),
    login TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS app.players (
    id UUID PRIMARY KEY gen-random-uuid(),
    name TEXT NOT NULL,
    profile_id UUID NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES app.profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS app.readers (
    id UUID PRIMARY KEY gen-random-uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS app.books (
    id UUID PRIMARY KEY gen-random-uuid(),
    name TEXT NOT NULL,
    author TEXT NOT NULL,
    reader_id UUID NOT NULL,
    FOREIGN KEY (reader_id) REFERENCES app.books(id) ON DELETE CASCADE
);