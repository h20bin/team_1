import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StageGamePanel extends JPanel implements ActionListener, KeyListener {
    private GameManager manager;
    private Timer timer;
    private Player player;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private int backgroundY = 0; // 배경 스크롤 위치
    private boolean[] keys = new boolean[256]; // 키 입력 상태 저장
    private Rectangle goal; // 스테이지 클리어 목표

    public StageGamePanel(GameManager manager, int stageNum) {
        this.manager = manager;
        this.player = manager.getPlayer();
        this.enemies = new ArrayList<>();
        this.bullets = new ArrayList<>();

        initializeStage(stageNum);

        // 게임 루프 시작
        timer = new Timer(16, this);
        timer.start();

        // 키 입력 처리 활성화
        setFocusable(true);
        // 포커스를 강제로 요청
        SwingUtilities.invokeLater(() -> {
            boolean focusRequested = requestFocusInWindow();
            System.out.println("Focus requested in SwingUtilities: " + focusRequested);
        });
        addKeyListener(this);

     // 디버깅: 패널이 포커스를 받았는지 확인
        System.out.println("Focusable: " + isFocusable());
        System.out.println("Request Focus: " + requestFocusInWindow());
    }

    private void initializeStage(int stageNum) {
        // 적 생성
        for (int i = 0; i < stageNum * 5; i++) {
            enemies.add(new Enemy(50 + (i * 40) % 300, -100 - (i * 80), 50, 2));
        }
        
        player.reset();
        
        // 목표 지점 (클리어 목표인 별의 좌표)
        goal = new Rectangle(160, -2560 + 100, 40, 40);
        System.out.println("Stage initialized. Goal set at: " + goal);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 그리기
        drawBackground(g);

        // 플레이어 그리기
        player.render(g);

        // 적 그리기
        for (Enemy enemy : enemies) {
            enemy.render(g);
        }

        // 총알 그리기
        for (Bullet bullet : bullets) {
            bullet.render(g);
        }

        // 목표 지점 (별) 그리기
        g.setColor(Color.YELLOW);
        g.fillRect(goal.x, goal.y + backgroundY, goal.width, goal.height);

        // HUD (체력, 골드 표시)
        g.setColor(Color.WHITE);
        g.drawString("HP: " + player.currentHP, 10, 20);
        g.drawString("Gold: " + player.getGold(), 10, 40);
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 배경 스크롤링
        g.setColor(Color.GRAY);
        for (int i = -2500 + backgroundY; i <= backgroundY; i += 100) {
            g.fillRect(0, i, getWidth(), 100); // 임시 배경 타일
        }

        // 스크롤 속도 조정: 초당 50픽셀 (16ms 간격으로 0.8 픽셀 이동)
        backgroundY += 1; // 약 50초 후 2500픽셀 스크롤 완료
        if (backgroundY > getHeight()) {
            backgroundY = 0;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // 게임 상태 업데이트
        updateGame();

        // 충돌 처리
        checkCollisions();

        // 화면 갱신
        repaint();
    }

    private void updateGame() {
        // WASD 이동 키 디버깅
        if (keys[KeyEvent.VK_A]) {
            player.move(-20, 0);
            System.out.println("Moving left");
        }
        if (keys[KeyEvent.VK_D]) {
            player.move(20, 0);
            System.out.println("Moving right");
        }
        if (keys[KeyEvent.VK_W]) {
            player.move(0, -20);
            System.out.println("Moving up");
        }
        if (keys[KeyEvent.VK_S]) {
            player.move(0, 20);
            System.out.println("Moving down");
        }

        // 적 이동
        for (Enemy enemy : enemies) {
            enemy.move();
        }

        // 총알 이동
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.move();
            if (bullet.getY() < 0) {
                bullets.remove(i--); // 화면 밖으로 나가면 제거
            }
        }
    }

    private void checkCollisions() {
        // Bullet과 Enemy 충돌 체크
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            Rectangle bulletBounds = bullet.getBounds();

            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                Rectangle enemyBounds = enemy.getBounds();

                if (bulletBounds.intersects(enemyBounds)) {
                    System.out.println("Collision detected: Bullet hit Enemy!");
                    enemy.takeDamage(bullet.getDamage());
                    bullets.remove(i--);
                    if (enemy.getHealth() <= 0) {
                        enemies.remove(j--);
                        System.out.println("Enemy defeated!");
                    }
                    break;
                }
            }
        }

        // Enemy와 Player 충돌 체크
        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            Rectangle playerBounds = player.getBounds();

            if (enemyBounds.intersects(playerBounds)) {
                System.out.println("Collision detected: Enemy hit Player!");
                player.takeDamage(10);
                if (player.currentHP <= 0) {
                    JOptionPane.showMessageDialog(this, "Game Over!");
                    manager.switchPanel(new TitlePanel(manager));
                    timer.stop();
                }
            }
        }

        // 목표 지점 도달 체크
        Rectangle playerBounds = player.getBounds();
        if (playerBounds.intersects(new Rectangle(goal.x, goal.y + backgroundY, goal.width, goal.height))) {
            JOptionPane.showMessageDialog(this, "Stage Cleared!");
            player.addGold(100);
            manager.switchPanel(new LobbyPanel(manager));
            timer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = true;
            System.out.println("Key Pressed: " + KeyEvent.getKeyText(keyCode));
        }

        // M키로 총알 발사
        if (keyCode == KeyEvent.VK_M) {
            bullets.add(new Bullet(player.getX() + 15, player.getY(), 5, 10));
            System.out.println("Bullet fired!");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = false;
            System.out.println("Key Released: " + KeyEvent.getKeyText(keyCode));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
