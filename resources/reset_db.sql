-- Drop existing tables with CASCADE to remove dependent objects
DROP TABLE IF EXISTS user_sessions CASCADE;
DROP TABLE IF EXISTS point_model CASCADE;
DROP TABLE IF EXISTS user_settings CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TYPE IF EXISTS role_enum CASCADE;

-- Recreate the tables
-- User table
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password TEXT                NOT NULL,
    role     VARCHAR(255)        NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    email    varchar(255)
);

-- User settings table
CREATE TABLE user_settings
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER      NOT NULL REFERENCES users (id),
    theme   VARCHAR(255) NOT NULL DEFAULT 'light'
);

-- Point model table
CREATE TABLE point_model
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER          NOT NULL REFERENCES users (id),
    x       DOUBLE PRECISION NOT NULL,
    y       DOUBLE PRECISION NOT NULL,
    r       DOUBLE PRECISION NOT NULL,
    result  BOOLEAN          NOT NULL
);

-- User sessions table
CREATE TABLE user_sessions
(
    id            SERIAL PRIMARY KEY,
    user_id       INTEGER   NOT NULL REFERENCES users (id),
    session_start TIMESTAMP NOT NULL,
    session_end   TIMESTAMP,
    last_activity TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

