-- Sequences for people
DROP SEQUENCE seq_id_pessoa;

CREATE SEQUENCE seq_id_pessoa
START WITH 1
INCREMENT BY 1
NOCYCLE;

DROP SEQUENCE seq_id_document;

CREATE SEQUENCE seq_id_document
START WITH 1
INCREMENT BY 1
NOCYCLE;


-- Sequences for books
DROP SEQUENCE seq_id_aluguer;

CREATE SEQUENCE seq_id_aluguer
START WITH 1
INCREMENT BY 1
NOCYCLE;


-- Sequences for publishers
DROP SEQUENCE seq_id_publisher;

CREATE SEQUENCE seq_id_publisher
START WITH 1
INCREMENT BY 1
NOCYCLE;


-- Sequences for authors
DROP SEQUENCE seq_id_author;

CREATE SEQUENCE seq_id_author
START WITH 1
INCREMENT BY 1
NOCYCLE;
