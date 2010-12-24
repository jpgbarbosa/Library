/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package librarynetbeans;

import DB.DatabaseHandler;
import GUI.*;
import GUI.documents.*;
import GUI.employees.*;
import GUI.readers.*;
import GUI.statistics.Statistics;

/**
 *
 * @author LG
 */
public class Constants {
    public static Login loginMenu;
    public static MainMenu mainMenu;
    public static Document documentsMenu;
    public static Employees employeesMenu;
    public static Reader readersMenu;
    public static Statistics statisticsMenu;
    public static DatabaseHandler dbhandler;

    public static Frame frame;

    final public static int DIM_H = 1000;
    final public static int DIM_V = 600;
    

    public Constants(Login login, MainMenu main, Document doc, Employees emp,Reader readers, Statistics stats, Frame f){
        loginMenu = login;
        mainMenu = main;
        documentsMenu = doc;
        employeesMenu = emp;
        readersMenu = readers;
        statisticsMenu = stats;
        frame = f;
        dbhandler = new DatabaseHandler();

    }

}
