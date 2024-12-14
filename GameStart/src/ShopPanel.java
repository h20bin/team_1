import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShopPanel extends JPanel {
    private GameManager manager;

    public ShopPanel(GameManager manager) throws IOException {
        this.manager = manager;
        setLayout(null);

        JLabel title = new JLabel("Shop", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        // Weapon buttons
        int yPosition = 100;
        for (int i = 1; i <= 1; i++) {
            int weaponID = i;
            System.out.println(getClass().getResource("Weapon/weapon-Sheet1.png"));
            // 무기 스프라이트와 총알 프레임 준비
            BufferedImage[] weaponSprites = loadSpriteSheet("/Weapon/weapon-Sheet"+ weaponID +".png", 72, 72);
            BufferedImage[] bulletFrames = loadSpriteSheet("/Weapon/bullet-Sheet"+ weaponID +".png", 6, 4);       

            JButton weaponButton = new JButton("Weapon " + weaponID + " - Cost: " + (weaponID * 100) + " Gold");
            weaponButton.setBounds(50, yPosition, 300, 50);

            weaponButton.addActionListener(e -> {
                Player player = manager.getPlayer();
                if (player.getGold() >= weaponID * 100) {
                    player.addGold(-(weaponID * 100));
                    player.setWeapon(new Weapon(weaponSprites[0], 3, weaponID * 10, 5, bulletFrames));
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

 // 유틸리티 메서드: 스프라이트 시트를 로드합니다.
    private BufferedImage[] loadSpriteSheet(String resourcePath, int frameWidth, int frameHeight) throws IOException {
        return new SpriteSheet(resourcePath, frameWidth, frameHeight).getAllFrames();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
