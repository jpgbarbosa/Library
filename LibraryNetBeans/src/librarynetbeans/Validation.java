/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package librarynetbeans;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Barbosa
 */
public class Validation {
        /**
     * This function check if the date textField is a valid date.
     *
     * @param dateString
     * @return an int array with day, month and year. null if invalid date
     */
    static public int [] checkInsertedDate(String dateString, boolean mustBeForward){
        try {
            String [] dateFields = dateString.split("/");
            int day = Integer.parseInt(dateFields[0]);
            int month = Integer.parseInt(dateFields[1]);
            int year = Integer.parseInt(dateFields[2]);

            int [] date = new int[3];

            date[0]=day;
            date[1]=month;
            date[2]=year;

            if(!mustBeForward){
                return date;
            } else if(new GregorianCalendar(year, month, day).after(Calendar.getInstance())){
                return date;
            } else {
                return null;
            }

        } catch (NumberFormatException e1) {
            return null;
        }
    }
}
