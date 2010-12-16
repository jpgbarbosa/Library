/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import DataStructures.Editora;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
     public ArrayList<Editora> getPublishers() {
        System.out.print("\n[Performing getEditoras] ... ");
        ArrayList<Editora> publishersList = new ArrayList<Editora>();
        Editora singlePublisher;
        try {
            //Execute statement
            Statement stmt;
            stmt = conn.createStatement();//create statement
            // execute querie
            ResultSet rset = stmt.executeQuery("SELECT * FROM Editora");//Select all from Album
            while (rset.next()) {//while there are still results left to read
                //create Album instance with current album information
                //you can get each of the albums attributes by using the column name
                singlePublisher= new Editora(rset.getInt("ID_EDITORAE"),rset.getString("NOME_EDITORA"));
                publishersList.add(singlePublisher);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return publishersList;

    }
    

}
