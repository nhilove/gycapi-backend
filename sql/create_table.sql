#
建表脚本
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create
database if not exists my_db;



CREATE TABLE `user`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`     varchar(256)          DEFAULT NULL COMMENT '昵称',
    `userAccount`  varchar(256)          DEFAULT NULL COMMENT '登录账号',
    `userPassword` varchar(512) NOT NULL COMMENT '密码',
    `avatarUrl`    varchar(1024)         DEFAULT NULL COMMENT '用户头像',
    `gender`       tinyint               DEFAULT NULL COMMENT '性别',
    `phone`        varchar(128)          DEFAULT NULL COMMENT '电话',
    `email`        varchar(512)          DEFAULT NULL COMMENT '邮箱',
    `userRole`     int          NOT NULL DEFAULT '0' COMMENT '用户角色 0-默认用户 1-管理员',
    `userStatus`   int          NOT NULL DEFAULT '0' COMMENT '用户状态0-正常(默认)',
    `createTime`   datetime              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除0未删除(默认)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户'





-- 切换库
use my_db;

-- 用户表
create table if not exists user
(
    id
    bigint
    auto_increment
    comment
    'id'
    primary
    key,
    userAccount
    varchar
(
    256
) not null comment '账号',
    userPassword varchar
(
    512
) not null comment '密码',
    unionId varchar
(
    256
) null comment '微信开放平台id',
    mpOpenId varchar
(
    256
) null comment '公众号openId',
    userName varchar
(
    256
) null comment '用户昵称',
    userAvatar varchar
(
    1024
) null comment '用户头像',
    userProfile varchar
(
    512
) null comment '用户简介',
    userRole varchar
(
    256
)   default 'user' not null comment '用户角色：user/admin/ban',
    accessKey  varchar(512) not null comment 'accessKey',
    secretKey  varchar(512) not null comment 'secretKey',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete tinyint default 0 not null comment '是否删除',
    index idx_unionId
(
    unionId
)
    ) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id
    bigint
    auto_increment
    comment
    'id'
    primary
    key,
    title
    varchar
(
    512
) null comment '标题',
    content text null comment '内容',
    tags varchar
(
    1024
) null comment '标签列表（json 数组）',
    thumbNum int default 0 not null comment '点赞数',
    favourNum int default 0 not null comment '收藏数',
    userId bigint not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete tinyint default 0 not null comment '是否删除',
    index idx_userId
(
    userId
)
    ) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id
    bigint
    auto_increment
    comment
    'id'
    primary
    key,
    postId
    bigint
    not
    null
    comment
    '帖子 id',
    userId
    bigint
    not
    null
    comment
    '创建用户 id',
    createTime
    datetime
    default
    CURRENT_TIMESTAMP
    not
    null
    comment
    '创建时间',
    updateTime
    datetime
    default
    CURRENT_TIMESTAMP
    not
    null
    on
    update
    CURRENT_TIMESTAMP
    comment
    '更新时间',
    index
    idx_postId
(
    postId
),
    index idx_userId
(
    userId
)
    ) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id
    bigint
    auto_increment
    comment
    'id'
    primary
    key,
    postId
    bigint
    not
    null
    comment
    '帖子 id',
    userId
    bigint
    not
    null
    comment
    '创建用户 id',
    createTime
    datetime
    default
    CURRENT_TIMESTAMP
    not
    null
    comment
    '创建时间',
    updateTime
    datetime
    default
    CURRENT_TIMESTAMP
    not
    null
    on
    update
    CURRENT_TIMESTAMP
    comment
    '更新时间',
    index
    idx_postId
(
    postId
),
    index idx_userId
(
    userId
)
    ) comment '帖子收藏';
