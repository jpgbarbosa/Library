/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

/**
 *
 * @author LG
 */
public class Person {
    private String nome;
    private int id;

    public Person(String nome, int id){
        this.nome = nome;
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public int getId(){
        return id;
    }
}
