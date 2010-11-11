package guiEmployee;

import java.awt.event.MouseEvent;
import common.WindowGUI;

/* In this menu, the employee can manage (register, impose restrictions or
 * penalties,...) the readers.
 */

public class ManageReadersGUI extends WindowGUI{
	private static final long serialVersionUID = 1L;
	
	/* The constructor. */
	public ManageReadersGUI(){
		
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
