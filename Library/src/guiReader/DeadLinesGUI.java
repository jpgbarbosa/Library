package guiReader;

import java.awt.event.MouseEvent;
import common.WindowGUI;

/* In this menu, the reader can see the dead lines for his/her books' deliveries. */

public class DeadLinesGUI extends WindowGUI{
	private static final long serialVersionUID = 1L;

	/* The constructor. */
	public DeadLinesGUI(){
		
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
