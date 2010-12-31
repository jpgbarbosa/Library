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
    String titulo, genero, descricao, autor, editora, paginas, data;
    int id, total, disponiveis, prateleira;

    public Book (String titulo, int id){
        this.titulo = titulo;
        this.id = id;
    }

    public Book (String titulo, int id, String genero, String descricao,
            String autor, String editora, int total, int disponiveis, String paginas, String data, int prateleira){
        this.titulo = titulo;
        this.id = id;
        this.genero = genero;
        this.autor = autor;
        this.editora = editora;
        this.descricao = descricao;
        this.total = total;
        this.disponiveis = disponiveis;
        this.paginas = paginas;
        this.data = data;
        this.prateleira = prateleira;
    }

    public String getTitle(){
        return titulo;
    }

    public int getId(){
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEditora() {
        return editora;
    }

    public String getGenero() {
        return genero;
    }

    public int getDisponiveis() {
        return disponiveis;
    }

    public int getTotal() {
        return total;
    }

    public String getPaginas(){
        return paginas;
    }

    public String getData(){
        return data;
    }

    public int getPrateleira(){
        return prateleira;
    }

    
}
