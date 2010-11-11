package guiEmployee;

import java.awt.event.MouseEvent;
import common.WindowGUI;

//ADMIN ONLY!!!!!!!
/* In this menu, the administrator can manage the employees. */

public class ManageEmployeesGUI extends WindowGUI{
	private static final long serialVersionUID = 1L;
	
	/* The constructor. */
	public ManageEmployeesGUI(){
		
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
