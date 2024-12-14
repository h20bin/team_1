import javax.swing.*;
import java.awt.*;

public class StagePanel extends JPanel {
    private GameManager manager;

    public StagePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);

        // 타이틀 라벨 생성
        JLabel title = new JLabel("Choose Stage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 20, 300, 50);
        add(title);

        int yPosition = 100;

        // 스테이지 버튼 생성 (1~10 스테이지)
        for (int i = 1; i <= 10; i++) { // 버튼 갯수를 10개로 확장
            int stageNum = i; // stageNum 변수에 현재 반복 단계 할당
            JButton stageButton = new JButton("Stage " + stageNum);
            stageButton.setBounds(50, yPosition, 300, 50); // 버튼 위치 및 크기 설정
            stageButton.addActionListener(e -> {
                manager.switchPanel(new StageGamePanel(manager, stageNum)); // 버튼 클릭 시 StageGamePanel로 이동
            });
            add(stageButton); // 패널에 버튼 추가
            yPosition += 60; // 버튼 간격 조정
        }

        // 로비로 돌아가기 버튼 생성
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(150, 400, 100, 50); // 위치 및 크기 설정
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager))); // 버튼 클릭 시 로비로 이동
        add(lobbyButton); // 패널에 버튼 추가
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.CYAN); // 배경색 설정
        g.fillRect(0, 0, getWidth(), getHeight()); // 화면 전체를 채움
    }
}
