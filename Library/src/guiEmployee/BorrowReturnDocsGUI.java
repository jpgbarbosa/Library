package guiEmployee;

import java.awt.event.MouseEvent;
import common.WindowGUI;

/* In this menu, the employee process the borrowing or returning process
 * of a document.
 */

public class BorrowReturnDocsGUI extends WindowGUI{
	private static final long serialVersionUID = 1L;

	/* The constructor. */
	public BorrowReturnDocsGUI(){
		
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
