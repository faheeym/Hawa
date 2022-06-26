drop database if exists hawa_db;
create database hawa_db;
use hawa_db;

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    disabled bit not null default(0)
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

create table user_profile(
user_profile_id int primary key auto_increment,
first_name longtext not null,
last_name longtext not null,
birthday date,
email varchar(100),
relation_status varchar(70),
profile_picture longtext,
app_user_id int not null,
constraint fk_user_profile_app_user_id
foreign key (app_user_id)
references app_user(app_user_id)
);

create table post(
post_id int primary key auto_increment,
picture longtext,
`description` longtext,
app_user_id int not null,
constraint fk_post_app_user_id
foreign key (app_user_id)
references app_user(app_user_id)
);