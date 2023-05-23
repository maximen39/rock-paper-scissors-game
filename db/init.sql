create database rockpaperscissors;

create table if not exists rockpaperscissors.hibernate_sequence
(
    next_val bigint null
);

create table if not exists rockpaperscissors.user
(
    id           bigint       not null primary key,
    created_at   datetime(6)  not null,
    last_auth_at datetime(6)  null,
    password     varchar(255) not null,
    username     varchar(255) not null
);

create table if not exists rockpaperscissors.game
(
    id      bigint not null primary key,
    outcome int    null,
    user_id bigint not null,
    constraint user_fk foreign key (user_id) references rockpaperscissors.user (id)
);

create table if not exists rockpaperscissors.game_step
(
    id          bigint not null primary key,
    outcome     int    not null,
    server_sign int    not null,
    user_sign   int    null,
    game_id     bigint not null,
    constraint game_fk foreign key (game_id) references rockpaperscissors.game (id)
);