/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import DataStructures.Book;
import DataStructures.Editora;
import DataStructures.Person;
import java.util.ArrayList;

/**
 * Interface for accessing data from the DB. The DatabaseHandler extends this interface.
 * @author Luís Cardoso & Pedro Catré
 */
public interface DataAccessInterface {
    
    public void close();
    public ArrayList<Editora> getPublishers();
    public int getIdReaderByName(String name);
    public void addPerson(String name, String morada, String bi, String telefone, String eMail, boolean isEmployee);
    public ArrayList<Person> getPersonsList(boolean isEmployee);
    public ArrayList<Book> getSpecificBooks(String type, String value);
}
