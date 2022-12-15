CREATE TABLE REPO
(
    ID   BIGSERIAL PRIMARY KEY,
    NAME TEXT NOT NULL,
    RELATIVE_PATH TEXT   NOT NULL,
    CONSTRAINT r_name_length CHECK (length(name) > 0 AND length(name) <= 512)
);
ALTER SEQUENCE REPO_ID_SEQ INCREMENT BY 100 RESTART 100;
CREATE UNIQUE INDEX REPO_RELATIVE_PATH_UNIQUE_IDX ON REPO (RELATIVE_PATH);

CREATE TABLE REPO_FILE
(
    ID            BIGSERIAL PRIMARY KEY,
    REPO_ID       BIGINT NOT NULL REFERENCES REPO (ID) ON DELETE CASCADE,
    NAME          TEXT   NOT NULL,
    RELATIVE_PATH TEXT   NOT NULL,
    CONTENT_TYPE  TEXT   NOT NULL,
    SIZE_BYTES    BIGINT,
    CONTENT       TEXT   NOT NULL,
    CONSTRAINT rf_name_length CHECK (length(name) > 0 AND length(name) <= 512)
);

ALTER SEQUENCE REPO_FILE_ID_SEQ INCREMENT BY 100 RESTART 100;
CREATE INDEX RF_R_ID_IDX ON REPO_FILE (REPO_ID);
CREATE INDEX RF_NAME_IDX ON REPO_FILE (NAME);
CREATE UNIQUE INDEX R_F_NAME_ID_UNIQUE_IDX ON REPO_FILE (REPO_ID, RELATIVE_PATH);

CREATE EXTENSION pg_trgm;
CREATE INDEX name ON repo_file USING gin(content gin_trgm_ops); --for queries of type "like '%term%'"