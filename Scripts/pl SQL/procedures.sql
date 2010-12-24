-- Adds a new reader to the data base.
CREATE OR REPLACE PROCEDURE addReader ( nomePessoa IN Pessoa.nome_pessoa%type, morada IN Pessoa.morada%type, bi IN NUMBER,telefone IN NUMBER, email IN Pessoa.e_mail%type) IS
	current_id NUMBER;
	
BEGIN
	SELECT seq_id_pessoa.nextval INTO current_id
	FROM dual;
	
	INSERT INTO Pessoa VALUES (nomePessoa, morada, bi, telefone, email, current_id);
	INSERT INTO Leitor VALUES (current_id, 0);
		
	COMMIT;
	
EXCEPTION
	WHEN DUP_VAL_ON_INDEX THEN
		current_id := -1;
	WHEN OTHERS THEN
		current_id := -1;
		
END;
/


CREATE OR REPLACE PROCEDURE addEmployee ( nomePessoa IN Pessoa.nome_pessoa%type, morada IN Pessoa.morada%type, bi IN NUMBER,telefone IN NUMBER, email IN Pessoa.e_mail%type) IS
	current_id NUMBER;
	
BEGIN
	SELECT seq_id_pessoa.nextval INTO current_id
	FROM dual;
	
	INSERT INTO Pessoa VALUES (nomePessoa, morada, bi, telefone, email, current_id);
	INSERT INTO Funcionario VALUES (current_id, sysdate, null);
		
	COMMIT;
	
EXCEPTION
	WHEN DUP_VAL_ON_INDEX THEN
		current_id := -1;
		-- Error raised when we insert the same BI.
		--TODO:  Return an appropriate value. Maybe we have to create a OUT variable
	WHEN OTHERS THEN
		--TODO: The same as above.
		current_id := -1;
	
END;


-- Script used to make requisitions.
CREATE OR REPLACE PROCEDURE newRequisition ( book_id IN Publicacao.id_doc%type, reader_id IN Pessoa.id_pessoa%type, employee_id IN Pessoa.id_pessoa%type) IS
	no_available INTEGER; 
	id_pessoa_conf INTEGER; 
	current_id NUMBER;
	
	ID_NOT_FOUND exception; 
	PRAGMA exception_init(ID_NOT_FOUND, -2291); 
	
BEGIN 
	SELECT seq_id_aluguer.nextval INTO current_id
	FROM dual;
	
	SELECT p.disponiveis INTO no_available 
	FROM Publicacao p 
	WHERE p.id_doc = book_id; 
	
	IF (no_available > 0) THEN 
		UPDATE Publicacao p SET p.disponiveis = p.disponiveis - 1 
		WHERE p.id_doc = book_id; 
		
		UPDATE Leitor SET no_emprestimos = no_emprestimos + 1
		WHERE id_pessoa = reader_id;
		
		INSERT INTO Emprestimo VALUES (reader_id, employee_id, book_id, current_id, SYSDATE, SYSDATE + 7, null ); 
		 
	ELSE 
		id_pessoa_conf := -1; 
	END IF; 
	
	COMMIT; 
	
EXCEPTION 
	WHEN NO_DATA_FOUND THEN 
		id_pessoa_conf := -1; 
	WHEN ID_NOT_FOUND THEN 
		id_pessoa_conf := -1; 

END; 