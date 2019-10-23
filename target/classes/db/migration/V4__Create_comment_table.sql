create table comment
(
	id bigint auto_increment,
	parent_id bigint null comment '父类ID',
	type int null comment '父类类型',
	commentator int null comment '评论人ID',
	gmt_create bigint null comment '创建时间',
	gmt_modified bigint null comment '修改时间',
	like_count bigint default 0 comment '喜爱数',
	content varchar(1024) null comment '评论内容',
	constraint comment_pk
		primary key (id)
);