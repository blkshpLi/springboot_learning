create table comment_like
(
	id bigint auto_increment,
	comment_id bigint not null,
	user_id bigint not null,
	gmt_create int not null,
	constraint comment_like_pk
		primary key (id)
);