import javax.swing.*;
import java.awt.*;

public class StagePanel extends JPanel {
    private GameManager manager;
    private ImageIcon stagebackgroundImageIcon;

    public StagePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);
        
        stagebackgroundImageIcon = new ImageIcon(getClass().getResource("/background/stageback.jpeg"));

        JLabel title = new JLabel("Choose Stage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        int yPosition = 100;

        // 스테이지 버튼 생성 (1부터 10까지)
        for (int i = 1; i <= 10; i++) {
            int stageNum = i;
            JButton stageButton = new JButton("Stage " + stageNum);
            stageButton.setBounds(50, yPosition, 300, 50);
            stageButton.addActionListener(e -> {
                manager.switchPanel(new StageGamePanel(manager, stageNum));
            });
            add(stageButton);
            yPosition += 60;  // 버튼 간격
        }

        // 로비로 돌아가기 버튼
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(150, 700, 100, 50);  // 버튼 위치를 조정 (여기서는 yPosition에 맞춰 설정)
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
        add(lobbyButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if (stagebackgroundImageIcon != null) {
            Image backgroundImage = stagebackgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지를 패널 크기에 맞게 그리기
        } else {
            // 이미지가 로드되지 않았을 경우 검정색 배경
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
