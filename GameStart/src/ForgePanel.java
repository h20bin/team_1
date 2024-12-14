import javax.swing.*;
import java.awt.*;

public class ForgePanel extends JPanel {
    private GameManager manager;
    private int hpUpgradeCost = 100;
    private int speedUpgradeCost = 150;
    private int attackCycleUpgradeCost = 200;

    public ForgePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        JLabel title = new JLabel("Forge", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        int yPosition = 100;

        // Upgrade MaxHP
        JButton hpButton = new JButton("Upgrade MaxHP - " + hpUpgradeCost + " Gold");
        hpButton.setBounds(50, yPosition, 300, 50);
        hpButton.addActionListener(e -> attemptUpgrade("MaxHP", hpUpgradeCost, () -> {
            Player player = manager.getPlayer();
            player.increaseMaxHP(10);
            JOptionPane.showMessageDialog(this, "MaxHP upgraded!");
            // 50골드씩 증가
            hpUpgradeCost += 50;
            hpButton.setText("Upgrade MaxHP - " + hpUpgradeCost + " Gold");
        }));
        add(hpButton);
        yPosition += 60;

        // Upgrade MaxSpeed
        JButton speedButton = new JButton("Upgrade MaxSpeed - " + speedUpgradeCost + " Gold");
        speedButton.setBounds(50, yPosition, 300, 50);
        speedButton.addActionListener(e -> attemptUpgrade("MaxSpeed", speedUpgradeCost, () -> {
            Player player = manager.getPlayer();
            player.increaseMaxSpeed(0.1);
            JOptionPane.showMessageDialog(this, "MaxSpeed upgraded!");
            // 50골드씩 증가
            speedUpgradeCost += 50;
            speedButton.setText("Upgrade MaxSpeed - " + speedUpgradeCost + " Gold");
        }));
        add(speedButton);
        yPosition += 60;

        // Upgrade Attack Cycle
        JButton attackCycleButton = new JButton("Upgrade Attack Cycle - " + attackCycleUpgradeCost + " Gold");
        attackCycleButton.setBounds(50, yPosition, 300, 50);
        attackCycleButton.addActionListener(e -> attemptUpgrade("Attack Cycle", attackCycleUpgradeCost, () -> {
            Player player = manager.getPlayer();
            player.reduceAttackCycle(0.1);
            JOptionPane.showMessageDialog(this, "Attack Cycle upgraded!");
            // 50골드씩 증가
            attackCycleUpgradeCost += 50;
            attackCycleButton.setText("Upgrade Attack Cycle - " + attackCycleUpgradeCost + " Gold");
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
