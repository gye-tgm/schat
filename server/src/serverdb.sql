DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id            TEXT,
    public_key    BLOB,
    symmetric_key BLOB,
    PRIMARY KEY(id)
);
DROP TABLE IF EXISTS message;
CREATE TABLE message (
    receiver_id     TEXT,
    content         BLOB,
    PRIMARY KEY(receiver_id, content)
);
