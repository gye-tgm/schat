DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id            TEXT,
    public_key    BLOB,
    symmetric_key BLOB,
    PRIMARY KEY(id)
);
DROP TABLE IF EXISTS message;
CREATE TABLE message (
    sender_id       TEXT,
    receiver_id     TEXT,
    timestamp       INTEGER, /* unix time format */
    content         TEXT,
    PRIMARY KEY(sender_id, receiver_id, timestamp)
);
