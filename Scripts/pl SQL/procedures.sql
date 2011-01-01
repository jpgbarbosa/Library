-- Adds a new reader to the data base.
CREATE OR REPLACE PROCEDURE addReader ( nomePessoa IN Pessoa.nome_pessoa%type, morada IN Pessoa.morada%type, bi IN NUMBER, data IN date ,telefone IN NUMBER, email IN Pessoa.e_mail%type,
										returnValue OUT INTEGER) IS
	current_id NUMBER;
	
BEGIN
	SELECT seq_id_pessoa.nextval INTO current_id
	FROM dual;
	
	INSERT INTO Pessoa VALUES (nomePessoa, morada, bi, data , telefone, email, current_id);
	INSERT INTO Leitor VALUES (current_id, 0);
		
	returnValue := 0;
	COMMIT;
	
EXCEPTION
	-- Error raised when we insert the same BI.
	WHEN DUP_VAL_ON_INDEX THEN
		ROLLBACK;
		returnValue := -1;
	WHEN OTHERS THEN
		ROLLBACK;
		returnValue := -2;
		
END;
/

--finish this procedur that sets data_saida to sysdate. pay attention when we list employees
CREATE OR REPLACE PROCEDURE fireEmployee(biFuncionario IN Pessoa.id_pessoa%type, retVal OUT INTEGER) IS

	idPessoa Pessoa.id_pessoa%type;
	varData Funcionario.DATA_SAIDA%type;

BEGIN

	SELECT id_pessoa INTO idPessoa FROM pessoa WHERE BI = biFuncionario FOR UPDATE;
	SELECT data_saida INTO varData FROM funcionario WHERE id_pessoa = idPessoa FOR UPDATE;
	
	IF (varData IS NOT NULL) THEN
		ROLLBACK;
		retVal:=-3;
		RETURN;	
	END IF;
	
	UPDATE Funcionario SET DATA_SAIDA = SYSDATE WHERE idPessoa = id_pessoa;
	
	retVal :=1;
	COMMIT;
	
EXCEPTION
	WHEN NO_DATA_FOUND THEN
		ROLLBACK;
		retVal :=-1;
		RETURN;
	WHEN OTHERS THEN
		ROLLBACK;
		retVal := -2;
		RETURN;
END;
/

CREATE OR REPLACE PROCEDURE updateReader (nomePessoa IN Pessoa.nome_pessoa%type, pmorada IN Pessoa.morada%type, pbi IN NUMBER, varData IN DATE ,ptelefone IN NUMBER, email IN Pessoa.e_mail%type,
											returnValue OUT INTEGER) IS

	idPessoa Pessoa.ID_Pessoa%type;
											
BEGIN

	SELECT id_pessoa INTO idPessoa FROM pessoa p WHERE p.bi = pbi FOR UPDATE;

	UPDATE pessoa p SET p.nome_pessoa = nomePessoa, p.morada=pmorada, p.data = varData, p.telefone = ptelefone, p.e_mail=email
	WHERE p.id_pessoa = idPessoa;

	returnValue := 1;
	COMMIT;

EXCEPTION
	WHEN no_data_found THEN
		ROLLBACK;
		returnValue := -1;
		RETURN;
	WHEN OTHERS THEN
		ROLLBACK;
		returnValue := -2;
		RETURN;
END;
/

CREATE OR REPLACE PROCEDURE updateEmployee (nomePessoa IN Pessoa.nome_pessoa%type, pmorada IN Pessoa.morada%type, pbi IN NUMBER, varData IN DATE ,ptelefone IN NUMBER, email IN Pessoa.e_mail%type,
											ppassword IN Autenticacao.password%type, returnValue OUT INTEGER) IS

	idPessoa Pessoa.ID_Pessoa%type;
											
BEGIN

	SELECT id_pessoa INTO idPessoa FROM pessoa p WHERE p.bi = pbi FOR UPDATE;

	UPDATE Pessoa p SET p.nome_pessoa = nomePessoa, p.morada=pmorada, p.data = varData, p.telefone = ptelefone, p.e_mail=email
	WHERE p.id_pessoa = idPessoa;
	
	UPDATE Autenticacao a SET a.password = ppassword
	WHERE a.Id_empregado = idPessoa;

	returnValue:=1;
	COMMIT;

EXCEPTION
	WHEN no_data_found THEN
		ROLLBACK;
		returnValue := -1;
		RETURN;
	WHEN OTHERS THEN
		ROLLBACK;
		returnValue := -2;
		RETURN;
END;
/

CREATE OR REPLACE PROCEDURE addEmployee ( nomePessoa IN Pessoa.nome_pessoa%type, morada IN Pessoa.morada%type, bi IN NUMBER, data in date ,telefone IN NUMBER, email IN Pessoa.e_mail%type,
											password IN Autenticacao.password%type, returnValue OUT INTEGER) IS
	current_id NUMBER;
	ID_NOT_FOUND exception; 
	PRAGMA exception_init(ID_NOT_FOUND, -2291); 
	
BEGIN
	SELECT seq_id_pessoa.nextval INTO current_id
	FROM dual;
	
	INSERT INTO Pessoa VALUES (nomePessoa, morada, bi, data , telefone, email, current_id);
	INSERT INTO Funcionario VALUES (current_id, sysdate, null);
	INSERT INTO AUTENTICACAO VALUES (current_id, password);
		
	returnValue := current_id;
	COMMIT;
	
EXCEPTION
	-- Error raised when we insert the same BI.
	WHEN DUP_VAL_ON_INDEX THEN
		ROLLBACK;
		returnValue := -1;
	WHEN OTHERS THEN
		ROLLBACK;
		returnValue := -2;
	
END;
/

CREATE OR REPLACE FUNCTION getPrateleira (gen IN PRATELEIRA.GENERO%type, total IN PUBLICACAO.TOTAL%type) RETURN PRATELEIRA.ID_PRATELEIRA%type IS

	prat PRATELEIRA.ID_PRATELEIRA%type:=-1;

	CURSOR shelf IS
		SELECT id_prateleira FROM prateleira WHERE UPPER(genero) LIKE UPPER(gen) AND (OCUPACAO+total)<CAPACIDADE;

BEGIN

	OPEN shelf;
	FETCH shelf INTO prat;
	CLOSE shelf;

	RETURN prat;	

EXCEPTION
	WHEN no_data_found THEN
		RETURN prat;
	
END;
/


CREATE OR REPLACE FUNCTION getEditora (edi IN EDITORA.NOME_EDITORA%type) RETURN EDITORA.ID_EDITORA%type IS

	edit EDITORA.ID_EDITORA%type:=-1;

BEGIN

	SELECT ID_EDITORA INTO edit FROM editora WHERE UPPER(NOME_EDITORA) LIKE UPPER(edi);

	RETURN edit;

EXCEPTION
	WHEN no_data_found THEN
		RETURN edit;
	
END;
/


-- falta verificar a questao das prateleiras! atencao a rollbacks!
CREATE OR REPLACE PROCEDURE addDocument ( Aut IN AUTOR.NOME_AUTOR%type, Edi IN EDITORA.NOME_EDITORA%type,
											gen IN PRATELEIRA.GENERO%type,
											pages IN PUBLICACAO.PAGINAS%type,
											descri IN PUBLICACAO.DESCRICAO%type,
											varData IN PUBLICACAO.DATA%type, nome IN PUBLICACAO.NOME_DOC%type, 
											total IN PUBLICACAO.TOTAL%type, retVal OUT INTEGER) IS
	current_id NUMBER;
	idAut AUTOR.ID_AUTOR%type;
	idEdi EDITORA.ID_EDITORA%type;
	idPra PRATELEIRA.ID_PRATELEIRA%type;
	
BEGIN
	BEGIN
		SELECT id_autor INTO idAut FROM Autor WHERE UPPER(nome_autor) LIKE UPPER(Aut) FOR UPDATE;
		IF (SQL%ROWCOUNT > 1) THEN
			ROLLBACK;
			retval := -1;
			RETURN;
		END IF;
		
		EXCEPTION
			WHEN no_data_found then
				SELECT seq_id_author.NEXTVAL INTO idAut FROM dual;
				INSERT INTO AUTOR VALUES (Aut,idAut);
	END;
	
	idPra := getPrateleira(gen,total);
	
	IF (idPra = -1) THEN --n existem prateleiras para esse genero
		SELECT seq_id_shelf.NEXTVAL INTO idPra FROM dual;
		IF (total > 100) THEN
			INSERT INTO prateleira VALUES(total+10,0,idPra,gen);
		ELSE
			INSERT INTO prateleira VALUES(100,0,idPra,gen);
		END IF;
	END IF;
	
	idEdi := getEditora(Edi);
	
	IF (idEdi = -1) THEN 
		SELECT seq_id_publisher.NEXTVAL INTO idEdi FROM dual;
		INSERT INTO editora VALUES (idEdi,Edi);
	END IF;


	SELECT seq_id_document.NEXTVAL INTO current_id
	FROM dual;
	
	INSERT INTO PUBLICACAO VALUES (current_id, idPra, idAut, idEdi, pages, descri, varData, nome, total, total);
	UPDATE prateleira SET OCUPACAO = OCUPACAO+total WHERE ID_PRATELEIRA = idPra;
	
	retVal := 0;
	COMMIT;
	
EXCEPTION
	-- Error raised when we insert the same title
	WHEN DUP_VAL_ON_INDEX THEN
		ROLLBACK;
		retVal := -1;
	WHEN OTHERS THEN
		ROLLBACK;
		retVal := -2;
	
END;
/


--penso que esta a funcionar como deve ser. verificar melhor
CREATE OR REPLACE TRIGGER checkShelf AFTER UPDATE OF TOTAL ON PUBLICACAO FOR EACH ROW WHEN (new.total != old.total)

DECLARE

	oldPratRow PRATELEIRA%ROWTYPE;
	newPratRow PRATELEIRA%ROWTYPE;
	pub publicacao%rowtype;
	idPra PRATELEIRA.ID_PRATELEIRA%type;

BEGIN

	--Procuramos se a prateleira q ja contem estas publicacoes tem espaco. se nao devolver nada, entao significa q ainda existe espaco nessa prateleira
	SELECT * INTO oldPratRow FROM prateleira WHERE :old.id_prateleira = id_prateleira 
		AND (capacidade < OCUPACAO +(:new.total-:old.total)) FOR UPDATE;
		
	BEGIN
		--Procuramos um prateleira do mesmo genero que tenha espaco
		SELECT * INTO newPratRow FROM prateleira WHERE (capacidade >= OCUPACAO +(:new.total)) 
			AND genero LIKE oldPratRow.genero FOR UPDATE;
		
	EXCEPTION 
		--se nao existir entao criamos uma nova
		WHEN no_data_found THEN
			SELECT seq_id_shelf.NEXTVAL INTO idPra FROM dual;
			IF (:new.total > 100) THEN
				INSERT INTO prateleira VALUES(:new.total+10,0,idPra,oldPratRow.genero);
			ELSE
				INSERT INTO prateleira VALUES(100,0,idPra,oldPratRow.genero);
			END IF;
			
			SELECT * INTO newPratRow FROM prateleira WHERE (capacidade >= OCUPACAO +(:new.total)) 
				AND genero like oldPratRow.genero FOR UPDATE;
					
	END;
	
	UPDATE prateleira SET OCUPACAO=OCUPACAO+:new.total WHERE ID_PRATELEIRA = newPratRow.id_prateleira;
	UPDATE prateleira SET OCUPACAO=OCUPACAO-:old.total WHERE ID_PRATELEIRA = oldPratRow.id_prateleira;
	
	COMMIT;
	
EXCEPTION
	WHEN no_data_found THEN
		UPDATE prateleira set ocupacao=ocupacao+(:new.total-:old.total) where ID_PRATELEIRA = :old.id_prateleira;
		COMMIT;
		RETURN;
	WHEN OTHERS THEN
		ROLLBACK;
		RETURN;
END;
/

--Updates the number of available books and the number of requisitions of a given reader.
CREATE OR REPLACE TRIGGER updateReqsAndCopies AFTER INSERT ON Emprestimo FOR EACH ROW

BEGIN
	UPDATE Publicacao p SET p.disponiveis = p.disponiveis - 1 
	WHERE p.id_doc = :new.id_doc; 
		
	UPDATE Leitor SET no_emprestimos = no_emprestimos + 1
	WHERE id_pessoa = :new.lei_id_pessoa;
END;
/

CREATE OR REPLACE PROCEDURE removeCopyDocument(idDoc IN PUBLICACAO.ID_DOC%type, noRem IN PUBLICACAO.TOTAL%type, retVal OUT NUMBER) IS

	avail publicacao.disponiveis%type;
	pra publicacao.id_prateleira%type;

BEGIN
	SELECT disponiveis INTO avail FROM publicacao WHERE id_doc = idDoc FOR UPDATE;
	SELECT id_prateleira INTO pra FROM publicacao WHERE id_doc = idDoc FOR UPDATE;
	
	IF (avail >= noRem) THEN
		UPDATE PUBLICACAO
		SET DISPONIVEIS=DISPONIVEIS-noRem, TOTAL=TOTAL-noRem
		WHERE id_doc = idDoc;
	
		UPDATE PRATELEIRA
		SET OCUPACAO=OCUPACAO-noRem
		WHERE ID_PRATELEIRA = pra;
		
		retVal :=1;
	ELSE
		retVal :=0;
	END IF;
	
	COMMIT;

EXCEPTION
	WHEN NO_DATA_FOUND THEN
		ROLLBACK;
		retVal := -1;
	WHEN OTHERS THEN
		ROLLBACK;
		retVal := -2;
END;
/


-- falta verificar a questao das prateleiras!
CREATE OR REPLACE PROCEDURE addCopyDocument(idDoc IN PUBLICACAO.ID_DOC%type, novos IN PUBLICACAO.TOTAL%type, retVal OUT NUMBER) IS
	checker INTEGER;

BEGIN
	SELECT id_doc INTO checker
	FROM Publicacao
	WHERE id_doc = idDoc
	FOR UPDATE;

	UPDATE PUBLICACAO
	SET DISPONIVEIS=DISPONIVEIS+novos, TOTAL=TOTAL+novos
	WHERE id_doc = idDoc;
	
	retVal := 0;
	COMMIT;

EXCEPTION
	WHEN NO_DATA_FOUND THEN
		ROLLBACK;
		retVal := -1;
	WHEN OTHERS THEN
		ROLLBACK;
		retVal := -2;

END;
/	


-- Script used to make requisitions.
CREATE OR REPLACE PROCEDURE newRequisition ( book_id IN Publicacao.id_doc%type, reader_id IN Pessoa.id_pessoa%type, employee_id IN Pessoa.id_pessoa%type,
											returnValue OUT INTEGER) IS
	no_available INTEGER; 
	id_pessoa_conf INTEGER; 
	current_id NUMBER;
	no_requisitions INTEGER;
	no_faulty_requisitions INTEGER;
	
	ID_NOT_FOUND exception; 
	PRAGMA exception_init(ID_NOT_FOUND, -2291); 
	
BEGIN 
	SELECT seq_id_aluguer.nextval INTO current_id
	FROM dual;
	
	no_requisitions := 0;
	no_faulty_requisitions := 0;
	returnValue := 0;
	
	BEGIN
		SELECT p.disponiveis INTO no_available 
		FROM Publicacao p 
		WHERE p.id_doc = book_id; 
	EXCEPTION
		WHEN NO_DATA_FOUND THEN 
			returnValue := -2;
			RETURN;
	END;
		
	--Then, we have to see if the reader hasn't more than three books or
	BEGIN
		SELECT COUNT(*) INTO no_requisitions
		FROM Emprestimo
		WHERE LEI_ID_PESSOA = reader_id AND Data_entrega IS NULL;
		
		IF (no_requisitions = 3) THEN
			returnValue := -5;
			RETURN;
		END IF;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_requisitions := 0;
	END;

	-- isn't delaying in delivering some books.			
	BEGIN
		SELECT COUNT(*) INTO no_faulty_requisitions
		FROM Emprestimo
		WHERE LEI_ID_PESSOA = reader_id AND Data_prevista - SYSDATE < 0 AND Data_entrega IS NULL;
		
		IF (no_faulty_requisitions > 0) THEN
			returnValue := -6;
			RETURN;
		END IF;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_faulty_requisitions := 0;
	END;
		
	-- We check if there are enough copies for this requisition.
	IF (no_available > 0) THEN 
		INSERT INTO Emprestimo VALUES (reader_id, employee_id, book_id, current_id, SYSDATE, SYSDATE + 7, null ); 
		returnValue := current_id;
	ELSIF (no_available = 0) THEN
		returnValue := -1;
	END IF; 
	
	COMMIT; 
	
EXCEPTION 
	WHEN NO_DATA_FOUND THEN 
		ROLLBACK;
		returnValue := -3; 
	WHEN OTHERS THEN
		ROLLBACK;
		returnValue := -4;

END;
/


-- Script used to return requisitions.
CREATE OR REPLACE PROCEDURE returnRequisition ( req_id IN Emprestimo.id_emprestimo%type, returnValue OUT INTEGER) IS
	return_date DATE; 
	id_book Publicacao.id_doc%type;
	
	ID_NOT_FOUND exception; 
	PRAGMA exception_init(ID_NOT_FOUND, -2291); 
	
BEGIN 
	
	BEGIN
		SELECT e.data_entrega INTO return_date 
		FROM Emprestimo e 
		WHERE e.id_emprestimo = req_id
		FOR UPDATE; 
	EXCEPTION
		-- If there's no requisiton with this ID.
		WHEN NO_DATA_FOUND THEN 
			ROLLBACK;
			returnValue := -2;
			RETURN;
	END;
	
	IF (return_date IS NULL) THEN 
	
		SELECT e.id_doc INTO id_book
		FROM Emprestimo e
		WHERE e.id_emprestimo = req_id
		FOR UPDATE;
		
		UPDATE Publicacao SET disponiveis = disponiveis + 1
		WHERE id_doc = id_book;
		
		UPDATE Emprestimo SET data_entrega = SYSDATE
		WHERE id_emprestimo = req_id;
		
		returnValue := 0;
	ELSE 
		-- if the requisition was already returned.
		returnValue := -1;
	END IF; 
	
	COMMIT; 
	
EXCEPTION 
	WHEN OTHERS THEN
		ROLLBACK;
		returnValue := -3;

END;
/

-- STATISTICS

-- Employees
CREATE OR REPLACE PROCEDURE employeesStats (no_entries OUT INTEGER, fired_employees OUT INTEGER, avg_working_time OUT FLOAT) IS
	
BEGIN
	
	SET TRANSACTION READ ONLY;
	
	-- Checks the number of entries
	BEGIN
		SELECT COUNT(*) INTO no_entries
		FROM Funcionario;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_entries := 0;
	END;
	
	-- Checks the number of fired employees, meaning the ones who have an exit date
	BEGIN
		SELECT COUNT(*) INTO fired_employees
		FROM Funcionario
		WHERE Data_saida IS NOT NULL;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			fired_employees := 0;
	END;
	
	BEGIN
		-- If the employees hasn't been fired, we consider the actual date as the exit date, just for counting purposes.
		SELECT ROUND(AVG(NVL(Data_saida, SYSDATE) - Data_entrada), 2) INTO avg_working_time
		FROM Funcionario;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			avg_working_time := 0;
	END;
	
	COMMIT;

END;
/


-- Readers
CREATE OR REPLACE PROCEDURE readersStats (no_entries OUT INTEGER, readers_with_books OUT INTEGER, faulty_readers OUT INTEGER) IS
	
BEGIN

	SET TRANSACTION READ ONLY;
	
	-- Checks the number of entries
	BEGIN
		SELECT COUNT(*) INTO no_entries
		FROM Leitor;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_entries := 0;
	END;
	
	-- Checks the number of fired employees, meaning the ones who have an exit date
	BEGIN
		SELECT COUNT(*) INTO readers_with_books
		FROM (	SELECT LEI_ID_PESSOA "Pessoas"
				FROM Emprestimo
				WHERE Data_entrega IS NULL
				GROUP BY LEI_ID_PESSOA) red;
	
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			readers_with_books := 0;
	END;
	
	BEGIN
		SELECT COUNT(*) INTO faulty_readers
		FROM Emprestimo
		WHERE Data_prevista - SYSDATE < 0 AND Data_entrega IS NULL
		GROUP BY LEI_ID_PESSOA;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			faulty_readers := 0;
	END;
	
	COMMIT;

END;
/

-- Books and Shelves
CREATE OR REPLACE PROCEDURE booksAndShelvesStats (no_books OUT INTEGER, max_pages OUT INTEGER, min_pages OUT INTEGER,
													avg_pages OUT FLOAT, avg_copies OUT FLOAT, no_shelves OUT INTEGER,
													occupation OUT FLOAT, avg_capacity OUT FLOAT) IS
	
BEGIN
	
	SET TRANSACTION READ ONLY;
	
	-- Checks the number of books
	BEGIN
		SELECT COUNT(*) INTO no_books
		FROM Publicacao;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_books := 0;
	END;
	
	-- max pages
	BEGIN
		SELECT MAX(Paginas) INTO max_pages
		FROM Publicacao;
		IF (max_pages IS NULL) THEN
			max_pages := 0;
		END IF;
	END;
	
	-- min pages
	BEGIN
		SELECT MIN(Paginas) INTO min_pages
		FROM Publicacao;
		IF (min_pages IS NULL) THEN
			min_pages := 0;
		END IF;
	END;
	
	-- Check the average number of copies per publication
	BEGIN
		SELECT ROUND(AVG(Paginas),2) INTO avg_pages
		FROM Publicacao;
		IF (avg_pages IS NULL) THEN
			avg_pages := 0;
		END IF;
	END;
	
	-- Check the average number of copies per publication
	BEGIN
		SELECT ROUND(AVG(Total),2) INTO avg_copies
		FROM Publicacao;
		IF (avg_copies IS NULL) THEN
			avg_copies := 0;
		END IF;	
	END;
	
	-- SHELVES
	
	-- Checks the number of books
	BEGIN
		SELECT COUNT(*) INTO no_shelves
		FROM Prateleira;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_shelves := 0;
	END;
	
	-- Checks the occupation rate of the shelves
	BEGIN
		SELECT ROUND(SUM(OCUPACAO)/SUM(CAPACIDADE), 2) INTO occupation
		FROM Prateleira;
		IF (occupation IS NULL) THEN
			occupation := 0;
		END IF;			
	END;
	
	-- Checks the number of books
	BEGIN
		SELECT ROUND(AVG(CAPACIDADE),2) INTO avg_capacity
		FROM Prateleira;
		IF (avg_capacity IS NULL) THEN
			avg_capacity := 0;
		END IF;	
	END;	
	
	COMMIT;

END;
/

-- Requisitions
CREATE OR REPLACE PROCEDURE requisitionsStats (no_entries OUT INTEGER, on_going_reqs OUT INTEGER, finished_reqs OUT INTEGER,
												no_faulty_reqs OUT INTEGER, avg_reqs_per_day OUT FLOAT, no_days_with_reqs OUT INTEGER) IS
	
BEGIN
	
	SET TRANSACTION READ ONLY;
	
	-- Checks the number of requisitons
	BEGIN
		SELECT COUNT(*) INTO no_entries
		FROM Emprestimo;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_entries := 0;
	END;
	
	-- Checks the number of on going requisitons
	BEGIN
		SELECT COUNT(*) INTO on_going_reqs
		FROM Emprestimo
		WHERE Data_entrega IS NULL;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			on_going_reqs := 0;
	END;
	
	-- Checks the number of finished requisitions
	BEGIN
		SELECT COUNT(*) INTO finished_reqs
		FROM Emprestimo
		WHERE Data_entrega IS NOT NULL;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			finished_reqs := 0;
	END;
	
	--Checks the faulty requisitions
	BEGIN
		SELECT COUNT(*) INTO no_faulty_reqs
		FROM Emprestimo
		WHERE Data_prevista - SYSDATE < 0 AND Data_entrega IS NULL;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_faulty_reqs := 0;
	END;
	
	-- Checks the average of requisitions per day, counting only days with at least one requisition
	BEGIN
		SELECT ROUND(AVG(COUNT(*)),2) INTO avg_reqs_per_day
		FROM Emprestimo
		GROUP BY TO_CHAR(DATA_DE_REQUISITO,'yyyy-mm-dd');
		IF (avg_reqs_per_day IS NULL) THEN
			avg_reqs_per_day := 0;
		END IF;	
	END;
	
	-- Checks the number of days with requisitions
	BEGIN
		SELECT COUNT(*) INTO no_days_with_reqs
		FROM Emprestimo
		GROUP BY TO_CHAR(DATA_DE_REQUISITO,'yyyy-mm-dd');
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_days_with_reqs := 0;
	END;
	
	COMMIT;

END;
/


-- Authors and Publishers
CREATE OR REPLACE PROCEDURE authorsAndPublishersStats (no_authors OUT INTEGER, avg_doc_per_author OUT FLOAT,
														no_publishers OUT INTEGER, avg_doc_per_publisher OUT FLOAT) IS
	
BEGIN
	
	SET TRANSACTION READ ONLY;
	
	-- AUTHORS
	-- Checks the number of entries
	BEGIN
		SELECT COUNT(*) INTO no_authors
		FROM Autor;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_authors := 0;
	END;
	
	-- Checks the average of copies per author
	BEGIN
		SELECT ROUND(p.soma/aut.total_aut) INTO avg_doc_per_author
		FROM (SELECT COUNT(*) total_aut FROM Autor) aut, (SELECT SUM(Total) soma FROM Publicacao) p;
		IF (avg_doc_per_author IS NULL) THEN
			avg_doc_per_author := 0;
		END IF;		
	END;
	
	-- PUBLISHERS
	
	-- Checks the number of entries
	BEGIN
		SELECT COUNT(*) INTO no_publishers
		FROM Editora;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			no_publishers := 0;
	END;
	
	-- Checks the average of copies per publisher
	BEGIN
		SELECT ROUND(p.soma/e.total_edit) INTO avg_doc_per_publisher
		FROM (SELECT COUNT(*) total_edit FROM Editora) e, (SELECT SUM(Total) soma FROM Publicacao) p;
		IF (avg_doc_per_publisher IS NULL) THEN
			avg_doc_per_publisher := 0;
		END IF;
	END;
	
	COMMIT;

END;
/

CREATE OR REPLACE PROCEDURE login (username IN AUTENTICACAO.ID_EMPREGADO%type, pw IN AUTENTICACAO.PASSWORD%type, returnValue OUT INTEGER) IS

	-- Just to check if we have this ID.
	temp INTEGER;
BEGIN
	SELECT a.ID_EMPREGADO INTO temp
	FROM Autenticacao a
	WHERE a.ID_EMPREGADO = username AND a.PASSWORD = pw;
	
	returnValue := 1;

EXCEPTION
	WHEN NO_DATA_FOUND THEN
		returnValue := -1;
		
END;
/