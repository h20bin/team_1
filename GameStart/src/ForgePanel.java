import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ForgePanel extends JPanel {
    private GameManager manager;

    public ForgePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        JLabel title = new JLabel("Forge", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        int yPosition = 100;

        // Upgrade MaxHP
        JButton hpButton = new JButton("Upgrade MaxHP - 100 Gold");
        hpButton.setBounds(50, yPosition, 300, 50);
        hpButton.addActionListener(e -> attemptUpgrade("MaxHP", 100, () -> {
            Player player = manager.getPlayer();
            player.maxHP += 10;
            JOptionPane.showMessageDialog(this, "MaxHP upgraded!");
        }));
        add(hpButton);
        yPosition += 60;

        // Upgrade MaxSpeed
        JButton speedButton = new JButton("Upgrade MaxSpeed - 150 Gold");
        speedButton.setBounds(50, yPosition, 300, 50);
        speedButton.addActionListener(e -> attemptUpgrade("MaxSpeed", 150, () -> {
            Player player = manager.getPlayer();
            player.maxSpeed += 0.1;
            JOptionPane.showMessageDialog(this, "MaxSpeed upgraded!");
        }));
        add(speedButton);
        yPosition += 60;

        // Upgrade Attack Cycle
        JButton attackCycleButton = new JButton("Upgrade Attack Cycle - 200 Gold");
        attackCycleButton.setBounds(50, yPosition, 300, 50);
        attackCycleButton.addActionListener(e -> attemptUpgrade("Attack Cycle", 200, () -> {
            Player player = manager.getPlayer();
            player.attackCycle -= 0.1;
            JOptionPane.showMessageDialog(this, "Attack Cycle upgraded!");
        }));
        add(attackCycleButton);

        // Go back to lobby
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(150, 400, 100, 50);
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
        add(lobbyButton);
    }

    private void attemptUpgrade(String stat, int cost, Runnable upgradeAction) {
        Player player = manager.getPlayer();
        if (player.getGold() >= cost) {
            player.addGold(-cost);
            if (Math.random() < 0.8) { // 80% success rate
                upgradeAction.run();
            } else {
                JOptionPane.showMessageDialog(this, "Upgrade failed!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Not enough gold!");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
