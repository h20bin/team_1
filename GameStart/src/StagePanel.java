import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StagePanel extends JPanel {
    private GameManager manager;

    public StagePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        JLabel title = new JLabel("Choose Stage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        int yPosition = 100;

        // Stage Buttons
        for (int i = 1; i <= 4; i++) {
            int stageNum = i;
            JButton stageButton = new JButton("Stage " + stageNum);
            stageButton.setBounds(50, yPosition, 300, 50);
            stageButton.addActionListener(e -> {
                System.out.println("Stage " + stageNum + " selected!");
                manager.switchPanel(new StageGamePanel(manager, stageNum));
            });
            add(stageButton);
            yPosition += 60;
        }

        // Go back to lobby
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(150, 400, 100, 50);
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
        add(lobbyButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
