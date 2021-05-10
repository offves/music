create table album
(
    id         bigint       not null
        primary key,
    name       varchar(255) null,
    picUrl     varchar(255) null,
    updateTime timestamp    null on update CURRENT_TIMESTAMP
)
    comment '专辑' collate = utf8mb4_general_ci;

create table comment
(
    commentId           bigint     not null
        primary key,
    songId              bigint     null,
    userId              bigint     null,
    content             text       null,
    commentLocationType tinyint    null,
    liked               tinyint(1) null,
    likedCount          int        null,
    status              tinyint(1) null,
    time                datetime   null,
    updateTime          timestamp  null on update CURRENT_TIMESTAMP
)
    comment '评论' collate = utf8mb4_general_ci;

create table comment_replied
(
    id             bigint auto_increment
        primary key,
    commentId      bigint    null,
    repliedUserId  bigint    null,
    repliedContent text      null,
    updateTime     timestamp null on update CURRENT_TIMESTAMP
)
    comment '评论回复' collate = utf8mb4_general_ci;

create table playlist
(
    id                 bigint       not null
        primary key,
    name               varchar(255) null,
    userId             bigint       null,
    playCount          int          null,
    shareCount         int          null,
    commentCount       int          null,
    creator            bigint       null,
    tags               varchar(500) null,
    coverImgUrl        varchar(255) null,
    coverImgId         bigint       null,
    backgroundCoverUrl varchar(255) null,
    backgroundCoverId  bigint       null,
    titleImageUrl      varchar(255) null,
    titleImage         bigint       null,
    englishTitle       varchar(255) null,
    status             tinyint      null,
    description        text         null,
    highQuality        tinyint(1)   null,
    newImported        tinyint(1)   null,
    specialType        tinyint      null,
    createTime         datetime     null,
    updateTime         datetime     null,
    dateUpdateTime     timestamp    null on update CURRENT_TIMESTAMP
)
    comment '歌单' collate = utf8mb4_general_ci;

create table playlist_song
(
    id         bigint auto_increment
        primary key,
    playlistId bigint    null,
    songId     bigint    null,
    updateTime timestamp null on update CURRENT_TIMESTAMP
)
    comment '歌单-歌曲关联表' collate = utf8mb4_general_ci;

create table singer
(
    id         bigint            not null
        primary key,
    name       varchar(255)      null,
    craw       tinyint default 0 not null,
    updateTime timestamp         null on update CURRENT_TIMESTAMP
)
    comment '歌手' collate = utf8mb4_general_ci;

create table song
(
    id          bigint        not null
        primary key,
    name        varchar(255)  null,
    lyric       text          null,
    singer      varchar(255)  null,
    albumId     bigint        null,
    downloadUrl varchar(255)  null,
    publishTime datetime      null,
    pullComment int default 0 null,
    updateTime  timestamp     null on update CURRENT_TIMESTAMP
)
    comment '歌曲' collate = utf8mb4_general_ci;

