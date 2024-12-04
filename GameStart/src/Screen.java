import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Screen extends JPanel implements ComponentListener, KeyListener{
	private Graphics bg;
	private Image offScreen;
	private Dimension dim;
	private Character player = new Character();
	private int stage = 0;
	
	public Screen() {
		
	}
	
	private void initBuffer() {
		this.dim = getSize();
		this.offScreen = createImage(dim.width, dim.height);
		this.bg = this.offScreen.getGraphics();
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		bg.clearRect(0, 0, dim.width, dim.height);
		
		/*
		if(stage == 0) {
			Title.draw(bg, this);
		}
		else if(stage == 1) {
			Palyer.draw(bg, this);
		}
		else if(stage == 2) {
			Palyer.draw(bg, this);
		}
		else if(stage == 3) {
			Palyer.draw(bg, this);
		}
		else if(stage == 4) {
			Palyer.draw(bg, this);
		}
		else if(stage == 5) {
			Palyer.draw(bg, this);
		}
		else if(stage == 6) {
			Shop.draw(bg, this);
		}
		*/
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		initBuffer();
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
