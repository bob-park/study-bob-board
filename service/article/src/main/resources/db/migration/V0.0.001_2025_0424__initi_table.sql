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