/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     06-12-2010 18:00:18                          */
/*==============================================================*/


alter table EMPRESTIMO
   drop constraint FK_EMPRESTI_ALUGAR_FUNCIONA;

alter table EMPRESTIMO
   drop constraint FK_EMPRESTI_INCLUIR_E_PUBLICAC;

alter table EMPRESTIMO
   drop constraint FK_EMPRESTI_REQUISITA_LEITOR;

alter table FUNCIONARIO
   drop constraint FK_FUNCIONA_TIPO2_PESSOA;

alter table LEITOR
   drop constraint FK_LEITOR_TIPO_PESSOA;

alter table PUBLICACAO
   drop constraint FK_PUBLICAC_PERTENCER_PRATELEI;

alter table PUBLICACAO
   drop constraint FK_PUBLICAC_POSSUIR_EDITORA;

drop table EDITORA cascade constraints;

drop index ALUGAR_FK;

drop index INCLUIR_EMPRESTIMO_FK;

drop index REQUISITAR_FK;

drop table EMPRESTIMO cascade constraints;

drop table FUNCIONARIO cascade constraints;

drop table LEITOR cascade constraints;

drop table PESSOA cascade constraints;

drop table PRATELEIRA cascade constraints;

drop index PERTENCER_FK;

drop index POSSUIR_FK;

drop table PUBLICACAO cascade constraints;

/*==============================================================*/
/* Table: EDITORA                                               */
/*==============================================================*/
create table EDITORA 
(
   ID_EDITORAE          NUMBER               not null,
   NOME_EDITORA         VARCHAR2(1024)       not null,
   constraint PK_EDITORA primary key (ID_EDITORAE)
);

/*==============================================================*/
/* Table: EMPRESTIMO                                            */
/*==============================================================*/
create table EMPRESTIMO 
(
   LEI_ID_PESSOA        INTEGER              not null,
   ID_PUB               NUMBER               not null,
   ID_PESSOA            INTEGER              not null,
   DATA_DE_REQUISITO    DATE                 not null,
   DATA_PREVISTA        DATE                 not null,
   DATA_ENTREGA         DATE,
   constraint PK_EMPRESTIMO primary key (LEI_ID_PESSOA, ID_PUB, ID_PESSOA)
);

/*==============================================================*/
/* Index: REQUISITAR_FK                                         */
/*==============================================================*/
create index REQUISITAR_FK on EMPRESTIMO (
   LEI_ID_PESSOA ASC
);

/*==============================================================*/
/* Index: INCLUIR_EMPRESTIMO_FK                                 */
/*==============================================================*/
create index INCLUIR_EMPRESTIMO_FK on EMPRESTIMO (
   ID_PUB ASC
);

/*==============================================================*/
/* Index: ALUGAR_FK                                             */
/*==============================================================*/
create index ALUGAR_FK on EMPRESTIMO (
   ID_PESSOA ASC
);

/*==============================================================*/
/* Table: FUNCIONARIO                                           */
/*==============================================================*/
create table FUNCIONARIO 
(
   ID_PESSOA            INTEGER              not null,
   DATA_ENTRADA         DATE                 not null,
   DATA_SAIDA           DATE,
   PASSWORD             CHAR(50)             not null,
   ISADMIN              SMALLINT             not null,
   constraint PK_FUNCIONARIO primary key (ID_PESSOA)
);

/*==============================================================*/
/* Table: LEITOR                                                */
/*==============================================================*/
create table LEITOR 
(
   ID_PESSOA            INTEGER              not null,
   NO_EMPRESTIMOS       INTEGER,
   constraint PK_LEITOR primary key (ID_PESSOA)
);

/*==============================================================*/
/* Table: PESSOA                                                */
/*==============================================================*/
create table PESSOA 
(
   MORADA               VARCHAR2(1024)       not null,
   BI                   NUMBER               not null,
   TELEFONE             NUMBER,
   E_MAIL               VARCHAR2(1024),
   ID_PESSOA            INTEGER              not null,
   constraint PK_PESSOA primary key (ID_PESSOA)
);

/*==============================================================*/
/* Table: PRATELEIRA                                            */
/*==============================================================*/
create table PRATELEIRA 
(
   CAPACIDADE           INTEGER              not null,
   OCUPACAO             INTEGER              not null,
   ID_PRATELEIRA        INTEGER              not null,
   GENERO               VARCHAR2(1024),
   constraint PK_PRATELEIRA primary key (ID_PRATELEIRA)
);

/*==============================================================*/
/* Table: PUBLICACAO                                            */
/*==============================================================*/
create table PUBLICACAO 
(
   ID_PUB               NUMBER               not null,
   ID_PRATELEIRA        INTEGER              not null,
   ID_EDITORAE          NUMBER,
   DESCRICAO            VARCHAR2(1024)       not null,
   AUTOR                VARCHAR2(1024)       not null,
   DISPONIVEIS          NUMBER               not null,
   TOTAL                NUMBER               not null,
   constraint PK_PUBLICACAO primary key (ID_PUB)
);

/*==============================================================*/
/* Index: POSSUIR_FK                                            */
/*==============================================================*/
create index POSSUIR_FK on PUBLICACAO (
   ID_EDITORAE ASC
);

/*==============================================================*/
/* Index: PERTENCER_FK                                          */
/*==============================================================*/
create index PERTENCER_FK on PUBLICACAO (
   ID_PRATELEIRA ASC
);

alter table EMPRESTIMO
   add constraint FK_EMPRESTI_ALUGAR_FUNCIONA foreign key (ID_PESSOA)
      references FUNCIONARIO (ID_PESSOA);

alter table EMPRESTIMO
   add constraint FK_EMPRESTI_INCLUIR_E_PUBLICAC foreign key (ID_PUB)
      references PUBLICACAO (ID_PUB);

alter table EMPRESTIMO
   add constraint FK_EMPRESTI_REQUISITA_LEITOR foreign key (LEI_ID_PESSOA)
      references LEITOR (ID_PESSOA);

alter table FUNCIONARIO
   add constraint FK_FUNCIONA_TIPO2_PESSOA foreign key (ID_PESSOA)
      references PESSOA (ID_PESSOA);

alter table LEITOR
   add constraint FK_LEITOR_TIPO_PESSOA foreign key (ID_PESSOA)
      references PESSOA (ID_PESSOA);

alter table PUBLICACAO
   add constraint FK_PUBLICAC_PERTENCER_PRATELEI foreign key (ID_PRATELEIRA)
      references PRATELEIRA (ID_PRATELEIRA);

alter table PUBLICACAO
   add constraint FK_PUBLICAC_POSSUIR_EDITORA foreign key (ID_EDITORAE)
      references EDITORA (ID_EDITORAE);

