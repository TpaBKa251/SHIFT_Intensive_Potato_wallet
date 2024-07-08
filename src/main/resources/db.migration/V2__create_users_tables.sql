create table wallets
(
    id          uuid                                not null
        primary key,
    amount      bigint                              not null,
    last_update timestamp default CURRENT_TIMESTAMP not null
);


create table users
(
    id                uuid                                not null
        primary key,
    wallet_id         uuid
        unique
        references wallets,
    first_name        varchar(50)                         not null,
    last_name         varchar(50)                         not null,
    middle_name       varchar(50),
    email             varchar(100)                        not null
        unique,
    birth_date        date                                not null
        constraint users_birth_date_check
            check ((CURRENT_DATE - birth_date) >= 6570),
    registration_date timestamp default CURRENT_TIMESTAMP not null,
    last_update_date  timestamp default CURRENT_TIMESTAMP not null,
    phone             bigint
        unique,
    password          varchar                             not null
);