import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LobbyPanel extends JPanel {
    private GameManager manager;
    private ImageIcon lobbybackgroundImageIcon;

    public LobbyPanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);
        
        lobbybackgroundImageIcon = new ImageIcon(getClass().getResource("/background/lobbyback.jpeg"));

        JLabel playerLabel = new JLabel("Player");
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        playerLabel.setBounds(150, 50, 200, 30);
        add(playerLabel);

        JButton shopButton = new JButton("Shop");
        shopButton.setBounds(100, 250, 100, 50);
        shopButton.addActionListener(e -> {
            try {
                manager.switchPanel(new ShopPanel(manager));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        add(shopButton);

        JButton stageButton = new JButton("Stage");
        stageButton.setBounds(200, 250, 100, 50);
        stageButton.addActionListener(e -> manager.switchPanel(new StagePanel(manager)));
        add(stageButton);

        JButton forgeButton = new JButton("Forge");
        forgeButton.setBounds(300, 250, 100, 50);
        forgeButton.addActionListener(e -> manager.switchPanel(new ForgePanel(manager)));
        add(forgeButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // 기존 내용을 클리어하여 화면을 초기화
        
        // 배경 색을 회색으로 채움
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 배경 이미지가 존재할 경우 그리기
        if (lobbybackgroundImageIcon != null) {
            Image backgroundImage = lobbybackgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지를 패널 크기에 맞게 그리기
        }

        // HUD 정보 표시
        Player player = manager.getPlayer();
        g.setColor(Color.BLACK);
        g.drawString("HP: " + player.getCurrentHP() + "/" + player.getMaxHP(), 20, 20);
        g.drawString("Gold: " + player.getGold(), 20, 40);

        // 로비에서는 플레이어 스프라이트를 그리지 않음
        if (!manager.isInLobby()) {
            player.render(g);
        }
    }
}