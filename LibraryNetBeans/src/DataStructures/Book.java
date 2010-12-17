/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

/**
 *
 * @author LG
 */
public class Book {
    String titulo;
    int id;

    public Book (String titulo, int id){
        this.titulo = titulo;
        this.id = id;
    }

    public String getTitle(){
        return titulo;
    }

    public int getId(){
        return id;
    }
}
