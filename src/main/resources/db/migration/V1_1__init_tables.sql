-- NB Flyway requires lowercase for table names
create schema if not exists dbjuddata;

create table judicial_user(
	sno varchar(255)  null,
	firstName varchar(255) null,
	LastName varchar(255)  null,
	Circuit varchar(255)  null,
	Area varchar(255) null
);

create table judicial_office_appointment(
	sno varchar(255) not null,
	firstName varchar(255) not null,
	LastName varchar(255) not null,
	Circuit varchar(255) not null,
	Area varchar(255) not null
);

create table judicial_office_authorization(
	sno varchar(255) not null,
	firstName varchar(255) not null,
	LastName varchar(255) not null,
	Circuit varchar(255) not null,
	Area varchar(255) not null
);

