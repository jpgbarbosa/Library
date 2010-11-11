package guiEmployee;

import java.awt.event.MouseEvent;
import common.WindowGUI;

/* In this menu, the employee add a new document to the library. */

public class AddNewDocGUI extends WindowGUI{
	private static final long serialVersionUID = 1L;

	/* The constructor. */
	public AddNewDocGUI(){
		
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
