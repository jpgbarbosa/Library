CREATE OR REPLACE PROCEDURE addReader ( nomePessoa IN Pessoa.nome_pessoa%type, morada IN Pessoa.morada%type, bi IN NUMBER,telefone IN NUMBER, email IN Pessoa.e_mail%type) IS
	current_id NUMBER;
	
BEGIN
	SELECT seq_id_pessoa.nextval INTO current_id
	FROM dual;
	
	INSERT INTO Pessoa VALUES (nomePessoa, morada, bi, telefone, email, current_id);
	INSERT INTO Leitor VALUES (current_id, 0);
		
	COMMIT;
	
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
	
END;

/


CREATE OR REPLACE PROCEDURE addDocument ( idPra IN PUBLICACAO.ID_PRATELEIRA%type,
											idAut IN PUBLICACAO.ID_AUTOR%type, idEdi IN PUBLICACAO.ID_EDITORA%type, descri IN PUBLICACAO.DESCRICAO%type,
											DATA IN PUBLICACAO.DATA%type, nome IN PUBLICACAO.NOME_DOC%type, disp IN PUBLICACAO.DISPONIVEIS%type,
											total IN PUBLICACAO.TOTAL%type) IS
	current_id NUMBER;
	
BEGIN
	SELECT seq_id_document.nextval INTO current_id
	FROM dual;
	
	INSERT INTO PUBLICACAO VALUES (current_id, idPra, idAut, idEdi, descri, DATA, nome, disp, total);
		
	COMMIT;
	
END;

/

CREATE OR REPLACE PROCEDURE addCopyDocument(idDoc IN PUBLICACAO.ID_DOC%type, novos IN PUBLICACAO.TOTAL%type) IS

Begin
	update PUBLICACAO
		set DISPONIVEIS=DISPONIVEIS+novos, TOTAL=TOTAL+novos
		where id_doc = idDoc;

commit;

Exception
	when no_data_found then
		return;

end;

/	