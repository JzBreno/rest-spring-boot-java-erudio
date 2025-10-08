create table if not exists teste
(
    id         bigint auto_increment
    primary key,
    address    varchar(80) not null,
    birthday   date        null,
    first_name varchar(80) not null,
    gender     varchar(6)  not null,
    last_name  varchar(80) not null
    );