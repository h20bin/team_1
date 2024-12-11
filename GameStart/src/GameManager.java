import javax.swing.*;

public class GameManager {
    private JFrame frame;
    private JPanel currentPanel;
    private Player player;

    public GameManager() {
        // 싱글톤 Player 인스턴스 가져오기
        player = Player.getInstance();

        // 초기 상태 설정
        player.reset();

        frame = new JFrame("Shooting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 768);
        frame.setResizable(false);

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
    }

    public Player getPlayer() {
        return player;
    }

    public static void main(String[] args) {
        new GameManager();
    }
}
