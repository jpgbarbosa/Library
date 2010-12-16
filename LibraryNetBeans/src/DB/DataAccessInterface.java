/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import DataStructures.Editora;
import java.util.ArrayList;

/**
 * Interface for accessing data from the DB. The DatabaseHandler extends this interface.
 * @author Luís Cardoso & Pedro Catré
 */
public interface DataAccessInterface {
    
    public void close();
    public ArrayList<Editora> getPublishers();
    public int getIdReaderByName(String name);
    public void addReader(String name, String morada, String bi, String telefone, String eMail);
    
}
