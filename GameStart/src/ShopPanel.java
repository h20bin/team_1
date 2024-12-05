import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopPanel extends JPanel {
    private GameManager manager;

    public ShopPanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        JLabel title = new JLabel("Shop", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        // Weapon buttons
        int yPosition = 100;
        for (int i = 1; i <= 4; i++) {
            int weaponID = i;
            JButton weaponButton = new JButton("Weapon " + weaponID + " - Cost: " + (weaponID * 100) + " Gold");
            weaponButton.setBounds(50, yPosition, 300, 50);
            weaponButton.addActionListener(e -> {
                Player player = manager.getPlayer();
                if (player.getGold() >= weaponID * 100) {
                    player.addGold(-(weaponID * 100));
                    player.weapon = new Weapon(weaponID, weaponID * 10, 3);
                    JOptionPane.showMessageDialog(this, "Weapon " + weaponID + " purchased!");
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough gold!");
                }
            });
            add(weaponButton);
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
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
