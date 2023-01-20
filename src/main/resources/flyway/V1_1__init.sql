create type payment_method as enum (
    'CASH',
    'CASH_ON_DELIVERY',
    'VISA',
    'MASTERCARD',
    'AMEX',
    'JCB'
    );

CREATE CAST (CHARACTER VARYING as payment_method) WITH INOUT AS IMPLICIT;

create table sales
(
    uuid             uuid primary key,
    price            decimal        not null,
    price_modifier   float          not null,
    payment_method   payment_method not null,
    point_multiplier float          not null,
    datetime         timestamp      not null
);
