create table comments
(
    comment_id        bigint        not null primary key,
    content           varchar(3000) not null,
    article_id        bigint        not null,
    parent_comment_id bigint,
    writer_id         bigint        not null,
    deleted           bool          not null,
    created_at        timestamp     not null
);

create index idx_article_id_parent_comment_id_comment_id on comments (article_id asc, parent_comment_id asc, comment_id asc);

create table comments_v2
(
    comment_id bigint        not null primary key,
    content    varchar(3000) not null,
    article_id bigint        not null,
    writer_id  bigint        not null,
    path       varchar(25)   not null,
    deleted    bool          not null,
    created_at timestamp     not null
);

create unique index idx_article_id_path on comments_v2 (article_id asc, path asc);

create table articles_comments_count
(
    article_id    bigint not null primary key,
    comment_count bigint not null
);

create table outboxs
(
    outbox_id  bigint        not null primary key,
    shard_key  bigint        not null,
    event_type varchar(100)  not null,
    payload    varchar(5000) not null,
    created_at timestamp     not null
);

create index idx_shard_key_created_at on outbox (shard_key asc, created_at asc);