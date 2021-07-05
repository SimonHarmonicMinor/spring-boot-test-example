create table person
(
    id           serial primary key,
    first_name   text,
    last_name    text,
    date_created timestamp with time zone
);