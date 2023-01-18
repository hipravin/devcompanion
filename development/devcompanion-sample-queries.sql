SET search_path to devcompanion;

select r.*, f.*
from repo_file f
         join repo r on f.repo_id = r.id
where f.id in (22226, 17963);

select *
from flyway_schema_history;

select count(*)
from repo_file;

delete
from repo
where relative_path like '%\\%';

select *
from repo_file
where content ilike '%Suppose we require the user ID of the file owner on a system that%';

select r.*, rf.*
from repo_file rf
         inner join repo r on r.id = rf.repo_id
where content ilike '%BigDecimal%';

explain
analyze
select count(*)
from repo_file;

explain
analyze
select count(null)
from repo_file;

explain
analyze
select count(1)
from repo_file;

select count(*)
from repo_file
where content like '%%'
  and content like '%%';

explain
analyze
select *
from repo_file
where content like '%BigDecimal%';

explain
analyze
select *
from repo_file
where content ilike '%BigDecimal%';

select count(*)
from repo_file
where content ilike '%BigDecimal%';

explain
select *
from repo_file
where name = 'i';

select *
from repo;

delete
from repo;

select distinct name
from repo_file;

select *
from repo_file
where content ilike 'Dockerfile';
