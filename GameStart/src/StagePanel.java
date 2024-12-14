import javax.swing.*;
import java.awt.*;

public class StagePanel extends JPanel {
    private GameManager manager;
    private ImageIcon stagebackgroundImageIcon;
    private JButton[] stageButtons;  // 스테이지 버튼들을 저장할 배열

    public StagePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        stagebackgroundImageIcon = new ImageIcon(getClass().getResource("/background/stageback.jpeg"));

        JLabel title = new JLabel("Choose Stage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        stageButtons = new JButton[10];  // 10개의 스테이지 버튼을 저장할 배열

        int yPosition = 100;

        // 스테이지 버튼 생성 (1부터 10까지)
        for (int i = 1; i <= 10; i++) {
            int stageNum = i;
            stageButtons[i - 1] = new JButton("Stage " + stageNum);
            stageButtons[i - 1].setBounds(50, yPosition, 300, 50);
            stageButtons[i - 1].addActionListener(e -> {
                manager.switchPanel(new StageGamePanel(manager, stageNum));  // 스테이지 게임으로 전환
            });

            // 첫 번째 스테이지만 활성화하고 나머지 스테이지는 비활성화
            if (i == 1) {
                stageButtons[i - 1].setEnabled(true);  // 첫 번째 스테이지는 활성화
            } else {
                stageButtons[i - 1].setEnabled(false);  // 나머지 스테이지는 비활성화
            }

            add(stageButtons[i - 1]);
            yPosition += 60;  // 버튼 간격
        }

        // 로비로 돌아가기 버튼
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(150, 700, 100, 50);  // 버튼 위치를 조정 (여기서는 yPosition에 맞춰 설정)
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));
        add(lobbyButton);
    }

    // 스테이지 클리어 후 버튼 상태를 갱신하는 메서드
    public void updateStageButtons(int completedStage) {
        if (completedStage < 10) {
            // 현재 스테이지를 클리어한 경우, 그 다음 스테이지 버튼을 활성화
            stageButtons[completedStage].setEnabled(true);
        }
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
