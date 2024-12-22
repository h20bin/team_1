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

public class Stage9 extends JPanel implements ActionListener, KeyListener {
    private GameManager manager;
    private Timer timer;
    private Player player;
    private List<Enemy> enemies;
    private int backgroundY = 768; // 배경 스크롤 위치
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

    public Stage9 (GameManager manager, int stageNum) {
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
    // 유틸리티 메서드: 스프라이트 시트를 로드합니다.
 // 유틸리티 메서드: 스프라이트 시트를 로드합니다.
    private BufferedImage[] loadSpriteSheet(String resourcePath, int frameWidth, int frameHeight) throws IOException {
        // 스프라이트 시트를 로드합니다.
    	
        SpriteSheet spriteSheet = new SpriteSheet(resourcePath, frameWidth, frameHeight);
        if (spriteSheet.getFrameCount() <= 0) {
            throw new IllegalStateException("No frames available in the sprite sheet.");
        }

        // 첫 번째 프레임만 배열로 반환합니다.
        return new BufferedImage[]{spriteSheet.getFrame(0)};
    }

    
    private void initializeStage(int stageNum) {
        // 플레이어 초기화 먼저 수행
        player.reset();
        BufferedImage weaponSprites1; 
        BufferedImage weaponSprites2; 
        BufferedImage weaponSprites3; 
        BufferedImage weaponSprites4; 
        BufferedImage[] bulletFrames;

        try {
            weaponSprites1 = new SpriteSheet("/Weapon/ball1.png", 72, 108).getFrame(0);
            weaponSprites2 = new SpriteSheet("/Weapon/ball2.png", 72, 108).getFrame(0);
            weaponSprites3 = new SpriteSheet("/Weapon/ball3.png", 72, 108).getFrame(0);
            weaponSprites4 = new SpriteSheet("/Weapon/ball4.png", 72, 108).getFrame(0);
            bulletFrames = loadSpriteSheet("/Character/bora-Sheet.png", 20, 20);
        } catch (IOException e) {
            e.printStackTrace();
            return; // 리소스를 로드하지 못하면 초기화 종료
        }

        // 적 초기화
        for (int i = 0; i < stageNum*45; i++) {
            BufferedImage enemySprite1;
            BufferedImage enemySprite2 = null;
            BufferedImage enemySprite3 = null;
            BufferedImage enemySprite4 = null;
            try {
                enemySprite1 = new SpriteSheet("/Weapon/ball1.png", 72, 108).getFrame(0);
                enemySprite2 = new SpriteSheet("/Weapon/ball2.png", 72, 108).getFrame(0);
                enemySprite3 = new SpriteSheet("/Weapon/ball3.png", 72, 108).getFrame(0);
                enemySprite4 = new SpriteSheet("/Weapon/ball4.png", 72, 108).getFrame(0);
            } catch (IOException e) {
                e.printStackTrace();
                enemySprite1 = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                Graphics g = enemySprite1.getGraphics();
                g.setColor(Color.RED);
                g.fillRect(0, 0, 40, 40);
                g.dispose();
            }

            // 패턴에 따라 적의 위치와 무기 설정
            Enemy enemy;
            switch (i % 4) {
                case 0: // 직선 배열
                    enemy = new Enemy(50 + (i * 40), -100, 50, enemySprite1, null);
                    break;

                case 1: // 지그재그 배열
                    enemy = new Enemy(50 + (i % 2 == 0 ? i * 40 : -i * 40), -100 - (i * 80), 50, enemySprite2, null);
                    break;

                case 2: // 원형 배열
                    double angle = 2 * Math.PI / stageNum * i; // 각도 계산
                    int centerX = 256; // 원의 중심
                    int centerY = -300;
                    int radius = 100; // 원의 반지름
                    int x = (int) (centerX + radius * Math.cos(angle));
                    int y = (int) (centerY + radius * Math.sin(angle));
                    enemy = new Enemy(x, y, 50, enemySprite3, null);
                    break;

                case 3: // 랜덤 위치
                    int randomX = random.nextInt(512 - 40); // 화면 너비 내에서 랜덤 X
                    int randomY = random.nextInt(200) - 300; // 화면 상단 위쪽에서 랜덤 Y
                    enemy = new Enemy(randomX, randomY, 50, enemySprite4, null);
                    break;

                default: // 기본: 직선 배열
                    enemy = new Enemy(50 + (i * 40), -100, 50, enemySprite1, null);
            }

            // 무기를 적에게 설정
            Weapon enemyWeapon = new Weapon(enemy, weaponSprites1, 3, 5, 2, bulletFrames, 6);
            enemy.setWeapon(enemyWeapon);
            enemies.add(enemy);
        }

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

        // 적 렌더링
        for (Enemy enemy : enemies) {
            enemy.render(g);
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
        int bgWidth = background.getWidth(null);
        int bgHeight = background.getHeight(null); // 배경 이미지의 높이 가져오기

        // 배경 이미지의 현재 y값 기준으로 그리기
        int startY = backgroundY - bgHeight; // 스크롤 시 이어지도록 위쪽 배경 계산
       
        
        if (backgroundY >= 3000) {
        	g.drawImage(background, 0, 0, bgWidth,bgHeight, this);
        	generateGoal();
            return;
        }else {
        	g.drawImage(background, 0, startY, bgWidth, bgHeight, this);
        	
            backgroundY += scrollSpeed; // 스크롤 속도 조정
            scrollSpeed =3;
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
        if (keys[KeyEvent.VK_A]) player.move(this.playerSpeed2, 0);
        if (keys[KeyEvent.VK_D]) player.move(this.playerSpeed, 0);
        if (keys[KeyEvent.VK_W]) {
        	player.move(0,this.playerSpeed2);
        	this.scrollSpeed = this.playerSpeed/5;
        }
        if (keys[KeyEvent.VK_S]) player.move(0,this.playerSpeed);

        // 적 이동
        for (Enemy enemy : enemies) {
            enemy.move(0, 20);
        }

        // 탄환 업데이트
        player.getWeapon().updateBullets(512, 768);

        // 목표 생성 (중복 제거)
        if (enemies.isEmpty()) {
        	this.scrollSpeed = 20;
        }
    }

    private void generateGoal() {
        // 목표를 화면 내 무작위 위치에 생성
        int randomX = 240;
        int randomY = 40;
        goal.setLocation(randomX, randomY - backgroundY);
        goalVisible = true;
    }

    private void checkCollisions() {
        // 적과 플레이어 간 충돌
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                player.takeDamage(10); // 플레이어가 적과 충돌하면 데미지
            }
        }

     // 플레이어의 총알과 적의 충돌
        Iterator<Bullet> playerBullets = player.getWeapon().getBullets().iterator();
        while (playerBullets.hasNext()) {
            Bullet bullet = playerBullets.next();

            // 적을 순회하며 충돌 검사
            Iterator<Enemy> enemyIterator = enemies.iterator(); // 적의 Iterator 생성
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();

                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    enemy.takeDamage(bullet.getDamage()); // 적 데미지 처리
                    playerBullets.remove(); // 충돌한 총알 제거

                 // 적의 체력이 0 이하라면 적 제거
                    if (enemy.getHealth() <= 0) {
                        enemyIterator.remove();
                        bullet.render(getGraphics());
                         
                        player.addGold(10);
                    }
                    break; // 하나의 적과 충돌 후 종료
                }
            }
        }

        // 적의 총알과 플레이어의 충돌
        
        for (Enemy enemy : enemies) {
            Iterator<Bullet> enemyBullets = enemy.getWeapon().getBullets().iterator();
            while (enemyBullets.hasNext()) {
                Bullet bullet = enemyBullets.next();
                if (bullet.getBounds().intersects(player.getBounds())) {
                    player.takeDamage(bullet.getDamage());
                    enemyBullets.remove(); // 충돌한 총알 제거
                    break;
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