create table sales
(
    uuid             uuid primary key,
    price            decimal   not null,
    price_modifier   float     not null,
    payment_method   text      not null,
    point_multiplier float     not null,
    datetime         timestamp not null
);
