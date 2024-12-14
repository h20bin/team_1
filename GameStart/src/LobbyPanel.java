import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LobbyPanel extends JPanel {
    private GameManager manager;

    public LobbyPanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        JLabel playerLabel = new JLabel("Player");
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        playerLabel.setBounds(180, 50, 200, 30);
        add(playerLabel);

        JButton shopButton = new JButton("Shop");
        shopButton.setBounds(80, 250, 100, 50);
        shopButton.addActionListener(e -> {
			try {
				manager.switchPanel(new ShopPanel(manager));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        add(shopButton);

        JButton stageButton = new JButton("Stage");
        stageButton.setBounds(180, 250, 100, 50);
        stageButton.addActionListener(e -> manager.switchPanel(new StagePanel(manager)));
        add(stageButton);

        JButton forgeButton = new JButton("Forge");
        forgeButton.setBounds(280, 250, 100, 50);
        forgeButton.addActionListener(e -> manager.switchPanel(new ForgePanel(manager)));
        add(forgeButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // HUD 표시
        Player player = manager.getPlayer();
        g.setColor(Color.BLACK);
        g.drawString("HP: " + player.getCurrentHP() + "/" + player.getMaxHP(), 20, 20);
        g.drawString("Gold: " + player.getGold(), 20, 40);

        // 플레이어 스프라이트 그리기
        player.render(g);
    }
}
