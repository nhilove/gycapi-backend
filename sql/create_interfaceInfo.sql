use my_db ;

#  用户调用接口关系表
create table if not exists user_interface_info
(
    `id`           bigint auto_increment comment '主键id'
        primary key,
    `userId`         bigint                             not null comment '调用者 id',
    `InterfaceInfoId`         bigint                    not null comment '接口 id',
    `totalNum`       int      default 0                 not null comment '接口总调用次数',
    `leftNum`        int      default 0                 not null comment '接口剩余调用次数',
    `status`         int      default 0                 not null comment '接口状态 0正常（默认）1禁用',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    `isDelete`      tinyint  default 0                 not null comment '是否删除 0不删除1删除'
)
    comment '用户调用接口关系表';

# 接口信息表
create table if not exists interface_info
(
    `id`           bigint auto_increment comment '主键id'
        primary key,
    `name`           varchar(256)                       not null comment '接口名称',
    `userId`         bigint                             not null comment '创建人',
    `url`            varchar(512)                       not null comment '接口地址',
    `method`         varchar(256)                       not null comment '请求类型',
    `description`    varchar(512)                       null comment '描述',
    `requestParams`  text                               null comment '请求参数',
    `requestHeader`  text                               null comment '请求头',
    `responseHeader` text                               null comment '响应头',
    `status`         int      default 0                 not null comment '接口状态 0关闭（默认）1开启',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    `isDelete`      tinyint  default 0                 not null comment '是否删除 0不删除1删除'
)
    comment '接口信息表'