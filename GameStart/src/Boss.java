import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

public class Boss extends Character {
	private int MaxHP;
	private int currentHP;
	public Weapon Hand1;
	public Weapon Hand2;
	public Weapon Hand3;
	public Weapon Hand4;
	public Timer patternTimer;
	private Skill pattern1;
	private Skill pattern2;
	private Skill pattern3;
	private Skill pattern4;
	
	Boss() {
		super(180,100,null,null);
		this.MaxHP = 1000;
		this.currentHP = 1000;
		
		try {
			BufferedImage[] BossSprites = loadSpriteSheet("/Character/boss.png", 128, 128);
			BufferedImage[] Hand1Sprites = loadSpriteSheet("/Weapon/HAND1.png", 128, 128);
	        BufferedImage[] Hand2Sprites = loadSpriteSheet("/Weapon/HAND1.png", 128, 128);
	        BufferedImage[] Hand3Sprites = loadSpriteSheet("/Weapon/HAND2.png", 128, 128);
	        BufferedImage[] Hand4Sprites = loadSpriteSheet("/Weapon/HAND2.png", 128, 128);
	        BufferedImage[] ball1Frames = loadSpriteSheet("/Weapon/ball1.png", 72, 108);
	        BufferedImage[] ball2Frames = loadSpriteSheet("/Weapon/ball2.png", 72, 108);
	        BufferedImage[] ball3Frames = loadSpriteSheet("/Weapon/ball3.png", 72, 108);
	        BufferedImage[] ball4Frames = loadSpriteSheet("/Weapon/ball4.png", 72, 108);
	        
	        Weapon hand1 = new Weapon(Hand1Sprites[0], 3, 20, 5, ball1Frames, 4);
	        Weapon hand2 = new Weapon(Hand2Sprites[0], 3, 20, 5, ball2Frames, 4);
	        Weapon hand3 = new Weapon(Hand3Sprites[0], 3, 20, 5, ball3Frames, 5);
	        Weapon hand4 = new Weapon(Hand4Sprites[0], 3, 20, 5, ball4Frames, 5);
	        
	        this.Hand1 = hand1;
	        this.Hand2 = hand2;
	        this.Hand3 = hand3;
	        this.Hand4 = hand4;
	        
	        this.sprite = BossSprites[0];
	        
	        Skill skill1 = new Skill(hand1, 4,1);
	        Skill skill2 = new Skill(hand2, 4,2);
	        Skill skill3 = new Skill(hand3, 5,1);
	        Skill skill4 = new Skill(hand4, 5,2);
	        
	        
	        this.pattern1 = skill1; 
	        this.pattern2 = skill2; 
	        this.pattern3 = skill3; 
	        this.pattern4 = skill4; 
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 패턴 타이머 설정 (5초마다 실행)
        patternTimer = new Timer(5000, e -> usingPattern());
        patternTimer.start(); // 타이머 시작
	}
	
	public void usingPattern() {
        Random random = new Random();
        int randomNumber = random.nextInt(15) + 1;

        switch (randomNumber) {
            case 1 -> {
                pattern1.shooting();
                pattern3.shooting();
            }
            case 2 -> {
                pattern1.shooting();
                pattern4.shooting();
            }
            case 3 -> {
                pattern2.shooting();
                pattern3.shooting();
            }
            case 4 -> {
                pattern2.shooting();
                pattern4.shooting();
            }
            case 5 -> pattern1.shooting();
            case 6 -> pattern4.shooting();
            case 7 -> pattern3.shooting();
            case 8 -> pattern2.shooting();
            case 9 -> {
                pattern1.shooting();
                pattern2.shooting();
            }
            case 10 -> {
                pattern3.shooting();
                pattern4.shooting();
            }
            case 11 -> {
                pattern1.shooting();
                pattern2.shooting();
                pattern4.shooting();
            }
            case 12 -> {
                pattern1.shooting();
                pattern2.shooting();
                pattern3.shooting();
            }
            case 13 -> {
                pattern1.shooting();
                pattern3.shooting();
                pattern4.shooting();
            }
            case 14 -> {
                pattern1.shooting();
                pattern2.shooting();
                pattern3.shooting();
                pattern4.shooting();
            }
            case 15 -> {
                pattern2.shooting();
                pattern3.shooting();
                pattern4.shooting();
            }
        }
    }




	
	// 유틸리티 메서드: 스프라이트 시트를 로드합니다.
    private BufferedImage[] loadSpriteSheet(String resourcePath, int frameWidth, int frameHeight) throws IOException {
        return new SpriteSheet(resourcePath, frameWidth, frameHeight).getAllFrames();
    }
    

    private void renderHand(Graphics g) {
       this.Hand1.render(g, x- 128, y - 50);
       this.Hand2.render(g, x- 64, y - 120);
       this.Hand3.render(g, x+ 64, y - 120);
       this.Hand4.render(g, x+ 128, y - 50);

    }
    
    private void renderBody(Graphics g) {
       g.drawImage(sprite, x, y, null);
    }
    
    @Override
    public void render(Graphics g) {
        super.render(g);
        renderHand(g);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 120, 120);
    }
    
    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP <= 0) {
            onDeath();
        }
    }
    
    public void movePattern() {
        int dx = new Random().nextInt(10) - 5; // -5 ~ 5
        int dy = new Random().nextInt(10) - 5; // -5 ~ 5
        this.move(dx, dy);
    }

    private void onDeath() {
        System.out.println("Boss defeated!");
        patternTimer.stop(); // 보스가 죽으면 타이머 중지
    }

	public int getCurrentHP() {
		// TODO Auto-generated method stub
		return this.currentHP;
	}

	@Override
	public void move(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}

}
