-- ATTENTION! YOU HAVE TO LOGIN AS THE DATA BASE ADMINISTRATOR TO PERFORM THE FOLLOWING ACTIONS!

--Creates the profile.
CREATE PROFILE perfil_funcionario LIMIT
Sessions_per_user UNLIMITED
Logical_reads_per_session UNLIMITED
Logical_reads_per_call 100
Idle_time 30;

-- Now, creates the user.
CREATE USER employee
IDENTIFIED BY employee
DEFAULT TABLESPACE users
QUOTA 500M ON users
PROFILE perfil_funcionario;

--Grants permission to connect to this user.
GRANT CONNECT TO employee;
-- The username might be different if you have no user BD01.
GRANT CREATE PUBLIC SYNONYM TO BD01;

-- NOW, CHANGE YOUR USER TO BD01 (OR THE PROFILE YOU ARE USING AS THE ADMIN OF THE APPLICATION).

--Grants the permissions to the user employee.
--The tables.
GRANT ALL ON AUTOR TO employee;
GRANT ALL ON EDITORA TO employee;
GRANT ALL ON EMPRESTIMO TO employee;
GRANT SELECT, UPDATE ON FUNCIONARIO TO employee;
GRANT ALL ON LEITOR TO employee;
GRANT ALL ON PESSOA TO employee;
GRANT ALL ON PRATELEIRA TO employee;
GRANT ALL ON PUBLICACAO TO employee;
GRANT SELECT ON AUTENTICACAO TO employee;

--And then the procedures.
GRANT EXECUTE ON addReader TO employee;
GRANT EXECUTE ON updateReader TO employee;
GRANT EXECUTE ON updateEmployee TO employee;
GRANT EXECUTE ON getPrateleira TO employee;
GRANT EXECUTE ON getEditora TO employee;
GRANT EXECUTE ON addDocument TO employee;
GRANT EXECUTE ON removeCopyDocument TO employee;
GRANT EXECUTE ON addCopyDocument TO employee;
GRANT EXECUTE ON newRequisition TO employee;
GRANT EXECUTE ON returnRequisition TO employee;
GRANT EXECUTE ON employeesStats TO employee;
GRANT EXECUTE ON readersStats TO employee;
GRANT EXECUTE ON booksAndShelvesStats TO employee;
GRANT EXECUTE ON requisitionsStats TO employee;
GRANT EXECUTE ON authorsAndPublishersStats TO employee;
GRANT EXECUTE ON login TO employee;

--Then, we create the alias so the user employee can use the names of the tables directly.
--For the tables.
CREATE PUBLIC SYNONYM AUTOR FOR BD01.AUTOR;
CREATE PUBLIC SYNONYM EDITORA FOR BD01.EDITORA;
CREATE PUBLIC SYNONYM EMPRESTIMO FOR BD01.EMPRESTIMO;
CREATE PUBLIC SYNONYM FUNCIONARIO FOR BD01.FUNCIONARIO;
CREATE PUBLIC SYNONYM LEITOR FOR BD01.LEITOR;
CREATE PUBLIC SYNONYM PESSOA FOR BD01.PESSOA;
CREATE PUBLIC SYNONYM PRATELEIRA FOR BD01.PRATELEIRA;
CREATE PUBLIC SYNONYM PUBLICACAO FOR BD01.PUBLICACAO;
CREATE PUBLIC SYNONYM AUTENTICACAO FOR BD01.AUTENTICACAO;

--And then the procedures.
CREATE PUBLIC SYNONYM addReader FOR BD01.addReader;
CREATE PUBLIC SYNONYM updateReader FOR BD01.updateReader;
CREATE PUBLIC SYNONYM updateEmployee FOR BD01.updateEmployee;
CREATE PUBLIC SYNONYM getPrateleira FOR BD01.getPrateleira;
CREATE PUBLIC SYNONYM getEditora FOR BD01.getEditora;
CREATE PUBLIC SYNONYM addDocument FOR BD01.addDocument;
CREATE PUBLIC SYNONYM removeCopyDocument FOR BD01.removeCopyDocument;
CREATE PUBLIC SYNONYM addCopyDocument FOR BD01.addCopyDocument;
CREATE PUBLIC SYNONYM newRequisition FOR BD01.newRequisition;
CREATE PUBLIC SYNONYM returnRequisition FOR BD01.returnRequisition;
CREATE PUBLIC SYNONYM employeesStats FOR BD01.employeesStats;
CREATE PUBLIC SYNONYM readersStats FOR BD01.readersStats;
CREATE PUBLIC SYNONYM booksAndShelvesStats FOR BD01.booksAndShelvesStats;
CREATE PUBLIC SYNONYM requisitionsStats FOR BD01.requisitionsStats;
CREATE PUBLIC SYNONYM authorsAndPublishersStats FOR BD01.authorsAndPublishersStats;
CREATE PUBLIC SYNONYM login FOR BD01.login;
