set search_path to sqlbook;

select * from emp;

select * from dept;

select * from (
select ename, job, sal as salary from emp) x where salary > 500
order by salary desc;

select ename || ' works as ' || job, row_number() over (order by sal desc) from emp order by empno;

select ename, sal,
       case when sal < 1000 then 'under'
            when sal > 2000 then 'over'
            else 'OK' end
from emp;

select * from emp order by sal desc limit 5;

select * from emp order by random() limit 5;

select coalesce(comm, 0), * from emp;

select * from emp where deptno in (10, 20)
    and (ename like '%I%' or job like '%ER%');

select * from emp order by 1,2,3,4;

select ename, job, substr(job, length(job) -1) from emp
    order by substr(job, length(job) -1);

select e.ename, d.loc from emp e, dept d
    where e.deptno = d.deptno and d.deptno = 10;

select e.ename, d.loc from emp e
    inner join dept d on (e.deptno = d.deptno)
    where d.deptno = 10;

select deptno from dept
    intersect
select deptno from emp;

select deptno from dept
    except
select deptno from emp;

select deptno from dept d
    where not exists(
        select 1 from emp e where e.deptno = d.deptno
        );

select d.* from dept d
    left outer join emp e on (d.deptno = e.deptno)
    where e.ename is null;

select e.ename, d.loc from emp e
    join dept d on e.deptno = d.deptno
    where d.deptno  = 10;

select e.ename, d.loc from emp e, dept d
    where e.deptno=d.deptno and e.deptno = 10;


select * from emp e
    full outer join dept d on e.deptno = d.deptno;


select * from emp e
    where coalesce(e.comm,0) < (select comm from emp where ename = 'WARD');

select * from emp e
    where comm is null or comm < (select comm from emp where ename = 'WARD');

--insert into
--create table t2 as select ...

update emp set sal = sal - 1 where deptno = 20;

--merge

--duplicates
create table dupes (id integer, name text);
insert into dupes values (1, 'NAPOLEON' );
insert into dupes values (2, 'DYNAМIТE' );
insert into dupes values (3, 'DYNAМIТE' );
insert into dupes values (4, 'SНЕ SELLS' );
insert into dupes values (5, 'SEA SНELLS' );
insert into dupes values (6, 'SEA SНELLS' );
insert into dupes values (7, 'SEA SНELLS' );

delete from dupes;

select * from dupes;

select name from dupes
group by name having count(id) > 1 ;


delete from dupes
       where name in
            (select name from dupes
                group by name having count(id) > 1)
        and (id, name) not in
            (select min(id), name from dupes
             group by name having count(id) > 1);


delete from dupes
     where id not in (select min(id)
        from dupes group by name) ;

---  METADATA ---
select * from information_schema.tables;

--tables in schema postgres
select table_name
     from information_schema.tables
 where table_schema = 'sqlbook';

--table columns
select column_name, data_type, ordinal_position from information_schema.columns
    where table_schema = 'sqlbook' and table_name = 'emp'
    order by ordinal_position;

--indexes on table
select ind.indexname, ind.indexdef from pg_indexes ind
where ind.tablename = 'emp' and ind.schemaname = 'sqlbook';

--constraints
select a.table_name,
       a.constraint_name,
       b.column_name,
       a.constraint_type
 from information_schema.table_constraints a,
    information_schema.key_column_usage b
 where a.table_name = 'emp'
    and a.table_schema = 'sqlbook'
    and a.table_name = b.table_name
    and a.table_schema = b.table_schema
    and a.constraint_name = b.constraint_name;


select fkeys.table_name,
    fkeys.constraint_name,
    fkeys.column_name,
    ind_cols.indexname
    from (
select a.constraint_schema,
       a.table_name,
       a.constraint_name,
       a.column_name
    from information_schema.key_column_usage a,
         information_schema.referential_constraints b
    where a.constraint_name = b.constraint_name
        and a.constraint_schema = b.constraint_schema
        and a.constraint_schema = 'sqlbook'
        and a.table_name = 'emp'
         ) fkeys
        left join
        (
    select a.schemaname, a.tablename, a.indexname, b.column_name
        from pg_catalog.pg_indexes a,
             information_schema.columns b

    where a.tablename = b.table_name
        and a.schemaname = b.table_schema
    ) ind_cols
        on (fkeys.constraint_schema = ind_cols.schemaname
            and fkeys.table_name = ind_cols.tablename
            and fkeys.column_name = ind_cols.column_name)
    where ind_cols.indexname is null; --not sure it works

---

--count, sum, avg, min, max

select ename, sal,
       sum(sal) over (order by sal, empno) as running_total
from emp
    order by 2;

select ename, sal,
       row_number() over (partition by sal order by sal),
       rank() over (partition by sal order by sal),
       dense_rank() over (partition by sal order by sal)
from emp
order by 2;

select ename, sal,
       avg(sal) over (order by sal),
       row_number() over (order by sal),
       rank() over (order by sal),
       dense_rank() over (order by sal)
from emp
order by 2;

select empno, ename, sal,
    lag(sal) over (order by sal, empno) as lag1,
    lag(sal, 2) over (order by sal, empno) as lag2,
    lag(sal, 3) over (order by sal, empno) as lag3
from emp order by sal;

select empno, ename, sal, (lag1 + lag2 + lag3) * 0.33333 as avg3 from
(select empno, ename, sal,
       lag(sal) over (order by sal, empno) as lag1,
       lag(sal, 2) over (order by sal, empno) as lag2,
       lag(sal, 3) over (order by sal, empno) as lag3
from emp order by sal) x;

--median
select percentile_cont(0.5)
     within group(order by sal)
     from emp
     where deptno=20;

--dates
select ename, hiredate, hiredate + interval '1 year' as hireplus from emp;

-- generate
select generate_series as id from generate_series(1, 10);

--paging
select * from (
select *, row_number() over (order by sal) as rn
    from emp) x where rn between 1 and 5;

select * from emp limit 5;

--min, max
select * from (
select *, min(sal) over() as minsal, max(sal) over() as maxsal
    from emp) x
 where sal in (minsal, maxsal);


--group and null - use *
select job, count(comm) from emp
    group by job;

select job, count(*) from emp
    group by job;

--window
select ename, deptno,
       count(*) over() as cnt from emp
    where deptno in (10,20)
order by 1;


select ename, deptno,
       count(*) over(partition by deptno) as dcnt
    from emp
order by deptno;

select ename,
       deptno, count(*) over(partition by deptno) as dcnt,
       job, count(*) over(partition by job) as jcnt,
       count(*) over(partition by deptno,job) as djcnt
from emp
order by deptno, job;

-- window aggregaye
select ename,
       deptno, hiredate, sal,
       sum(sal) over(partition by deptno) as pdnototal,
       sum(sal) over() as overtotal,
       sum(sal) over(order by hiredate) as running_total
from emp
where deptno = 10;


select ename,
       deptno, hiredate, sal,
       sum(sal) over(partition by deptno) as pdnototal,
       sum(sal) over() as overtotal,
       sum(sal) over(order by hiredate
           range between unbounded preceding and current row) as running_total
from emp
where deptno = 10;

--with
with emp10 as (select * from emp where deptno = 10)
select * from emp10;