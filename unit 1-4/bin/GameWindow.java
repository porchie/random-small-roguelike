
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;


public class GameWindow extends JFrame implements ActionListener{

  private JFrame j;
  private JPanel panel = new JPanel();;
  private JPanel panel2 = new JPanel();
  private JPanel panel3 = new JPanel();
  private JLabel label;
  private JLabel label2;
  private JLabel label3;
  private Game game = new Game(15 , 4);


  
	public GameWindow(String display) {

    
    
    
   
   
   
    Font font = new Font( "Monospaced", Font.PLAIN, 12 );

   
    j = new JFrame(display);
    j.setSize(500, 330);
    j.setLocation(5, 5);
    j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    j.setLayout(new BorderLayout());
    label = new JLabel("");
    label2 = new JLabel("<html>You are @<br>WASD to move<br>H to use heal potion<br>Move into an enemy \"E\" to attack it<br>Collect a key \"K\" and enter door \"0\"<br>Also click this window before inputting</html>", SwingConstants.CENTER);
    label3 = new JLabel("");
    panel.add(label);
    panel2.add(label2);
    panel3.add(label3);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    panel2.setBorder(BorderFactory.createLineBorder(Color.black));
    panel3.setBorder(BorderFactory.createLineBorder(Color.black));
    label3.setFont(font);
    

    panel.setPreferredSize(new Dimension(250, 150));
    
  
    label3.setText(game.toString());
    j.add(panel , BorderLayout.EAST);
    j.add(panel2 , BorderLayout.NORTH);
    j.add(panel3, BorderLayout.WEST);



    
  
    
    j.addKeyListener(new KeyTracker());
    j.show();
   
    

    // works without doing anything sometimes, sometimes doesnt???? why
    while (!game.playerDead())
    {
      //label2.setText(label2.getText() + "");
    } 
    
    
    String floors = game.getFloorNums() + " floor";
    if (game.getFloorNums() < 1)
    {
      floors += "s";
    }

    String death = 
    "<html>"                                  + 
    "And so our brilliant @ was slain...<br>" +
    "You have conquered: " + floors + "<br>"  + 
    "</html>";
    ;
    
    JLabel lose = new JLabel(death, SwingConstants.CENTER);
    
    j.getContentPane().removeAll();
    j.repaint();
    j.add(lose);
    j.validate();
   

  
    
  

    
  }

  public void actionPerformed(ActionEvent e) {
		System.out.println("Action performed!");
	}

  
	
  public class KeyTracker extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_A) {

        game.moveLeft();
        game.updateWorld();
        label.setText(game.status());
        label3.setText(game.toString());
      
      }
      if (key == KeyEvent.VK_S) {
        game.moveDown();
        game.updateWorld();
        label.setText(game.status());
        label3.setText(game.toString());
       
      }
      if (key == KeyEvent.VK_D) {
        game.moveRight();
        game.updateWorld();
        label.setText(game.status());
        label3.setText(game.toString());
        
      }
      if (key == KeyEvent.VK_W) {
        game.moveUp();
        game.updateWorld();
        label.setText(game.status());
        label3.setText(game.toString());
        
      }
      if (key == KeyEvent.VK_H) {
        if(game.usePot() == 1)
        {
          game.updateWorld();
        }
        label.setText(game.status());
        label3.setText(game.toString());
        
      }
    }
  

    

  }
   public void clearCon()
    {
      System.out.print("\033[H\033[2J");
      System.out.flush();
    }
}