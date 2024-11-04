/*1 тестовый набор*/
select * from "Cities"

select * from "Substitutions"

select * from "Modules"

select * from "Employees"

select * from "ReasonsAbsences"

select * from "AbsencesEmployees"

select * from "ModulesEmployees"

/*2 тестовый набор*/
update "Employees"
set number_phone = '+79200754345'
where full_name = 'Астапчик Дмитрий Алексеевич'

update "AbsencesEmployees"
set amount_day = 9
where employee_id = 'e0a6dc16-d4d3-45e2-9ff5-6ad35ed0421a'

/*3 тестовый набор*/
insert into "AbsencesEmployees" (reson_id, employee_id, begin_date, amount_day)
values ('a7b00882-9224-4ac9-8639-d7b147bde60e', '9bb7606d-6e02-4ae1-a5da-7e95c86e8f8c', '2024-11-12', 15)

/*4 тестовый набор*/
call UpdateDaysOffOnceAMonth()
call UpdateDaysVacationsUserAfterOneYearWorks()