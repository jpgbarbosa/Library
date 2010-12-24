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
    public int getIdReaderByName(String name){
        int id = -1;

        System.out.print("\n[Performing getIdReaderByName]");
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery(
                    "SELECT * FROM Pessoa p, Leitor l "
                     + "WHERE p.Id_pessoa = l.Id_pessoa AND p.nome_pessoa = '" + name + "'");
            while (rset.next()) {//while there are still results left to read
                id = rset.getInt("ID_PESSOA");
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        

        return id;

    }

     public void addDocument(String Autor, String Editora, String genero, String descri,
             String nome, int total){
         System.out.print("\n[Performing addDocument]");
        //Execute statement
        CallableStatement proc = null;
        try {
            /* If it's not an employee, than it is a reader. */
            proc = conn.prepareCall("{ call addDocument(?, ?, ?, ?, ?, ?, ?, ?) }");

            proc.setString(1, Autor);
            proc.setString(2,Editora);
            proc.setString(3, genero);
            proc.setString(4, descri);
            proc.setDate(5, new Date((new GregorianCalendar()).getTimeInMillis()));
            proc.setString(6, nome);
            proc.setInt(7, total);
            proc.registerOutParameter(8, java.sql.Types.INTEGER);
            proc.execute();

            // TODO tratar valor de retorno
            // TODO IDEIA: numero de livros nas prateleiras mantem-se inalterados. quando procuramos um livro
            // e este se encontra em mais q uma, dizemos q existem nas 2

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
     }

    @Override
    public void addPerson(String name, String morada, String bi, String telefone, String eMail, boolean isEmployee){
        System.out.print("\n[Performing addPerson]");
        //Execute statement
        CallableStatement proc = null;
        try {
            /* If it's not an employee, than it is a reader. */
            if (isEmployee){
                proc = conn.prepareCall("{ call addEmployee(?, ?, ?, ?, ?) }");
            }
            else{
                proc = conn.prepareCall("{ call addReader(?, ?, ?, ?, ?) }");
            }
            
            proc.setString(1, name);
            proc.setString(2, morada);
            proc.setInt(3, Integer.parseInt(bi));
            proc.setInt(4, Integer.parseInt(telefone));
            proc.setString(5, eMail);
            proc.execute();

            proc.close();

        }catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }


    }

    @Override
    public ArrayList<Person> getPersonsList(boolean isEmployee, String orderBy){

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
                appendix = "SELECT f.id_pessoa FROM Funcionario f";
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
    public ArrayList<Book> getSpecificBooks(String type, String value){
        ArrayList<Book> booksList = new ArrayList<Book>();
        Book singleBook;
        String query = "";
        
        System.out.print("\n[Performing getSpecificBooks] ... ");
        

        if (type.equals("Genre")){
            query = "SELECT p.nome_doc, p.id_doc FROM publicacao p, prateleira pr "
                    + "WHERE p.id_prateleira = pr.id_prateleira AND pr.genero = '" + value + "'";
        }
        else if(type.equals("Title")){
            query = "SELECT nome_doc, id_doc FROM publicacao WHERE nome_doc = '" + value + "'";
        }
        else if(type.equals("Publisher")){
            query = "SELECT nome_doc, id_doc FROM publicacao p, editora e "
                    + "WHERE e.id_editora = p.id_editora AND e.nome_editora = '" + value + "'";
        }
        else if(type.equals("Author")){
            query = "SELECT nome_doc, id_doc FROM publicacao p, autor a "
                    + "WHERE a.id_autor = p.id_autor AND a.nome_autor = '" + value + "'";
        }
        else if(type.equals("Pages")){
            //TODO: Make this
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
    public void newRequisiton(int[] book_ids, int id_reader, int id_employee){
        System.out.print("\n[Performing newRequisiton]");
        //Execute statement
        CallableStatement proc = null;
        int i;

        for (i = 0; i < book_ids.length; i++){

            /* This is not a valid book. */
            if (book_ids[i] == -1){
                continue;
            }
            try {
            proc = conn.prepareCall("{ call newRequisition (?, ?, ?) }");

            proc.setInt(1, book_ids[i]);
            proc.setInt(2, id_reader);
            proc.setInt(3, id_employee);
            proc.execute();

            proc.close();

            }catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }

        
        
        
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
                                + "p.nome_pessoa = '" + name + "'"
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
                    + "p.nome_pessoa = '" + name + "' "
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
                    + "pra.genero, a.nome_autor, e.nome_editora, p.total, p.disponiveis " +
                    "FROM Publicacao p, Prateleira pra, Autor a, Editora e "
                    + "WHERE p.id_doc = " + id + " "
                    + "AND p.id_autor = a.id_autor "
                    + "AND p.id_prateleira = pra.id_prateleira "
                    + "AND p.id_editora = e.id_editora");//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                System.out.println("Here");
                singleBook = new Book(rset.getString("NOME_DOC"), rset.getInt("ID_DOC"),
                        rset.getString("GENERO"), rset.getString("DESCRICAO"),
                        rset.getString("NOME_AUTOR"), rset.getString("NOME_EDITORA"),
                        rset.getInt("TOTAL"), rset.getInt("DISPONIVEIS"));
                booksList.add(singleBook);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return booksList;
    }
}
