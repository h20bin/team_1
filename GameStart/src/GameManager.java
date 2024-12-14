import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameManager {
    private JFrame frame;
    private JPanel currentPanel;
    private boolean[] stagesCompleted;  // 각 스테이지의 완료 여부를 추적하는 배열
    private Player player;
    private boolean inLobby;  // 로비 상태를 관리하는 변수
    private ArrayList<Enemy> enemies;  // 적들을 관리할 리스트

    public GameManager() {
        player = Player.getInstance();  // 싱글톤 Player 인스턴스 가져오기
        player.reset();

        stagesCompleted = new boolean[10];  // 스테이지 10개를 초기화 (모두 미완료 상태로 시작)

        frame = new JFrame("Shooting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 768);
        frame.setResizable(false);

        enemies = new ArrayList<>();  // 적들을 관리할 리스트 초기화

        // 기본적으로 TitlePanel을 로드
        switchPanel(new TitlePanel(this));
        frame.setVisible(true);

        startEnemyGeneration();  // 적 생성 시작
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

    // 적을 무한으로 생성하는 메서드
    public void startEnemyGeneration() {
        new Thread(() -> {
            while (true) {
                if (!isStageCompleted(1)) {  // 1스테이지가 완료될 때까지 적을 생성
                    // 적 생성 (각 스테이지마다 다른 설정을 할 수 있음)
                    int enemyX = (int) (Math.random() * 512);  // 화면 너비 안에서 랜덤한 x값
                    int enemyY = 0;  // y값은 0에서 시작 (위쪽에서 시작)
                    int enemyHealth = 100;  // 기본 체력
                    BufferedImage[] enemyFrames = new BufferedImage[5]; // 임시 이미지 배열 (교체 필요)
                    // 임시로 빈 BufferedImage 객체를 생성 (적 이미지 설정 필요)
                    for (int i = 0; i < enemyFrames.length; i++) {
                        enemyFrames[i] = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);  // 빈 이미지
                    }
                    Weapon enemyWeapon = new Weapon();  // 기본 무기 (교체 필요)

                    Enemy newEnemy = new Enemy(enemyX, enemyY, enemyHealth, enemyFrames[0], enemyWeapon);
                    enemies.add(newEnemy);

                    // 잠시 대기 후 다음 적 생성 (속도 조절)
                    try {
                        Thread.sleep(1000);  // 1초마다 적 생성
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;  // 스테이지가 완료되면 적 생성 멈춤
                }
            }
        }).start();
    }

    // 모든 적을 업데이트하고 렌더링하는 메서드
    public void updateAndRenderEnemies(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.move(0, 5);  // 적을 y축으로 5만큼 이동 (아래로)
            enemy.render(g);  // 적을 화면에 그리기
        }
    }

    public static void main(String[] args) {
        new GameManager();
    }

    public StagePanel getCurrentPanel() {
        return null;
    }
}
