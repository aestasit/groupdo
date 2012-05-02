# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table project (
  id                        bigint not null,
  name                      varchar(255),
  creator_id                bigint,
  constraint pk_project primary key (id))
;

create table task (
  id                        bigint not null,
  label                     varchar(255),
  completed                 timestamp,
  opened_id                 bigint,
  closed_id                 bigint,
  project_id                bigint,
  constraint pk_task primary key (id))
;

create table user (
  id                        bigint not null,
  username                  varchar(255),
  password                  varchar(255),
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;


create table project_user (
  project_id                     bigint not null,
  user_id                        bigint not null,
  constraint pk_project_user primary key (project_id, user_id))
;
create sequence project_seq;

create sequence task_seq;

create sequence user_seq;

alter table project add constraint fk_project_creator_1 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_project_creator_1 on project (creator_id);
alter table task add constraint fk_task_opened_2 foreign key (opened_id) references user (id) on delete restrict on update restrict;
create index ix_task_opened_2 on task (opened_id);
alter table task add constraint fk_task_closed_3 foreign key (closed_id) references user (id) on delete restrict on update restrict;
create index ix_task_closed_3 on task (closed_id);
alter table task add constraint fk_task_project_4 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_task_project_4 on task (project_id);



alter table project_user add constraint fk_project_user_project_01 foreign key (project_id) references project (id) on delete restrict on update restrict;

alter table project_user add constraint fk_project_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists project;

drop table if exists project_user;

drop table if exists task;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists project_seq;

drop sequence if exists task_seq;

drop sequence if exists user_seq;

