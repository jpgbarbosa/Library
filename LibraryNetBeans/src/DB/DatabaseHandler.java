/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import DataStructures.Book;
import DataStructures.Editora;
import DataStructures.Person;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import librarynetbeans.Validation;

/**
 * This class handles the database connection and queries that are performed
 * @author Luís Cardoso & Pedro Catré
 */
public class DatabaseHandler implements DB.DataAccessInterface{

    //YOU MIGHT NEED TO CHANGE THIS INFORMATION!!!
    final String username = "bd01";//username of the DB (YOU MIGHT NEED TO CHANGE THIS IF YOU USERNAME IS DIFFERENT!)
    final String password = "bd01";//password of the DB (YOU MIGHT NEED TO CHANGE THIS IF YOU PASSWORD IS DIFFERENT!)
    final String url = "jdbc:oracle:thin:@localhost:1521:orcl"; //you might need to change this URL... it ends with orcl (instead of xe) if you are using Oracle. If you are using Oracle Express use xe in the end instead of orcl
    private Connection conn; //connection to the database

    /**
     * Creates the connection to the database
     */
    public DatabaseHandler() {	

        try {
            //Connect to the database
            //The following snippet registers the driver and gets a connection to the database, in the case of a normal java application.
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //instantiate oracle.jdbc.driver.OracleDriver
            //in other words... load the driver
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e1) {
            System.out.println("SQLException, leaving the program...");
            System.exit(-1);
        } catch (InstantiationException e) {
            System.out.println("InstantiationException, leaving the program...");
            System.exit(-1);
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException, leaving the program...");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException, leaving the program...");
            System.exit(-1);
        }
    }

    /**
     * Closes the connection with the database
     */
    @Override
    public void close(){
        try {
            conn.close();//close the connection to the database
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int login(int userName, String password){
        System.out.print("\n[Performing login]...");
        //Execute statement
        CallableStatement proc = null;
        int retVal = 0;

        try {
            proc = conn.prepareCall("{ call login(?, ?, ?) }");

            proc.setInt(1, userName);
            proc.setString(2, password);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.execute();

            retVal = (Integer)proc.getObject(3);

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        return retVal;
    }

    /**
     * Performs a query in order to get all albums currently in the database
     * @return
     */
    @Override
     public ArrayList<Editora> getPublishers() {
        ArrayList<Editora> publishersList = new ArrayList<Editora>();
        Editora singlePublisher;

        System.out.print("\n[Performing getEditoras] ... ");

        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("SELECT * FROM Editora");//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singlePublisher= new Editora(rset.getInt("ID_EDITORA"),rset.getString("NOME_EDITORA"));
                publishersList.add(singlePublisher);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return publishersList;

    }

     @Override
    public void getIdReaderByName(String name, JTextArea textArea){
        Person reader;
        ArrayList <Person> readers = new ArrayList<Person>();

        System.out.print("\n[Performing getIdReaderByName]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery(
                    "SELECT * FROM Pessoa p, Leitor l "
                     + "WHERE p.Id_pessoa = l.Id_pessoa AND upper(p.nome_pessoa) like '%'||upper('" + name + "')||'%'");
            while (rset.next()) {//while there are still results left to read
                reader= new Person(rset.getString("NOME_PESSOA"),rset.getInt("ID_PESSOA"));
                readers.add(reader);

            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        textArea.setText("");
        if (readers.size() > 0){
            for (Person p : readers)
                textArea.append("ID: " + p.getId()  + "\nNAME: " + p.getNome() + "\n\n");
        }
        else{
            textArea.setText("There are no readers with such name!");
        }

    }
     
      public  ArrayList<String> getEmployeeById(String id){

        ArrayList<String> dados = new ArrayList<String>(6);

        System.out.print("\n[Performing getIdReaderByName]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery(
                    "SELECT * FROM Pessoa p, Funcionario l "
                     + "WHERE p.Id_pessoa = l.Id_pessoa AND p.Id_pessoa = " + id);
            while (rset.next()) {//while there are still results left to read

                dados.add(""+rset.getString("NOME_PESSOA"));
                dados.add(""+rset.getString("MORADA"));
                dados.add(Validation.formatDate(""+rset.getDate("DATA")));
                dados.add(""+rset.getInt("BI"));
                dados.add(""+rset.getInt("TELEFONE"));
                dados.add(""+rset.getString("E_MAIL"));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

      public boolean changePersonData(String name, String morada, String bi, String telefone, String eMail, int [] date, boolean isEmployee){
        System.out.print("\n[Performing addPerson]...");
        //Execute statement
        CallableStatement proc = null;
        Integer returnValue=0;

        try {
            if(isEmployee){
                proc = conn.prepareCall("{ call updateEmployee(?, ?, ?, ?, ?, ?, ?) }");

                proc.setString(1, name);
                proc.setString(2, morada);
                proc.setInt(3, Integer.parseInt(bi));
                proc.setDate(4, new Date((new GregorianCalendar(date[2], date[1]-1, date[0])).getTimeInMillis()));
                proc.setInt(5, Integer.parseInt(telefone));
                proc.setString(6, eMail);
                proc.registerOutParameter(7, java.sql.Types.INTEGER);
                proc.execute();

                returnValue = (Integer) proc.getObject(7);

                proc.close();
            } else {
                proc = conn.prepareCall("{ call updateReader(?, ?, ?, ?, ?, ?, ?) }");

                proc.setString(1, name);
                proc.setString(2, morada);
                proc.setInt(3, Integer.parseInt(bi));
                proc.setDate(4, new Date((new GregorianCalendar(date[2], date[1]-1, date[0])).getTimeInMillis()));
                proc.setInt(5, Integer.parseInt(telefone));
                proc.setString(6, eMail);
                proc.registerOutParameter(7, java.sql.Types.INTEGER);
                proc.execute();

                returnValue = (Integer) proc.getObject(7);

                proc.close();
            }
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        if (returnValue <=0){
           return false;
        }
        return true;
      }


     @Override
     public int addDocument(String Autor, String Editora, String genero, String descri,
        String nome, int total, int numberOfPages){
        System.out.print("\n[Performing addDocument]...");
        //Execute statement
        CallableStatement proc = null;
        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call addDocument(?, ?, ?, ?, ?, ?, ?, ?, ?) }");

            proc.setString(1, Autor);
            proc.setString(2,Editora);
            proc.setString(3, genero);
            proc.setInt(4, numberOfPages);
            proc.setString(5, descri);
            proc.setDate(6, new Date((new GregorianCalendar()).getTimeInMillis()));
            proc.setString(7, nome);
            proc.setInt(8, total);
            proc.registerOutParameter(9, java.sql.Types.INTEGER);
            proc.execute();
            int outcome = proc.getInt(9);
            proc.close();

            return outcome;
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
     }

      @Override
      public boolean addCopyDocument(int id, int novos){
        System.out.print("\n[Performing addCopyDocument"+id+"]...");
        //Execute statement
        CallableStatement proc = null;
        Integer retVal = -1;
        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call addCopyDocument(?, ?, ?) }");

            proc.setInt(1, id);
            proc.setInt(2, novos);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.execute();

            retVal = (Integer) proc.getObject(3);

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println(retVal);
        if (retVal == 0)
            return true;
        
        return false;
      }

      public ArrayList<Person> findPersonByBirthDate(int [] date, boolean isEmployee, String orderBy){

        ArrayList<Person> personsList = new ArrayList<Person>();
        Person singlePerson;

        System.out.print("\n[Performing findPersonByBirthDate] ... ");

        String [] strDate = Validation.formatDateToSQL(date);

        String d = strDate[0]+"/"+strDate[1]+"/"+strDate[2];

        System.out.println(d);

        try {
            String appendix;
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            if (isEmployee){
                appendix = "SELECT f.id_pessoa FROM Funcionario f";
            }
            else{
                appendix = "SELECT l.id_pessoa FROM Leitor l";
            }
            ResultSet rset = stmt.executeQuery("SELECT p.nome_pessoa, p.id_pessoa " +
                            "FROM Pessoa p WHERE to_char(p.data,'dd/mm/yyyy') like '"+d+"' AND p.id_pessoa IN ("
                                + appendix + ")"
                                + "ORDER BY " + orderBy);//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singlePerson= new Person(rset.getString("NOME_PESSOA"), rset.getInt("ID_PESSOA"));
                personsList.add(singlePerson);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return personsList;
    }

    @Override
    public int addPerson(String name, String morada, String bi, String telefone, String eMail, int [] date, boolean isEmployee, String password){
        System.out.print("\n[Performing addPerson]");
        //Execute statement
        CallableStatement proc = null;
        try {
            /* If it's not an employee, than it is a reader. */
            if (isEmployee){
                proc = conn.prepareCall("{ call addEmployee(?, ?, ?, ?, ?, ?, ?, ?) }");
            }
            else{
                proc = conn.prepareCall("{ call addReader(?, ?, ?, ?, ?, ?, ?) }");
            }
            
            proc.setString(1, name);
            proc.setString(2, morada);
            proc.setInt(3, Integer.parseInt(bi));
            proc.setDate(4, new Date((new GregorianCalendar(date[2], date[1]-1, date[0])).getTimeInMillis()));
            proc.setInt(5, Integer.parseInt(telefone));
            proc.setString(6, eMail);

            Integer returnValue;
            if (isEmployee){
                proc.setString(7, password);
                proc.registerOutParameter(8, java.sql.Types.INTEGER);
            }
            else{
                proc.registerOutParameter(7, java.sql.Types.INTEGER);
            }
            
            proc.execute();

            if (isEmployee){
               returnValue = (Integer) proc.getObject(8);
            }
            else{
                returnValue = (Integer) proc.getObject(7);
            }
            

            proc.close();
            return returnValue;
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
    }



    @Override
    public ArrayList<Person> getPersonsList(boolean isEmployee, String orderBy, boolean listAll){

        ArrayList<Person> personsList = new ArrayList<Person>();
        Person singlePerson;

        System.out.print("\n[Performing getPersonsList] ... ");

        try {
            String appendix;
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            if (isEmployee){
                appendix = "SELECT f.id_pessoa FROM Funcionario f" + (listAll ? "" : " WHERE f.DATA_SAIDA IS NULL ");
            }
            else{
                appendix = "SELECT l.id_pessoa FROM Leitor l";
            }
            ResultSet rset = stmt.executeQuery("SELECT p.nome_pessoa, p.id_pessoa " +
                                "FROM Pessoa p WHERE p.id_pessoa IN ("
                                + appendix + ")"
                                + "ORDER BY " + orderBy);//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singlePerson= new Person(rset.getString("NOME_PESSOA"), rset.getInt("ID_PESSOA"));
                personsList.add(singlePerson);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return personsList;
    }

    @Override
    public ArrayList<Person> getFaultyReadersList(String orderBy){

        ArrayList<Person> personsList = new ArrayList<Person>();
        Person singlePerson;

        System.out.print("\n[Performing getFaultyReadersList] ... ");

        try {
            String appendix;
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("SELECT p.nome_pessoa, p.id_pessoa " +
                                "FROM Pessoa p WHERE p.id_pessoa IN ("
                                + "SELECT e.lei_id_pessoa FROM Emprestimo e "
                                + "WHERE Data_prevista - SYSDATE < 0 "
                                + "GROUP BY e.lei_id_pessoa)"
                                + "ORDER BY " + orderBy);//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singlePerson= new Person(rset.getString("NOME_PESSOA"), rset.getInt("ID_PESSOA"));
                personsList.add(singlePerson);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return personsList;
    }

    @Override
    public ArrayList<Book> getSpecificBooks(String type, String value){
        ArrayList<Book> booksList = new ArrayList<Book>();
        Book singleBook;
        String query = "";
        
        System.out.print("\n[Performing getSpecificBooks] ... ");
        

        if (type.equals("Genre")){
            query = "SELECT p.nome_doc, p.id_doc FROM publicacao p, prateleira pr "
                    + "WHERE p.id_prateleira = pr.id_prateleira AND pr.genero = '" + value + "' order by 2";
        }
        else if(type.equals("Title")){
            query = "SELECT nome_doc, id_doc FROM publicacao WHERE upper(nome_doc) like '%'||upper('" + value + "')||'%' order by 2";
        }
        else if(type.equals("Publisher")){
            query = "SELECT nome_doc, id_doc FROM publicacao p, editora e "
                    + "WHERE e.id_editora = p.id_editora AND upper(e.nome_editora) like '%'||upper('" + value + "')||'%' order by 2";
        }
        else if(type.equals("Author")){
            query = "SELECT nome_doc, id_doc FROM publicacao p, autor a "
                    + "WHERE a.id_autor = p.id_autor AND upper(a.nome_autor) like '%'||upper('" + value + "')||'%' order by 2";
        }
        else if(type.equals("Pages")){
            query = "SELECT nome_doc, id_doc FROM publicacao p "
                    + "WHERE p.paginas " + value+" order by p.paginas";
        } else if(type.equals("Date")){     
             query = "SELECT nome_doc, id_doc FROM publicacao p "
                    + "WHERE to_char(p.data,'dd/mm/yyyy') like '"+value+"' order by 2";
        }

        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery(query);//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singleBook= new Book(rset.getString("NOME_DOC"), rset.getInt("ID_DOC"));
                booksList.add(singleBook);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return booksList;
    }

    @Override
    public int[] newRequisiton(int[] book_ids, int id_reader, int id_employee){
        System.out.print("\n[Performing newRequisiton]...");
        //Execute statement
        CallableStatement proc = null;
        int [] outcome = new int[3];
        int i;

        for (i = 0; i < book_ids.length; i++){

            /* This is not a valid book. */
            if (book_ids[i] == -1){
                continue;
            }

            try {
            proc = conn.prepareCall("{ call newRequisition (?, ?, ?, ?) }");

            proc.setInt(1, book_ids[i]);
            proc.setInt(2, id_reader);
            proc.setInt(3, id_employee);
            proc.registerOutParameter(4, java.sql.Types.INTEGER);
            proc.execute();

            outcome[i] = proc.getInt(4);

            proc.close();


            }catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                 outcome[i] = -3;
            } 
        }
        return outcome;

    }

    @Override
    public int returnRequisiton(int requisition_id){
        System.out.print("\n[Performing returnRequisiton]...");
        //Execute statement
        CallableStatement proc = null;
        int i, outcome = -3;

        try {
        proc = conn.prepareCall("{ call returnRequisition (?, ?) }");

        proc.setInt(1, requisition_id);
        proc.registerOutParameter(2, java.sql.Types.INTEGER);
        proc.execute();

        outcome = proc.getInt(2);

        proc.close();


        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
             return -3;
        }
        return outcome;

    }
    
    @Override
    public ArrayList<Person> findReaderByName(String name, String orderBy){

        ArrayList<Person> personsList = new ArrayList<Person>();
        Person singlePerson;

        System.out.print("\n[Performing findReaderByName]... ");

        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("SELECT p.nome_pessoa, p.id_pessoa " +
                                "FROM Pessoa p WHERE p.id_pessoa IN ("
                                + "SELECT l.id_pessoa FROM Leitor l) AND "
                                + "upper(p.nome_pessoa) like '%'||upper('" + name + "')||'%'"
                                + "ORDER BY " + orderBy);//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singlePerson= new Person(rset.getString("NOME_PESSOA"), rset.getInt("ID_PESSOA"));
                personsList.add(singlePerson);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return personsList;
    }

    @Override
    public ArrayList<Person> findEmployeeByName(String name, String orderBy){

        ArrayList<Person> personsList = new ArrayList<Person>();
        Person singlePerson;

        System.out.print("\n[Performing findEmployeeByName] ... ");

        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("SELECT p.nome_pessoa, p.id_pessoa " +
                    "FROM Pessoa p WHERE p.id_pessoa IN ("
                    + "SELECT f.id_pessoa FROM Funcionario f) AND "
                    + "upper(p.nome_pessoa) like '%'||upper('" + name + "')||'%' "
                    + "ORDER BY " + orderBy);//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singlePerson= new Person(rset.getString("NOME_PESSOA"), rset.getInt("ID_PESSOA"));
                personsList.add(singlePerson);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return personsList;
    }

    @Override
    public ArrayList<Book> findBookByName(String name){

        ArrayList<Book> booksList = new ArrayList<Book>();
        Book singleBook;

        System.out.print("\n[Performing findBookByName] ... ");

        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("SELECT p.nome_doc, p.id_doc " +
                    "FROM Publicacao p WHERE p.nome_doc = '" + name + "' "
                    + "ORDER BY p.nome_doc");//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singleBook = new Book(rset.getString("NOME_DOC"), rset.getInt("ID_DOC"));
                booksList.add(singleBook);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return booksList;
    }

    @Override
    public ArrayList<Book> findBookById(int id){

        ArrayList<Book> booksList = new ArrayList<Book>();
        Book singleBook;

        System.out.print("\n[Performing findBookById] ... ");

        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("SELECT p.nome_doc, p.id_doc, p.descricao, "
                    + "pra.genero, a.nome_autor, e.nome_editora, p.total, p.disponiveis, "
                    + "p.paginas, p.data, p.id_prateleira "
                    + "FROM Publicacao p, Prateleira pra, Autor a, Editora e "
                    + "WHERE p.id_doc = " + id + " "
                    + "AND p.id_autor = a.id_autor "
                    + "AND p.id_prateleira = pra.id_prateleira "
                    + "AND p.id_editora = e.id_editora");//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singleBook = new Book(rset.getString("NOME_DOC"), rset.getInt("ID_DOC"),
                        rset.getString("GENERO"), rset.getString("DESCRICAO"),
                        rset.getString("NOME_AUTOR"), rset.getString("NOME_EDITORA"),
                        rset.getInt("TOTAL"), rset.getInt("DISPONIVEIS"), rset.getString("PAGINAS"),
                        rset.getString("DATA"), rset.getInt("ID_PRATELEIRA"));
                booksList.add(singleBook);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return booksList;
    }



    // STATISTICS //
    @Override
    public void readersStatistics(JTextArea textArea){
        System.out.print("\n[Performing readersStatistics]...");
        //Execute statement
        CallableStatement proc = null;

        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call readersStats(?, ?, ?) }");

            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.registerOutParameter(2, java.sql.Types.INTEGER);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.execute();

            Integer noEntries = (Integer) proc.getObject(1);
            Integer noReadersWithBooks = (Integer) proc.getObject(2);
            Integer noFaultyReaders = (Integer) proc.getObject(3);

            textArea.setText("Number of Readers registered: " + noEntries + "\n"
                    + "Number of Readers with Books Borrowed: " + noReadersWithBooks + "\n"
                    + "Number of Faulty Readers: " + noFaultyReaders + "\n");

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
     }

    @Override
    public void employeesStatistics(JTextArea textArea){
        System.out.print("\n[Performing employeesStatistics]...");
        //Execute statement
        CallableStatement proc = null;

        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call employeesStats(?, ?, ?) }");

            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.registerOutParameter(2, java.sql.Types.INTEGER);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.execute();

            Integer noEntries = (Integer) proc.getObject(1);
            Integer noFired = (Integer) proc.getObject(2);
            Integer avg = (Integer) proc.getObject(3);
            
            textArea.setText("Number of Employees registered: " + noEntries + "\n"
                    + "Number of Employees fired: " + noFired + "\n"
                    + "Average Working Days: " + avg + "\n");

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
     }

    @Override
    public void booksAndShelvesStatistics(JTextArea textArea){
        System.out.print("\n[Performing booksAndShelvesStatistics]...");
        //Execute statement
        CallableStatement proc = null;

        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call booksAndShelvesStats(?, ?, ?, ?, ?, ?, ?, ?) }");

            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.registerOutParameter(2, java.sql.Types.INTEGER);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.registerOutParameter(4, java.sql.Types.FLOAT);
            proc.registerOutParameter(5, java.sql.Types.FLOAT);
            proc.registerOutParameter(6, java.sql.Types.INTEGER);
            proc.registerOutParameter(7, java.sql.Types.FLOAT);
            proc.registerOutParameter(8, java.sql.Types.FLOAT);
            proc.execute();

            Integer noBooks = (Integer) proc.getObject(1);
            Integer maxPages = (Integer) proc.getObject(2);
            Integer minPages = (Integer) proc.getObject(3);
            Double avgPages = (Double) proc.getObject(4);
            Double avgNoCopies = (Double) proc.getObject(5);
            Integer noShelves = (Integer) proc.getObject(6);
            Double occupation = (Double) proc.getObject(7);
            Double avgCapacity = (Double) proc.getObject(8);

            textArea.setText("Number of Books: " + noBooks + "\n"
                    + "Maximum Number of Pages: " + maxPages + "\n"
                    + "Minimum Number of Pages: " + minPages + "\n"
                    + "Average Number of Pages: " + avgPages + "\n"
                    + "Average Number of Copies: " + avgNoCopies + "\n"
                    + "Number of Shelves: " + noShelves + "\n"
                    + "Occupation Rate: " + occupation + "\n"
                    + "Average Shelf Capacity: " + avgCapacity + "\n");

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
     }

    @Override
    public void requisitionsStatistics(JTextArea textArea){
        System.out.print("\n[Performing requisitionsStatistics]...");
        //Execute statement
        CallableStatement proc = null;

        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call requisitionsStats(?, ?, ?, ?, ?, ?) }");

            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.registerOutParameter(2, java.sql.Types.INTEGER);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.registerOutParameter(4, java.sql.Types.INTEGER);
            proc.registerOutParameter(5, java.sql.Types.FLOAT);
            proc.registerOutParameter(6, java.sql.Types.INTEGER);

            proc.execute();

            Integer noEntries = (Integer) proc.getObject(1);
            Integer onGoing = (Integer) proc.getObject(2);
            Integer finished = (Integer) proc.getObject(3);
            Integer faulty = (Integer) proc.getObject(4);
            Double avgReqs = (Double) proc.getObject(5);
            Integer noDays = (Integer) proc.getObject(6);

            textArea.setText("Number of Requisitons: " + noEntries + "\n"
                    + "Number of Not Finished Requisitions: " + onGoing + "\n"
                    + "Number of Finished Requisitions: " + finished + "\n"
                    + "Number of Faulty Requisitions: " + faulty + "\n"
                    + "Number of Requisitons Per Day (Days with at least one requistion): " + avgReqs + "\n"
                    + "Number of Days with Requisitions: " + noDays);

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
     }

    @Override
    public void authorsAndPublishersStatistics(JTextArea textArea){
        System.out.print("\n[Performing authorsAndPublishersStatistics]...");
        //Execute statement
        CallableStatement proc = null;

        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call authorsAndPublishersStats(?, ?, ?, ?) }");

            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.registerOutParameter(2, java.sql.Types.FLOAT);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.registerOutParameter(4, java.sql.Types.FLOAT);
            proc.execute();

            Integer noAuthors = (Integer) proc.getObject(1);
            Double avgPerAuthor = (Double) proc.getObject(2);
            Integer noPublishers = (Integer) proc.getObject(3);
            Double avgPerPublisher = (Double) proc.getObject(4);

            textArea.setText("Number of Authors: " + noAuthors + "\n"
                    + "Average Copies Per Author: " + avgPerAuthor + "\n"
                    + "Number of Publishers: " + noPublishers + "\n"
                    + "Average Copies Per Publisher: " + avgPerPublisher + "\n");

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
     }

    public int fireEmployee(String id) {
        System.out.print("\n[Performing fireEmployee]... " + id);
        //Execute statement
        CallableStatement proc = null;
        int retVal = 0;

        try {
            proc = conn.prepareCall("{ call fireEmployee(?, ?) }");

            proc.setInt(1, Integer.parseInt(id));
            proc.registerOutParameter(2, java.sql.Types.INTEGER);
            proc.execute();

            retVal = (Integer)proc.getObject(2);
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return -4;
        }

        return retVal;
    }

    public ArrayList<String> getReaderById(String id) {
         ArrayList<String> dados = new ArrayList<String>(6);

        System.out.print("\n[Performing getIdReaderByID]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery(
                    "SELECT * FROM Pessoa p, Leitor l "
                     + "WHERE p.Id_pessoa = l.Id_pessoa AND p.Id_pessoa = " + id);
            while (rset.next()) {//while there are still results left to read

                dados.add(""+rset.getString("NOME_PESSOA"));
                dados.add(""+rset.getString("MORADA"));
                dados.add(Validation.formatDate(""+rset.getDate("DATA")));
                dados.add(""+rset.getInt("BI"));
                dados.add(""+rset.getInt("TELEFONE"));
                dados.add(""+rset.getString("E_MAIL"));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

    public int getBIEmployeeById(int bi) {
         int outcome = -1;

        System.out.print("\n[Performing getBIEmployeeById]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery(
                    "SELECT p.Id_pessoa FROM Pessoa p "
                     + "WHERE p.bi = " + bi);

            while (rset.next()){
                outcome =  rset.getInt("ID_PESSOA");
            }

            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return  outcome;
    }

    public ArrayList<String> getRequisitionByReaderId(String value) {
        ArrayList<String> dados = new ArrayList<String>();
        System.out.print("\n[Performing getRequisitionByReaderId: "+value+"]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("select ID_EMPRESTIMO, ID_DOC, LEI_ID_PESSOA "
                    + "from emprestimo "
                    + "where LEI_ID_PESSOA ="+value+" AND Data_entrega IS NULL "
                    + "order by 1,2");
                 
            while (rset.next()) {//while there are still results left to read
                dados.add("ReqID: "+rset.getString("ID_EMPRESTIMO")+
                        " DocID: "+rset.getString("ID_DOC")+
                        " ReaderID: "+rset.getString("LEI_ID_PESSOA"));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

    public ArrayList<String> getRequisitionById(String value) {
        ArrayList<String> dados = new ArrayList<String>();
        int val=Integer.parseInt(value);
        System.out.print("\n[Performing getRequisitionById]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("select ID_EMPRESTIMO, ID_DOC, lei_id_pessoa "
                    + "from emprestimo "
                    + "where ID_EMPRESTIMO ="+val+" order by 1,3,2");

            while (rset.next()) {//while there are still results left to read
                dados.add("ReqID: "+rset.getString("ID_EMPRESTIMO")+
                        " DocID: "+rset.getString("ID_DOC")+
                        " ReaderID: "+rset.getString("LEI_ID_PESSOA"));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

    public ArrayList<String> getRequisitionByDocTitle(String value) {
        ArrayList<String> dados = new ArrayList<String>();
        System.out.print("\n[Performing getRequisitionByDocTitle]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("select e.ID_EMPRESTIMO, e.ID_DOC, e.lei_id_pessoa "
                    + "from emprestimo e, publicacao p "
                    + "where upper(p.NOME_DOC) like '%'||upper('"+value+"')||'%' AND e.ID_DOC = p.ID_DOC	"
                    + "order by 2,3,1");

            while (rset.next()) {//while there are still results left to read
                    dados.add("ReqID: "+rset.getString("ID_EMPRESTIMO")+
                        " DocID: "+rset.getString("ID_DOC")+
                        " ReaderID: "+rset.getString("LEI_ID_PESSOA"));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

    public ArrayList<String> getRequisitionByDocId(String value) {
        ArrayList<String> dados = new ArrayList<String>();
        int val=Integer.parseInt(value);
        System.out.print("\n[Performing getRequisitionByDocId]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("select ID_EMPRESTIMO, ID_DOC, lei_id_pessoa "
                    + "from emprestimo "
                    + "where ID_DOC = "+val+" order by 2,3,1");

            while (rset.next()) {//while there are still results left to read
                dados.add("ReqID: "+rset.getString("ID_EMPRESTIMO")+
                        " DocID: "+rset.getString("ID_DOC")+
                        " ReaderID: "+rset.getString("LEI_ID_PESSOA"));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

    public ArrayList<String> getRequisitionByDate(String value) {
        ArrayList<String> dados = new ArrayList<String>();
        System.out.print("\n[Performing getRequisitionByDate]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("select e.ID_EMPRESTIMO, e.ID_DOC, e.lei_id_pessoa "
                    + "from emprestimo e "
                    + "where to_char(e.DATA_DE_REQUISITO,'dd/mm/yyyy') like '"+value+"' "
                    + "order by 1,3,2");

            while (rset.next()) {//while there are still results left to read
                dados.add("ReqID: "+rset.getString("ID_EMPRESTIMO")+
                        " DocID: "+rset.getString("ID_DOC")+
                        " ReaderID: "+rset.getString("LEI_ID_PESSOA"));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

    public ArrayList<String> getRequisitionInfo(String value) {
        ArrayList<String> dados = new ArrayList<String>();
        int val=Integer.parseInt(value);
        System.out.print("\n[Performing getRequisitionInfo]...");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("select e.Lei_id_pessoa, "
                    + "p.nome_pessoa, e.id_pessoa, pp.nome_pessoa, e.id_doc, "
                    + "doc.nome_doc, e.DATA_DE_REQUISITO, e.DATA_PREVISTA, "
                    + "e.DATA_ENTREGA, doc.ID_PRATELEIRA "
                        + "from emprestimo e, pessoa p, pessoa pp, publicacao doc "
                        + "where e.id_emprestimo = "+val+" "
                        + "AND e.lei_id_pessoa = p.id_pessoa "
                        + "AND e.id_pessoa = pp.id_pessoa "
                        + "AND e.id_doc = doc.id_doc "
                    + "order by 1,7,8");
            int rowCount = rset.getFetchSize();
            while (rset.next()) {//while there are still results left to read
                for(int i=1;i<=rowCount; i++){
                    dados.add(rset.getString(i));
                }
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return  dados;
    }

    public int removeCopyDocument(int id, int amount) {
        System.out.print("\n[Performing removeCopyDocument"+id+"]...");
        //Execute statement
        CallableStatement proc = null;
        Integer retVal = -1;
        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call removeCopyDocument(?, ?, ?) }");

            proc.setInt(1, id);
            proc.setInt(2, amount);
            proc.registerOutParameter(3, java.sql.Types.INTEGER);
            proc.execute();

            retVal = (Integer) proc.getObject(3);

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        System.out.println(retVal);
        return retVal;
    }

}
