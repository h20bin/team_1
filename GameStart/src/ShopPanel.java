import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShopPanel extends JPanel {
    private GameManager manager;
    private int[] weaponID = {1,2,3,4,5};
    private int yPosition = 100;

    public ShopPanel(GameManager manager) throws IOException {
        this.manager = manager;
        setLayout(null);

        JLabel title = new JLabel("Shop", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        // 무기 스프라이트와 총알 프레임 준비
        BufferedImage[] weaponSprites1 = loadSpriteSheet("/Weapon/weapon-Sheet"+ weaponID[0] +".png", 72, 72);
        BufferedImage[] bulletFrames1 = loadSpriteSheet("/Weapon/bullet-Sheet"+ weaponID[0] +".png", 6, 4);   
        
        BufferedImage[] weaponSprites2 = loadSpriteSheet("/Weapon/weapon-Sheet"+ weaponID[1] +".png", 72, 72);
        BufferedImage[] bulletFrames2 = loadSpriteSheet("/Weapon/bullet-Sheet"+ weaponID[1] +".png", 24, 30);    
        
        BufferedImage[] weaponSprites3 = loadSpriteSheet("/Weapon/weapon-Sheet"+ weaponID[2] +".png", 72, 72);
        BufferedImage[] bulletFrames3 = loadSpriteSheet("/Weapon/bullet-Sheet"+ weaponID[2] +".png", 24, 30);    

        
        
        JButton weaponButton1 = new JButton("Weapon - Cost: " + (weaponID[0]) + " Gold");
        weaponButton1.setBounds(50, yPosition, 300, 50);

        weaponButton1.addActionListener(e -> {
            Player player = manager.getPlayer();
            if (player.getGold() >= weaponID[0] * 100) {
                player.addGold(-(weaponID[0] * 100));
                player.setWeapon(new Weapon(weaponSprites1[0], 3, weaponID[0] * 100, 20, bulletFrames1));
                JOptionPane.showMessageDialog(this, "Weapon purchased!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        });
        
        add(weaponButton1);
        yPosition += 60;
        
        JButton weaponButton2 = new JButton("Weapon - Cost: " + (weaponID[1] * 100) + " Gold");
        weaponButton2.setBounds(50, yPosition, 300, 50);

        weaponButton2.addActionListener(e -> {
            Player player = manager.getPlayer();
            if (player.getGold() >= weaponID[1] * 100) {
                player.addGold(-( weaponID[1] * 100));
                player.setWeapon(new Weapon(weaponSprites2[0], 3,  weaponID[1] * 100, 20, bulletFrames2));
                JOptionPane.showMessageDialog(this, "Weapon purchased!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        });
        
        add(weaponButton2);
        yPosition += 60;
        
        JButton weaponButton3 = new JButton("Weapon - Cost: " + (weaponID[2] * 100) + " Gold");
        weaponButton3.setBounds(50, yPosition, 300, 50);

        weaponButton3.addActionListener(e -> {
            Player player = manager.getPlayer();
            if (player.getGold() >= weaponID[2] * 100) {
                player.addGold(-(weaponID[2] * 100));
                player.setWeapon(new Weapon(weaponSprites3[0], 3, weaponID[2] * 100, 20, bulletFrames3));
                JOptionPane.showMessageDialog(this, "Weapon purchased!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        });
        
        add(weaponButton3);
        yPosition += 30;

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
