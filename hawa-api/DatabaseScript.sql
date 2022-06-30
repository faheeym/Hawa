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
birthday date not null,
email varchar(100) not null unique,
relation_status varchar(70),
profile_picture longtext,
app_user_id int not null unique,
gender varchar(45) not null,
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

insert into app_role (`name`) values
    ('USER'),
    ('ADMIN');

-- passwords are set to "P@ssw0rd!"
insert into app_user (app_user_id, username, password_hash) values
	(1,'Admin','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa'),
	(2,'Test','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa');


insert into app_user_role (app_user_id, app_role_id) values
	(1,2),
	(2,1);
    
 insert into user_profile( gender, first_name, last_name, birthday, email, relation_status, profile_picture, app_user_id) 
 values("Male","Fahim","Hossain","1999/12/12","Faheeym@gmail.com","Married", "https://i.ibb.co/GQHPzTJ/IMG-2567.jpg",1);   