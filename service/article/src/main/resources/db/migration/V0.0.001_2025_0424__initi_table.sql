-- articles
create table articles
(
    article_id  bigint                   not null primary key,
    title       varchar(100)             not null,
    content     varchar(3000)            not null,
    board_id    bigint                   not null,
    writer_id   bigint                   not null,
    created_at  timestamp default not () not null,
    modified_at timestamp                not null
);

create index idx_board_id_article_id on article (board_id asc, article_id desc);

-- TODO postgres 로 변경 필요
create table board_article_count
(
    board_id      bigint not null primary key,
    article_count bigint not null
);

create table outbox
(
    id         bigint                  not null primary key,
    shard_key  bigint                  not null,
    type       varchar(100)            not null,
    payload    text                    not null,
    created_at timestamp default now() not null
);

create index idx_shard_key_created_at on outbox (shard_key asc, created_at asc);

