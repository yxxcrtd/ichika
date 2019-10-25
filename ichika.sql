
-- 1，创建登录的用户角色（包含密码）
DROP USER IF EXISTS ichika;
CREATE ROLE ichika WITH LOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT NOREPLICATION CONNECTION LIMIT -1 PASSWORD 'ichika';

-- 2，远程终端执行：
创建目录：mkdir /home/data/ichika
目录授权：chown postgres /home/data/ichika

-- 3，创建表空间
DROP TABLESPACE IF EXISTS ichika;
CREATE TABLESPACE ichika OWNER ichika LOCATION '/home/data/ichika';

-- 4，创建数据库
DROP DATABASE IF EXISTS ichika;
CREATE DATABASE ichika WITH OWNER = ichika ENCODING = 'UTF8' TABLESPACE = ichika CONNECTION LIMIT = -1;


-- 用户
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id                  serial                  not null,                               -- 主键ID
    phone               varchar(11)             not null,                               -- 手机
    password            varchar(32)             not null,                               -- 密码
    title               varchar(256)            null,                                   -- 商户名称
    address             varchar(256)            null,                                   -- 商户地址
    name                varchar(16)             null,                                   -- 联系人
    kind                int                     not null default 0,                     -- 商品类型
    company_file        varchar(22)             null,                                   -- 营业执照
    id_face             varchar(22)             null,                                   -- 身份证正面图片（有头像的一面）
    id_back             varchar(22)             null,                                   -- 身份证反面图片（有发证机关的一面）
	status              int2                    not null default 0,                     -- 用户状态：0-待审核；1-正常
    create_time         timestamptz             not null default now(),                 -- 创建时间
    CONSTRAINT pk_t_user PRIMARY KEY (id)
);
INSERT INTO t_user (phone, password, name, status) VALUES ('admin', '3a0e57b1b65ec0a5dbe43d310c002d33', '系统管理员', 1);



-- 分类
DROP TABLE IF EXISTS t_category;
CREATE TABLE t_category (
    id                  serial                  not null,                               -- 主键ID
    parent_id           int2                    not null default 0,                     -- 分类父ID
    name                varchar(32)             not null,                               -- 名称
    CONSTRAINT pk_t_category PRIMARY KEY (id)
);

INSERT INTO t_category (name) VALUES ('水果');
INSERT INTO t_category (name) VALUES ('素食');
INSERT INTO t_category (name) VALUES ('休闲零食');
INSERT INTO t_category (name) VALUES ('乳品冲饮');
INSERT INTO t_category (name) VALUES ('饮料');
INSERT INTO t_category (name) VALUES ('粮油');
INSERT INTO t_category (name) VALUES ('调味品');
INSERT INTO t_category (name) VALUES ('肉类');
INSERT INTO t_category (name) VALUES ('蔬菜蛋品');
INSERT INTO t_category (name) VALUES ('水产');
INSERT INTO t_category (name) VALUES ('冷饮冻食');
INSERT INTO t_category (name) VALUES ('乳豆制品');
INSERT INTO t_category (name) VALUES ('培烤食品');
INSERT INTO t_category (name) VALUES ('茶叶酒水');
INSERT INTO t_category (name) VALUES ('传统滋补');
INSERT INTO t_category (name) VALUES ('其他');

INSERT INTO t_category (parent_id, name) VALUES (1, '苹果');
INSERT INTO t_category (parent_id, name) VALUES (1, '香蕉');
INSERT INTO t_category (parent_id, name) VALUES (1, '柚子');
INSERT INTO t_category (parent_id, name) VALUES (1, '橘子');
INSERT INTO t_category (parent_id, name) VALUES (1, '芒果');
INSERT INTO t_category (parent_id, name) VALUES (1, '冬枣');
INSERT INTO t_category (parent_id, name) VALUES (1, '橙子');
INSERT INTO t_category (parent_id, name) VALUES (1, '西柚');
INSERT INTO t_category (parent_id, name) VALUES (1, '木瓜');
INSERT INTO t_category (parent_id, name) VALUES (1, '山竹');
INSERT INTO t_category (parent_id, name) VALUES (1, '梨子');
INSERT INTO t_category (parent_id, name) VALUES (1, '奇异果');
INSERT INTO t_category (parent_id, name) VALUES (1, '柠檬');
INSERT INTO t_category (parent_id, name) VALUES (1, '石榴');
INSERT INTO t_category (parent_id, name) VALUES (1, '车厘子');
INSERT INTO t_category (parent_id, name) VALUES (1, '百香果');
INSERT INTO t_category (parent_id, name) VALUES (1, '榴莲');
INSERT INTO t_category (parent_id, name) VALUES (1, '牛油果');
INSERT INTO t_category (parent_id, name) VALUES (1, '杨梅');
INSERT INTO t_category (parent_id, name) VALUES (1, '西瓜');
INSERT INTO t_category (parent_id, name) VALUES (1, '火龙果');
INSERT INTO t_category (parent_id, name) VALUES (1, '葡萄');
INSERT INTO t_category (parent_id, name) VALUES (1, '提子');

INSERT INTO t_category (parent_id, name) VALUES (2, '手抓饼');
INSERT INTO t_category (parent_id, name) VALUES (2, '香肠');
INSERT INTO t_category (parent_id, name) VALUES (2, '培根');
INSERT INTO t_category (parent_id, name) VALUES (2, '午餐肉');
INSERT INTO t_category (parent_id, name) VALUES (2, '米线');
INSERT INTO t_category (parent_id, name) VALUES (2, '自热小火锅');
INSERT INTO t_category (parent_id, name) VALUES (2, '方便面');
INSERT INTO t_category (parent_id, name) VALUES (2, '酸辣粉');
INSERT INTO t_category (parent_id, name) VALUES (2, '粉丝');
INSERT INTO t_category (parent_id, name) VALUES (2, '螺蛳粉罐头');

INSERT INTO t_category (parent_id, name) VALUES (3, '零食');
INSERT INTO t_category (parent_id, name) VALUES (3, '糖果');
INSERT INTO t_category (parent_id, name) VALUES (3, '软糖');





-- 视频
DROP TABLE IF EXISTS t_video;
CREATE TABLE t_video (
    id                  serial                  not null,                               -- 主键ID
    category_id         int                     null,                                   -- 分类ID
    user_id             int                     not null,                               -- 上传用户ID
    title               varchar(768)            null,                                   -- 标题
    file                varchar(22)             not null,                               -- 视频文件
    picture             varchar(22)             not null,                               -- 视频截图
    favorite            int                     not null default 0,                     -- 点赞数
    comment             int                     not null default 0,                     -- 评论数
    share               int                     not null default 0,                     -- 分享数
    create_time         timestamptz             not null default now(),                 -- 上传时间
	status              int2                    not null default 0,                     -- 视频状态：0-未审核；1-已审核；2-已删除
    audit_time          timestamptz             null,                                   -- 审核时间
    CONSTRAINT pk_t_video PRIMARY KEY (id)
);


-- 点赞
DROP TABLE IF EXISTS t_favorite;
CREATE TABLE t_favorite (
    id                  serial                  not null,                               -- 主键ID
    video_id            int                     not null,                               -- 视频ID
    user_id             int                     not null,                               -- 用户ID
	type                int2                    not null default 0,                     -- 点赞类型：0-点赞；1-取消
    create_time         timestamptz             not null default now(),                 -- 点赞时间
    CONSTRAINT pk_t_favorite PRIMARY KEY (id)
);


-- 分享
DROP TABLE IF EXISTS t_share;
CREATE TABLE t_share (
    id                  serial                  not null,                               -- 主键ID
    user_id             int                     not null,                               -- 用户ID
    video_id            int                     not null,                               -- 视频ID
    target              varchar(32)             not null,                               -- 分享目标
    create_time         timestamptz             not null default now(),                 -- 分享时间
    CONSTRAINT pk_t_share PRIMARY KEY (id)
);


-- 评论
DROP TABLE IF EXISTS t_comment;
CREATE TABLE t_comment (
    id                  serial                  not null,                               -- 主键ID
    video_id            int                     not null,                               -- 视频ID
    user_id             int                     not null,                               -- 用户ID
    to_user_id          int                     not null,                               -- 目标用户ID

    content             varchar(4096)           not null,                               -- 评论内容

    create_time         timestamptz             not null default now(),                 -- 评论时间
    CONSTRAINT pk_t_comment PRIMARY KEY (id)
);


-- 消息
DROP TABLE IF EXISTS t_news;
CREATE TABLE t_news (
    id                  serial                  not null,                               -- 主键ID
    title               varchar(128)            not null,                               -- 标题
    content             text                    not null,                               -- 内容
	status              int2                    not null default 0,                     -- 状态：0-上线；1-下线
    create_time         timestamptz             not null default now(),                 -- 发布时间
    CONSTRAINT pk_t_news PRIMARY KEY (id)
);


-- 系统配置表
DROP TABLE IF EXISTS t_config;
CREATE TABLE t_config (
    id                  serial                  not null,                               -- 主键ID
    key                 varchar(32)             not null,                               -- 健
    value               varchar(256)            not null,                               -- 值
    remark              varchar(128)            null,                                   -- 备注
    CONSTRAINT pk_t_config PRIMARY KEY (id)
);
INSERT INTO t_config (key, value, remark) VALUES ('user_video_audit', '0', '普通用户视频是否需要审核（0：不用审核直接上线；1：要审核）');
INSERT INTO t_config (key, value, remark) VALUES ('merchant_video_audit', '0', '商户视频是否需要审核（0：不用审核直接上线；1：要审核）');



-- 广告
DROP TABLE IF EXISTS t_advertisement;
CREATE TABLE t_advertisement (
    id                  serial                  not null,                               -- 主键ID
    title               varchar(128)            not null,                               -- 标题
    picture             varchar(22)             not null,                               -- 图片
    link                varchar(256)            not null,                               -- 链接
	hits                int                     not null default 0,                     -- 点击量
	status              int2                    not null default 0,                     -- 状态：0-上线；1-下线
    create_time         timestamptz             not null default now(),                 -- 发布时间
    end_date            timestamptz             null,                                   -- 到期时间
    phone               varchar(11)             null,                                   -- 联系人
    CONSTRAINT pk_t_advertisement PRIMARY KEY (id)
);


