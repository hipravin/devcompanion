---
set search_path to masteringp13;

select now();

show timezone;

CREATE TABLE t_oil (
                       region text,
                       country text,
                       year int,
                       production int,
                       consumption int
);

COPY t_oil FROM PROGRAM '
 curl https://www.cybertec-postgresql.com/secret/oil_ext.txt ';


COPY t_oil FROM '/usr/share/import/oil_ext.txt';

select count(*) from t_oil;

select region, avg(production)
from t_oil group by region
order by 2 desc;

select region, avg(production)
from t_oil group by ROLLUP(region);

-- ROLLUP GROUP BY
SELECT region, country, avg(production)
FROM t_oil
WHERE country IN ('USA', 'Canada', 'Iran', 'Oman')
GROUP BY ROLLUP (region, country);

-- CUBE GROUP BY
SELECT region, country, avg(production)
FROM t_oil
WHERE country IN ('USA', 'Canada', 'Iran', 'Oman')
GROUP BY CUBE (region, country);

-- GROUPING SETS GROUP BY
SELECT region, country, avg(production)
FROM t_oil
WHERE country IN ('USA', 'Canada', 'Iran', 'Oman')
GROUP BY GROUPING SETS ( (), region, country);

-- with filter
SELECT region,
       avg(production) AS all,
       avg(production) FILTER (WHERE year < 1990) AS old,
       avg(production) FILTER (WHERE year >= 1990) AS new
FROM t_oil
GROUP BY ROLLUP (region);

-- WITHIN GROUP
SELECT region,
       percentile_disc(0.5) WITHIN GROUP (ORDER BY production)
FROM t_oil
GROUP BY 1;

SELECT region,
       percentile_disc(0.5) WITHIN GROUP (ORDER BY production)
FROM t_oil
GROUP BY ROLLUP (1);
--
SELECT percentile_disc(0.62) WITHIN GROUP (ORDER BY id),
       percentile_cont(0.62) WITHIN GROUP (ORDER BY id)
FROM generate_series(1, 5) AS id;

-- mode for most frequent value
SELECT country, mode() WITHIN GROUP (ORDER BY production)
FROM t_oil
WHERE country = 'Other Middle East'
GROUP BY 1;

--
SELECT country, year, production, consumption,
    avg(production) OVER (PARTITION BY country)
FROM t_oil;

--
SELECT year, production,
    avg(production) OVER (PARTITION BY year < 1990)
FROM t_oil
WHERE country = 'Canada'
ORDER BY year;

--
SELECT country, year, production,
    min(production) OVER (PARTITION BY country ORDER BY year)
FROM t_oil
WHERE year BETWEEN 1978 AND 1983
  AND country IN ('Iran', 'Oman');

---
SELECT country, year, production,
    min(production)
    OVER (PARTITION BY country
    ORDER BY year ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING)
FROM t_oil
WHERE year BETWEEN 1978 AND 1983
  AND country IN ('Iran', 'Oman');
---
SELECT *, array_agg(id)
    OVER (ORDER BY id ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING)
FROM generate_series(1, 5) AS id;

--
SELECT *,
       array_agg(id) OVER (ORDER BY id ROWS BETWEEN
           UNBOUNDED PRECEDING AND 0 FOLLOWING)
FROM generate_series(1, 5) AS id;
--
SELECT year,
    production,
    array_agg(production) OVER (ORDER BY year
    ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING
    EXCLUDE CURRENT ROW)
FROM t_oil
WHERE country = 'USA'
  AND year < 1970;
--
SELECT country, year, production,
    min(production) OVER (w),
    max(production) OVER (w)
FROM t_oil
WHERE country = 'Canada'
  AND year BETWEEN 1980
  AND 1985
    WINDOW w AS (ORDER BY year);

--
SELECT year, production,
    rank() OVER (ORDER BY production)
FROM t_oil
WHERE country = 'Other Middle East'
ORDER BY rank
    LIMIT 7;

SELECT year, production,
    dense_rank() OVER (ORDER BY production)
FROM t_oil
WHERE country = 'Other Middle East'
ORDER BY dense_rank
    LIMIT 7;

SELECT year, production,
    ntile(4) OVER (ORDER BY production)
FROM t_oil
WHERE country = 'Iraq'
  AND year BETWEEN 2000 AND 2006;

--duplicates
SELECT *
FROM (SELECT t_oil, lag(t_oil) OVER (ORDER BY year)
      FROM t_oil
      WHERE country = 'USA'
     ) AS x
WHERE t_oil = lag;
