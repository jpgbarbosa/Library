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

    public void connect(String url, String username, String password);
    public void close();
    public int login (int username, String password);
    public ArrayList<Editora> getPublishers();
    public int getBIEmployeeById(int id);
    public String getNumberReqsAndIsFaulty(String id);
    public void getIdReaderByName(String name, JTextArea textArea);
    public int addPerson(String name, String morada, String bi, String telefone, String eMail,int [] date, boolean isEmployee, String password);
    public ArrayList<Person> getPersonsList(boolean isEmployee, String orderBy, boolean listAll);
    public ArrayList<Person> getFaultyReadersList(String orderBy);
    public ArrayList<Book> getSpecificBooks(String type, String value);
    public int[] newRequisiton(int[] book_ids, int id_reader, int id_employee);
    public int returnRequisiton(int requisition_ids);
    public ArrayList<Person> findEmployeeByName(String name, String orderBy);
    public ArrayList<Person> findReaderByName(String name, String orderBy);
    public ArrayList<Book> findBookByName(String name);
    public ArrayList<Book> findBookById(int id);
    public int addDocument(String Autor, String Editora, String genero, String descri,String nome, int total, int numberOfPages);
    public ArrayList<Person> findPersonByBirthDate(int [] date, boolean isEmployee, String orderBy);
    public boolean addCopyDocument(int id, int novos);
    /* Statistics */
    public void readersStatistics(JTextArea textArea);
    public void employeesStatistics(JTextArea textArea);
    public void booksAndShelvesStatistics(JTextArea textArea);
    public void requisitionsStatistics(JTextArea textArea);
    public void authorsAndPublishersStatistics(JTextArea textArea);
}
