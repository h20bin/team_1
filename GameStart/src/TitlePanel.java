import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TitlePanel extends JPanel {
    private GameManager manager;

    public TitlePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        JLabel title = new JLabel("Shooting Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 50, 300, 50);
        add(title);

        JButton startButton = new JButton("Start");
        startButton.setBounds(150, 150, 100, 50);
        startButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
        add(startButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(150, 220, 100, 50);
        resetButton.addActionListener(e -> {
            manager.getPlayer().addGold(-manager.getPlayer().getGold());
            System.out.println("Game Reset!");
        });
        add(resetButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        // Render background or animations
    }
}
