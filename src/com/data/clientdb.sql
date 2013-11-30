CREATE TABLE myuser (
    id            TEXT,
    public_key    BLOB,
    symmetric_key BLOB,
    PRIMARY KEY(id)
);

CREATE TABLE message (
    sender_id       TEXT,
    receiver_id     TEXT,
    mtimestamp      INTEGER, /* unix time format */
    content         TEXT,
    PRIMARY KEY(sender_id, receiver_id, mtimestamp)
);
