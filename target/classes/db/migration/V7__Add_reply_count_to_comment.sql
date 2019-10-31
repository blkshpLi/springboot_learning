alter table comment modify like_count int default 0;

alter table comment
	add reply_count int default 0 null comment '评论回复数';