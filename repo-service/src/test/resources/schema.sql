CREATE SEQUENCE repo_id_seq START WITH 100 INCREMENT BY 100;
CREATE SEQUENCE repo_file_id_seq START WITH 100 INCREMENT BY 100;

CREATE TABLE REPO
(
    ID   BIGSERIAL PRIMARY KEY,
    NAME TEXT NOT NULL,
    CONSTRAINT r_name_length CHECK (length(name) > 0 AND length(name) <= 512)
);
CREATE UNIQUE INDEX REPO_NAME_UNIQUE_IDX ON REPO (NAME);

CREATE TABLE REPO_FILE
(
    ID            BIGSERIAL PRIMARY KEY,
    REPO_ID       BIGINT NOT NULL REFERENCES REPO (ID),
    NAME          TEXT   NOT NULL,
    RELATIVE_PATH TEXT   NOT NULL,
    CONTENT_TYPE  TEXT   NOT NULL,
    SIZE_BYTES    BIGINT,
    CONTENT       TEXT   NOT NULL,
    CONSTRAINT rf_name_length CHECK (length(name) > 0 AND length(name) <= 512)
);

CREATE INDEX RF_R_ID_IDX ON REPO_FILE (REPO_ID);
CREATE INDEX RF_NAME_IDX ON REPO_FILE (NAME);
CREATE UNIQUE INDEX R_F_NAME_ID_UNIQUE_IDX ON REPO_FILE (REPO_ID, RELATIVE_PATH);