create
or replace procedure UpdateDaysOffOnceAMonth() 
language plpgsql as $$
begin
  update "Employees"
  set days_off = 3
  where days_off < 3;  
end;
$$;

create 
or replace procedure UpdateDaysVacationsUserAfterOneYearWorks()
language plpgsql as $$
begin
  update "Employees"
  set days_vacations = 28
  where extract(month from hire_date) = extract(month from now())
  and extract(day from hire_date) = extract(day from now() at time zone 'Europe/Moscow')
  and days_vacations < 28;
end;
$$; 


select
  cron.schedule (
    'TaskUpdateDaysOffOnceAMonth',
    '0 0 1 * *', /*в 0 минут, в 12 часов, в 1 день месяца, каждый месяц, в любой день недели*/
    'CALL UpdateDaysOffOnceAMonth()'
  );


select
  cron.schedule (
    'TaskUpdateDaysVacationsUserAfterOneYearWorks',
    '0 0 * * *', /*в 0 минут, в 12 часов, каждый день месяца, каждый месяц, в любой день недели*/
    'CALL UpdateDaysVacationsUserAfterOneYearWorks()'
  );

select cron.unschedule('TaskUpdateDaysOffOnceAMonth');

create extension if not exists pg_cron;

SELECT schema_name
FROM information_schema.schemata
WHERE schema_name = 'cron';

SELECT * FROM cron.job;