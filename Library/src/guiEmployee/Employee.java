package guiEmployee;


import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import common.SearchDocsGUI;
import common.WindowGUI;


public class Employee {
	/* The list of managers that the BackOffice will deal with. */
	
	/* Window dimensions for the graphical interface. */
	private int dimH = 1000;
	private int dimV = 600;
	/* The windows used on the graphical interface. */
	private Menu menu;
	private SearchDocsGUI searchMenu;
	
	/* The main constructor. */
	public Employee(){
		//TODO: Managers go here.
		
		menu = new Menu();
		searchMenu = new SearchDocsGUI();
		
	}
	
	public static void main(String[] args) {

		
		Employee backOffice = new Employee();
		
		backOffice.executeGraphics();

	}

	/* The method to authenticate the administrator. */
	public boolean loginAdmin(String usename, String password){
		
		return false;
	}
	
	
    public void executeGraphics(){
		
		JFrame f = new JFrame();
		f.setSize(dimH,dimV);
		f.setTitle("Móveis PIB");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.lightGray);
		panel.setVisible(true);
		
		panel.add(menu);
		panel.add(searchMenu);
		
		/*menu.CreateImage("./src/imagens/furniture.jpg","Visite as nossas exposições!",250,100,500,340);
		menu.CreateImage("./src/imagens/finalBackground.jpg","",0,0,990,570);
		
		start.CreateImage("./src/imagens/finalBackground.jpg","",0,0,990,570);
		setup.CreateImage("./src/imagens/finalBackground.jpg","",0,0,990,570);
		seeds.CreateImage("./src/imagens/finalBackground.jpg","",0,0,990,570);*/
		
		/* Sets all the windows invisible, except, naturally, the main menu. */
		menu.setVisible(true);
		searchMenu.setVisible(false);
		
		f.setContentPane(panel);
		f.setVisible(true);
		
	}
	
	@SuppressWarnings("serial")
	private class Menu extends WindowGUI{
		public Menu(){
			CreateTitle("Bem-vindo ao Simulador da Empresa Móveis PIB",Color.orange,30,180,50,800,30);
			CreateButton("Iniciar",Color.white, "Iniciar a simulação",15,60,500,100,30);
			CreateButton("Opções",Color.white, "Especificar as condições inicias", 15,180,500,100,30);
			CreateButton("Sair",Color.white,"Sair do simulador" ,15,820,500,100,30);
		}
		
		public void mouseReleased(MouseEvent e){
			if(e.getComponent().getName().equals("Iniciar")){
				/*menu.setVisible(false);
				beginSimulation();
				start.resetJanelaReplica();
				start.update();
				start.setVisible(true);*/
			}
			else if(e.getComponent().getName().equals("Opções")){
				/*menu.setVisible(false);
				setup.setVisible(true);*/
			}
			else if (e.getComponent().getName().equals("Sair")){
				/*JOptionPane jp= new JOptionPane("Os Móveis PIB desejam-lhe um bom-dia!",JOptionPane.INFORMATION_MESSAGE);
				JDialog jd = jp.createDialog("Adeus!");
				jd.setBounds(new Rectangle(340,200,320,120));
				jd.setVisible(true);
				System.exit( 0 );*/
			}
		}
	}
	
}
