--Attention! You have to enter as the Data Base Admin to perform these actions!

CREATE PROFILE perfil_funcionario LIMIT
Sessions_per_user UNLIMITED
Logical_reads_per_session UNLIMITED
Logical_reads_per_call 100
Idle_time 30;

CREATE USER employee
IDENTIFIED BY employee
DEFAULT TABLESPACE users
QUOTA 500M ON users
PROFILE perfil_funcionario;

GRANT CONNECT TO employee;
GRANT ALL ON Emprestimo TO employee;

GRANT perfil_funcionario TO employee;

REVOKE INSERT, UPDATE, DELETE
ON FUNCIONARIO
FROM perfil_funcionario;

-- ONLY FROM HERE THE ABOVE IS JUST TESTING


--Attention! You have to enter as the Data Base Admin to perform these actions!
CREATE USER employee
IDENTIFIED BY employee;

GRANT DBA TO employee;