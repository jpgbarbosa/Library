package common;

import java.awt.event.MouseEvent;

/* In this menu, both employees and clients can look for documents registered
 * in the library. 
 */
public class SearchDocsGUI extends WindowGUI{
	private static final long serialVersionUID = 1L;
	
	/* The constructor. */
	public SearchDocsGUI(){
		
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
