package guiEmployee;

import java.awt.event.MouseEvent;
import common.WindowGUI;

/* In this menu, the employee can look for information about other employees
 * in the system.
 */

public class SearchEmployeesGUI extends WindowGUI{
	private static final long serialVersionUID = 1L;
	
	/* The constructor. */
	public SearchEmployeesGUI(){
		
	}
	
	/* The option buttons that can be selected by the user. */
	public void mouseReleased(MouseEvent e){
		if(e.getComponent().getName().equals("Iniciar")){

		}
		else if(e.getComponent().getName().equals("Opções")){

		}
		else if (e.getComponent().getName().equals("Sair")){

		}
	}
}
