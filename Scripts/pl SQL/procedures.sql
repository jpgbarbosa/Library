CREATE OR REPLACE PROCEDURE addReader ( nomePessoa IN Pessoa.nome_pessoa%type, morada IN Pessoa.morada%type, bi IN NUMBER,telefone IN NUMBER, email IN Pessoa.e_mail%type) IS
	current_id NUMBER;
	
BEGIN
	SELECT seq_id_pessoa.nextval INTO current_id
	FROM dual;
	
	INSERT INTO Pessoa VALUES (nomePessoa, morada, bi, telefone, email, current_id);
	INSERT INTO Leitor VALUES (current_id, 0);
		
	COMMIT;
	
END;


CREATE OR REPLACE PROCEDURE addEmployee ( nomePessoa IN Pessoa.nome_pessoa%type, morada IN Pessoa.morada%type, bi IN NUMBER,telefone IN NUMBER, email IN Pessoa.e_mail%type) IS
	current_id NUMBER;
	
BEGIN
	SELECT seq_id_pessoa.nextval INTO current_id
	FROM dual;
	
	INSERT INTO Pessoa VALUES (nomePessoa, morada, bi, telefone, email, current_id);
	INSERT INTO Funcionario VALUES (current_id, sysdate, null);
		
	COMMIT;
	
END;