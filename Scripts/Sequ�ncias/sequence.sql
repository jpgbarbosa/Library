-- Sequences for people
DROP SEQUENCE seq_id_pessoa;

CREATE SEQUENCE seq_id_pessoa
--We have to start in 2 because the number 1 is reserved for the admin.
START WITH 2
INCREMENT BY 1
NOCYCLE;

-- Sequences for books
DROP SEQUENCE seq_id_document;

CREATE SEQUENCE seq_id_document
START WITH 1
INCREMENT BY 1
NOCYCLE;


-- Sequences for requisitions
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

-- Sequences for shelfs
DROP SEQUENCE seq_id_shelf;

CREATE SEQUENCE seq_id_shelf
START WITH 1
INCREMENT BY 1
NOCYCLE;

