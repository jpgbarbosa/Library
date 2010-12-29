
--SCRIPTS PARA O MENU DA DEVOLUCAO DE EMPRESTIMOS

--seleccionar EMPRESTIMO por id_pessoa

	--entrada -> leiIdPessoa
select ID_EMPRESTIMO, ID_DOC, lei_id_pessoa from emprestimo where lei_id_pessoa = leiIdPessoa order by 1,2;

	--entrada -> idReq
select ID_EMPRESTIMO, ID_DOC, lei_id_pessoa from emprestimo where ID_EMPRESTIMO = idReq order by 1,3,2;

	--entrada -> nomeDoc
select e.ID_EMPRESTIMO, e.ID_DOC, e.lei_id_pessoa from emprestimo e, publicacao p 
	where upper(p.NOME_DOC) like '%'||upper('nomeDoc')||'%'
		AND e.ID_DOC = p.ID_DOC
	order by 2,3,1;

	--entrada -> pDate: String dd/mm/yyyy
select e.ID_EMPRESTIMO, e.ID_DOC, e.lei_id_pessoa from emprestimo e 
	where to_char(e.DATA_DE_REQUISITO,'dd/mm/yyyy') like pDate
	order by 1,3,2;

	--entrada idDoc
select ID_EMPRESTIMO, ID_DOC, LEI_ID_PESSOA from emprestimo
	where ID_DOC = idDoc
	order by 2,3,1;

-- obter todos dados de um det requesito
	--entrada -> idReq
select e.Lei_id_pessoa, p.nome_pessoa, e.id_pessoa, pp.nome_pessoa, e.id_doc, doc.nome_doc, e.DATA_DE_REQUISITO, e.DATA_PREVISTA, e.DATA_ENTREGA, doc.ID_PRATELEIRA
	from emprestimo e, pessoa p, pessoa pp, publicacao doc
	where e.id_emprestimo = idReq
			AND e.lei_id_pessoa = p.id_pessoa
			AND e.id_pessoa = pp.id_pessoa
			AND e.id_doc = doc.id_doc
order by 1,7,8;	
	
--fazer entrega do requisito JÁ HÁ UMA FUNÇÃO MAIS ACTUALIZADA!!!!!!!! Chamada returnRequisition ou assim uma coisa
create or replace procedure deliverRequest ( idReq in emprestimo.id_emprestimo%type, retVal out number) is

idDoc publicacao.id_doc%type;

begin
	
	select id_doc into idDoc from emprestimo where ID_EMPRESTIMO = idReq;
	
	update emprestimo set data_entrega = sysdate where ID_EMPRESTIMO = idReq;
	update publicacao set DISPONIVEIS = DISPONIVEIS + 1 where id_doc = idDoc;
	
	commit;
	
	Exception
		when no_data_found then
			retVal:=-1;
			return;
		when dup_val_on_index then 
			retVal:=-2;
			return;
		when others then
			rollback;
			retVal:=-3;
			return;
end;

/



	
