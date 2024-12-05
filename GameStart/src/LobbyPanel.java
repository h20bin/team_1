import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LobbyPanel extends JPanel {
    private GameManager manager;

    public LobbyPanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        // 플레이어 캐릭터 표시
        JLabel playerLabel = new JLabel("Player");
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        playerLabel.setBounds(100, 50, 200, 30);
        add(playerLabel);

        // 캐릭터 이미지 (간단히 박스 사용)
        JPanel characterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY); // Weapon
                g.fillRect(40, 10, 40, 10);
                g.setColor(Color.BLUE); // Body
                g.fillRect(50, 30, 20, 40);
                g.setColor(Color.RED); // Pen
                g.fillRect(55, 70, 10, 20);
            }
        };
        characterPanel.setBounds(140, 100, 120, 100);
        add(characterPanel);

        // Shop 버튼
        JButton shopButton = new JButton("Shop");
        shopButton.setBounds(50, 250, 100, 50);
        shopButton.addActionListener(e -> manager.switchPanel(new ShopPanel(manager)));
        add(shopButton);

        // StageChoose 버튼
        JButton stageButton = new JButton("Stage");
        stageButton.setBounds(150, 250, 100, 50);
        stageButton.addActionListener(e -> manager.switchPanel(new StagePanel(manager)));
        add(stageButton);

        // Forge 버튼
        JButton forgeButton = new JButton("Forge");
        forgeButton.setBounds(250, 250, 100, 50);
        forgeButton.addActionListener(e -> manager.switchPanel(new ForgePanel(manager)));
        add(forgeButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 설정
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // HUD 표시
        Player player = manager.getPlayer();
        g.setColor(Color.BLACK);
        g.drawString("HP: " + player.maxHP, 20, 20);
        g.drawString("Gold: " + player.getGold(), 20, 40);
    }
}
