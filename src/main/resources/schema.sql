DROP TABLE IF EXISTS photo;

create table photo (
   photoId integer PRIMARY KEY auto_increment,
   fileName varchar(255) not null,
   originalFileName varchar(255) not null,
   fileExtension varchar(255) not null,
);