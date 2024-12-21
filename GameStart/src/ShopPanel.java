import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShopPanel extends JPanel {
    private GameManager manager;
    private int[] weaponID = {1, 2, 3, 4, 5};
    private int yPosition = 100;
    private ImageIcon shopbackgroundImageIcon;

    public ShopPanel(GameManager manager) throws IOException {
        this.manager = manager;
        setLayout(null);
        shopbackgroundImageIcon = new ImageIcon(getClass().getResource("/background/shopback.jpeg"));

        JLabel title = new JLabel("Shop", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setBounds(95, 30, 300, 50);
        title.setForeground(Color.WHITE);
        add(title);

        // 무기 스프라이트와 총알 프레임 준비
        BufferedImage[] weaponSprites1 = loadSpriteSheet("/Weapon/weapon-Sheet" + weaponID[0] + ".png", 72, 72);
        BufferedImage[] bulletFrames1 = loadSpriteSheet("/Weapon/bullet-Sheet" + weaponID[0] + ".png", 6, 4);

        BufferedImage[] weaponSprites2 = loadSpriteSheet("/Weapon/weapon-Sheet" + weaponID[1] + ".png", 72, 72);
        BufferedImage[] bulletFrames2 = loadSpriteSheet("/Weapon/bullet-Sheet" + weaponID[1] + ".png", 24, 30);

        BufferedImage[] weaponSprites3 = loadSpriteSheet("/Weapon/weapon-Sheet" + weaponID[2] + ".png", 72, 72);
        BufferedImage[] bulletFrames3 = loadSpriteSheet("/Weapon/bullet-Sheet" + weaponID[2] + ".png", 24, 30);
        
        BufferedImage[] weaponSprites4 = loadSpriteSheet("/Weapon/HAND1.png", 128, 128);
        BufferedImage[] bulletFrames4 = loadSpriteSheet("/Weapon/ball1.png", 72, 108);
        BufferedImage[] bulletFrames5 = loadSpriteSheet("/Weapon/ball2.png", 72, 108);
        
        BufferedImage[] weaponSprites5 = loadSpriteSheet("/Weapon/HAND2.png", 128, 128);
        BufferedImage[] bulletFrames6 = loadSpriteSheet("/Weapon/ball3.png", 72, 108);
        BufferedImage[] bulletFrames7 = loadSpriteSheet("/Weapon/ball4.png", 72, 108);
        

        // 무기 버튼 추가
        JButton weaponButton1 = new JButton("Weapon - Cost: " + (weaponID[0]) + " Gold");
        weaponButton1.setBounds(90, yPosition, 300, 50);
        weaponButton1.setFont(new Font("Arial", Font.PLAIN, 16));
        weaponButton1.setToolTipText("Purchase Weapon 1 for " + (weaponID[0] * 100) + " Gold");

        weaponButton1.addActionListener(e -> {
            Player player = manager.getPlayer();
            if (player.getGold() >= weaponID[0] * 100) {
                player.addGold(-(weaponID[0] * 100));
                player.setWeapon(new Weapon(weaponSprites1[3], 3, weaponID[0] * 100, 20, bulletFrames1, 1));
                JOptionPane.showMessageDialog(this, "Weapon purchased!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        });

        add(weaponButton1);
        yPosition += 60;

        JButton weaponButton2 = new JButton("Weapon - Cost: " + (weaponID[1] * 100) + " Gold");
        weaponButton2.setBounds(90, yPosition, 300, 50);
        weaponButton2.setFont(new Font("Arial", Font.PLAIN, 16));
        weaponButton2.setToolTipText("Purchase Weapon 2 for " + (weaponID[1] * 100) + " Gold");

        weaponButton2.addActionListener(e -> {
            Player player = manager.getPlayer();
            if (player.getGold() >= weaponID[1] * 100) {
                player.addGold(-(weaponID[1] * 100));
                player.setWeapon(new Weapon(weaponSprites2[3], 3, weaponID[1] * 100, 20, bulletFrames2, 2));
                JOptionPane.showMessageDialog(this, "Weapon purchased!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        });

        add(weaponButton2);
        yPosition += 60;

        JButton weaponButton3 = new JButton("Weapon - Cost: " + (weaponID[2] * 100) + " Gold");
        weaponButton3.setBounds(90, yPosition, 300, 50);
        weaponButton3.setFont(new Font("Arial", Font.PLAIN, 16));
        weaponButton3.setToolTipText("Purchase Weapon 3 for " + (weaponID[2] * 100) + " Gold");

        weaponButton3.addActionListener(e -> {
            Player player = manager.getPlayer();
            if (player.getGold() >= weaponID[2] * 100) {
                player.addGold(-(weaponID[2] * 100));
                player.setWeapon(new Weapon(weaponSprites3[3], 3, weaponID[2] * 100, 20, bulletFrames3, 3));
                JOptionPane.showMessageDialog(this, "Weapon purchased!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        });

        add(weaponButton3);
        yPosition += 30;

        // Go back to lobby 버튼 크기 변경
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(140, 400, 200, 60);  // 크기와 위치 변경
        lobbyButton.setFont(new Font("Arial", Font.BOLD, 18));
        lobbyButton.setBackground(Color.CYAN);
        lobbyButton.setForeground(Color.BLACK);
        lobbyButton.setFocusPainted(false);
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
        add(lobbyButton);

        // 추가적인 UI 요소 - 구매 가능 골드 표시
        JLabel goldLabel = new JLabel("Gold: " + manager.getPlayer().getGold(), SwingConstants.CENTER);
        goldLabel.setFont(new Font("Arial", Font.BOLD, 18));
        goldLabel.setBounds(90, 460, 300, 40);
        goldLabel.setForeground(Color.YELLOW);
        add(goldLabel);
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

        if (shopbackgroundImageIcon != null) {
            Image backgroundImage = shopbackgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지를 패널 크기에 맞게 그리기
        } else {
            // 이미지가 로드되지 않았을 경우 검정색 배경
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
