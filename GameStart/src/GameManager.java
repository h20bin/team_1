import javax.swing.*;

public class GameManager {
    private JFrame frame;
    private JPanel currentPanel;
    private boolean[] stagesCompleted;  // 각 스테이지의 완료 여부를 추적하는 배열
    private Player player;
    private boolean inLobby;  // 로비 상태를 관리하는 변수

    public GameManager() {
        player = Player.getInstance();  // 싱글톤 Player 인스턴스 가져오기
        player.reset();

        stagesCompleted = new boolean[10];  // 스테이지 10개를 초기화 (모두 미완료 상태로 시작)

        frame = new JFrame("Shooting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 768);
        frame.setResizable(false);

        // 기본적으로 TitlePanel을 로드
        switchPanel(new TitlePanel(this));
        frame.setVisible(true);
    }

    public void switchPanel(JPanel newPanel) {
        if (currentPanel != null) {
            frame.remove(currentPanel);
        }
        currentPanel = newPanel;
        frame.add(currentPanel);
        frame.revalidate();
        frame.repaint();

        // 패널이 로비로 변경되면 inLobby을 true로 설정
        if (newPanel instanceof LobbyPanel) {
            inLobby = true;
        } else {
            inLobby = false;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isInLobby() {
        return inLobby;  // 로비에 있을 때 true 반환
    }

    // 특정 스테이지가 완료되었는지 확인하는 메서드
    public boolean isStageCompleted(int stageNum) {
        return stagesCompleted[stageNum - 1];  // 스테이지 완료 여부 반환
    }

    // 스테이지를 완료했을 때 호출되는 메서드
    public void completeStage(int stageNum) {
        if (stageNum >= 1 && stageNum <= 10) {
            stagesCompleted[stageNum - 1] = true;  // 해당 스테이지를 완료로 표시
        }
    }

    public static void main(String[] args) {
        new GameManager();
    }

	public StagePanel getCurrentPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}
