import javax.swing.*;
import java.awt.*;

public class StagePanel extends JPanel {
    private GameManager manager;
    private ImageIcon stagebackgroundImageIcon;
    private JButton[] stageButtons;  // 스테이지 버튼들을 저장할 배열

    public StagePanel(GameManager manager) {
        this.manager = manager;
        setLayout(null);  // 패널의 레이아웃을 null로 설정하여 절대 위치 지정

        // 배경 이미지 아이콘을 클래스 경로에서 로드
        stagebackgroundImageIcon = new ImageIcon(getClass().getResource("/background/stageback.jpeg"));

        // 제목 라벨을 설정 (스윙 컴포넌트의 레이아웃 관리자가 없으면 위치를 직접 설정)
        JLabel title = new JLabel("Choose Stage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(80, 20, 300, 50);
        add(title);

        // 10개의 스테이지 버튼을 배열에 저장
        stageButtons = new JButton[10];  // 10개의 스테이지 버튼을 저장할 배열

        // 스테이지 버튼들을 화면에 배치할 y좌표 시작값
        int yPosition = 80;

        // 1부터 10까지의 스테이지 버튼을 생성하고 배치
        for (int i = 1; i <= 10; i++) {
            int stageNum = i;  // 각 스테이지 번호

            // 버튼 생성: "Stage " + i로 버튼 라벨 설정
            stageButtons[i - 1] = new JButton("Stage " + stageNum);
            stageButtons[i - 1].setBounds(80, yPosition, 300, 50);  // 버튼 위치와 크기 설정

            // 버튼 클릭 시 실행할 동작 설정
            stageButtons[i - 1].addActionListener(e -> {
                manager.switchPanel(new StageGamePanel(manager, stageNum));  // 스테이지 게임으로 전환
            });

            // 첫 번째 스테이지만 활성화하고 나머지 스테이지는 비활성화
            if (i == 1) {
                stageButtons[i - 1].setEnabled(true);  // 첫 번째 스테이지는 활성화
            } else {
                stageButtons[i - 1].setEnabled(false);  // 나머지 스테이지는 비활성화
            }

            // 생성한 버튼을 패널에 추가
            add(stageButtons[i - 1]);

            // 버튼 간의 간격을 60px로 설정
            yPosition += 60;  // 버튼 간격
        }

        // 로비로 돌아가기 버튼 생성
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(160, 680, 150, 50);  // 버튼 위치를 조정 (여기서는 yPosition에 맞춰 설정)
        lobbyButton.addActionListener(e -> manager.switchPanel(new LobbyPanel(manager)));  // 클릭 시 로비 화면으로 전환
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

        // 배경을 채우는 색을 설정
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());  // 전체 화면을 cyan 색으로 채우기

        // 배경 이미지가 null이 아닌 경우 배경 이미지를 그리기
        if (stagebackgroundImageIcon != null) {
            Image backgroundImage = stagebackgroundImageIcon.getImage(); // ImageIcon에서 Image 객체 추출
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지를 패널 크기에 맞게 그리기
        } else {
            // 이미지가 로드되지 않았을 경우 검정색 배경
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());  // 검정색 배경을 채우기
        }
    }

    // 추가적인 메서드로 비슷한 기능을 반복하는 코드를 만들어 코드 길이를 늘릴 수 있음
    private void setButtonProperties(JButton button, String text, int x, int y, int width, int height) {
        button.setText(text);
        button.setBounds(x, y, width, height);
    }

    // UI 컴포넌트의 색상 설정 메서드
    private void setComponentColor(JComponent component, Color color) {
        component.setBackground(color);
        component.setForeground(color);
    }

    // 버튼을 비활성화 하는 메서드
    private void disableButton(JButton button) {
        button.setEnabled(false);
    }

    // 버튼을 활성화하는 메서드
    private void enableButton(JButton button) {
        button.setEnabled(true);
    }

    // 제목을 설정하는 메서드
    private void setTitle(String title) {
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(50, 20, 300, 50);
        add(titleLabel);
    }

    // Stage 버튼들을 초기화하는 메서드
    private void initializeStageButtons() {
        for (int i = 1; i <= 10; i++) {
            JButton stageButton = new JButton("Stage " + i);
            stageButton.setBounds(50, 100 + i * 60, 300, 50);
            add(stageButton);
        }
    }

    // 로비 버튼 초기화 메서드
    private void initializeLobbyButton() {
        JButton lobbyButton = new JButton("Go to Lobby");
        lobbyButton.setBounds(150, 700, 100, 50);
        add(lobbyButton);
    }
}