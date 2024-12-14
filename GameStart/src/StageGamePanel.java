import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StageGamePanel extends JPanel implements ActionListener, KeyListener {
    private GameManager manager;
    private Timer timer;
    private Player player;
    private List<Enemy> enemies;
    private int backgroundY = 0; // 배경 스크롤 위치
    private boolean[] keys = new boolean[256]; // 키 입력 상태 저장
    private Rectangle goal; // 스테이지 클리어 목표
    private boolean goalVisible = false; // 목표의 가시성 여부
    private BufferedImage background; // 배경 이미지
    private Clip shootSound; // 총 소리 클립
    private Clip backgroundMusic; // 배경 음악 클립
    private Random random = new Random(); // 랜덤 위치 생성을 위한 Random 객체
    private boolean isShooting;

    public StageGamePanel(GameManager manager, int stageNum) {
        this.manager = manager;
        this.player = manager.getPlayer();
        this.enemies = new ArrayList<>();

        initializeStage(stageNum);
        loadShootSound(); // 총 소리 로드
        loadBackgroundMusic(); // 배경 음악 로드

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
                enemySprite = new SpriteSheet("/Character/bora-sheet.png", 36, 36).getFrame(i % 4);
            } catch (IOException e) {
                e.printStackTrace();
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

        // 목표 지점 초기화
        goal = new Rectangle(0, 0, 40, 40); // 초기에는 화면 밖에 설정
        goalVisible = false;

        // 배경 이미지 로드
        try {
            background = ImageIO.read(Paths.get("GameStart/src/background.jpg").toFile());
        } catch (IOException e) {
            e.printStackTrace();
            background = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private void loadShootSound() {
        try {
            File soundFile = new File("GameStart/src/gunsound.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            shootSound = AudioSystem.getClip();
            shootSound.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBackgroundMusic() {
        try {
            File musicFile = new File("GameStart/src/backgroundmusic.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    private void playShootSound() {
        if (shootSound != null) {
            shootSound.setFramePosition(0);
            shootSound.start();
        }
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
        if (goalVisible) {
            g.setColor(Color.YELLOW);
            g.fillRect(goal.x, goal.y + backgroundY, goal.width, goal.height);
        }

        // HUD (체력, 골드 표시)
        g.setColor(Color.WHITE);
        g.drawString("HP: " + player.getCurrentHP() + "/" + player.getMaxHP(), 10, 20);
        g.drawString("Gold: " + player.getGold(), 10, 40);
    }

    private void drawBackground(Graphics g) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int bgHeight = background.getHeight();

        // 배경 이미지의 반복적 그리기
        g.drawImage(background, 0, backgroundY, panelWidth, panelHeight, this);
        g.drawImage(background, 0, backgroundY - bgHeight, panelWidth, panelHeight, this);

        // 스크롤 업데이트
        backgroundY += 1;
        if (backgroundY >= bgHeight) {
            backgroundY = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        checkCollisions();

        // 자동 발사 로직
        if (isShooting) {
            player.shoot();
            playShootSound();
        }

        repaint();
    }

    private void updateGame() {
        // 키 입력에 따른 플레이어 이동
        if (keys[KeyEvent.VK_A]) player.move(-20, 0);
        if (keys[KeyEvent.VK_D]) player.move(20, 0);
        if (keys[KeyEvent.VK_W]) player.move(0, -20);
        if (keys[KeyEvent.VK_S]) player.move(0, 20);

        // 적 이동
        for (Enemy enemy : enemies) {
            enemy.move(0, 2);
        }

        // 탄환 업데이트
        player.getWeapon().updateBullets();

        // 목표 생성 (중복 제거)
        if (enemies.isEmpty() && !goalVisible) {
            generateGoal();
        }
    }

    private void generateGoal() {
        // 목표를 화면 내 무작위 위치에 생성
        int randomX = random.nextInt(getWidth() - goal.width);
        int randomY = random.nextInt(getHeight() - goal.height);
        goal.setLocation(randomX, randomY - backgroundY);
        goalVisible = true;
    }

    private void checkCollisions() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            Rectangle enemyBounds = enemy.getBounds();

            if (player.getBounds().intersects(enemyBounds)) {
                player.takeDamage(10, 0);
                System.out.println("Player collided with enemy! Player HP: " + player.getCurrentHP());
            }

            for (Bullet bullet : player.getWeapon().getBullets()) {
                if (enemyBounds.intersects(bullet.getBounds())) {
                    enemy.takeDamage(bullet.getDamage());
                    if (enemy.getHealth() <= 0) {
                        enemyIterator.remove();
                        player.addGold(10);
                        break;
                    }
                }
            }
        }

        if (goalVisible) {
            Rectangle goalBounds = new Rectangle(goal.x, goal.y + backgroundY, goal.width, goal.height);
            if (player.getBounds().intersects(goalBounds)) {
                JOptionPane.showMessageDialog(this, "Stage Cleared!");
                player.addGold(100);

                // 음악 멈춤
                stopBackgroundMusic();

                manager.switchPanel(new LobbyPanel(manager));
                timer.stop();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;

        // 발사 키 처리
        if (e.getKeyCode() == KeyEvent.VK_M) {
            isShooting = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;

        // 총알 발사 중지
        if (e.getKeyCode() == KeyEvent.VK_M) {
            isShooting = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
