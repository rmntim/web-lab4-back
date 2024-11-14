-- Create tables if they do not exist
-- User table
CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password TEXT                NOT NULL,
    role     VARCHAR(255)        NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    email    varchar(255)
);

-- User settings table
CREATE TABLE IF NOT EXISTS user_settings
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER      NOT NULL REFERENCES users (id),
    theme   VARCHAR(255) NOT NULL DEFAULT 'light'
);

-- Point model table
CREATE TABLE IF NOT EXISTS point_model
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER          NOT NULL REFERENCES users (id),
    x       DOUBLE PRECISION NOT NULL,
    y       DOUBLE PRECISION NOT NULL,
    r       DOUBLE PRECISION NOT NULL,
    result  BOOLEAN          NOT NULL
);

-- User sessions table
CREATE TABLE IF NOT EXISTS user_sessions
(
    id            SERIAL PRIMARY KEY,
    user_id       INTEGER   NOT NULL REFERENCES users (id),
    session_start TIMESTAMP NOT NULL,
    session_end   TIMESTAMP,
    last_activity TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

