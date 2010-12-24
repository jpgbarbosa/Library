-- Insert new authors
INSERT INTO Autor VALUES ('José Saramago', 1);
INSERT INTO Autor VALUES ('William Shakespeare', 2);
INSERT INTO Autor VALUES ('Luís de Camões', 3);
INSERT INTO Autor VALUES ('George Colouris', 4);

-- Insert new publishers
INSERT INTO Editora VALUES (1,'Caminho');
INSERT INTO Editora  VALUES (2, 'Simon and Schuster');
INSERT INTO Editora  VALUES (3, 'Lusitania');
INSERT INTO Editora  VALUES (4, 'Addison Wesley');

-- Insert new shelfs
INSERT INTO Prateleira VALUES (1, 100, 1 , 'Fiction');
INSERT INTO Prateleira VALUES (1, 100, 2 , 'Classics');
INSERT INTO Prateleira VALUES (1, 100, 3 , 'Poetry');
INSERT INTO Prateleira VALUES (1, 100, 4 , 'Computing and the net');

-- Insert new books
INSERT INTO Publicacao VALUES (1, 1, 1 , 1, 300, 'Descricao', SYSDATE, 'Ensaio Sobre a Cegueira', 2, 2);
INSERT INTO Publicacao VALUES (2, 2, 2 , 2, 500, 'Descricao', SYSDATE, 'Hamlet', 1, 1);
INSERT INTO Publicacao VALUES (3, 3, 3 , 3, 400, 'Descricao', SYSDATE, 'Os Lusíadas', 5, 5);
INSERT INTO Publicacao VALUES (4, 4, 4 , 4, 200, 'Descricao', SYSDATE, 'Distributed Systems: Design and Concepts', 3, 3);

COMMIT;