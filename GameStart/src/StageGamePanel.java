import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StageGamePanel extends JPanel implements ActionListener, KeyListener {
    private GameManager manager;
    private Timer timer;
    private Player player;
    private List<Enemy> enemies;
    private int backgroundY = 0; // 배경 스크롤 위치
    private boolean[] keys = new boolean[256]; // 키 입력 상태 저장
    private Rectangle goal; // 스테이지 클리어 목표

    public StageGamePanel(GameManager manager, int stageNum) {
        this.manager = manager;
        this.player = manager.getPlayer();
        this.enemies = new ArrayList<>();

        initializeStage(stageNum);

        // 게임 루프 시작
        timer = new Timer(16, this);
        timer.start();

        // 키 입력 처리 활성화
        setFocusable(true);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
        addKeyListener(this);
    }

    private void initializeStage(int stageNum) {
        // 적 생성
        for (int i = 0; i < stageNum * 5; i++) {
            BufferedImage enemySprite = null;
            try {
                // 파일 경로를 통해 리소스를 로드
                enemySprite = new SpriteSheet("/Character/bora-sheet.png", 36, 36).getFrame(i % 4);
            } catch (IOException e) {
                e.printStackTrace();
                // 초기화 실패 시 기본 스프라이트 생성
                enemySprite = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                Graphics g = enemySprite.getGraphics();
                g.setColor(Color.RED);
                g.fillRect(0, 0, 40, 40);
                g.dispose();
            }

            Weapon enemyWeapon = new Weapon(enemySprite, 3, 5, 2, new BufferedImage[0]);
            enemies.add(new Enemy(50 + (i * 40) % 300, -100 - (i * 80), 50, enemySprite, enemyWeapon));
        }

        player.reset();

        // 목표 지점 (클리어 목표인 별의 좌표)
        goal = new Rectangle(160, -2560 + 100, 40, 40);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 그리기
        drawBackground(g);

        // 적 렌더링
        for (Enemy enemy : enemies) {
            enemy.render(g);
        }

        // 플레이어 렌더링
        player.render(g);

        // 목표 지점 (별) 렌더링
        g.setColor(Color.YELLOW);
        g.fillRect(goal.x, goal.y + backgroundY, goal.width, goal.height);

        // HUD (체력, 골드 표시)
        g.setColor(Color.WHITE);
        g.drawString("HP: " + player.getCurrentHP() + "/" + player.getMaxHP(), 10, 20);
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

        backgroundY += 1;
        if (backgroundY > getHeight()) {
            backgroundY = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        checkCollisions();
        repaint();
    }

    private void updateGame() {
        // 플레이어 이동
        if (keys[KeyEvent.VK_A]) player.move(-20, 0);
        if (keys[KeyEvent.VK_D]) player.move(20, 0);
        if (keys[KeyEvent.VK_W]) player.move(0, -20);
        if (keys[KeyEvent.VK_S]) player.move(0, 20);

        // 적 이동
        for (Enemy enemy : enemies) {
            enemy.move(0, 2);
        }

        // 플레이어 무기 업데이트
        player.getWeapon().updateBullets();
    }

    private void checkCollisions() {
        // 적과 총알 충돌
        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            for (Bullet bullet : player.getWeapon().getBullets()) {
                if (enemyBounds.intersects(bullet.getBounds())) {
                    enemy.takeDamage(bullet.getDamage());
                    if (enemy.getHealth() <= 0) {
                        enemies.remove(enemy);
                        player.addGold(10);
                        break;
                    }
                }
            }
        }

        // 목표 지점 도달
        if (player.getBounds().intersects(new Rectangle(goal.x, goal.y + backgroundY, goal.width, goal.height))) {
            JOptionPane.showMessageDialog(this, "Stage Cleared!");
            player.addGold(100);
            manager.switchPanel(new LobbyPanel(manager));
            timer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (e.getKeyCode() == KeyEvent.VK_M) {
            player.getWeapon().shoot(player.getX(), player.getY());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
