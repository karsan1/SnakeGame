import javax.swing.JFrame;

public class GameFrame extends JFrame {
	 
	GameFrame(){
		
		//GamePanel panel = new GamePanel();
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); //add components to a JFrame is going to take the JFrame and fit it snuggle around all frames
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
