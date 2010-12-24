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

/

create or replace function getPrateleira (gen IN PRATELEIRA.GENERO%type) return PRATELEIRA.ID_PRATELEIRA%type is

prat PRATELEIRA.ID_PRATELEIRA%type:=-1;

begin

select id_prateleira into prat from prateleira where upper(genero) like upper(gen);

if SQL%NOTFOUND then
	return prat;
end if;
	
return prat;
	
end;

/


create or replace function getEditora (edi IN EDITORA.NOME_EDITORA%type) return EDITORA.ID_EDITORA%type is

edit EDITORA.ID_EDITORA%type:=-1;

begin

select ID_EDITORA into edit from editora where upper(NOME_EDITORA) like upper(edi);

if SQL%NOTFOUND then
	return edit;
end if;
	
return edit;
	
end;

/


-- falta verificar a questao das prateleiras! atencao a rollbacks!
CREATE OR REPLACE PROCEDURE addDocument ( Aut IN AUTOR.NOME_AUTOR%type, Edi IN EDITORA.NOME_EDITORA%type,
											gen IN PRATELEIRA.GENERO%type,
											descri IN PUBLICACAO.DESCRICAO%type,
											DATA IN PUBLICACAO.DATA%type, nome IN PUBLICACAO.NOME_DOC%type, 
											total IN PUBLICACAO.TOTAL%type, retVal out integer) IS
	current_id NUMBER;
	idAut AUTOR.ID_AUTOR%type;
	idEdi EDITORA.ID_EDITORA%type;
	idPra PRATELEIRA.ID_PRATELEIRA%type;
	
BEGIN

	Begin
		select id_autor into idAut from Autor where upper(nome_autor) like upper(Aut);

		if SQL%NOTFOUND then
			SELECT seq_id_author.nextval INTO idAut FROM dual;
			insert into editora values (idAut,Aut);
		elsif SQL%ROWCOUNT>1 then
			retVal:=-1;
			return;
		end if;
		
		idPra := getPrateleira(gen);
		
		if idPra=-1 then --n existem prateleiras para esse genero
			retVal:=-2;
			return;
		end if;
		
		idEdi := getEditora(Edi);
		
		if idEdi=-1 then 
			SELECT seq_id_publisher.nextval INTO idEdi FROM dual;
			insert into editora values (idEdi,Edi);
		end if;

	end;


	SELECT seq_id_document.nextval INTO current_id
	FROM dual;
	
	INSERT INTO PUBLICACAO VALUES (current_id, idPra, idAut, idEdi, descri, DATA, nome, total, total);
		
	COMMIT;
	
END;

/

-- falta verificar a questao das prateleiras!
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

/
