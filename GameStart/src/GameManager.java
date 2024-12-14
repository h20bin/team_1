import javax.swing.*;

public class GameManager {
    private JFrame frame;
    private JPanel currentPanel;
    private Player player;
    private boolean inLobby;  // 로비 상태를 관리하는 변수

    public GameManager() {
        // 싱글톤 Player 인스턴스 가져오기
        player = Player.getInstance();

        // 초기 상태 설정
        player.reset();

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

    public static void main(String[] args) {
        new GameManager();
    }
}