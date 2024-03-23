CREATE SCHEMA masteringp13;
-- # psql -d playground -U postgres
set search_path to masteringp13,public;
--
SELECT count(*),
       count(*) FILTER (WHERE id < 5),
       count(*) FILTER (WHERE id > 2)
FROM generate_series(1, 10) AS id;

--

SELECT *
FROM generate_series(1, 4) AS x,
     LATERAL (SELECT array_agg(y)
              FROM generate_series(1, x) AS y
         ) AS z;
--
WITH x AS (SELECT avg(id)
           FROM generate_series(1, 10) AS id)
SELECT *, y - (SELECT avg FROM x) AS diff
FROM generate_series(1, 10) AS y
WHERE y > (SELECT avg FROM x);


--missing indices
SELECT schemaname, relname, seq_scan, seq_tup_read,
       idx_scan, seq_tup_read / seq_scan AS avg
FROM pg_stat_user_tables
WHERE seq_scan > 0
ORDER BY seq_tup_read DESC
LIMIT 20;

--

CREATE EXTENSION btree_gist with schema pg_catalog;

select * from pg_available_extensions where name like '%trg%';
select * from pg_available_extension_versions where name like '%gist%';
----
-- NOT NULL DEFAULT nextval('masteringp13.t_perf_insert_serial_id_seq'::regclass)

drop table t_uuid;

CREATE TABLE masteringp13.t_uuid
(
    id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    name text
);
insert into t_uuid (name) values ('name1'), ('name2');

select avg(id) from (
    select gen_random_uuid() uuid, id from generate_series(1, 1000000) as id) x ;

insert into t_uuid (id, name) values ('5f998d32-b269-42b7-ad19-38ed08feb9fb', '1');

insert into t_uuid (id, name) values (gen_random_uuid(), 'name1'), (gen_random_uuid(), 'name2');

select * from t_uuid;

select gen_random_uuid();
----


show search_path;
---
SELECT pg_create_restore_point('my_daily_process_ended');
---
drop table t_perf_insert_assigned;
drop table t_perf_insert_serial;
---
create table t_perf_insert_assigned (
  id bigint not null primary key,
  name text
);
create table t_perf_insert_serial (
  id bigserial not null primary key,
  name text
);

select * from t_perf_insert_assigned limit 100;
select * from t_perf_insert_serial limit 100;

truncate t_perf_insert_assigned;
truncate t_perf_insert_serial;

insert into t_perf_insert_assigned
    select id, 'something' from generate_series(1, 100000) id;

insert into t_perf_insert_serial (name)
    select 'something' from generate_series(1, 100000);


select * from pg_stat_ssl;
--- cursors
select * from c(100);

CREATE OR REPLACE FUNCTION c(int)
RETURNS setof text AS
$$
DECLARE
 v_rec record;
BEGIN
 FOR v_rec IN SELECT tablename
 FROM pg_tables
 LIMIT $1
 LOOP
 RETURN NEXT v_rec.tablename;
 END LOOP;
 RETURN;
END;
$$ LANGUAGE 'plpgsql';


--- anonymous code blocks


DO
$$
    BEGIN
        RAISE NOTICE 'current time: %', now();
    END;
$$ LANGUAGE 'plpgsql';

---
select error_test1(1,0);
---
CREATE FUNCTION error_test1(int, int) RETURNS int AS
$$
BEGIN
    RAISE NOTICE 'debug message: % / %', $1, $2;
    BEGIN
        RETURN $1 / $2;
    EXCEPTION
        WHEN division_by_zero THEN
            RAISE NOTICE 'division by zero detected: %', sqlerrm;
        WHEN others THEN
            RAISE NOTICE 'some other error: %', sqlerrm;
    END;
    RAISE NOTICE 'all errors handled';
    RETURN 0;
END;
$$ LANGUAGE 'plpgsql';

----
show join_collapse_limit;
show work_mem;

EXPLAIN WITH x AS
                 (
                     SELECT *
                     FROM generate_series(1, 1000) AS id
                 )

        SELECT *
        FROM x AS a
                 JOIN x AS b ON (a.id = b.id)
                 JOIN x AS c ON (b.id = c.id)
                 JOIN x AS d ON (c.id = d.id)
                 JOIN x AS e ON (d.id = e.id)
                 JOIN x AS f ON (e.id = f.id);

----

SHOW config_file;

SELECT round((100 * total_exec_time / sum(total_exec_time)
                                      OVER ())::numeric, 2) percent,
       round(total_exec_time::numeric, 2) AS total,
       calls,
       round(mean_exec_time::numeric, 2) AS mean,
       substring(query, 1, 40)
FROM pg_stat_statements

ORDER BY total_exec_time DESC
LIMIT 10;


select * from pg_stat_statements where query ilike '%ITEM%';

---- LOGS STATS

select * from pg_stat_activity;

select * from pg_stat_database;

select * from pg_stat_user_tables;
select * from pg_statio_user_tables;

-- which tables may need indexes
SELECT schemaname, relname, seq_scan, seq_tup_read,
       seq_tup_read / seq_scan AS avg, idx_scan
FROM pg_stat_user_tables
WHERE seq_scan > 0
ORDER BY seq_tup_read DESC LIMIT 25;

--
select * from pg_stat_user_indexes;

--
SELECT schemaname, relname, indexrelname, idx_scan,
       pg_size_pretty(pg_relation_size(indexrelid)) AS idx_size,
       pg_size_pretty(sum(pg_relation_size(indexrelid))
                      OVER (ORDER BY idx_scan, indexrelid)) AS total
FROM pg_stat_user_indexes
ORDER BY 6 ;

--
select * from pg_stat_bgwriter;
select * from pg_stat_ssl;
-- transactions
select * from pg_stat_xact_user_tables;

-- vacuum
select * from pg_stat_progress_vacuum;

create extension pg_stat_statements;

select * from pg_stat_statements;

----


drop table t_test;

create table t_test(id serial, name text);

insert into t_test (name) select 'hans' from generate_series(1, 2000000) as id;

insert into t_test (name) select 'paul' from generate_series(1, 2000000) as id;


select name, count(*) from t_test group by 1;


EXPLAIN select * from t_test where id = 234569;

SELECT pg_size_pretty(pg_relation_size('t_test'));

SELECT pg_relation_size('t_test') / 8192;

SHOW cpu_tuple_cost;
SHOW cpu_operator_cost;

SHOW random_page_cost; -- consider setting to 1 for SSD

create index idx_id on t_test(id);



CREATE TABLE t_sva (sva text);
INSERT INTO t_sva VALUES ('1118090878');
INSERT INTO t_sva VALUES ('2345010477');



select sva, normalize_si(sva) from t_sva;

CREATE OR REPLACE FUNCTION normalize_si(text) RETURNS text AS $$
BEGIN
    RETURN substring($1, 9, 2) ||
           substring($1, 7, 2) ||
           substring($1, 5, 2) ||
           substring($1, 1, 4);
END; $$
    LANGUAGE 'plpgsql' IMMUTABLE;

---
CREATE OR REPLACE FUNCTION si_lt(text, text) RETURNS boolean AS $$
BEGIN
    RETURN normalize_si($1) < normalize_si($2);
END;
$$ LANGUAGE 'plpgsql' IMMUTABLE;

-- lower equals
CREATE OR REPLACE FUNCTION si_le(text, text)
    RETURNS boolean AS
$$
BEGIN
    RETURN normalize_si($1) <= normalize_si($2);
END;
$$
    LANGUAGE 'plpgsql' IMMUTABLE;
-- greater equal
CREATE OR REPLACE FUNCTION si_ge(text, text)
    RETURNS boolean AS
$$
BEGIN
    RETURN normalize_si($1) >= normalize_si($2);
END;
$$
    LANGUAGE 'plpgsql' IMMUTABLE;
-- greater
CREATE OR REPLACE FUNCTION si_gt(text, text)
    RETURNS boolean AS
$$
BEGIN
    RETURN normalize_si($1) > normalize_si($2);
END;
$$
    LANGUAGE 'plpgsql' IMMUTABLE;

-- define operators
CREATE OPERATOR <# ( PROCEDURE=si_lt,
    LEFTARG=text,
    RIGHTARG=text);

CREATE OPERATOR <=# ( PROCEDURE=si_le,
    LEFTARG=text,
    RIGHTARG=text);
CREATE OPERATOR >=# ( PROCEDURE=si_ge,
    LEFTARG=text,
    RIGHTARG=text);
CREATE OPERATOR ># ( PROCEDURE=si_gt,
    LEFTARG=text,
    RIGHTARG=text);

CREATE OR REPLACE FUNCTION si_same(text, text) RETURNS int AS $$
BEGIN
    IF normalize_si($1) < normalize_si($2)
    THEN
        RETURN -1;
    ELSIF normalize_si($1) > normalize_si($2)
    THEN
        RETURN +1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE 'plpgsql' IMMUTABLE;

CREATE OPERATOR CLASS sva_special_ops
    FOR TYPE text USING btree
    AS
    OPERATOR 1 <# ,
    OPERATOR 2 <=# ,
    OPERATOR 3 = ,
    OPERATOR 4 >=# ,
    OPERATOR 5 ># ,
    FUNCTION 1 si_same(text, text);

CREATE INDEX idx_special ON t_sva (sva sva_special_ops);


SET enable_seqscan TO off;

SET enable_seqscan TO on;

explain SELECT * FROM t_sva WHERE sva = '0000112273';

select * from pg_am;

select to_tsvector('a car car car cat cats sdfh hfgh dfgh русский русская');

SELECT cfgname FROM pg_ts_config;

select

select * from pg_stat_statements; -- ???
select * from pg_stat_user_tables;

call samplesjpaid.transfer_multiple();

select * from samplesjpaid.account where id in (1,2,3) FOR UPDATE;


select pg_advisory_lock(2);
select pg_advisory_unlock(2);