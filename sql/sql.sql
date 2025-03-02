create table article
(
    id              bigint auto_increment comment 'id'
        primary key,
    articleTitle    varchar(128)                       not null comment '新闻标题',
    articleDesc     varchar(2048)                      null comment '新闻描述',
    articleAvatar   varchar(1024)                      null comment '新闻封面',
    articleCategory tinyint  default 0                 not null comment '文章类型（0-新闻类，1-科普类，2-通知类）',
    reviewStatus    int      default 0                 not null comment '审核状态：0-待审核, 1-通过, 2-拒绝',
    reviewMessage   varchar(512)                       null comment '审核信息',
    reviewerId      bigint                             null comment '审核人 id',
    reviewTime      datetime                           null comment '审核时间',
    authorId        bigint                             not null comment '作者 id',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    articleContent  text                               not null comment '文章内容，存储富文本',
    viewCount       bigint   default 0                 not null comment '文章点击量',
    isCarousel      int      default 0                 not null comment '是否为轮播新闻',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    tags            varchar(255)                       null
)
    comment '文章' collate = utf8mb4_unicode_ci;

create index idx_articleName
    on article (articleTitle);

create table categories
(
    id          int auto_increment comment '分类ID'
        primary key,
    name        varchar(255)                        not null comment '分类名称',
    description text                                null comment '分类描述',
    createTime  timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    int       default 0                 not null comment '是否删除'
)
    comment '文章分类表' collate = utf8mb4_unicode_ci;

create table log
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    name          varchar(255) not null comment '操作内容',
    createTime    datetime     null comment '操作时间',
    userAccount   varchar(255) not null comment '操作人',
    actionType    varchar(255) null comment '操作类型',
    targetType    varchar(255) null comment '目标类型',
    targetId      bigint       null comment '目标ID',
    requestMethod varchar(255) null comment '请求方法',
    requestPath   varchar(255) null comment '请求路径',
    requestIP     varchar(255) null comment '请求IP',
    params        text         null comment '请求参数'
)
    comment '操作日志';

create table notification
(
    id         bigint auto_increment comment 'id'
        primary key,
    title      varchar(255)                       not null comment '公告标题',
    content    varchar(2048)                      not null comment '公告内容',
    startTime  datetime                           null comment '开始时间',
    endTime    datetime                           null comment '结束时间',
    userId     varchar(256)                       not null comment '所属用户',
    status     tinyint  default 0                 not null comment '0: 关闭，1: 启用',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete   tinyint  default 0                 null comment '是否删除'
);

create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/writer/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    email        varchar(1024)                          null comment '邮箱'
)
    comment '用户' collate = utf8mb4_unicode_ci;

create table comments
(
    id              bigint auto_increment comment '评论的唯一ID'
        primary key,
    articleId       bigint                               not null comment '所属新闻ID',
    userId          bigint                               not null comment '评论的用户ID',
    content         text                                 not null comment '评论内容',
    createTime      datetime   default CURRENT_TIMESTAMP null comment '评论时间',
    updateTime      datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    parentCommentId bigint                               null comment '父评论ID，支持评论回复',
    likes           int        default 0                 null comment '评论点赞数',
    isDelete        tinyint(1) default 0                 null comment '删除状态',
    isShow          int        default 1                 not null,
    constraint comments_ibfk_1
        foreign key (articleId) references article (id)
            on delete cascade,
    constraint comments_ibfk_2
        foreign key (userId) references user (id)
            on delete cascade,
    constraint comments_ibfk_3
        foreign key (parentCommentId) references comments (id)
            on delete cascade
);

create index article_id
    on comments (articleId);

create index parent_comment_id
    on comments (parentCommentId);

create index user_id
    on comments (userId);

create index idx_unionId
    on user (unionId);

