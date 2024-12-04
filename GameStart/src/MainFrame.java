import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrame extends JFrame{
	public MainFrame() {
		setTitle("Shooting Game");
		setSize(360,576);
		add(new Screen());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel titlePage = new JLabel(new ImageIcon("GameStart/src/hehe.jpg"));
		titlePage.setBounds(0,0,360,576);
		add(titlePage);
		
		JButton Start = Button("Start", 100, 360);
		JButton Reset = Button("Reset", 100, 460);
		
		titlePage.add(Start);
		titlePage.add(Reset);
	}
	
	public JButton Button(String text, int x, int y) {
		JButton button = new JButton(text);
		button.setBounds(x,y,120,50);
		button.setBackground(Color.DARK_GRAY);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 15));
		return button;
	}
}
