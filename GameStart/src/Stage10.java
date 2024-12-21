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

public class Stage10 extends JPanel implements ActionListener, KeyListener {
    private GameManager manager;
    private Timer timer;
    private Player player;
    private Boss boss; // 보스 객체 추가
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
    public int stageNum;
    private BufferedImage starImage;
    private int playerSpeed;
    private int playerSpeed2 = 0;
    private int scrollSpeed = 0;
    private JButton pauseButton; // 일시정지 버튼
    private JButton mainMenuButton; // 메인 메뉴로 돌아가는 버튼
    private Timer bossPatternTimer; // 보스 패턴 타이머

    public Stage10(GameManager manager, int stageNum) {
        this.manager = manager;
        this.player = manager.getPlayer();
        this.enemies = new ArrayList<>();
        this.stageNum = stageNum;
        this.scrollSpeed = 1;

        initializeStage(stageNum);
        loadShootSound(); // 총 소리 로드
        loadBackgroundMusic(); // 배경 음악 로드
        this.playerSpeed = this.player.getspeed();
        this.playerSpeed2 -= this.playerSpeed;
        // 별 이미지 로드 시 알파 채널을 처리하도록 수정
        try {
            starImage = ImageIO.read(Paths.get("GameStart/src/background/star.png").toFile());
            if (starImage.getType() != BufferedImage.TYPE_INT_ARGB) {
                BufferedImage temp = new BufferedImage(starImage.getWidth(), starImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = temp.createGraphics();
                g.drawImage(starImage, 0, 0, null);
                g.dispose();
                starImage = temp; // 알파 채널을 지원하는 형식으로 변환
            }
        } catch (IOException e) {
            e.printStackTrace();
            starImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // 오류 발생 시 빈 이미지 생성
        }
        
        
        // 게임 루프 시작
        timer = new Timer(16, this);
        timer.start();
        
     // 보스 패턴 타이머 (5초마다 패턴 실행)
        bossPatternTimer = new Timer(5000, e -> {
            if (boss != null) {
                boss.usingPattern();
            }
        });
        
        // 키 입력 처리 활성화
        setFocusable(true);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
        addKeyListener(this);

        // 설정 UI 구성
        createSettingsUI();
    }

    private void createSettingsUI() {
        // 일시정지 버튼
        pauseButton = new JButton("Pause");
        pauseButton.setBounds(10, 10, 100, 30);
        pauseButton.addActionListener(e -> togglePause());
        this.add(pauseButton);
        
     // 메인 메뉴 버튼 추가
        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setBounds(120, 10, 120, 30);
        mainMenuButton.addActionListener(e -> goToMainMenu());
        this.add(mainMenuButton);
    }


    private void togglePause() {
        if (timer.isRunning()) {
            // 게임이 실행 중일 때 일시정지
            timer.stop();
            backgroundMusic.stop(); // 배경 음악 멈추기
            JOptionPane.showMessageDialog(this, "Game Paused");
            // 일시정지 상태에서는 키 입력 비활성화
            setFocusable(false);
        } else {
            // 게임이 일시정지 상태일 때 복구
            timer.start();
            backgroundMusic.setFramePosition(0); // 배경 음악을 처음으로 되돌리기
            backgroundMusic.start(); // 음악 다시 시작
            JOptionPane.showMessageDialog(this, "Game Resumed");
            // 일시정지 해제되면 키 입력 활성화
            setFocusable(true);
            requestFocusInWindow();  // 포커스를 다시 설정하여 키 입력을 받을 수 있게 함
        }
    }
    
    private void goToMainMenu() {
        // 게임 중지
        timer.stop();
        backgroundMusic.stop(); // 배경 음악 멈추기
        
        // 메인 메뉴로 돌아가기
        manager.switchPanel(new TitlePanel(manager)); // 메인 메뉴로 이동
    }

    private void initializeStage(int stageNum) {
    	// 보스 생성
        boss = new Boss();


        player.reset();

        // 목표 지점 초기화
        goal = new Rectangle(0, 0, 40, 40); // 초기에는 화면 밖에 설정
        goalVisible = false;

        // 배경 이미지 로드
        try {
            background = ImageIO.read(Paths.get("GameStart/src/background/Stage1.png").toFile());
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
        
        // 보스 렌더링
        if (boss != null) {
            boss.render(g);
        }

        // 플레이어 렌더링
        player.render(g);

        // 목표 지점 (별) 렌더링
        if (goalVisible) {
        	g.drawImage(starImage, goal.x, goal.y + backgroundY, goal.width, goal.height, this);

        }

        // HUD (체력, 골드 표시)
        g.setColor(Color.WHITE);
        g.drawString("HP: " + player.getCurrentHP() + "/" + player.getMaxHP(), 10, 20);
        g.drawString("Gold: " + player.getGold(), 10, 40);
    }

    private void drawBackground(Graphics g) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int bgHeight = background.getHeight(null);

        int startY = backgroundY - bgHeight + 768;
        g.drawImage(background, 0, startY, panelWidth, bgHeight, this);
        backgroundY += 1;

        if (backgroundY >= bgHeight) {
        	backgroundY = 3000;
            return;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();       // 게임 업데이트
        checkCollisions();  // 충돌 처리
        
        // 자동 발사 로직
        if (isShooting) {
            player.shoot();
            playShootSound();
        }

        repaint(); // 화면 갱신
    }


    private void updateGame() {
        // 키 입력에 따른 플레이어 이동
        if (keys[KeyEvent.VK_A]) player.move(this.playerSpeed2, 0);
        if (keys[KeyEvent.VK_D]) player.move(this.playerSpeed, 0);
        if (keys[KeyEvent.VK_W]) player.move(0,this.playerSpeed2);
        if (keys[KeyEvent.VK_S]) player.move(0,this.playerSpeed);

        // 탄환 업데이트
        player.getWeapon().updateBullets(512,768);

        // 보스 이동 및 패턴 실행
        if (boss != null) {
            boss.Hand1.updateBullets(getWidth(), getHeight());
            boss.Hand2.updateBullets(getWidth(), getHeight());
            boss.Hand3.updateBullets(getWidth(), getHeight());
            boss.Hand4.updateBullets(getWidth(), getHeight());
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
        // 적과의 충돌 처리
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            Rectangle enemyBounds = enemy.getBounds();

            // 플레이어와 적의 충돌 처리
            if (player.getBounds().intersects(enemyBounds)) {
                player.takeDamage(10, 0);
                System.out.println("Player collided with enemy! Player HP: " + player.getCurrentHP());
            }
            
            // 보스와의 충돌 처리
            if (boss != null && player.getBounds().intersects(boss.getBounds())) {
                player.takeDamage(10, 0);
            }

            // 플레이어의 총알과 보스의 충돌 처리
            Iterator<Bullet> bulletIterator = player.getWeapon().getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (boss != null && boss.getBounds().intersects(bullet.getBounds())) {
                    boss.takeDamage(bullet.getDamage());
                    bulletIterator.remove(); // 충돌한 총알 제거
                    System.out.println("Boss hit! Current HP: " + boss.getCurrentHP());
                    if (boss.getCurrentHP() <= 0) {
                        JOptionPane.showMessageDialog(this, "Boss Defeated! Stage Cleared!");
                        manager.switchPanel(new LobbyPanel(manager));
                        timer.stop();
                    }
                }
            }
        }

        // 목표와의 충돌 처리
        if (goalVisible) {
            Rectangle goalBounds = new Rectangle(goal.x, goal.y + backgroundY, goal.width, goal.height);
            if (player.getBounds().intersects(goalBounds)) {
                JOptionPane.showMessageDialog(this, "Stage Cleared!");
                player.clearStage[this.stageNum] = true;
                player.addGold(100);
                manager.switchPanel(new LobbyPanel(manager));
                backgroundMusic.stop();  // 배경 음악 멈추기
                timer.stop();  // 게임 루프 중지
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                togglePause(); // 일시정지/재개 토글
        }
    }

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