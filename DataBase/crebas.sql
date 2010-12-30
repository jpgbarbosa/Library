/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     16-12-2010 17:05:25                          */
/*==============================================================*/


alter table EMPRESTIMO
   drop constraint FK_EMPRESTI_ALUGAR_FUNCIONA;

alter table EMPRESTIMO
   drop constraint FK_EMPRESTI_CONTER_PU_PUBLICAC;

alter table EMPRESTIMO
   drop constraint FK_EMPRESTI_REQUISITA_LEITOR;

alter table FUNCIONARIO
   drop constraint FK_FUNCIONA_TIPO2_PESSOA;

alter table LEITOR
   drop constraint FK_LEITOR_TIPO_PESSOA;

alter table PUBLICACAO
   drop constraint FK_PUBLICAC_AUTOR_DE_AUTOR;

alter table PUBLICACAO
   drop constraint FK_PUBLICAC_PERTENCER_PRATELEI;

alter table PUBLICACAO
   drop constraint FK_PUBLICAC_POSSUIR_EDITORA;

drop table AUTOR cascade constraints;

drop table EDITORA cascade constraints;

drop index CONTER_PUBLICACAO_FK;

drop index ALUGAR_FK;

drop index REQUISITAR_FK;

drop table EMPRESTIMO cascade constraints;

drop table FUNCIONARIO cascade constraints;

drop table LEITOR cascade constraints;

drop table PESSOA cascade constraints;

drop table PRATELEIRA cascade constraints;

drop index AUTOR_DE_FK;

drop index PERTENCER_FK;

drop index POSSUIR_FK;

drop table PUBLICACAO cascade constraints;

/*==============================================================*/
/* Table: AUTOR                                                 */
/*==============================================================*/
create table AUTOR 
(
   NOME_AUTOR           VARCHAR2(1024)       not null,
   ID_AUTOR             INTEGER              not null,
   constraint PK_AUTOR primary key (ID_AUTOR)
);

/*==============================================================*/
/* Table: EDITORA                                               */
/*==============================================================*/
create table EDITORA 
(
   ID_EDITORA           NUMBER               not null,
   NOME_EDITORA         VARCHAR2(1024)       not null,
   constraint PK_EDITORA primary key (ID_EDITORA)
);

/*==============================================================*/
/* Table: EMPRESTIMO                                            */
/*==============================================================*/
create table EMPRESTIMO 
(
   LEI_ID_PESSOA        INTEGER              not null,
   ID_PESSOA            INTEGER              not null,
   ID_DOC               NUMBER               not null,
   ID_EMPRESTIMO        INTEGER              not null,
   DATA_DE_REQUISITO    DATE                 not null,
   DATA_PREVISTA        DATE                 not null,
   DATA_ENTREGA         DATE,
   constraint PK_EMPRESTIMO primary key (LEI_ID_PESSOA, ID_PESSOA, ID_DOC, ID_EMPRESTIMO)
);

/*==============================================================*/
/* Index: REQUISITAR_FK                                         */
/*==============================================================*/
create index REQUISITAR_FK on EMPRESTIMO (
   LEI_ID_PESSOA ASC
);

/*==============================================================*/
/* Index: ALUGAR_FK                                             */
/*==============================================================*/
create index ALUGAR_FK on EMPRESTIMO (
   ID_PESSOA ASC
);

/*==============================================================*/
/* Index: CONTER_PUBLICACAO_FK                                  */
/*==============================================================*/
create index CONTER_PUBLICACAO_FK on EMPRESTIMO (
   ID_DOC ASC
);

/*==============================================================*/
/* Table: FUNCIONARIO                                           */
/*==============================================================*/
create table FUNCIONARIO 
(
   ID_PESSOA            INTEGER              not null,
   DATA_ENTRADA         DATE                 not null,
   DATA_SAIDA           DATE,
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
   NOME_PESSOA          VARCHAR2(1024)       not null,
   MORADA               VARCHAR2(1024)       not null,
   BI                   NUMBER               not null,
   DATA					DATE				 not null,
   TELEFONE             NUMBER,
   E_MAIL               VARCHAR2(1024),
   ID_PESSOA            INTEGER              not null,
   UNIQUE (BI),
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
   ID_DOC               NUMBER               not null,
   ID_PRATELEIRA        INTEGER              not null,
   ID_AUTOR             INTEGER              not null,
   ID_EDITORA			INTEGER				 not null,
   PAGINAS        		INTEGER				 not null,
   DESCRICAO            VARCHAR2(1024),
   DATA                 DATE,
   NOME_DOC             VARCHAR2(1024)       not null,
   DISPONIVEIS          INTEGER,
   TOTAL                INTEGER,
   UNIQUE(NOME_DOC),
   constraint PK_PUBLICACAO primary key (ID_DOC)
);

/*==============================================================*/
/* Index: POSSUIR_FK                                            */
/*==============================================================*/
create index POSSUIR_FK on PUBLICACAO (
   ID_EDITORA ASC
);

/*==============================================================*/
/* Index: PERTENCER_FK                                          */
/*==============================================================*/
create index PERTENCER_FK on PUBLICACAO (
   ID_PRATELEIRA ASC
);

/*==============================================================*/
/* Index: AUTOR_DE_FK                                           */
/*==============================================================*/
create index AUTOR_DE_FK on PUBLICACAO (
   ID_AUTOR ASC
);

alter table EMPRESTIMO
   add constraint FK_EMPRESTI_ALUGAR_FUNCIONA foreign key (ID_PESSOA)
      references FUNCIONARIO (ID_PESSOA);

alter table EMPRESTIMO
   add constraint FK_EMPRESTI_CONTER_PU_PUBLICAC foreign key (ID_DOC)
      references PUBLICACAO (ID_DOC);

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
   add constraint FK_PUBLICAC_AUTOR_DE_AUTOR foreign key (ID_AUTOR)
      references AUTOR (ID_AUTOR);

alter table PUBLICACAO
   add constraint FK_PUBLICAC_PERTENCER_PRATELEI foreign key (ID_PRATELEIRA)
      references PRATELEIRA (ID_PRATELEIRA);

alter table PUBLICACAO
   add constraint FK_PUBLICAC_POSSUIR_EDITORA foreign key (ID_EDITORA)
      references EDITORA (ID_EDITORA);

	 
drop table autenticacao;

/*==============================================================*/
/* Table: AUTENTICACAO                                            */
/*==============================================================*/
create table AUTENTICACAO 
(
   ID_EMPREGADO	       INTEGER              not null,
   PASSWORD            VARCHAR2(1024)		not null,
   constraint PK_AUTENTICACAO primary key (ID_EMPREGADO)
);

-- Inserts the admin login.
insert into AUTENTICACAO values (1, 'admin');