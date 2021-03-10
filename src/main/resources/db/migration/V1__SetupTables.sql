CREATE TABLE policy_violation
(
    id   VARCHAR(1000) NOT NULL PRIMARY KEY,
    message_id VARCHAR(1000) NOT NULL,
    thread_id VARCHAR(1000) NOT NULL,
    user_id VARCHAR(1000) NOT NULL,
    word VARCHAR(1000) NOT NULL
);
