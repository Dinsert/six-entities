create EXTENSION IF NOT EXISTS pgcrypto;

create SCHEMA IF NOT EXISTS app;

create TABLE IF NOT EXISTS app.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username TEXT NOT NULL
);

create TABLE IF NOT EXISTS app.coupons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    discount NUMERIC(10, 2) NOT NULL
);

create TABLE IF NOT EXISTS app.user_coupons (
    user_id UUID NOT NULL,
    coupon_id UUID NOT NULL,
    PRIMARY KEY (user_id, coupon_id),
    FOREIGN KEY (user_id) REFERENCES app.users(id),
    FOREIGN KEY (coupon_id) REFERENCES app.coupons(id)
);

create TABLE IF NOT EXISTS app.profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login TEXT NOT NULL,
    password TEXT NOT NULL
);

create TABLE IF NOT EXISTS app.players (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    profile_id UUID NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES app.profiles(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS app.readers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL
);

create TABLE IF NOT EXISTS app.books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    author TEXT NOT NULL,
    reader_id UUID NOT NULL,
    FOREIGN KEY (reader_id) REFERENCES app.readers(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS app.outbox_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    message_key TEXT NOT NULL,
    payload JSONB NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);