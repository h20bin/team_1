import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
        for (int i = 1; i <= 1; i++) {
            int weaponID = i;

            // 무기 스프라이트와 총알 프레임 준비
            BufferedImage weaponSprite = loadSprite("/Weapon/weapon-Sheet" + weaponID + ".png", 72, 72);
            BufferedImage[] bulletFrames = loadAllFrames("/Weapon/bullet-Sheet" + weaponID + ".png", 6, 4);

            JButton weaponButton = new JButton("Weapon " + weaponID + " - Cost: " + (weaponID * 100) + " Gold");
            weaponButton.setBounds(50, yPosition, 300, 50);

            weaponButton.addActionListener(e -> {
                Player player = manager.getPlayer();
                if (player.getGold() >= weaponID * 100) {
                    player.addGold(-(weaponID * 100));
                    player.setWeapon(new Weapon(weaponSprite, 3, weaponID * 10, 5, bulletFrames));
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

    private BufferedImage loadSprite(String path, int width, int height) {
        try {
            return new SpriteSheet(getClass().getResource(path).getPath(), width, height).getFrame(0);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            // 기본값 생성
            BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = placeholder.getGraphics();
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, width, height);
            g.dispose();
            return placeholder;
        }
    }

    private BufferedImage[] loadAllFrames(String path, int frameWidth, int frameHeight) {
        try {
            return new SpriteSheet(getClass().getResource(path).getPath(), frameWidth, frameHeight).getAllFrames();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            // 기본값 생성
            BufferedImage placeholder = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = placeholder.getGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, frameWidth, frameHeight);
            g.dispose();
            return new BufferedImage[]{placeholder};
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
