-- Sequences for people
DROP SEQUENCE seq_id_pessoa;

CREATE SEQUENCE seq_id_pessoa
START WITH 1
INCREMENT BY 1
NOCYCLE;

-- Sequences for books
DROP SEQUENCE seq_id_aluguer;

CREATE SEQUENCE seq_id_aluguer
START WITH 1
INCREMENT BY 1
NOCYCLE;
