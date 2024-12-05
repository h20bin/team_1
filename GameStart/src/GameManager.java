import javax.swing.*;
import java.awt.*;

public class GameManager {
    private JFrame frame;
    private JPanel currentPanel;
    private Player player;

    public GameManager() {
        player = Player.getInstance(); // Singleton Player
        frame = new JFrame("Shooting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setResizable(false);

        // Load the title panel initially
        switchPanel(new TitlePanel(this));
        frame.setVisible(true);
    }

    public void switchPanel(JPanel newPanel) {
        if (currentPanel != null) {
            frame.remove(currentPanel);
        }
        currentPanel = newPanel;
        frame.add(currentPanel);
        frame.revalidate();
        frame.repaint();
    }

    public Player getPlayer() {
        return player;
    }

    public static void main(String[] args) {
        new GameManager();
    }
}
