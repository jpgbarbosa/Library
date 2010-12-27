/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import DataStructures.Book;
import DataStructures.Editora;
import DataStructures.Person;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 * Interface for accessing data from the DB. The DatabaseHandler extends this interface.
 * @author Luís Cardoso & Pedro Catré
 */
public interface DataAccessInterface {
    
    public void close();
    public ArrayList<Editora> getPublishers();
    public int getIdReaderByName(String name);
    public int addPerson(String name, String morada, String bi, String telefone, String eMail,int [] date, boolean isEmployee);
    public ArrayList<Person> getPersonsList(boolean isEmployee, String orderBy);
    public ArrayList<Book> getSpecificBooks(String type, String value);
    public void newRequisiton(int[] book_ids, int id_reader, int id_employee);
    public ArrayList<Person> findEmployeeByName(String name, String orderBy);
    public ArrayList<Person> findReaderByName(String name, String orderBy);
    public ArrayList<Book> findBookByName(String name);
    public ArrayList<Book> findBookById(int id);
    public void addDocument(String Autor, String Editora, String genero, String descri,String nome, int total, int numberOfPages);
    public ArrayList<Person> findPersonByBirthDate(int [] date, boolean isEmployee, String orderBy);

    /* Statistics */
    public void readersStatistics(JTextArea textArea);
    public void employeesStatistics(JTextArea textArea);
    public void booksAndShelvesStatistics(JTextArea textArea);
    public void requisitionsStatistics(JTextArea textArea);
    public void authorsAndPublishersStatistics(JTextArea textArea);
}
